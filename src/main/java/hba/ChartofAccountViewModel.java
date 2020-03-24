package hba;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;
import layout.MenuModel;
import model.AccountsModel;
import model.CustomerModel;
import model.DataFilter;
import model.ImportExcelFiles;
import model.OtherNamesModel;
import model.QbListsModel;

public class ChartofAccountViewModel{

	private Logger logger = Logger.getLogger(this.getClass());
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private String uploadedFilePath;
	private boolean canPreview;
	private boolean canSave;
	private boolean canPreviewMapping;
	private boolean canPreviewFinalList;
	private boolean canPreviewtemperoryList;
	private String message;
	private String attFile;

	private String importIsSelected;

	private boolean imoprtNotSelected;

	private String importComapnyStandard;

	private boolean importAnyExlec;

	private Integer rowHeadderNo=0;

	private Integer rowDataNo=0;

	private String alreadyselectedfileName="";

	private  String listType;

	Media media;

	private List<List<OtherNamesModel>> browsedGriddata = new ArrayList<List<OtherNamesModel>>();
	private List<String> headers = new ArrayList<String>();
	private List<String> auxHeaderValues ;
	private List<ImportExcelFiles> headers1 = new ArrayList<ImportExcelFiles>();
	private String selectedItem;
	ImportExcelFiles importExcelFiles;
	DecimalFormat dcf=new DecimalFormat("0.00");
	Calendar calendr = Calendar.getInstance();
	ChartOFAccountData chartOfAccountData=new ChartOFAccountData();
	HBAData data = new HBAData();
	private List<AccountsModel> lstItems =new ArrayList<AccountsModel>();
	private LinkedList<AccountsModel> lstItemsTemp=new LinkedList<AccountsModel>();
	private List<AccountsModel> lstAllItems;
	private AccountsModel selectedItems;
	private DataFilter filter=new DataFilter();
	private String footer;
	private boolean adminUser;
	//private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;

	private List<String> lstStatus;
	private String selectedStatus;

	private List<String> lstSortBy;
	private String selectedSortBy;


	private MenuModel companyRole;

	public ChartofAccountViewModel()
	{
		try{
			FillStatusList();
			importComapnyStandard="A";
			importIsSelected="B";
			rowHeadderNo=4;
			rowDataNo=5;
			lstItems=chartOfAccountData.fillChartofAccounts("",false);
			lstAllItems=lstItems;
			if(lstItems.size()>0)
				selectedItems=lstItems.get(0);

			lstAllPageSize=new ArrayList<String>();
			lstAllPageSize.add("15");
			lstAllPageSize.add("30");
			lstAllPageSize.add("50");
			lstAllPageSize.add("All");
			selectedAllPageSize=lstAllPageSize.get(2);
			selectedPageSize=50;

			canSave=false;
			canPreview=false;
			canPreviewMapping=false;
			canPreviewFinalList=true;
			canPreviewtemperoryList=false;
			uploadedFilePath="";

			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				setAdminUser(dbUser.getFirstname().equals("admin"));
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> init", ex);
		}

	}

	private void FillStatusList()
	{
		lstStatus=new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		selectedStatus=lstStatus.get(0);

		lstSortBy=new ArrayList<String>();
		lstSortBy.add("Name");
		lstSortBy.add("Created Date");
		lstSortBy.add("Company Name");
		lstSortBy.add("Account Number");
		selectedSortBy=lstSortBy.get(0);

	}

