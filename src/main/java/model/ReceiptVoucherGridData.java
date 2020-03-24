package model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

public class ReceiptVoucherGridData {
	
	Calendar c = Calendar.getInstance();	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	private int recNo;
	
	private int LineNo;
	
	private int receiptVoucherKey;
	
	private String selectedPaymentMethod;
	
	private double amount;
	
	private Date checkDate;
	
	private String chequeNO;
	
	private BanksModel selectedBank;
	
	private AccountsModel seletedDepositeTo;
	
	private ClassModel selectedClass;
	
	private String memo;
	
	private AccountsModel seletedCuc;
	
	
	private boolean cucVisible;
	
	private boolean shouldContinue;
	
	
	private boolean readonlyForTYpeCash=true;

	public ReceiptVoucherGridData()
	{
		try {
			checkDate=df.parse(sdf.format(c.getTime()));
			cucVisible=false;
			readonlyForTYpeCash=false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public int getLineNo() {
		return LineNo;
	}

	public void setLineNo(int lineNo) {
		LineNo = lineNo;
	}

	public int getReceiptVoucherKey() {
		return receiptVoucherKey;
	}

	public void setReceiptVoucherKey(int receiptVoucherKey) {
		this.receiptVoucherKey = receiptVoucherKey;
	}

	public String getSelectedPaymentMethod() {
		return selectedPaymentMethod;
	}

	@NotifyChange({"readonlyForTYpeCash","checkDate","chequeNO","selectedBank"})
	public void setSelectedPaymentMethod(String selectedPaymentMethod) {
		this.selectedPaymentMethod = selectedPaymentMethod;
		if(selectedPaymentMethod.equalsIgnoreCase("Cash"))
		{
			readonlyForTYpeCash=true;
			checkDate=null;
			chequeNO="";
			selectedBank=null;
		}
		else
		{
			readonlyForTYpeCash=false;
			try {
				checkDate=df.parse(sdf.format(c.getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public double getAmount() {
		return amount;
	}

	@NotifyChange({"amount"})
	public void setAmount(double amount) {
		this.amount = Double.parseDouble(BigDecimal.valueOf(amount).toPlainString());
		if(amount>99999999999.99)
		{
			Messagebox.show("The Ammount Should be less than 99999999999.99 ","Receipt Voucher",Messagebox.OK,Messagebox.INFORMATION);
			this.amount=0.0;
		}
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	

	public String getChequeNO() {
		return chequeNO;
	}

	@NotifyChange({"chequeNO"})
	public void setChequeNO(String chequeNO) {
		this.chequeNO = chequeNO;
			if(chequeNO!=null && chequeNO.length()>20)
			{
				Messagebox.show("The Cheque Number should not exceed 20 Charchers ","Receipt Voucher",Messagebox.OK,Messagebox.INFORMATION);
				this.chequeNO ="";
			}
	}

	public BanksModel getSelectedBank() {
		return selectedBank;
	}

	public void setSelectedBank(BanksModel selectedBank) {
		this.selectedBank = selectedBank;
	}

	public AccountsModel getSeletedDepositeTo() {
		return seletedDepositeTo;
	}

	@NotifyChange({"cucVisible"})
	public void setSeletedDepositeTo(AccountsModel seletedDepositeTo) {
		this.seletedDepositeTo = seletedDepositeTo;
		if(seletedDepositeTo!=null && seletedDepositeTo.getAccountType().equalsIgnoreCase("Cheque Under Collection"))
				cucVisible=true;
			else
				cucVisible=false;
	
	}

	public ClassModel getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public AccountsModel getSeletedCuc() {
		return seletedCuc;
	}

	public void setSeletedCuc(AccountsModel seletedCuc) {
		this.seletedCuc = seletedCuc;
	}
	public boolean isCucVisible() {
		return cucVisible;
	}
	public void setCucVisible(boolean cucVisible) {
		this.cucVisible = cucVisible;
	}
	public boolean isReadonlyForTYpeCash() {
		return readonlyForTYpeCash;
	}
	public void setReadonlyForTYpeCash(boolean readonlyForTYpeCash) {
		this.readonlyForTYpeCash = readonlyForTYpeCash;
	}
	public boolean isShouldContinue() {
		return shouldContinue;
	}
	public void setShouldContinue(boolean shouldContinue) {
		this.shouldContinue = shouldContinue;
	}
	
	
	

}
