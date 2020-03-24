package hba;


import java.io.Serializable;
import java.util.Comparator;

import model.RecieptVoucherReportModel;

import org.zkoss.zul.GroupComparator;

public class ReceiptVoucherComparator implements Comparator<RecieptVoucherReportModel>,
		GroupComparator<RecieptVoucherReportModel>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(RecieptVoucherReportModel o1, RecieptVoucherReportModel o2) {
		return o1.getRvNumber().compareTo(o2.getRvNumber().toString());
	}

	public int compareGroup(RecieptVoucherReportModel o1, RecieptVoucherReportModel o2) {
		if (o1.getRvNumber().equals(o2.getRvNumber()))
			return 0;
		else
			return 1;
	}
}
