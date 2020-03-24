package company;

import java.io.Serializable;
import java.util.Comparator;

import layout.MenuModel;


import org.zkoss.zul.GroupComparator;

public class MenuComparator implements Comparator<MenuModel>, GroupComparator<MenuModel>, Serializable {
    private static final long serialVersionUID = 1L;
 
    public int compare(MenuModel o1, MenuModel o2) {
        return o1.getTitle().compareTo(o2.getTitle().toString());
    }
 
    public int compareGroup(MenuModel o1, MenuModel o2) {
        if(o1.getTitle().equals(o2.getTitle()))
            return 0;
        else
            return 1;
    }
}

