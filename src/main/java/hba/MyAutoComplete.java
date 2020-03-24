package hba;

import java.util.List;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;

@SuppressWarnings("rawtypes")
public class MyAutoComplete extends SelectorComposer{
	
	 @Wire
	    private Combobox combo;
	    public void afterCompose() {
	     //   super.afterCompose();
	        combo.setModel(ListModels.toListSubModel(new ListModelList(getAllItems())));
	    }
	    
	    List getAllItems() {
			return _resolvers;
	        //return all items
	    }

}
