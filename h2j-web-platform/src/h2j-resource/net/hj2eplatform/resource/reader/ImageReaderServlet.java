/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hj2eplatform.models.SystemResource;
import net.hj2eplatform.resource.dto.Folder;
import net.hj2eplatform.resource.iservices.IResourceService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This Class is responsible for uploading the images from UI to the DB It will
 * provide as response the callback method and URL for the image that has been
 * uploaded
 *
 * @author mcristea
 *
 */
@SuppressWarnings("unchecked")
public class ImageReaderServlet extends HttpServlet {

    public final static String IMAGE_KEY = "imgId";
    private IResourceService resourceService;
    private static final long serialVersionUID = -7570633768412575697L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imgstrId = request.getParameter(IMAGE_KEY);

        if (imgstrId == null) {
            return;
        }
        Long imgId = null;
        try {
            imgId = Long.valueOf(imgstrId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        try {


            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().
                    getServletContext());
            resourceService = (IResourceService) ctx.getBean("resourceServiceImpl");
            SystemResource systemResource = resourceService.loadEntity(SystemResource.class, imgId);
            if (systemResource == null) {
                return;
            }
            ServletContext cntx = getServletContext();
            // Get the absolute path of the image
            String filename = Folder.ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + systemResource.getFolderPath() + Folder.FOLDER_PATH_SPACE + systemResource.getFileRealName();
            String mime = cntx.getMimeType(filename);
            if (mime == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType(mime);
            File file = new File(filename);
            response.setContentLength((int) file.length());

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }
}
