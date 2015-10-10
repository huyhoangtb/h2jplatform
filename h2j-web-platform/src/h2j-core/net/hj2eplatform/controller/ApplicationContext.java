/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.controller;

import java.io.Serializable;
import net.hj2eplatform.context.H2jContextDefine;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.utils.UserGender;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author HuyHoang
 */
@Controller(value = "H2jApplication")
@Scope("application")
public class ApplicationContext implements Serializable {
    public Organization getRootOrganization() {
        return H2jContextDefine.getH2jRootOrg();
    }
}
