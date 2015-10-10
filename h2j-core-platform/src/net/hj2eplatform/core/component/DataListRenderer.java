package net.hj2eplatform.core.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class DataListRenderer extends org.primefaces.component.datalist.DataListRenderer  implements  java.io.Serializable{

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

//		net.hj2eplatform.component.DataList listExt = (net.hj2eplatform.component.DataList) component;
//
//		if (listExt.isLazy()) {
//                    System.out.println("load lazy");
//			listExt.loadLazyData();
//		}
		super.encodeEnd(context, component);
	}
}
