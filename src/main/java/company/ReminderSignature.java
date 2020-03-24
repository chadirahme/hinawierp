package company;



import java.sql.ResultSet;
import java.util.Map;

import model.EmailSignatureModel;
import model.ReminderSettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import db.DBHandler;

public class ReminderSignature {

	WebusersModel dbUser = null;

	//HBAData hbadata = new HBAData();

	//CustomerFeedBackData feedbackData = null;

	private Logger logger = Logger.getLogger(this.getClass());

	EmailSignatureModel emailSignatureModel = new EmailSignatureModel();

	private String signature = "";

	private ReminderSettingsModel selectedReminderTypes=new ReminderSettingsModel();
	
	EmailSignatureModel selectedemailSignature= new EmailSignatureModel();

	ReminderData data=new ReminderData();

	@Init
	public void init() {
		try
		{

			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/reminderEmailSignature");

			if(type.equalsIgnoreCase("add"))
			{
				win.setTitle("Company Signature");
			}
			selectedReminderTypes=(ReminderSettingsModel)map.get("selectedReminderType");
			selectedemailSignature=(EmailSignatureModel)map.get("slectedSignature");
			
			if(selectedemailSignature!=null)
			{
				signature=selectedemailSignature.getSignature();
			}
		}
		catch(Exception e)
		{
			logger.error("ERROR in ReminderSignature ----> init", e);			
		}
	}

	public EmailSignatureModel getEmailSignature(int userId) {

		//return feedbackData.getEmailSignature(userId);
		return  null;
	}

	@Command
	public void saveEmailSignatures(@BindingParam("cmp") Window x) {
		try {

			if(selectedReminderTypes!=null)
			{
				if(signature!=null && signature.equalsIgnoreCase(""))
				{
					Clients.showNotification("Signature is Empty.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}

				int result = 0;
				EmailSignatureModel emailSignature= new EmailSignatureModel();
				emailSignature.setReminderId(selectedReminderTypes.getReminderid());
				emailSignature.setCompanyId(dbUser.getCompanyid());
				emailSignature.setSignature(signature);
				emailSignature.setReminderName(selectedReminderTypes.getRemindername());
				emailSignature.setUserId(selectedemailSignature.getUserId());
				
				int reminderId = selectedemailSignature.getReminderId();
				if (reminderId > 0) {
					result = data.updateSignature(emailSignature);
				} else {
					result = data.addSignature(emailSignature);
				}
				if (result > 0) {

					if (reminderId > 0) {
						Messagebox.show("Signature is updated successfully.",
								"Save Signature", Messagebox.OK,
								Messagebox.INFORMATION);
					} else {
						Messagebox.show("Signature is saved successfully.",
								"Save Signature", Messagebox.OK,
								Messagebox.INFORMATION);

					}
				} else
					Messagebox.show("Error at save Signature  !! ",		"Save Signature", Messagebox.OK, Messagebox.ERROR);
				x.detach();
			}

		} catch (Exception ex) {
			logger.error(
					"error at ReminderSignature>>saveEmailSignatures>> ", ex);
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
