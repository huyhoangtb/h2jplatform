/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.reader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hj2eplatform.resource.dto.Folder;
import net.hj2eplatform.resource.iservices.IResourceService;
import org.imgscalr.Scalr;

/**
 * This Class is responsible for uploading the images from UI to the DB It will
 * provide as response the callback method and URL for the image that has been
 * uploaded
 *
 * @author mcristea
 *
 */
@SuppressWarnings("unchecked")
public class ImageReaderSrcServlet extends HttpServlet {

    public final static String IMAGE_SRC = "src";
    private IResourceService resourceService;
    private static final long serialVersionUID = -7570633768412575697L;
    public final static String WIDTH_KEY = "width";
    public final static String HEIGHT_KEY = "height";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imgsrc = request.getParameter(IMAGE_SRC);
        String imgWidth = request.getParameter(WIDTH_KEY);
        String imgHeight = request.getParameter(HEIGHT_KEY);
        if (imgsrc == null) {
            return;
        }

        try {


            ServletContext cntx = getServletContext();
            // Get the absolute path of the image
            String filename = Folder.ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + imgsrc;

            filename = filename.replace("@h2j@", "/");
            File fileImg = new File(filename);
            String mime = cntx.getMimeType(filename);
            if (mime == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType(mime);
            BufferedImage resizeImageJpg = ImageIO.read(fileImg);

            if (imgWidth != null && imgHeight != null) {
               resizeImageJpg  =
                        Scalr.resize(resizeImageJpg, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH,
                        Integer.valueOf(imgWidth), Integer.valueOf(imgHeight), Scalr.OP_ANTIALIAS);
            }

            InputStream fileInCus = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizeImageJpg, Folder.extension(filename), os);
            fileInCus = new ByteArrayInputStream(os.toByteArray());
            response.setContentLength((int) os.size());

            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = fileInCus.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            fileInCus.close();


        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }

}
