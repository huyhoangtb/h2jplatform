/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.hj2eplatform.models.SystemResource;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.resource.dto.Folder;
import net.hj2eplatform.resource.dto.FolderTree;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.SystemDefine;
//import net.quanxa.controller.CommonController;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This Class is responsible for uploading the images from UI to the DB It will
 * provide as response the callback method and URL for the image that has been
 * uploaded
 *
 * @author mcristea
 *
 */
@SuppressWarnings("unchecked")
public class CKEditorUploadServlet extends HttpServlet {

    private static final long serialVersionUID = -7570633768412575697L;
    private static final String ERROR_FILE_UPLOAD = "An error occurred to the file upload process.";
    private static final String ERROR_NO_FILE_UPLOAD = "No file is present for upload process.";
    private static final String ERROR_INVALID_CALLBACK = "Invalid callback.";
    private static final String CKEDITOR_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String CKEDITOR_HEADER_NAME = "Cache-Control";
    private static final String CKEDITOR_HEADER_VALUE = "no-cache";
    private static final Pattern PATTERN = Pattern.compile("[\\w\\d]*");
    private String errorMessage = "";
    private String fileName = "";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        Folder folder = FolderTree.getRootFolder();

        File targetFolder = new File(folder.getPath());
        response.setContentType(CKEDITOR_CONTENT_TYPE);
        response.setHeader(CKEDITOR_HEADER_NAME, CKEDITOR_HEADER_VALUE);

        if (!targetFolder.exists()) {
            targetFolder.mkdir();
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> items = upload.parseRequest(request);
            if (!items.isEmpty() && items.get(0) != null) {
                fileName = ((DiskFileItem) items.get(0)).getName();
                ResourceManagerController controller = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
                SystemResource resource = controller.cloneResource(folder, fileName);

                OutputStream outStream = new FileOutputStream(new File(targetFolder, resource.getFileRealName()));
                outStream.write(items.get(0).get());
                outStream.flush();
                outStream.close();
                resource.setResourceId(controller.getResourceService().getSequence(SystemDefine.SEQUENCE_RESOURCE_ID).longValue());
                controller.getResourceService().persistEntity(resource);

            } else {
                errorMessage = ERROR_NO_FILE_UPLOAD;
            }

        } catch (FileUploadException e) {
            errorMessage = ERROR_FILE_UPLOAD;
            e.printStackTrace();
        }

        // CKEditorFuncNum Is the location to display when the callback
        String callback = request.getParameter("CKEditorFuncNum");
        // verify if the callback contains only digits and letters in order to
        // avoid vulnerability on parsing parameter
        if (!PATTERN.matcher(callback).matches()) {
            callback = "";
            errorMessage = ERROR_INVALID_CALLBACK;
        }
//        String pathToFile = request.getContextPath() + "/" + CommonController.UPLOAD_ROOT + "/default/" + fileName.replaceAll(" ", "%20");
//        out.println(
//                "<script type = \"text/javascript\">window.parent.CKEDITOR.tools.callFunction("
//                + callback + ",'" + pathToFile + "','" + errorMessage + "')");
//        out.println("</script>");
        out.flush();
        out.close();
    }
}
