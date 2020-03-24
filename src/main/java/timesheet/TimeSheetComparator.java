package timesheet;


import java.io.Serializable;
import java.util.Comparator;

import model.TimeSheetGridModel;

import org.zkoss.zul.GroupComparator;

public class TimeSheetComparator implements Comparator<TimeSheetGridModel>, GroupComparator<TimeSheetGridModel>, Serializable {
    private static final long serialVersionUID = 1L;
 
    public int compare(TimeSheetGridModel o1, TimeSheetGridModel o2) {
        return o1.getEnFullName().compareTo(o2.getEnFullName().toString());
    }
 
    public int compareGroup(TimeSheetGridModel o1, TimeSheetGridModel o2) {
        if(o1.getEnFullName().equals(o2.getEnFullName()))
            return 0;
        else
            return 1;
    }
}

