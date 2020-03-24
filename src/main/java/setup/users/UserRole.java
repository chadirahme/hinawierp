package setup.users;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "userrole")
public class UserRole implements java.io.Serializable {

	
		
	private int roleid;
	
	private String roletitle;

	
	private List<Webusers> webUserses ;//= new HashSet<Webusers>(0);
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="userrole")
    public List<Webusers> getWebUserses() {
        return this.webUserses;
    }
    
    public void setWebUserses(List<Webusers> webUserses) {
        this.webUserses = webUserses;
    }
    
	/*
	@OneToMany(mappedBy = "Webusers")//(cascade={CascadeType.ALL})
	@JoinColumn(name="roleid")
	@IndexColumn(name="idx")
	private List<Webusers> lstWebUsers;
	*/

    //@Id     
    //@Column(name="roleid", unique=true, nullable=false)

    @Id
	@GeneratedValue//(strategy = GenerationType.IDENTITY)
	@Column(name = "roleid")
    public int getRoleid() {
	return roleid;
}
public void setRoleid(int roleid) {
	this.roleid = roleid;
}
public String getRoletitle() {
	return roletitle;
}
public void setRoletitle(String roletitle) {
	this.roletitle = roletitle;
}



}
