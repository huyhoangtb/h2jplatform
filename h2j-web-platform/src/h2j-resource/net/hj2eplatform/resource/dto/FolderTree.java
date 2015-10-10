/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.dto;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import net.hj2eplatform.resource.controller.ResourceManagerController;
import net.hj2eplatform.resource.iservices.IResourceService;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.SystemConfig;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author HuyHoang
 */
public class FolderTree implements Serializable{

    private final static String FOLDER_ROOT = "/";
    public final static String ROOT_IMG_FOLDER = SystemConfig.getResource("hj2eplatform.directory.fileupload");
    public final static String TMPL_RESOURCE = SystemConfig.getResource("hj2eplatform.directory.fileupload.temp");
    private TreeNode root;
    private TreeNode selectedNode;
    private String folderName;
    private String rootName = "Thư mục gốc";
    private int actionFlag = 0; // 0: create thu muc cung cap; 1 create thu muc con

    public FolderTree() {
        Folder folder = new Folder();
        root = new DefaultTreeNode(folder, null);
        Folder file = getRootFolder();
        file.setFolderName(rootName);
        TreeNode dynamicTreeNode = new DefaultTreeNode(file, root);

        file.isRoot = true;
        this.selectedNode = dynamicTreeNode;
        List<Folder> rootList = file.getAllSubFolder();

        if (rootList != null) {
            dynamicTreeNode.setExpanded(true);
            for (Folder f : rootList) {
                f.getAllSubFolder();
                TreeNode dynamicNode = new DefaultTreeNode(f, dynamicTreeNode);
                if (f.hasSubFolder) {
                    new DefaultTreeNode("fake", dynamicNode);
                }
            }
        }
    }

    public static Folder getRootFolder() {

        String rootPath = getRootFolderPath();
        Folder folder = new Folder(ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + rootPath, rootPath);
        return folder;
    }

    public static String getRootFolderPath() {

        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        String rootPath = "";
        if (authenticationController.getUser() == null) {
            rootPath = TMPL_RESOURCE;
        } else if (authenticationController.getOrganization() == null || authenticationController.getOrganization().getOrganizationId() == null) {
            rootPath = TMPL_RESOURCE;
        } else {
            rootPath = authenticationController.getOrganization().getOrganizationId().toString();
        }

        return rootPath;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void onNodeExpand(NodeExpandEvent event) {
        //<Remove dummy node>
        if (event.getTreeNode().getChildCount() == 1) {
            event.getTreeNode().getChildren().remove(0);
        }
        //</Remove dummy node>

        //<Generate some random children>

        Folder curent = (Folder) event.getTreeNode().getData();
        List<Folder> categoryRoot = curent.getAllSubFolder();
        for (Folder file : categoryRoot) {
            file.getAllSubFolder();
            TreeNode dynamicTreeNode = new DefaultTreeNode(file, event.getTreeNode());
            if (file.hasSubFolder) {
                new DefaultTreeNode("fake", dynamicTreeNode);
            }
        }

    }

    public void onNodeSelected(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        event.getTreeNode().setExpanded(true);
        Folder folder = (Folder) this.selectedNode.getData();
        
        ResourceManagerController controller = ControllerUtils.getBean(ControllerName.RESOURCE_MANAGER_CONTROLLER);
        controller.getResourceData().getFilters().put(IResourceService.SEARCH_FOLDER_PATH, folder.getFolderPath());

    }

    public void creeateTypeListener(Integer type) {
        this.actionFlag = type;
    }

    public void createFolder() {
        if (this.folderName == null || this.folderName.trim().compareTo("") == 0) {
            ControllerUtils.addAlertMessage("Vui lòng nhập tên thư mục!");
            return;
        }
        String filePath = "";
        TreeNode parent = this.selectedNode;
        Folder file = (Folder) this.selectedNode.getData();
        if (file.isRoot) {
            filePath = file.getPath() + Folder.FOLDER_PATH_SPACE + this.folderName;
        } else {
            parent = this.selectedNode.getParent();
            Object obj = parent.getData();
            if (obj instanceof String) {
                filePath = ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + this.getRootFolderPath();
            } else if (((Folder) obj).getContent() == null) {
                filePath = ROOT_IMG_FOLDER + Folder.FOLDER_PATH_SPACE + this.getRootFolderPath();
            } else {
                filePath = ((Folder) obj).getPath();
            }

            filePath += this.folderName;
        }
        java.io.File realFile = new java.io.File(filePath);
        if (realFile.exists()) {
            ControllerUtils.addAlertMessage("Tên thư mục đã tồn tại!");
            return;
        }
        realFile.mkdirs();
        String newFolderPath = file.getFolderPath() + Folder.FOLDER_PATH_SPACE + realFile.getName();
        new DefaultTreeNode(new Folder(realFile, newFolderPath), parent);
    }

    public void createSubFolder() {
        if (this.folderName == null || this.folderName.trim().compareTo("") == 0) {
            ControllerUtils.addAlertMessage("Vui lòng nhập tên thư mục!");
            return;
        }
        String path = ((Folder) this.selectedNode.getData()).getPath();
        path += Folder.FOLDER_PATH_SPACE + this.folderName;
        java.io.File realFile = new java.io.File(path);
        if (realFile.exists()) {
            ControllerUtils.addAlertMessage("Tên thư mục đã tồn tại!");
            return;
        }
        realFile.mkdirs();
        String newFolderPath = ((Folder) this.selectedNode.getData()).getFolderPath() + Folder.FOLDER_PATH_SPACE + realFile.getName();
        new DefaultTreeNode(new Folder(realFile, newFolderPath), this.selectedNode);
    }

    public void reImportDatabase() {
    }

    public void deleteThisFolder() {
        Folder file = (Folder) this.selectedNode.getData();
        FacesMessage message = null;
        if (file.isRoot) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Xóa thư mục không thành công.", "Không thể xóa thư mục gốc được!.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return;
        }
        if (file.hasSubFolder) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Xóa thư mục không thành công.", "Vui lòng xóa dữ liệu bên trong thư mục trước khi xóa thư mục.");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            return;
        }

        if (file.getContent().delete()) {
            if (this.selectedNode.getParent().getChildCount() <= 1 && !(this.selectedNode.getParent().getData() instanceof String)) {
                ((Folder) this.selectedNode.getParent().getData()).setHasSubFolder(false);
            }
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Xóa thư mục.", "Xóa thư mục thành công!");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Xóa thư mục không thành công.", "Có lỗi xảy ra khi thực hiện xóa thư mục. <p>Vui lòng kiểm tra và chắc chắn thư mục ko có dữ liệu!</p>");
        }


        this.selectedNode.getParent().getChildren().remove(this.selectedNode);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getParentFolder() {

        if (this.selectedNode == null) {
            return rootName;
        }
        if (this.selectedNode.getParent().getData() instanceof String) {
            return ((Folder) this.selectedNode.getData()).getFolderName();
        }

        Folder file = (actionFlag == 0 ? (Folder) this.selectedNode.getParent().getData() : (Folder) this.selectedNode.getData());

        return file.getFolderName();
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getActionFlag() {
        return actionFlag;
    }

    public void setActionFlag(int actionFlag) {
        this.actionFlag = actionFlag;
    }
}
