/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.dto;

import net.hj2eplatform.models.Organization;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author HuyHoang
 */
public class OrganizationTree extends DefaultTreeNode {

    public OrganizationTree() {
    }

    public OrganizationTree(Organization data, TreeNode parent) {
        super(data, parent);

    }
}
