package setup.users;

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


@Entity
@Table(name = "webusers")
public class Webusers implements java.io.Serializable {

	private int userid;
	private String username;
	private String userpwd;
	private String firstname;
	private String lastname;
	private String useremail;
	private String usermobile;
	
	private String userip;
	private String dbname;
	private String dbuser;
	private String dbpwd;
	
	
	 //@ManyToOne
	 //@JoinColumn(name="roleid")
	 

	 private UserRole userrole;
	 
	 @ManyToOne(fetch=FetchType.LAZY)
     @JoinColumn(name="roleid")
	 public UserRole getUserrole() 
	 {
			return userrole;
	 }
	  public void setUserrole(UserRole userrole) 
	  {
	    this.userrole = userrole;
	  }
		
	
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getUsermobile() {
		return usermobile;
	}
	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbuser() {
		return dbuser;
	}
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}
	public String getDbpwd() {
		return dbpwd;
	}
	public void setDbpwd(String dbpwd) {
		this.dbpwd = dbpwd;
	}
	
	
	
}
