/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.models.SystemResource;
import net.hj2eplatform.resource.dto.Folder;
import net.hj2eplatform.resource.dto.FolderTree;
import net.hj2eplatform.resource.iservices.IResourceService;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.resource.writer.ImageOptimizeWriter;
import net.hj2eplatform.core.utils.AccentRemover;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.core.utils.SystemDefine;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller("resourceManagerController")
@Scope("view")
public class ResourceManagerController implements Serializable {

    private FolderTree folderTree;
    @Autowired
    private IResourceService resourceService;
    private LazyDataSupportMapFilter<SystemResource> resourceData;
    private String searchData;
    private SystemResource myResource;
    private CroppedImage croppedImage;
    private final static String AVATAR_FOLDER_DRAFF = "/avatar/~draft/";
    private final static String AVATAR_FOLDER = "/avatar/";
    private String avatarUrl;
     private String avatarDraftUrl;

    @PostConstruct
    private void init() {
        folderTree = new FolderTree();
        resourceData = new LazyDataSupportMapFilter<SystemResource>(resourceService);
        resourceData.getFilters().put(IResourceService.SEARCH_FOLDER_PATH, FolderTree.getRootFolderPath());
        resourceData.getFilters().put(IResourceService.SEARCH_DATA, searchData);
    }

    public void viewImage(SystemResource res) {
        this.myResource = res;
    }

    public void searchActionListener() {
        resourceData.getFilters().put(IResourceService.SEARCH_DATA, searchData);
    }

    public void crop() {
        if (croppedImage == null) {
            return;
        }
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(Folder.ROOT_IMG_FOLDER + avatarUrl));
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
            File draft = new File(avatarDraftUrl);
            if(draft.exists() && draft.isFile()) {
                draft.deleteOnExit();
            }
            AuthenticationController authenticationController = AuthenticationController.getCurrentInstance();
            authenticationController.getHuman().setAvatarUrl(avatarUrl);
            authenticationController.saveLoginUserHuman();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cropping finished."));
        
    }

    public void handleFileUploadAvatar(FileUploadEvent event) {
        try {
            File fileDraff = new File(Folder.ROOT_IMG_FOLDER + AVATAR_FOLDER_DRAFF);
            File file = new File(Folder.ROOT_IMG_FOLDER + AVATAR_FOLDER);
            if (!fileDraff.exists()) {
                fileDraff.mkdirs();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            
            String fileName = Folder.filename(event.getFile().getFileName());
            String fileExtension = Folder.extension(event.getFile().getFileName());
            fileName = AccentRemover.toUrlFriendly(fileName);
            StringBuilder avatarName = new StringBuilder("uid").append(AuthenticationController.getCurrentHuman().getHumanId().toString()).append("-").append(fileName).append(".").append(fileExtension);
            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new java.io.File(fileDraff, avatarName.toString()));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            this.avatarUrl = new StringBuilder(AVATAR_FOLDER).append(avatarName).toString();
            this.avatarDraftUrl = new StringBuilder(AVATAR_FOLDER_DRAFF).append(avatarName).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            Folder selectedFile = (Folder) folderTree.getSelectedNode().getData();
            java.io.File targetFolder = new java.io.File(selectedFile.getPath());

            SystemResource systemResource = cloneResource(selectedFile, event.getFile().getFileName());
            systemResource.setFileSize(event.getFile().getSize());

            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new java.io.File(targetFolder,
                    systemResource.getFileRealName()));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            systemResource.setResourceId(resourceService.getSequence(SystemDefine.SEQUENCE_RESOURCE_ID).longValue());
            resourceService.persistEntity(systemResource);
            this.myResource = systemResource;
            new ImageOptimizeWriter(myResource.getImageUrl()).output();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SystemResource cloneResource(Folder file, String fileContent) {
        AuthenticationController controller = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        SystemResource resource = new SystemResource();
        String extensionUrl = "-";
        Date date = new Date();
        resource.setCreateDate(date);
        if (controller.getUser() != null) {
            resource.setCreateUser(BigInteger.valueOf(controller.getUser().getUserId()));
            resource.setOrgId(controller.getOrganization().getOrganizationId());
            resource.setRootOrgId(controller.getOrganization().getRootId());
            extensionUrl += "ORG" + controller.getOrganization().getOrganizationId();
            extensionUrl += "RORG" + controller.getOrganization().getRootId();
        }
        extensionUrl = "-" + DateTimeUtils.convertDateToString(date, "ddMMyyyyHHmmss") + extensionUrl;
        String fileName = Folder.filename(fileContent);
        String fileExtension = Folder.extension(fileContent);
        resource.setFileRealName(AccentRemover.toUrlFriendly(fileName) + extensionUrl + "." + fileExtension);
        resource.setFileName(fileName);
        resource.setFolderPath(file.getFolderPath());
        resource.setExtension(fileExtension);
        resource.setMetaTitle(fileName);

        resource.setMetaAlt(fileName);
        return resource;
    }

    public void deleteResource() {
        File file = new File(Folder.ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + myResource.getFolderPath() + Folder.FOLDER_PATH_SPACE + myResource.getFileRealName());
        if (file.delete()) {
            resourceService.removeEntity(myResource);
        } else {
            System.out.println("Cannot delete file");
        }
        myResource = new SystemResource();
    }

    public void loadMyResoure(Long resourceId) {
        try {
            if (resourceId == null) {
                myResource = new SystemResource();
            } else {
                myResource = this.resourceService.loadEntity(SystemResource.class, resourceId);
                if (myResource == null) {
                    myResource = new SystemResource();
                }
            }

        } catch (Exception e) {
            myResource = new SystemResource();
        }

    }

    public FolderTree getFolderTree() {
        return folderTree;
    }

    public void setFolderTree(FolderTree folderTree) {
        this.folderTree = folderTree;
    }

    public LazyDataSupportMapFilter<SystemResource> getResourceData() {
//        System.out.println(resourceData.getFilters().get(IResourceService.SEARCH_DATA));
        return resourceData;
    }

    public void setResourceData(LazyDataSupportMapFilter<SystemResource> resourceData) {
        this.resourceData = resourceData;
    }

    public SystemResource getMyResource() {
        return myResource;
    }

    public void setMyResource(SystemResource myResource) {
        this.myResource = myResource;
    }

    public static void main(String[] args) {
        final String FPATH = "/home/mem/index.html";

        System.out.println("Extension = " + Folder.extension(FPATH));
        System.out.println("Filename = " + Folder.filename(FPATH));
    }

    public String getSearchData() {
        return searchData;
    }

    public void setSearchData(String searchData) {
        this.searchData = searchData;
    }

    public IResourceService getResourceService() {
        return resourceService;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setResourceService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarDraftUrl() {
        return avatarDraftUrl;
    }

    public void setAvatarDraftUrl(String avatarDraftUrl) {
        this.avatarDraftUrl = avatarDraftUrl;
    }

}
