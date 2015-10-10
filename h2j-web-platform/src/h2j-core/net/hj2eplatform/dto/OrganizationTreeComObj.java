/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import java.util.List;
import java.util.Random;
import net.hj2eplatform.controller.OrganizationController;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Huy Hoang
 */
public class OrganizationTreeComObj {

    private TreeNode root;
    private TreeNode selectedNode;
    private IOrganizationService organizationService;

    public OrganizationTreeComObj(IOrganizationService organizationService) {

        this.organizationService = organizationService;
        root = new DefaultTreeNode("Root", null);

        List<Organization> organizationRoot = organizationService.getOrganizationRoot();
        for (Organization org : organizationRoot) {
            TreeNode dynamicTreeNode = new OrganizationTree(org, root);
            List orgList = organizationService.getOrganizationByParrentId(org.getOrganizationId(), true);
            if (orgList != null && orgList.size() > 0) {
                TreeNode node0 = new OrganizationTree(new Organization(), dynamicTreeNode);
            }

        }

        //<Add dummy node: This is to have + sign in node to be able to expand it in GUI>

        //<Add dummy node>
    }

    public TreeNode getRoot() {
        return root;
    }
    static Random random = new Random();

    public void onNodeExpand(NodeExpandEvent event) {
        if (event.getTreeNode().getChildCount() > 0) {
            for (int i = 0; i < event.getTreeNode().getChildCount(); i++) {
                event.getTreeNode().getChildren().remove(i);
            }
        }

        Organization curent = (Organization) event.getTreeNode().getData();
        List<Organization> organizationRoot = organizationService.getOrganizationByParrent(curent.getOrganizationId());

        for (Organization org : organizationRoot) {
            if(curent.getOrganizationId().longValue() == org.getOrganizationId().longValue()) {
                continue;
            }
            TreeNode dynamicTreeNode = new OrganizationTree(org, event.getTreeNode());
            List<Organization> orgList = organizationService.getOrganizationByParrentId(org.getOrganizationId(), true);
            if (orgList != null && orgList.size() > 0) {
                new DefaultTreeNode("fake", dynamicTreeNode);
            }
        }

    }

    public void onNodeSelected(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        event.getTreeNode().setExpanded(true);
    }

    public String addUserTreeOrganization() {
        return ControllerUtils.forwardToPage("/app/administrator/core/staff/edit_form_full").append("&orgId=").append(((Organization)getSelectedNode().getData()).getOrganizationId()).toString();
    }

    public void viewTreeOrganization() {
        OrganizationController organizationController = ControllerUtils.getBean(ControllerName.ORGANIZATION_CONTROLLER);
        organizationController.setDilogControl("true");
        ControllerUtils.getRequest().setAttribute("actionFlag", "org_edit");
        organizationController.setOrganization((Organization) getSelectedNode().getData());
    }

    public void addSubTreeOrganization() {
        OrganizationController organizationController = ControllerUtils.getBean(ControllerName.ORGANIZATION_CONTROLLER);
        organizationController.setDilogControl("true");
        organizationController.setOrganization(new Organization());
        organizationController.setOrganizationParent((Organization) getSelectedNode().getData());
        ControllerUtils.getRequest().setAttribute("actionFlag", "org_add_new");

    }

    public void deleteTreeOrganization() {
        try {
            OrganizationController organizationController = ControllerUtils.getBean(ControllerName.ORGANIZATION_CONTROLLER);
            organizationController.setOrganization((Organization) getSelectedNode().getData());
            organizationController.deletePublicOrganization();
            selectedNode.getParent().getChildren().remove(selectedNode);
            selectedNode.setParent(null);
            selectedNode = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("organization_cant_delete"));
        }

    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}
