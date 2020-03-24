package reports;

import java.io.Serializable;
import java.util.Comparator;

import model.TimeSheetDataModel;

import org.zkoss.zul.GroupComparator;

public class ReportsComaprator implements Comparator<TimeSheetDataModel>,
		GroupComparator<TimeSheetDataModel>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(TimeSheetDataModel o1, TimeSheetDataModel o2) {
		return o1.getEnFullName().compareTo(o2.getEnFullName().toString());
	}

	public int compareGroup(TimeSheetDataModel o1, TimeSheetDataModel o2) {
		if (o1.getEnFullName().equals(o2.getEnFullName()))
			return 0;
		else
			return 1;
	}
}
