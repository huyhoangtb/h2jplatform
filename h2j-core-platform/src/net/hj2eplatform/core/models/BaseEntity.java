/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.models;

import javax.persistence.Transient;
import net.hj2eplatform.core.component.LazyObject;
import net.hj2eplatform.core.utils.StatusDefine;
import net.hj2eplatform.core.utils.SystemDefine;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public abstract class BaseEntity extends LazyObject {

    @Transient
    public abstract Integer getStatus();

    @Transient
    public abstract Integer getType();

    @Transient
    public String getStatusName() {
        if (getStatus() == null) {
            return getStatus().toString();
        }
        if (getStatus().intValue() == StatusDefine.activate) {
            return StatusDefine.ACTIVE_LABLE;
        }
        if (getStatus().intValue() == StatusDefine.inactive) {
            return StatusDefine.INACTIVE_LABLE;
        }
        if (getStatus().intValue() == StatusDefine.destroy) {
            return StatusDefine.DESTROY_LABLE;
        }
        return getStatus().toString();
    }

    @Transient
    public String getTypeName() {
        if (getType() == null) {
            return getType().toString();
        }
        if (getType().intValue() == SystemDefine.customType.intValue()) {
            return SystemDefine.PERMISSION_CUSTOM_LABLE;
        }
        if (getType().intValue() == SystemDefine.permissionSystemType.intValue()) {
            return SystemDefine.PERMISSION_SYSTEM_LABLE;
        }
        return getType().toString();
    }
}
