package hba;

import java.io.Serializable;
import java.util.Comparator;

import model.ItemReceiptReportModel;

import org.zkoss.zul.GroupComparator;

public class ItemReceiptComparator implements Comparator<ItemReceiptReportModel>,
GroupComparator<ItemReceiptReportModel>, Serializable {
private static final long serialVersionUID = 1L;

public int compare(ItemReceiptReportModel o1, ItemReceiptReportModel o2) {
return o1.getIrLocalNo().compareTo(o2.getIrLocalNo().toString());
}

public int compareGroup(ItemReceiptReportModel o1, ItemReceiptReportModel o2) {
if (o1.getIrLocalNo().equals(o2.getIrLocalNo()))
	return 0;
else
	return 1;
}
}