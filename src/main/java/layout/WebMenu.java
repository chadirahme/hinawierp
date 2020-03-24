package layout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "webmenu")
public class WebMenu {

	
	private int menuid;
	private String title;
	private int level;
	private String href;
	private int parentid;
	
	//@OneToMany(mappedBy="WebMenu", cascade=CascadeType.ALL) 
	//@JoinTable(name = "UserMenu")
	//private Set<UserMenu> hsUserMenu;
	
	//@ManyToOne
	//@JoinColumn(name="menuid")
	//private UserMenu usermenu;
	
	
	
		
		
		private List<UserMenu> lstUserMenu;
		
		//@OneToMany(cascade={CascadeType.ALL})
		//@JoinColumn(name="menuid")
		//@IndexColumn(name="idx")
		
		@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="webmenu")
		public List<UserMenu> getLstUserMenu() {
			return lstUserMenu;
		}
		public void setLstUserMenu(List<UserMenu> lstUserMenu) {
			this.lstUserMenu = lstUserMenu;
		}
		
		
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menuid")
	public int getMenuid() {
		return menuid;
	}
	public void setMenuid(int menuId) {
		menuid = menuId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
	
		
}
