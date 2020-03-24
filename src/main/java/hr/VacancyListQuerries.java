package hr;

import java.text.SimpleDateFormat;

public class VacancyListQuerries {

	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public String getMainQuerry(int compkey, int loginUserId) {
		query = new StringBuffer();
		query.append("SELECT COMPSTANDPOS .* , HRLISVALUES_DEPT .Description As DepartmentName,HRLISVALUES_DEPT .Arabic As DepartmentArabic, HRLISVALUES_POSITION .Description As PostionName,");
		query.append("HRLISVALUES_POSITION .Arabic As PostionNameArabic,COMPSETUP.Comp_Name,COMPSETUP.Comp_Name_AR ");
		query.append(" FROM COMPSTANDPOS LEFT JOIN HRLISTVALUES As HRLISVALUES_DEPT ON ");
		query.append(" COMPSTANDPOS.DEP_ID= HRLISVALUES_DEPT.ID LEFT JOIN   HRLISTVALUES As HRLISVALUES_POSITION ON COMPSTANDPOS.ID= HRLISVALUES_POSITION.ID ");
		query.append(" INNER JOIN COMPSETUP ON COMPSTANDPOS.COMP_KEY=COMPSETUP.COMP_KEY ");
		query.append(" INNER JOIN USERCOMPANY ON COMPSTANDPOS.COMP_KEY=USERCOMPANY.COMP_KEY WHERE ");
		query.append("USERCOMPANY.USERID="+loginUserId+"");
		if (compkey != 0) {
			query.append(" and COMPSTANDPOS.COMP_KEY =" + compkey+" ");
		}
		query.append("order by departmentname");
		return query.toString();
	}
	
	public String getCvs(int positionId) {
		query = new StringBuffer();
		query.append("SELECT Count(*) as MaxCount FROM CVMAST WHERE POS_ID");
		query.append("=" + positionId + "");
		return query.toString();
	}
	
	public String actualNumberOfEmployees(int compkey, int depId,int positinId) {
		query = new StringBuffer();
		query.append("Select Count(*) as MaxCount from EMPMAST Where COMP_KEY="+compkey+" And DEP_ID="+depId+" AND POS_ID="+positinId+"");
		return query.toString();
	}
	

}
