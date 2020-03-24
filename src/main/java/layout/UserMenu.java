package layout;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "usermenu")
public class UserMenu 
{

	private int usermenuid;
	private int userid;
	private int menuid;
	private int menuorder;
	private int level;
	private WebMenu webmenu;
	//@ManyToOne
	//@JoinColumn(name = "menuid")
	//private WebMenu webmenu;
	
	//@Id
    //@Column(name="menuid")
   // @GeneratedValue(generator="gen")
    //@GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="usermenu"))
	//private int menuid;
    
	//@OneToMany(cascade={CascadeType.ALL})
	//@JoinColumn(name="menuid")
	//@IndexColumn(name="idx")
	
	//@OneToMany(mappedBy="usermenu")
	//private Set<WebMenu> lstWebMenu;
	
	
	/*
	@ManyToOne
	@JoinColumn(name="menuid", 
				insertable=false, updatable=false, 
				nullable=false)
	private WebMenu webmenu;
	public WebMenu getWebmenu() {
		return webmenu;
	}
	public void setWebmenu(WebMenu webmenu) {
		this.webmenu = webmenu;
	}
	*/
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usermenuid")
	public int getUsermenuid() {
		return usermenuid;
	}
	public void setUsermenuid(int usermenuid) {
		this.usermenuid = usermenuid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getMenuid() {
		return menuid;
	}
	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}
	public int getMenuorder() {
		return menuorder;
	}
	public void setMenuorder(int menuorder) {
		this.menuorder = menuorder;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="menuid",insertable=false, updatable=false)
	public WebMenu getWebmenu() {
		return webmenu;
	}
	public void setWebmenu(WebMenu webmenu) {
		this.webmenu = webmenu;
	}
	
	
	
	
}
