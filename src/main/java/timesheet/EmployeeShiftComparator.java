package timesheet;

import java.io.Serializable;
import java.util.Comparator;

import model.EmployeeShiftModel;

import org.zkoss.zul.GroupComparator;

public class EmployeeShiftComparator implements Comparator<EmployeeShiftModel>, GroupComparator<EmployeeShiftModel>, Serializable {
    private static final long serialVersionUID = 1L;
 
    public int compare(EmployeeShiftModel o1, EmployeeShiftModel o2) {
        return o1.getPosition().compareTo(o2.getPosition().toString());
    }
 
    public int compareGroup(EmployeeShiftModel o1, EmployeeShiftModel o2) {
        if(o1.getPosition().equals(o2.getPosition()))
            return 0;
        else
            return 1;
    }
}

