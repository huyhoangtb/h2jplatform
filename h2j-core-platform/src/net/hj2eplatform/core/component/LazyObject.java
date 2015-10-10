/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import javax.persistence.Transient;
import net.hj2eplatform.core.utils.StatusDefine;

/**
 * huyhoang for java platform. (h2j-platform)
 *
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public abstract class LazyObject implements java.io.Serializable {

    @Transient
    public abstract String getObjectKey();

    @Transient
    public abstract Integer getStatus();

    @Transient
    public String getStatusName() {
        if (getStatus() == null) {
            return null;
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
}
