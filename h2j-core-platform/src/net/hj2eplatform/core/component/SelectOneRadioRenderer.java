/*
 * Copyright 2009-2013 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hj2eplatform.core.component;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.util.HTML;

public class SelectOneRadioRenderer extends org.primefaces.component.selectoneradio.SelectOneRadioRenderer  implements  java.io.Serializable{
    protected void encodeMarkup(FacesContext context, SelectOneRadio radio) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = radio.getClientId(context);
        String style = radio.getStyle();
        String styleClass = radio.getStyleClass();
        styleClass = styleClass == null ? SelectOneRadio.STYLE_CLASS : SelectOneRadio.STYLE_CLASS + " " + styleClass;
        String layout = radio.getLayout();
        boolean custom = layout != null && layout.equals("custom");
        
        List<SelectItem> selectItems = getSelectItems(context, radio);
                
        if(custom) {
            //populate selectitems for radiobutton access
            radio.setSelectItems(getSelectItems(context, radio));
            
            //render dummy markup to enable processing of ajax behaviors (finding form on client side)
            writer.startElement("span", radio);
            writer.writeAttribute("id", radio.getClientId(context), "id");
            writer.endElement("span");
        }
        else {
            writer.startElement("div", radio);//<table>
            writer.writeAttribute("id", clientId, "id");
            writer.writeAttribute("class", styleClass, "styleClass");
            if(style != null) {
                writer.writeAttribute("style", style, "style");
            }

            encodeSelectItems(context, radio, selectItems, layout);

            writer.endElement("div");//<table>
        }
    }

    
    protected void encodeSelectItems(FacesContext context, SelectOneRadio radio, List<SelectItem> selectItems, String layout) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Converter converter = radio.getConverter();
        String name = radio.getClientId(context);
        boolean pageDirection = layout != null && layout.equals("pageDirection");
        Object value = radio.getSubmittedValue();
        if(value == null) {
            value = radio.getValue();
        }
        Class type = value == null ? String.class : value.getClass();
        
        int idx = -1;
        for(SelectItem selectItem : selectItems) {
            idx++;
            boolean disabled = selectItem.isDisabled() || radio.isDisabled();
            String id = name + UINamingContainer.getSeparatorChar(context) + idx;
            Object coercedItemValue = coerceToModelType(context, selectItem.getValue(), type);
            boolean selected = (coercedItemValue != null) && coercedItemValue.equals(value);
            
            if(pageDirection) {
                writer.startElement("div", null); //<tr>
            }

            encodeOption(context, radio, selectItem, id, name, converter, selected, disabled);

            if(pageDirection) {
                writer.endElement("div");//</tr>
            }
        }
    }
    
    protected void encodeOption(FacesContext context, SelectOneRadio radio, SelectItem option, String id, String name, Converter converter, boolean selected, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String itemValueAsString = getOptionAsString(context, radio, converter, option.getValue());
        String styleClass = radio.isPlain() ? HTML.RADIOBUTTON_NATIVE_CLASS : HTML.RADIOBUTTON_CLASS;
        
        writer.startElement("div", null);//<td>

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        encodeOptionInput(context, radio, id, name, selected, disabled, itemValueAsString);
        encodeOptionOutput(context, radio, selected, disabled);

        writer.endElement("div");
        writer.endElement("div");//<\td>

        writer.startElement("td", null);//<td>
        encodeOptionLabel(context, radio, id, option, disabled);
        writer.endElement("td");//<\td>
    }

    protected void encodeOptionInput(FacesContext context, SelectOneRadio radio, String id, String name, boolean checked, boolean disabled, String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "radio", null);
        writer.writeAttribute("value", value, null);

        if(radio.getTabindex() != null) writer.writeAttribute("tabindex", radio.getTabindex(), null);
        if(checked) writer.writeAttribute("checked", "checked", null);
        if(disabled) writer.writeAttribute("disabled", "disabled", null);
        if(radio.getOnchange() != null) writer.writeAttribute("onchange", radio.getOnchange(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(FacesContext context, SelectOneRadio radio, String containerClientId, SelectItem option, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);
        if(disabled)
            writer.writeAttribute("class", "ui-state-disabled", null);
        
        if(option.isEscape())
            writer.writeText(option.getLabel(),null);
        else
            writer.write(option.getLabel());
        
        writer.endElement("label");
    }

    protected void encodeOptionOutput(FacesContext context, SelectOneRadio radio, boolean selected, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String boxClass = HTML.RADIOBUTTON_BOX_CLASS;
        boxClass = selected ? boxClass + " ui-state-active" : boxClass;
        boxClass = disabled ? boxClass + " ui-state-disabled" : boxClass;
        boxClass = !radio.isValid() ? boxClass + " ui-state-error" : boxClass;

        String iconClass = HTML.RADIOBUTTON_BOX_CLASS;
        iconClass = selected ? iconClass + " " + HTML.RADIOBUTTON_CHECKED_ICON_CLASS : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", boxClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }
        
 
}
