/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HeadRenderer;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author hoang_000
 */
public class FacebookHeadRenderer extends HeadRenderer  implements  java.io.Serializable{

    private static final Attribute[] EXTRA_HEAD_ATTRIBUTES
            = {Attribute.attr("prefix")};

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        RenderKitUtils.renderPassThruAttributes(context,
                writer,
                component,
                EXTRA_HEAD_ATTRIBUTES);
    }

}
