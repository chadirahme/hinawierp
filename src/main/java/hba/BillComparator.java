package hba;

import java.io.Serializable;
import java.util.Comparator;

import model.BillReportModel;

import org.zkoss.zul.GroupComparator;

public class BillComparator implements Comparator<BillReportModel>,
GroupComparator<BillReportModel>, Serializable {
private static final long serialVersionUID = 1L;

public int compare(BillReportModel o1, BillReportModel o2) {
return o1.getBillNo().compareTo(o2.getBillNo().toString());
}

public int compareGroup(BillReportModel o1, BillReportModel o2) {
if (o1.getBillNo().equals(o2.getBillNo()))
	return 0;
else
	return 1;
}
}