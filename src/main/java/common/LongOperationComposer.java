package common;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

public class LongOperationComposer  extends SelectorComposer<Component> 
{
	//https://gist.github.com/dennischen/2766642
	
	
	@Wire
	Button btn1;
	
	@Wire
	Button btn2;
 
	@Listen("onClick=#btn1")
	public void onBusy() {
		
		Clients.showBusy("System is processing your request");
		Events.echoEvent("onLongOp", btn1, null);
	}
	
	@Listen("onClick=#btn2")
	public void onBusy1() {
		
		Clients.showBusy("System is processing your request,it might take a while to generate report");
		Events.echoEvent("onLongOp", btn2, null);
	}
 
	@Listen("onLongOp=#btn1")
	public void onClear() {
		//alert("ssssssssss");
		Clients.clearBusy();
	}
	
	@Listen("onLongOp=#btn2")
	public void onClear1() {
		//alert("ssssssssss");
		Clients.clearBusy();
	}
}
