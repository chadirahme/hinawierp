package hba;

import java.io.Serializable;
import java.util.Comparator;

import model.CashInvoiceModel;

import org.zkoss.zul.GroupComparator;

public class QuotationComparator implements Comparator<CashInvoiceModel>,
GroupComparator<CashInvoiceModel>, Serializable {
private static final long serialVersionUID = 1L;

public int compare(CashInvoiceModel o1, CashInvoiceModel o2) {
return o1.getInvoiceSaleNo().compareTo(o2.getInvoiceSaleNo().toString());
}

public int compareGroup(CashInvoiceModel o1, CashInvoiceModel o2) {
if (o1.getInvoiceSaleNo().equals(o2.getInvoiceSaleNo()))
	return 0;
else
	return 1;
}
}
