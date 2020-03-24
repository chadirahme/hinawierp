package reports;

import java.text.DecimalFormat;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.PivotHeaderContext;
import org.zkoss.pivot.PivotRendererExt;
import org.zkoss.pivot.Pivottable;

public class AccountsPivotRender implements PivotRendererExt
{
	private DecimalFormat _fnf = new DecimalFormat("##,###.00");
    private DecimalFormat _nnf = new DecimalFormat("##,###");
    
	@Override
	public int getColumnSize(Pivottable table, PivotHeaderContext colc,PivotField field) 
	{
		if(field.getFieldName().equals("Name")) 
		{
			 return 500;
		}
		return colc.isGrandTotal() && field != null ? 350 : 100;
	}

	@Override
	public int getRowSize(Pivottable arg0, PivotHeaderContext arg1,
			PivotField arg2) {
		// TODO Auto-generated method stub
		 return 20;
	}

	@Override
    public String renderCell(Number data, Pivottable table, 
            PivotHeaderContext rowContext, PivotHeaderContext columnContext,
            PivotField dataField) {
        	return data == null ? "" :
                data instanceof Integer ? _nnf.format(data) : _fnf.format(data);
    }
     	
	@Override
    public String renderDataField(PivotField field) {
        return field.getFieldName().toUpperCase();
    }

	@Override
    public String renderField(Object data, Pivottable table, PivotField field) {
        return field.getType() == PivotField.Type.DATA ?
                field.getTitle() : data == null ? "(null)" : String.valueOf(data);
    }

	
	@Override
    public String renderGrandTotalField(Pivottable table, PivotField field) {
        if (field == null) return "Grand Total";
        return "Grand Total of " + field.getTitle();
    }
	@Override
    public String renderSubtotalField(Object data, Pivottable table, 
            PivotField field, Calculator calculator) {
        String calLabel = calculator.getLabel();
        String dataLabel = data == null ? "Null" : data.toString();
        return dataLabel + " " + calLabel;
    }

	

	@Override
	public String renderCellSClass(Number arg0, Pivottable arg1,
			PivotHeaderContext arg2, PivotHeaderContext arg3, PivotField arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override	
	public String renderCellStyle(Number data, Pivottable table,
			PivotHeaderContext rowContext, PivotHeaderContext columnContext,
			PivotField dataField)
	{
		//if (!columnContext.isGrandTotal() && !rowContext.isGrandTotal()) 
		{
		if(data.doubleValue() >= 0)
		{			
			return "color: #006600; font-weight: bold";
		}
		else
		{		
			return "color: #EE1111; font-weight: bold";
		}
		
		}
		//return null;
	}
	

}
