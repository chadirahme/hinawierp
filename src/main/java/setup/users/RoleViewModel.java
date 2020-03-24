package setup.users;

import java.util.List;
import java.util.Set;

import layout.MenuModel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class RoleViewModel 
{

	RoleData _data;
	private ListModelList<MenuModel> lstMainMenu;
	private ListModelList<MenuModel> lstSubMenu;
	
	private Boolean checked; 
	private Set<MenuModel> selectedEntities;
	//private ListModelList<MenuModel> lstMenuModel;
	public RoleViewModel()
	{
		_data=new RoleData();		
		lstMainMenu= new ListModelList<MenuModel>(_data.getMenuList(0));
		lstMainMenu.setMultiple(true);
	}
	public List<MenuModel> getLstMainMenu() {
		return lstMainMenu;
	}
	public void setLstMainMenu(ListModelList<MenuModel> lstMainMenu) {
		this.lstMainMenu = lstMainMenu;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Set<MenuModel> getSelectedEntities() {
		return selectedEntities;
	}
	public void setSelectedEntities(Set<MenuModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}
	
	  @Command
	  public void show(){
	   // if(selectedEntities!=null)
	   // Messagebox.show("select item count = "+selectedEntities.size());
		  List<MenuModel> lst= _data.getDBMenuList(0);
		  Messagebox.show("select item count = "+lst.size());
	  }
		public ListModelList<MenuModel> getLstSubMenu() {
			return lstSubMenu;
		}
		public void setLstSubMenu(ListModelList<MenuModel> lstSubMenu) {
			this.lstSubMenu = lstSubMenu;
		}
		
		 @Command
		 @NotifyChange("lstSubMenu") 
		  public void showSubMenu(@BindingParam("todo") MenuModel todo)
		 {
			 _data=new RoleData();		
			 lstSubMenu= new ListModelList<MenuModel>(_data.getMenuList(todo.getLevel()));
			 lstSubMenu.setMultiple(true);
		 }
		 
}
