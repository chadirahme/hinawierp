package hba;

public class EstimateQueries {

	StringBuffer query;
	public String GetBOQEstimateMaterialsQuery()
	{
		query = new StringBuffer();
		query.append(" SELECT BoQEstimate.Estimateno,BoQEstimate.EstimateDate,BoQEstimate.Estimatename,");
		query.append(" INDUSTRYTYPELIST.DESCRIPTION AS IndustryTypeEn,INDUSTRYTYPELIST.ARABIC AS IndustryTypeAr,");
		query.append(" QBLists.FullName as CustName,QBLists.ArFullName as CustNameAR,");
		query.append("  BOQEstimateMaterials.*, ");
		query.append("  BOQList.BOQ_FullName as ItemName, BOQList.BOQ_ArFullName as ItemNameAR,BOQList.BOQ_type as BOQitemtype,");
		query.append("  ItemUnits.Description As UnitName,  ItemUnits.DescriptionAR As UnitNameAR ");
		query.append(" from BOQEstimateMaterials");
		query.append(" Left Join BOQList       on BOQList.BOQItem_Key            = BOQEstimateMaterials.BOQItem_Key  ");
		query.append(" Left Join ItemUnits     On BOQEstimateMaterials.Unit_ID   = ItemUnits.Unit_ID ");
		query.append(" Left Join BOQEstimate   on BOQEstimateMaterials.Recno     = BOQEstimate.Recno");
		query.append(" LEFT JOIN QBLists  ON BOQEstimate.CustomerRefKey          = QBLists.RecNo");
		query.append(" Left Join HRLISTVALUES AS INDUSTRYTYPELIST ON BOQEstimate.IndustryKey   =INDUSTRYTYPELIST.ID ");
		query.append(" Where BOQEstimateMaterials.ConsumedQty < BOQEstimateMaterials.Quantity ");
		query.append(" AND BOQEstimate.Status in ('A')");
		query.append(" Order by BOQEstimateMaterials.Recno,BOQEstimateMaterials.[Line_no]");
		return query.toString();
	}
}
