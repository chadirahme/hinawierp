package home;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EmailSelectionModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class CustomerEmailSelection {

	private Logger logger = Logger.getLogger(CustomerEmailSelection.class);
	
	private boolean selectAllTo;
	
	private boolean selectedAllCc;
	
	private boolean selectedAllBcc;
	

	List<EmailSelectionModel> emailSelectionPopUpList = new ArrayList<EmailSelectionModel>();
	
	List<EmailSelectionModel> tempEmailSelectionPopUpList = new ArrayList<EmailSelectionModel>();
	
	List<EmailSelectionModel> selectedEmailSelectionPopUp=new ArrayList<EmailSelectionModel>();

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public CustomerEmailSelection() {
		try {
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			emailSelectionPopUpList.addAll(((List) map.get("listOfemails")));
			Session sess = Sessions.getCurrent();
			Sessions.getCurrent().setAttribute("tempSelectionEmailList", ((List) map.get("listOfemails")));
			String type = (String) map.get("type");
		} catch (Exception ex) {
			logger.error("ERROR in CustomerEmailSelection ----> int", ex);
		}

	}
	
	@Command
	public void getSelectedEmails(@BindingParam("cmp") Window x)
	{		
		try
		{
			Map args = new HashMap();
			args.put("selectedEmailSelectionPopUp", emailSelectionPopUpList);
			BindUtils.postGlobalCommand(null, null,"getPopUpEmailList", args);
			x.detach();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerEmailSelection ----> getSelectedEmails", ex);			
		}
	}

	/**
	 * @return the emailSelectionPopUpList
	 */
	public List<EmailSelectionModel> getEmailSelectionPopUpList() {
		return emailSelectionPopUpList;
	}

	/**
	 * @param emailSelectionPopUpList the emailSelectionPopUpList to set
	 */
	public void setEmailSelectionPopUpList(
			List<EmailSelectionModel> emailSelectionPopUpList) {
		this.emailSelectionPopUpList = emailSelectionPopUpList;
	}

	/**
	 * @return the selectedEmailSelectionPopUp
	 */
	public List<EmailSelectionModel> getSelectedEmailSelectionPopUp() {
		return selectedEmailSelectionPopUp;
	}

	/**
	 * @param selectedEmailSelectionPopUp the selectedEmailSelectionPopUp to set
	 */
	public void setSelectedEmailSelectionPopUp(
			List<EmailSelectionModel> selectedEmailSelectionPopUp) {
		this.selectedEmailSelectionPopUp = selectedEmailSelectionPopUp;
	}

	/**
	 * @return the selectAllTo
	 */
	public boolean isSelectAllTo() {
		return selectAllTo;
	}

	/**
	 * @param selectAllTo the selectAllTo to set
	 */
	@NotifyChange({"emailSelectionPopUpList"})
	public void setSelectAllTo(boolean selectAllTo) {
		this.selectAllTo = selectAllTo;
		if(selectAllTo)
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setTo(true);
			}
		}
		else
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setTo(false);
			}
		}
	}

	/**
	 * @return the selectedAllCc
	 */
	public boolean isSelectedAllCc() {
		return selectedAllCc;
	}

	/**
	 * @param selectedAllCc the selectedAllCc to set
	 */
	@NotifyChange({"emailSelectionPopUpList"})
	public void setSelectedAllCc(boolean selectedAllCc) {
		this.selectedAllCc = selectedAllCc;
		if(selectedAllCc)
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setCc(true);
			}
		}
		else
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setCc(false);
			}
		}
	}

	/**
	 * @return the selectedAllBcc
	 */
	public boolean isSelectedAllBcc() {
		return selectedAllBcc;
	}

	/**
	 * @param selectedAllBcc the selectedAllBcc to set
	 */
	@NotifyChange({"emailSelectionPopUpList"})
	public void setSelectedAllBcc(boolean selectedAllBcc) {
		this.selectedAllBcc = selectedAllBcc;
		if(selectedAllBcc)
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setBcc(true);
			}
		}
		else
		{
			for(EmailSelectionModel model:emailSelectionPopUpList)
			{
				model.setBcc(false);
			}
		}
	}


	@Command
	@NotifyChange({"emailSelectionPopUpList"})
	public void setDefault()
	{		
		try
		{
			Session sess = Sessions.getCurrent();
			emailSelectionPopUpList=(List<EmailSelectionModel>)sess.getAttribute("tempSelectionEmailList");
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerEmailSelection ----> setDefault", ex);			
		}
	}

	/**
	 * @return the tempEmailSelectionPopUpList
	 */
	public List<EmailSelectionModel> getTempEmailSelectionPopUpList() {
		return tempEmailSelectionPopUpList;
	}

	/**
	 * @param tempEmailSelectionPopUpList the tempEmailSelectionPopUpList to set
	 */
	public void setTempEmailSelectionPopUpList(
			List<EmailSelectionModel> tempEmailSelectionPopUpList) {
		this.tempEmailSelectionPopUpList = tempEmailSelectionPopUpList;
	}
	
	

	
}
