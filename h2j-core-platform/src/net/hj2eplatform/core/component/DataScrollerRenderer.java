package net.hj2eplatform.core.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
/**
 * make sure we have a session
 */
public class DataScrollerRenderer extends org.primefaces.component.datascroller.DataScrollerRenderer  implements  java.io.Serializable{

   	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

//		net.hj2eplatform.component.DataScroller listExt = (net.hj2eplatform.component.DataScroller) component;
//
//		if (listExt.isLazy()) {
//                    System.out.println("load lazy");
//			listExt.loadLazyData();
//		}
		super.encodeEnd(context, component);
	}
}
