package common;

import model.CustomerModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ItemRenderer;

	
	public class ChossenBoxRendered implements ItemRenderer {
	    public String render(Component owner, Object data, int index) throws Exception {
	     CustomerModel container = (CustomerModel) data;
	        return container.getFullName(); //converting data to a string; it depends on your application's requirement
	    }
	}

