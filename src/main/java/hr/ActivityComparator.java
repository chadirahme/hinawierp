package hr;

import hr.model.ActivityModel;

import java.io.Serializable;
import java.util.Comparator;

import org.zkoss.zul.GroupComparator;

public class ActivityComparator implements Comparator<ActivityModel>, GroupComparator<ActivityModel>, Serializable {
    private static final long serialVersionUID = 1L;
 
    public int compare(ActivityModel o1, ActivityModel o2) {
        return o1.getEmployeeName().compareTo(o2.getEmployeeName().toString());
    }
 
    public int compareGroup(ActivityModel o1, ActivityModel o2) {
        if(o1.getEmployeeName().equals(o2.getEmployeeName()))
            return 0;
        else
            return 1;
    }
}

