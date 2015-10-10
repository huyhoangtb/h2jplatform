package net.hj2eplatform.core.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class DataGridRenderer extends org.primefaces.component.datagrid.DataGridRenderer  implements  java.io.Serializable{
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//		net.hj2eplatform.component.DataGrid gridExt = (net.hj2eplatform.component.DataGrid) component;
//
//		if (gridExt.isLazy()) {
//			gridExt.loadLazyData();
//		}

		super.encodeEnd(context, component);
	}
}