	private List<String> FillHeadderList()
	{
		auxHeaderValues=new ArrayList<String>();
		auxHeaderValues.add("Account Type");
		auxHeaderValues.add("Main-Account Number");
		auxHeaderValues.add("Main-Account Name");
		auxHeaderValues.add("Account Arabic Name For All Levels");
		auxHeaderValues.add("Level1-Account Number");
		auxHeaderValues.add("Level1-Account Name");
		auxHeaderValues.add("Level2-Account Number");
		auxHeaderValues.add("Level2-Account Name");
		auxHeaderValues.add("Level3-Account Number");
		auxHeaderValues.add("Level3-Account Name");
		auxHeaderValues.add("Level4-Account Number");
		auxHeaderValues.add("Level4-Account Name");
		return auxHeaderValues;
	}

	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange({"attFile","canPreview","headers1","headers","browsedGriddata","disableHeadder"})
	public void uploadFile(BindContext ctx)
	{
		try
		{
			browsedGriddata.clear();
			headers1.clear();
			headers.clear();
			final String sortBy="Name";
			final String isactive="Y";
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();

			if(null!=importIsSelected && !importIsSelected.equalsIgnoreCase(""))
			{
				if(null!=importComapnyStandard && !importComapnyStandard.equalsIgnoreCase(""))
				{
					if(rowDataNo==0 || rowHeadderNo==0)
					{
						Messagebox.show("Please enter the Headder column No. and Data column No.","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
						return;
					}
				}
				else
				{
					Messagebox.show("Please select type of excel you want to import","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}

			}

			if(alreadyselectedfileName.equalsIgnoreCase(event.getMedia().getName()))
			{

				Messagebox.show("You have already Selected this file once.!!! Do you want to still you the Same file .?? !! ","Import Other Name List", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener()
						{
							public void onEvent(Event evt) throws InterruptedException
							{
								if (evt.getName().equals("onYes"))
								{
									browsedGriddata=new ArrayList<List<OtherNamesModel>>();
									lstItems=chartOfAccountData.fillChartofAccounts("",false);
									lstAllItems=lstItems;
									if(lstItems.size()>0)
										selectedItems=lstItems.get(0);
									headers1= new ArrayList<ImportExcelFiles>();
									headers=new ArrayList<String>();
								}
								else
								{

								}
							}

						});

			}
			String fileFormat=event.getMedia().getFormat();
			alreadyselectedfileName=event.getMedia().getName();
			logger.info("format >> "+fileFormat);
			logger.info(event.getMedia().getContentType());
			logger.info(event.getMedia().getName());
			logger.info("size>> " + event.getMedia().getByteData().length);

			if(fileFormat.equals("xlsx") || fileFormat.equals("xls"))
			{
				String filePath="";
				String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"OtherNames"+File.separator;
				//Session sess = Sessions.getCurrent();
				String sessID=(Executions.getCurrent()).getDesktop().getId();
				logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
				String dirPath=repository+sessID;//session.getId();
				File dir = new File(dirPath);

				if(!dir.exists())
					dir.mkdirs();

				filePath = dirPath +File.separator +"." +event.getMedia().getFormat();
				createFile(event.getMedia().getStreamData(), filePath);
				logger.info("filePath>> " + filePath);

				logger.info("File Uploaded");
				uploadedFilePath=filePath;
				attFile=event.getMedia().getName() + " is uploaded.";
				canPreview=true;
			}
			else
			{
				Messagebox.show("Please select Excel in format (xls,xslx) !! ","Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		catch (Exception ex)
		{
			Messagebox.show("Error Loading the File Please Try agin with realoding page","Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
			logger.error("ERROR in uploadFile ----> uploadFile", ex);
		}
	}

	private int createFile( InputStream is, String filePath)
	{
		int res=0;
		try
		{
			File file = new File(filePath);
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			int c;
			while((c = is.read()) != -1)
			{
				out.writeByte(c);
			}
			is.close();
			out.close();
		}
		catch(Exception ex)
		{
			res=1;
			Messagebox.show(ex.getMessage(),"Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		return res;
	}


	@Command
	@NotifyChange({"canPreviewtemperoryList","canPreviewFinalList","canPreview","canPreviewMapping","canSave","browsedGriddata","message"})
	public void updateGrid()
	{
		int index=0;
		lstItems.clear();
		try
		{
			for(ImportExcelFiles f1:headers1)
			{
				f1.setIndexSelected(index);
				index++;
			}
			AccountsModel mappingData;
			if(isValidHeadder(headers1))
			{
				for(List<OtherNamesModel> csv : browsedGriddata)
				{
					mappingData=new AccountsModel();
					if(!csv.isEmpty())
					{
						//mappingData.setCreatedDate(df.parse(sdf.format(calendr.getTime())));

						for(int i=0; i < csv.size(); i++)
						{
							for(ImportExcelFiles f2:headers1)
							{


								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AccountArabicNameForAllLevels"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setDescription(csv.get(i).getName());
									else
										mappingData.setDescription("");

								}

								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AccountType"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setAccountType(csv.get(i).getName());
									else
										mappingData.setAccountType("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Main-AccountNumber"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setMainAccountNo(csv.get(i).getName());
									else
										mappingData.setMainAccountNo("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Main-AccountName"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setMainAccountName(csv.get(i).getName());
									else
										mappingData.setMainAccountName("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level1-AccountNumber"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel1AccNo(csv.get(i).getName());
									else
										mappingData.setLevel1AccNo("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level1-AccountName"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel1AccName(csv.get(i).getName());
									else
										mappingData.setLevel1AccName("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level2-AccountNumber"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel2AccNo(csv.get(i).getName());
									else
										mappingData.setLevel2AccNo("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level2-AccountName"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel2AccName(csv.get(i).getName());
									else
										mappingData.setLevel2AccName("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level3-AccountNumber"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel3AccNo(csv.get(i).getName());
									else
										mappingData.setLevel3AccNo("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level3-AccountName"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel3AccName(csv.get(i).getName());
									else
										mappingData.setLevel3AccName("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level4-AccountNumber"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel4AccNo(csv.get(i).getName());
									else
										mappingData.setLevel4AccNo("");

								}
								if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Level4-AccountName"))
								{
									if(csv.get(i).getName()!=null)
										mappingData.setLevel4AccName(csv.get(i).getName());
									else
										mappingData.setLevel4AccName("");

								}

							}

						}

					}


					lstItems.add(mappingData);

				}
				if(chartOFAccountMapAndCalulation(lstItems))
				{
					Messagebox.show("The Selected Headders are mapped to the following columns successfully","Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
					canPreviewtemperoryList=false;
					canPreviewFinalList=true;
					canPreview=false;
					canSave=true;
					canPreviewMapping=true;
					message="";
				}
			}
			else{

			}
		}
		catch(Exception ex)
		{
			Messagebox.show("Error Mapping the File Please Try agin with realoding page","Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
		}
	}


	public boolean chartOFAccountMapAndCalulation(List<AccountsModel> newList)
	{
		for(AccountsModel customerModel:newList)
		{
			if(null!=customerModel.getMainAccountNo() && customerModel.getLevel1AccNo()!=null && customerModel.getLevel2AccNo()!=null && customerModel.getLevel3AccNo()!=null && customerModel.getLevel4AccNo()!=null && customerModel.getMainAccountNo().equalsIgnoreCase("") && customerModel.getLevel1AccNo().equalsIgnoreCase("") && customerModel.getLevel2AccNo().equalsIgnoreCase("") && customerModel.getLevel3AccNo().equalsIgnoreCase("") && customerModel.getLevel4AccNo().equalsIgnoreCase("")){
				logger.log(Level.INFO, "No records in Excel");
			}
			else if(customerModel.getMainAccountNo()!=null&& !customerModel.getMainAccountNo().equalsIgnoreCase(""))
			{

				if(customerModel.getLevel1AccNo()!=null && !customerModel.getLevel1AccNo().equalsIgnoreCase(""))
				{
					logger.log(Level.INFO, "level1");
				}
				else
				{
					customerModel.setName(customerModel.getMainAccountName());
					customerModel.setAccountNumber(customerModel.getMainAccountNo());
					customerModel.setAccountName(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName());
					customerModel.setFullName(customerModel.getMainAccountName());
					customerModel.setActLevels(customerModel.getFullName());
					customerModel.setSubLevel(0);
					customerModel.setaCTLEVELSwithNO(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName());
				}
			}
			else if(customerModel.getLevel1AccNo()!=null && !customerModel.getLevel1AccNo().equalsIgnoreCase("") && customerModel.getMainAccountNo().equalsIgnoreCase("")){

				if(customerModel.getLevel2AccNo()!=null && !customerModel.getLevel2AccNo().equalsIgnoreCase(""))
				{
					logger.log(Level.INFO, "level2");
				}
				else
				{
					AccountsModel customerModelrepv=getPrevious(customerModel);
					customerModel.setAccountType(customerModelrepv.getAccountType());
					customerModel.setMainAccountName(customerModelrepv.getMainAccountName());
					customerModel.setMainAccountNo(customerModelrepv.getMainAccountNo());
					customerModel.setName(customerModel.getLevel1AccName());
					customerModel.setAccountNumber(customerModel.getLevel1AccNo());
					customerModel.setSubLevel(1);
					customerModel.setAccountName(customerModel.getLevel1AccNo()+"·"+customerModel.getLevel1AccName());
					customerModel.setFullName(customerModelrepv.getMainAccountName()+":"+customerModel.getName());
					customerModel.setActLevels(customerModel.getFullName());
					customerModel.setaCTLEVELSwithNO(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName()+":"+customerModel.getLevel1AccNo()+"·"+customerModel.getLevel1AccName());
				}
			}
			else if(customerModel.getMainAccountNo().equalsIgnoreCase("")  && customerModel.getLevel1AccNo().equalsIgnoreCase("") && customerModel.getLevel2AccNo()!=null && !customerModel.getLevel2AccNo().equalsIgnoreCase("") ){

				if(customerModel.getLevel3AccNo()!=null && !customerModel.getLevel3AccNo().equalsIgnoreCase(""))
				{
					logger.log(Level.INFO, "level3");
				}
				else
				{
					AccountsModel customerModelrepv=getPrevious(customerModel);
					customerModel.setAccountType(customerModelrepv.getAccountType());
					customerModel.setMainAccountName(customerModelrepv.getMainAccountName());
					customerModel.setMainAccountNo(customerModelrepv.getMainAccountNo());
					customerModel.setLevel1AccName(customerModelrepv.getLevel1AccName());
					customerModel.setLevel1AccNo(customerModelrepv.getLevel1AccNo());
					customerModel.setSubLevel(2);
					customerModel.setName(customerModel.getLevel2AccName());
					customerModel.setAccountNumber(customerModel.getLevel2AccNo());
					customerModel.setAccountName(customerModel.getLevel2AccNo()+"·"+customerModel.getLevel2AccName());
					customerModel.setFullName(customerModelrepv.getMainAccountName()+":"+customerModel.getLevel1AccName()+":"+customerModel.getName());
					customerModel.setActLevels(customerModel.getFullName());
					customerModel.setaCTLEVELSwithNO(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName()+":"+customerModel.getLevel1AccNo()+"·"+customerModel.getLevel1AccName()+":"+customerModel.getLevel2AccNo()+"·"+customerModel.getLevel2AccName());
				}

			}
			else if(customerModel.getMainAccountNo().equalsIgnoreCase("") && customerModel.getLevel1AccNo().equalsIgnoreCase("") && customerModel.getLevel2AccNo().equalsIgnoreCase("") && customerModel.getLevel3AccNo()!=null && !customerModel.getLevel3AccNo().equalsIgnoreCase("")){

				if(customerModel.getLevel4AccNo()!=null &&  !customerModel.getLevel4AccNo().equalsIgnoreCase(""))
				{
					logger.log(Level.INFO, "level4");
				}
				else{
					AccountsModel customerModelrepv=getPrevious(customerModel);
					customerModel.setAccountType(customerModelrepv.getAccountType());
					customerModel.setMainAccountName(customerModelrepv.getMainAccountName());
					customerModel.setMainAccountNo(customerModelrepv.getMainAccountNo());
					customerModel.setLevel1AccName(customerModelrepv.getLevel1AccName());
					customerModel.setLevel1AccNo(customerModelrepv.getLevel1AccNo());
					customerModel.setLevel2AccName(customerModelrepv.getLevel2AccName());
					customerModel.setLevel2AccNo(customerModelrepv.getLevel2AccNo());
					customerModel.setName(customerModel.getLevel3AccName());
					customerModel.setAccountNumber(customerModel.getLevel3AccNo());
					customerModel.setSubLevel(3);
					customerModel.setAccountName(customerModel.getLevel3AccNo()+"·"+customerModel.getLevel3AccName());
					customerModel.setFullName(customerModelrepv.getMainAccountName()+":"+customerModel.getLevel1AccName()+":"+customerModel.getLevel2AccName()+":"+customerModel.getName());
					customerModel.setActLevels(customerModel.getFullName());
					customerModel.setaCTLEVELSwithNO(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName()+":"+customerModel.getLevel1AccNo()+"·"+customerModel.getLevel1AccName()+":"+customerModel.getLevel2AccNo()+"·"+customerModel.getLevel2AccName()+":"+customerModel.getLevel3AccNo()+"·"+customerModel.getLevel3AccName());
				}

			}
			else if(customerModel.getMainAccountNo().equalsIgnoreCase("") && customerModel.getLevel1AccNo().equalsIgnoreCase("") && customerModel.getLevel2AccNo().equalsIgnoreCase("") && customerModel.getLevel3AccNo().equalsIgnoreCase("") && !customerModel.getLevel4AccNo().equalsIgnoreCase("") && customerModel.getLevel4AccNo()!=null){

				AccountsModel customerModelrepv=getPrevious(customerModel);
				customerModel.setAccountType(customerModelrepv.getAccountType());
				customerModel.setMainAccountName(customerModelrepv.getMainAccountName());
				customerModel.setMainAccountNo(customerModelrepv.getMainAccountNo());
				customerModel.setLevel1AccName(customerModelrepv.getLevel1AccName());
				customerModel.setLevel1AccNo(customerModelrepv.getLevel1AccNo());
				customerModel.setLevel2AccName(customerModelrepv.getLevel2AccName());
				customerModel.setLevel2AccNo(customerModelrepv.getLevel2AccNo());
				customerModel.setLevel3AccName(customerModelrepv.getLevel3AccName());
				customerModel.setLevel3AccNo(customerModelrepv.getLevel3AccNo());
				customerModel.setSubLevel(4);
				customerModel.setName(customerModel.getLevel4AccName());
				customerModel.setAccountNumber(customerModel.getLevel4AccNo());
				customerModel.setAccountName(customerModel.getLevel4AccNo()+"·"+customerModel.getLevel4AccName());
				customerModel.setFullName(customerModelrepv.getMainAccountName()+":"+customerModel.getLevel1AccName()+":"+customerModel.getLevel2AccName()+":"+customerModel.getLevel3AccName()+":"+customerModel.getName());
				customerModel.setActLevels(customerModel.getFullName());
				customerModel.setaCTLEVELSwithNO(customerModel.getMainAccountNo()+"·"+customerModel.getMainAccountName()+":"+customerModel.getLevel1AccNo()+"·"+customerModel.getLevel1AccName()+":"+customerModel.getLevel2AccNo()+"·"+customerModel.getLevel2AccName()+":"+customerModel.getLevel3AccNo()+"·"+customerModel.getLevel3AccName()+":"+customerModel.getLevel4AccNo()+"·"+customerModel.getLevel4AccName());
			}
			else{

				// resultNew=data.OtherNameListInsertQbListQuery(customerModel,tmpRecNo);
			}
		}

		List<AccountsModel> chartOfAccountsFullNames=chartOfAccountData.getFullNameFromChartOfAccountForValidation();

		List<AccountsModel> chartOfAccountsAccountNumber=chartOfAccountData.getAccountNumberFromChartOfAccountForValidation();


		for(AccountsModel ValidationName:chartOfAccountsFullNames)
		{
			for(AccountsModel customerModel:newList)
			{
				if(ValidationName.getName().replaceAll("\\s","").equalsIgnoreCase(customerModel.getFullName().replaceAll("\\s","")))
				{
					message="You cannot Continue.!!!Your Imported Excel Conatins a Duplicated Account Name '"+customerModel.getFullName()+"' or more, Please Change the Account Name in Previewed Data";
					//Messagebox.show("You cannot Continue.!!!Your Imported Excel Conatins a Duplicated Account Name '"+customerModel.getFullName()+"' Please Change the Account Name in Previewed Data","Import OtherList", Messagebox.OK , Messagebox.EXCLAMATION);
					return false;
				}
			}
		}

		for(AccountsModel ValidationNumber:chartOfAccountsAccountNumber)
		{
			for(AccountsModel customerModel:newList)
			{
				if(ValidationNumber.getAccountNumber().replaceAll("\\s","").equalsIgnoreCase(customerModel.getAccountNumber().replaceAll("\\s","")))
				{
					message="You cannot Continue.!!!Your Imported Excel Conatins a Duplicated Account Number '"+customerModel.getAccountNumber()+"' Please Change the Account Number in Previewed Data";
					//Messagebox.show("You cannot Continue.!!!Your Imported Excel Conatins a Duplicated Account Number '"+customerModel.getAccountNumber()+"' Please Change the Account Number in Previewed Data","Import OtherList", Messagebox.OK , Messagebox.EXCLAMATION);
					return false;
				}
			}
		}
		for(AccountsModel customerModel:newList)
		{
			String chartOfAccountsAccountType=chartOfAccountData.getAccountTypeFromChartOfAccountForValidation(customerModel.getAccountType());
			if(chartOfAccountsAccountType!=null && chartOfAccountsAccountType.equalsIgnoreCase(""))
			{
				message="You cannot Continue.!!!Your Imported Excel Conatins a Diffrent Account Type"+customerModel.getAccountType()+"'";
				return false;
			}
		}

		return true;

	}



	@Command
	@NotifyChange({"attFile","canSave","message","browsedGriddata","headers","headers1","canPreviewMapping","canPreviewtemperoryList","canPreviewFinalList","canPreview"})
	public void previewDataFile()//saveFile()
	{
		try
		{
			message="";
			/* message="Note:The columns which are selected as 'Dont Map' will be ignored ";*/
			headers1=new ArrayList<ImportExcelFiles>();
			browsedGriddata=new ArrayList<List<OtherNamesModel>>();
			headers=new ArrayList<String>();
			if(uploadedFilePath.equals(""))
			{
				Messagebox.show("Please select Excel File !!","Chart Of Account List", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}

			else
			{
				FileInputStream fis = null;
				fis = new FileInputStream(uploadedFilePath);
				Workbook workbook = WorkbookFactory.create(fis);
				Sheet sheet = workbook.getSheetAt(0);
				Iterator rows = sheet.rowIterator();
				List<OtherNamesModel> OtherNmList = new ArrayList<OtherNamesModel>();
				importExcelFiles=new ImportExcelFiles();

				while (rows.hasNext())
				{
					Row row =  (Row) rows.next();
					boolean emptyRow=isEmptyRow(row);
					Iterator cells;
					if(emptyRow==false)
					{
						cells = row.cellIterator();

						ArrayList<OtherNamesModel> dataLine = new ArrayList<OtherNamesModel>();

						if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("B"))
						{
							if (row.getRowNum() == rowHeadderNo-1)
							{
								while (cells.hasNext())
								{
									Cell cell1 =  (Cell) cells.next();
									if(!sheet.isColumnHidden(cell1.getColumnIndex()))
									{
										cell1.setCellType(Cell.CELL_TYPE_STRING);
										headers.add(cell1.getStringCellValue());
										importExcelFiles=new ImportExcelFiles();
										importExcelFiles.setDropdowns(null);
										importExcelFiles.setSelectedStatus(FillHeadderList().get(1));
										importExcelFiles.setDropdowns(FillHeadderList());
										headers1.add(importExcelFiles);
									}
								}


							}
							else if(row.getRowNum() >= rowDataNo-1){

								while (cells.hasNext())
								{
									Cell cell1 =  (Cell) cells.next();
									if(!sheet.isColumnHidden(cell1.getColumnIndex()))
									{
										cell1.setCellType(Cell.CELL_TYPE_STRING);
										OtherNamesModel customerModel=new OtherNamesModel();
										customerModel.setName(cell1.getStringCellValue());
										dataLine.add(customerModel);
									}
								}
								browsedGriddata.add(dataLine);

							}
						}
						else if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("A")){
							if (row.getRowNum() == rowHeadderNo-1)
							{
								int i=0;
								while (cells.hasNext())
								{
									Cell cell1 =  (Cell) cells.next();
									if(!sheet.isColumnHidden(cell1.getColumnIndex()))
									{
										cell1.setCellType(Cell.CELL_TYPE_STRING);
										headers.add(cell1.getStringCellValue());
										importExcelFiles=new ImportExcelFiles();
										importExcelFiles.setDropdowns(null);
										importExcelFiles.setSelectedStatus(FillHeadderList().get(i));
										List<String> list=new ArrayList<String>();
										list.add(FillHeadderList().get(i));
										i=i+1;
										importExcelFiles.setDropdowns(list);
										headers1.add(importExcelFiles);
									}
								}

							}
							else if(row.getRowNum() >= rowDataNo-1){
								while (cells.hasNext())
								{
									Cell cell1 =  (Cell) cells.next();
									if(!sheet.isColumnHidden(cell1.getColumnIndex()))
									{
										cell1.setCellType(Cell.CELL_TYPE_STRING);
										OtherNamesModel customerModel=new OtherNamesModel();
										customerModel.setName(cell1.getStringCellValue());
										dataLine.add(customerModel);
									}
								}
								browsedGriddata.add(dataLine);

							}
						}
					}

				}

				//validate Data
				if(isDataValid(browsedGriddata))
				{
					logger.info("lst size >> " + OtherNmList.size());
					if(browsedGriddata.size()>0)
						// canSave=true;
						canPreview=false;
					canPreviewMapping=true;
					canPreviewFinalList=false;
					canPreviewtemperoryList=true;
				}
				else
				{
					message="There is missing data in file. Please make sure you first cloumns are headders and rest coulmns has data.";
					canPreviewMapping=false;
					canPreviewtemperoryList=false;
					canPreviewFinalList=true;

				}

			}
		}

		catch (Exception ex)
		{
			logger.error("ERROR in ImportEmployeeViewModel ----> saveFile", ex);
		}
	}

	static boolean isEmptyRow(Row row){
		boolean isEmptyRow = true;
		for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++){
			Cell cell = row.getCell(cellNum);
			if(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType()!= Cell.CELL_TYPE_FORMULA){
				isEmptyRow = false;
			}
		}
		return isEmptyRow;
	}

	private boolean isValidHeadder( List<ImportExcelFiles> headers1)
	{
		return true;
	}

	private boolean isDataValid( List<List<OtherNamesModel>> EmpList )
	{
		return true;
	}
	private Date convertToDate(String value)
	{
		Date result = null;
		try
		{
			value=value==null?"":value;
			if(!value.equals(""))
			{
				value=value.replace("-", "/");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				result=df.parse(value);
			}
		}
		catch (Exception ex)
		{
			logger.error("ERROR in Chart Of Account List ----> convertToDate", ex);
		}
		return result;
	}

	private List<AccountsModel> filterData()
	{
		lstItems=lstAllItems;
		List<AccountsModel>  lst=new ArrayList<AccountsModel>();
		for (Iterator<AccountsModel> i = lstItems.iterator(); i.hasNext();)
		{
			AccountsModel tmp=i.next();
			if(tmp.getAccountName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase())&&
					tmp.getAccountType().toLowerCase().contains(filter.getType().toLowerCase())
					&&
					tmp.getIsActive().toLowerCase().contains(filter.getIsactive().toLowerCase())
					)

			{
				lst.add(tmp);
			}
		}
		return lst;
	}

	@Command
	@NotifyChange({"lstItems","footer"})
	public void changeFilter()
	{
		lstItems=filterData();

	}

	public AccountsModel getNext(AccountsModel uid) {
		int idx = lstItems.indexOf(uid);
		if (idx < 0 || idx+1 == lstItems.size()) return null;
		return lstItems.get(idx + 1);
	}

	public AccountsModel getPrevious(AccountsModel uid) {
		int idx = lstItems.indexOf(uid);
		if (idx <= 0) return null;
		return lstItems.get(idx - 1);
	}

	@Command
	@NotifyChange({"lstItems","footer"})
	public void updateChartofAccountList()
	{
		int result=0;
		for(AccountsModel accountsModel:lstItems)
		{
			int tmpRecNo=chartOfAccountData.getChartOfAccountRecNoQuery();
			if(null==accountsModel.getMainAccountNo() && accountsModel.getLevel1AccNo()==null && accountsModel.getLevel2AccNo()==null && accountsModel.getLevel3AccNo()==null && accountsModel.getLevel4AccNo()==null){
				System.out.println("blank record skipped");
			}
			else
				result=chartOfAccountData.chartOfAccountInsertQuery(accountsModel,tmpRecNo);
		}
		if(result>0)
			Messagebox.show("Chat Of Account List is Updated..","Chat Of Account List",Messagebox.OK , Messagebox.INFORMATION);
		Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
		Center center = bl.getCenter();
		Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
		tabbox.getSelectedPanel().getLastChild().invalidate();
	}



	@Command
	public void resetChartOfAccount()
	{
		try
		{
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> resetChartOfAccount", ex);
		}
	}
	@Command
	public void viewChartofAccountCommand(@BindingParam("row") AccountsModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("charOfaccountKey", row.getRec_No());
			arg.put("compKey",0);
			arg.put("type","view");
			Executions.createComponents("/hba/list/editChartOfAccount.zul", null,arg);
		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> viewChartofAccountCommand", ex);
		}
	}

	@Command
	public void editChartOfAccountCommand(@BindingParam("row") AccountsModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("charOfaccountKey", row.getRec_No());
			arg.put("compKey",0);
			arg.put("type","edit");
			Executions.createComponents("/hba/list/editChartOfAccount.zul", null,arg);
		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> editChartOfAccountCommand", ex);
		}
	}

	@Command
	public void addChartOfAccountCommand()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("charOfaccountKey", 0);
			arg.put("compKey",0);
			arg.put("type","Add");
			Executions.createComponents("/hba/list/editChartOfAccount.zul", null,arg);
		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> addChartOfAccountCommand", ex);
		}
	}

	@GlobalCommand
	@NotifyChange({"lstItems","selectedItems"})
	public void refreshParentChart(@BindingParam("slectedGridId")AccountsModel selectedRecord)
	{
		try
		{
			lstItems=chartOfAccountData.fillChartofAccounts("",false);
			lstAllItems=lstItems;
			for (AccountsModel accountSearch : lstItems) {
				if(accountSearch.getAccountName().equalsIgnoreCase(selectedRecord.getAccountName()))
				{
					selectedItems=lstItems.get(lstItems.indexOf(accountSearch));
					break;
				}
			}

		}
		catch (Exception ex)
		{
			logger.error("ERROR in ChartofAccountViewModel ----> refreshParent", ex);
		}
	}




	public List<AccountsModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<AccountsModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<AccountsModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<AccountsModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public AccountsModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(AccountsModel selectedItems) {
		this.selectedItems = selectedItems;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}


	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	@NotifyChange({"selectedPageSize"})
	public void setSelectedAllPageSize(String selectedAllPageSize)
	{
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equals("All"))
			selectedPageSize=lstItems.size();
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}

	/**
	 * @return the uploadedFilePath
	 */
	public String getUploadedFilePath() {
		return uploadedFilePath;
	}

	/**
	 * @param uploadedFilePath the uploadedFilePath to set
	 */
	public void setUploadedFilePath(String uploadedFilePath) {
		this.uploadedFilePath = uploadedFilePath;
	}

	/**
	 * @return the canPreview
	 */
	public boolean isCanPreview() {
		return canPreview;
	}

	/**
	 * @param canPreview the canPreview to set
	 */
	public void setCanPreview(boolean canPreview) {
		this.canPreview = canPreview;
	}

	/**
	 * @return the canSave
	 */
	public boolean isCanSave() {
		return canSave;
	}

	/**
	 * @param canSave the canSave to set
	 */
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	/**
	 * @return the canPreviewMapping
	 */
	public boolean isCanPreviewMapping() {
		return canPreviewMapping;
	}

	/**
	 * @param canPreviewMapping the canPreviewMapping to set
	 */
	public void setCanPreviewMapping(boolean canPreviewMapping) {
		this.canPreviewMapping = canPreviewMapping;
	}

	/**
	 * @return the canPreviewtemperoryList
	 */
	public boolean isCanPreviewtemperoryList() {
		return canPreviewtemperoryList;
	}

	/**
	 * @param canPreviewtemperoryList the canPreviewtemperoryList to set
	 */
	public void setCanPreviewtemperoryList(boolean canPreviewtemperoryList) {
		this.canPreviewtemperoryList = canPreviewtemperoryList;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the attFile
	 */
	public String getAttFile() {
		return attFile;
	}

	/**
	 * @param attFile the attFile to set
	 */
	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}

	/**
	 * @return the importIsSelected
	 */
	public String getImportIsSelected() {
		return importIsSelected;
	}

	/**
	 * @param importIsSelected the importIsSelected to set
	 */
	public void setImportIsSelected(String importIsSelected) {
		this.importIsSelected = importIsSelected;
	}

	/**
	 * @return the imoprtNotSelected
	 */
	public boolean isImoprtNotSelected() {
		return imoprtNotSelected;
	}

	/**
	 * @param imoprtNotSelected the imoprtNotSelected to set
	 */
	public void setImoprtNotSelected(boolean imoprtNotSelected) {
		this.imoprtNotSelected = imoprtNotSelected;
	}

	/**
	 * @return the importComapnyStandard
	 */
	public String getImportComapnyStandard() {
		return importComapnyStandard;
	}

	/**
	 * @param importComapnyStandard the importComapnyStandard to set
	 */
	@NotifyChange({"rowHeadderNo","rowDataNo",""})
	public void setImportComapnyStandard(String importComapnyStandard) {

		if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("A"))
		{
			rowHeadderNo=4;
			rowDataNo=5;
		}
		if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("B"))
		{
			rowHeadderNo=0;
			rowDataNo=0;
		}

		this.importComapnyStandard = importComapnyStandard;
	}
	/**
	 * @return the importAnyExlec
	 */
	public boolean isImportAnyExlec() {
		return importAnyExlec;
	}

	/**
	 * @param importAnyExlec the importAnyExlec to set
	 */
	public void setImportAnyExlec(boolean importAnyExlec) {
		this.importAnyExlec = importAnyExlec;
	}

	/**
	 * @return the rowHeadderNo
	 */
	public Integer getRowHeadderNo() {
		return rowHeadderNo;
	}

	/**
	 * @param rowHeadderNo the rowHeadderNo to set
	 */
	public void setRowHeadderNo(Integer rowHeadderNo) {
		this.rowHeadderNo = rowHeadderNo;
	}

	/**
	 * @return the rowDataNo
	 */
	public Integer getRowDataNo() {
		return rowDataNo;
	}

	/**
	 * @param rowDataNo the rowDataNo to set
	 */
	public void setRowDataNo(Integer rowDataNo) {
		this.rowDataNo = rowDataNo;
	}

	/**
	 * @return the listType
	 */
	public String getListType() {
		return listType;
	}

	/**
	 * @param listType the listType to set
	 */
	public void setListType(String listType) {
		this.listType = listType;
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return the browsedGriddata
	 */
	public List<List<OtherNamesModel>> getBrowsedGriddata() {
		return browsedGriddata;
	}

	/**
	 * @param browsedGriddata the browsedGriddata to set
	 */
	public void setBrowsedGriddata(List<List<OtherNamesModel>> browsedGriddata) {
		this.browsedGriddata = browsedGriddata;
	}

	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the auxHeaderValues
	 */
	public List<String> getAuxHeaderValues() {
		return auxHeaderValues;
	}

	/**
	 * @param auxHeaderValues the auxHeaderValues to set
	 */
	public void setAuxHeaderValues(List<String> auxHeaderValues) {
		this.auxHeaderValues = auxHeaderValues;
	}

	/**
	 * @return the headers1
	 */
	public List<ImportExcelFiles> getHeaders1() {
		return headers1;
	}

	/**
	 * @param headers1 the headers1 to set
	 */
	public void setHeaders1(List<ImportExcelFiles> headers1) {
		this.headers1 = headers1;
	}

	/**
	 * @return the selectedItem
	 */
	public String getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	/**
	 * @return the importExcelFiles
	 */
	public ImportExcelFiles getImportExcelFiles() {
		return importExcelFiles;
	}

	/**
	 * @param importExcelFiles the importExcelFiles to set
	 */
	public void setImportExcelFiles(ImportExcelFiles importExcelFiles) {
		this.importExcelFiles = importExcelFiles;
	}

	/**
	 * @return the canPreviewFinalList
	 */
	public boolean isCanPreviewFinalList() {
		return canPreviewFinalList;
	}

	/**
	 * @param canPreviewFinalList the canPreviewFinalList to set
	 */
	public void setCanPreviewFinalList(boolean canPreviewFinalList) {
		this.canPreviewFinalList = canPreviewFinalList;
	}

	/**
	 * @return the alreadyselectedfileName
	 */
	public String getAlreadyselectedfileName() {
		return alreadyselectedfileName;
	}

	/**
	 * @param alreadyselectedfileName the alreadyselectedfileName to set
	 */
	public void setAlreadyselectedfileName(String alreadyselectedfileName) {
		this.alreadyselectedfileName = alreadyselectedfileName;
	}

	/**
	 * @return the lstStatus
	 */
	public List<String> getLstStatus() {
		return lstStatus;
	}

	/**
	 * @param lstStatus the lstStatus to set
	 */
	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param selectedStatus the selectedStatus to set
	 */
	@NotifyChange({"lstItems"})
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equalsIgnoreCase("Active"))
			status="Y";
		else if(selectedStatus.equalsIgnoreCase("Inactive"))
			status="N";
		else if(selectedStatus.equalsIgnoreCase("All"))
			status="";
		lstItems=chartOfAccountData.fillChartofAccounts(status,false);
		lstAllItems=lstItems;
		if(lstItems.size()>0)
			selectedItems=lstItems.get(0);
	}

	/**
	 * @return the lstSortBy
	 */
	public List<String> getLstSortBy() {
		return lstSortBy;
	}

	/**
	 * @param lstSortBy the lstSortBy to set
	 */
	public void setLstSortBy(List<String> lstSortBy) {
		this.lstSortBy = lstSortBy;
	}

	/**
	 * @return the selectedSortBy
	 */
	public String getSelectedSortBy() {
		return selectedSortBy;
	}

	/**
	 * @param selectedSortBy the selectedSortBy to set
	 */
	public void setSelectedSortBy(String selectedSortBy) {
		this.selectedSortBy = selectedSortBy;
	}

	/**
	 * @return the companyRole
	 */
	public MenuModel getCompanyRole() {
		return companyRole;
	}

	/**
	 * @param companyRole the companyRole to set
	 */
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}


	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles)
		{
			if(item.getMenuid()==76)
			{
				companyRole=item;
				break;
			}
		}
	}


}
