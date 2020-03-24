package home;

public class QuotationQueries 
{
	StringBuffer query;
	public String getCountriesQuery()
	{
		query=new StringBuffer();
		 query.append("select CountryId,CountryName,countrycode from country order by CountryName asc");
		return query.toString();		
	}
	
	public String getPositionsQuery()
	{
		query=new StringBuffer();
		query.append("select positionid,positionname from position ");//order by positionname asc
		return query.toString();		
	}
	
	public String insertQuotation1(QuotationModel objQuot)
	{
		query=new StringBuffer();
		query.append(" insert into Prospective ");
		query.append(" ( Name,FullName,Telephone2)");
		query.append(" values ('"+objQuot.getCompanyName()+"','"+objQuot.getContactName()+"','" + objQuot.getTelephone2() +"') ");
		return query.toString();	
	}
	
	public String insertQuotation(QuotationModel objQuot)
	{
		query=new StringBuffer();
		query.append(" insert into Prospective ");
		query.append(" ( companyName,FullName,Telephone2)");
		query.append(" values ('"+objQuot.getCompanyName()+"','"+objQuot.getContactName()+"','" + objQuot.getTelephone2() +"') ");
		return query.toString();	
	}
	
	
	public String getQuotationData(int quotationid)
	{
		query=new StringBuffer();
		query.append(" SELECT q.* ,p.positionname,c.countryname ,countrycode from quotation q");
		query.append(" INNER JOIN position p ON q.positionId=p.positionid");
		query.append(" INNER JOIN country c ON c.countryid=q.countryId");
		query.append(" ");
		query.append(" ");
		query.append(" where quotationid=" + quotationid);
		return query.toString();	
		
	}
	public String getQuotationProduct(int quotationid,String type)
	{
		query=new StringBuffer();
		query.append(" SELECT * from quotationproduct q");		
		query.append(" where quotationid=" + quotationid);
		query.append(" and producttype='" + type +"'");
		return query.toString();	
		
	}
	
	public String getQuotationAttachment(int quotationid)
	{
		query=new StringBuffer();
		query.append(" SELECT * from quotationattachment");		
		query.append(" where quotationid=" + quotationid);		
		return query.toString();	
		
	}
	
	//Histroy
	public String getQuotationHistory(String fromDate,String toDate)
	{
		query=new StringBuffer();
		query.append("select q.* ,countryname from quotation q INNER JOIN country c ON q.countryId=c.countryid ");
		query.append(" where DATE(creationdate) between '"+fromDate +"' and '"+toDate+"' ORDER BY creationdate DESC");
		return query.toString();		
	}
	public String getProductsQuery(int quotationId)
	{
		query=new StringBuffer();
		 query.append("select * from quotationproduct where quotationid= "+ quotationId);
		return query.toString();		
	}
	
	
	public String insertWebQuotation(QuotationModel objQuot)
	{
		query=new StringBuffer();
		query.append(" insert into Web_Quotation ");
		query.append(" (custtype,companyName,contactTitle,contactName,otherposition,positionName,countryName,city,telephone1,telephone2,mobile1,mobile2,email");
		query.append(" ,website,howyouknow,auditor,pobox,skypeid,businesstype,companysize,totalEmployee,externalAuditorname,currentAccount,currentHr,otherSoftware,softwareLanguage");
		query.append(" ,partofgroup,groupName,fromTime,toTime,fromDay,toDay,address,companytype,notes)");
		query.append(" values ('%s','%s','%s','%s','%s', '%s','%s','%s','%s','%s', '%s','%s','%s',");
		query.append("         '%s','%s','%s','%s','%s', '%s','%s','%s','%s','%s', '%s','%s','%s',");
		query.append("         '%s','%s','%s','%s','%s', '%s','%s','%s','%s' ");
		query.append(" )");

		return query.toString().format(query.toString(),objQuot.getCusttype(),objQuot.getCompanyName(),objQuot.getContactTitle(),objQuot.getContactName(),objQuot.getOtherposition(),
				objQuot.getPositionName(),objQuot.getCountryName(),objQuot.getCity(),
						objQuot.getTelephone1(),objQuot.getTelephone2(),objQuot.getMobile1(),objQuot.getMobile2(),
						objQuot.getEmail(),objQuot.getWebsite(),objQuot.getHowyouknow(),objQuot.getAuditor(),
						objQuot.getPobox(),objQuot.getSkypeid(),objQuot.getBusinesstype(),objQuot.getCompanysize(),objQuot.getTotalEmployee(),objQuot.getExternalAuditorname(),
						objQuot.getCurrentAccount(),objQuot.getCurrentHr(),objQuot.getOtherSoftware(),objQuot.getSoftwareLanguage(),objQuot.getPartofgroup(),objQuot.getGroupName(),
						objQuot.getFromTime(),objQuot.getToTime(),objQuot.getFromDay(),objQuot.getToDay(),objQuot.getAddress(),objQuot.getCompanytype(),objQuot.getNotes());
	
	}
	
	public String insertWebQuotationProduct(QuotationProductModel objProduct,int quotationid)
	{
		query=new StringBuffer();
		query.append(" insert into Web_Quotation_Product(quotationid,producttype,description,nooflicense,memo) ");
		query.append(" values ('%s','%s','%s','%s','%s')");
		return query.toString().format(query.toString(),String.valueOf(quotationid),objProduct.getType(),objProduct.getDescription(),objProduct.getNooflicense(),objProduct.getMemo());
		
	}
	
}
