package company;

import java.util.Comparator;
import java.util.List;

import layout.MenuModel;

import org.zkoss.exporter.util.GroupsModelArrayAdapter;

public class MenuGroupAdapter extends GroupsModelArrayAdapter<MenuModel, String[], String[], Object> 
{

	private boolean showGroup;
	public MenuGroupAdapter(List<MenuModel> data,Comparator<MenuModel> cmpr,boolean showGroup)
	{
		 super(data.toArray(new MenuModel[0]), cmpr);
		 this.showGroup = showGroup;
		
	}
	
	protected String[] createGroupHead(MenuModel[]  groupdata, int index, int col)
	{
		 String[] header=new String[8];
		  if (groupdata.length > 0) 
	      {
			header[0]=groupdata[0].getTitle();  
	      }
		 return header;
	}
}
