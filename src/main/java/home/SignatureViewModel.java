package home;


import hba.HBAQueries;

import java.sql.ResultSet;
import java.util.Map;

import model.CompanyDBModel;
import model.EmailSignatureModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import db.DBHandler;
import setup.users.WebusersModel;

public class SignatureViewModel {

	private int webUserID = 0;

	WebusersModel dbUser = null;

	private boolean adminUser;

	private int supervisorID = 0;
	private int employeeKey = 0;

	//HBAData hbadata = new HBAData();

	//CustomerFeedBackData feedbackData = null;

	private Logger logger = Logger.getLogger(this.getClass());

	EmailSignatureModel emailSignatureModel = new EmailSignatureModel();

	private String signature = "";

	@Init
	public void init(@BindingParam("type") String type) {
		try
		{
			
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		Session sess = Sessions.getCurrent();
		dbUser = (WebusersModel) sess.getAttribute("Authentication");
		DBHandler mysqldb=new DBHandler();
		ResultSet rs=null;
		CompanyDBModel obj=new CompanyDBModel();
		HBAQueries query = new HBAQueries();
		rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
				.getCompanyid()));
		while (rs.next()) {
			obj.setCompanyId(rs.getInt("companyid"));
			obj.setDbid(rs.getInt("dbid"));
			obj.setUserip(rs.getString("userip"));
			obj.setDbname(rs.getString("dbname"));
			obj.setDbuser(rs.getString("dbuser"));
			obj.setDbpwd(rs.getString("dbpwd"));
			obj.setDbtype(rs.getString("dbtype"));
		}
		//feedbackData=new CustomerFeedBackData(obj);

		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");

			if (adminUser) {
				webUserID = 0;
			} else {
				webUserID = dbUser.getUserid();
			}
		}

		supervisorID = dbUser.getSupervisor();// logged in as supervisor
		employeeKey = dbUser.getEmployeeKey();
		if (employeeKey > 0)
			supervisorID = employeeKey;// logged in as employee

		if (supervisorID > 0)
			webUserID = supervisorID;

		WebusersModel dbUser = (WebusersModel) sess
				.getAttribute("Authentication");
		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");

			emailSignatureModel = getEmailSignature(webUserID);

			if (emailSignatureModel != null
					&& !emailSignatureModel.getSignature().equalsIgnoreCase("")) {
				signature = emailSignatureModel.getSignature();
			} else {
				signature = "";
			}

		}
		}
		catch(Exception e)
		{
			logger.error("ERROR in SignatureViewModel ----> init", e);			
		}
	}

	public EmailSignatureModel getEmailSignature(int userId) {

		return null;
		//return feedbackData.getEmailSignature(userId);
	}

	@Command
	public void saveEmailSignatures(@BindingParam("cmp") Window x) {
		try {

			int result = 0;
			int recNo = emailSignatureModel.getRecNo();
			if (emailSignatureModel.getRecNo() > 0)
				emailSignatureModel.setRecNo(emailSignatureModel.getRecNo());
			else
//				emailSignatureModel.setRecNo(hbadata.getMaxID(
//						"feedbackSendSignature", "recNo"));
			emailSignatureModel.setUserId(webUserID);
			emailSignatureModel.setSignature(signature);
			if (recNo > 0) {
			//	result = feedbackData.updateSignature(emailSignatureModel);
			} else {
				//result = feedbackData.addSignature(emailSignatureModel);
			}
			if (result > 0) {

				if (recNo > 0) {
					Messagebox.show("Signature is updated successfully.",
							"Save Signature", Messagebox.OK,
							Messagebox.INFORMATION);
				} else {
					Messagebox.show("Signature is saved successfully.",
							"Save Signature", Messagebox.OK,
							Messagebox.INFORMATION);

				}
			} else
				Messagebox.show("Error at save Signature  !! ",
						"Save Signature", Messagebox.OK, Messagebox.ERROR);
			  x.detach();
			
		} catch (Exception ex) {
			logger.error(
					"error at CustomerFeebackSend>>saveEmailSignatures>> ", ex);
		}
	}

	/**
	 * @return the emailSignatureModel
	 */
	public EmailSignatureModel getEmailSignatureModel() {
		return emailSignatureModel;
	}

	/**
	 * @param emailSignatureModel
	 *            the emailSignatureModel to set
	 */
	public void setEmailSignatureModel(EmailSignatureModel emailSignatureModel) {
		this.emailSignatureModel = emailSignatureModel;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
