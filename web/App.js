$(document).ready(function(){
	//$('#element').val('loading.......');

	var currentdate = new Date(); 
	var datetime = "Last Sync: " + currentdate.getDate() + "/"
    + (currentdate.getMonth()+1)  + "/" 
    + currentdate.getFullYear() + " @ "  
    + currentdate.getHours() + ":"  
    + currentdate.getMinutes() + ":" 
    + currentdate.getSeconds();
	$('.lblDateNow').append(datetime);
	
	$.ajax({
	url: 'rest/HRServices/getEmployeeProfile',
	dataType: 'json',
	success: function (data) {
		//alert(data);		
		//$('#enCompanyName').val(data.dateOfBirth.split('T')[0]);
		$('#enCompanyName').val(data.companyName);
		//$('#fullName').val(data.fullName);
		$('#divfullName').text(data.fullName);
		$('#divArfullName').text(data.arabicName);
		$('#divEmail').text(data.email);
		
		$('#employeeNo').val(data.employeeNo);
		$('#employeementDateString').val(data.employeementDateString);
		
		$('#department').val(data.department);
		$('#position').val(data.position);
		
		
		$('#country').val(data.country);
		$('#gender').val(data.gender);
		$('#marital').val(data.marital);		
		
		$('#passportExpiry').val(data.passportExpiry);
		$('#residanceExpiry').val(data.residanceExpiry);
		$('#labourCradExpiry').val(data.labourCradExpiry);
		
		
	},
	 error: function(jqXHR, textStatus, errorThrown) {
		 //window.location="login.zul";
         //alert(jqXHR);
         //alert(textStatus); // this comes back as "error"
         //alert(errorThrown); // this comes back as "undefined"
         
     }	
	});
	
	$('#lblTimesheet').text('loading.......');
	$.ajax({
		url: 'rest/HRServices/getTomorrowPlanTimeSheet',
		dataType: 'json',
		success: function (data) {
			$('#lblTimesheet').text('');
			$(data).each(function(index){
				$('#lblTimesheet').append('<li>' + this.timesheetDate + " : " + this.tomorrowPlan + '</li>');
			});
			//$('#lblTimesheet').val(data.companyName);			
		},
		 error: function(jqXHR, textStatus, errorThrown) {
			 //window.location="login.zul";
	         //alert(jqXHR);
	         //alert(textStatus); // this comes back as "error"
	         //alert(errorThrown); // this comes back as "undefined"
	         
	     }	
		});
		
	/*$('#lblLeaveRequest').text('loading.......');
	$.ajax({
		url: 'rest/HRServices/getLeaveRequest',
		dataType: 'json',
		success: function (data) {
			$('#lblLeaveRequest').text('');
			$(data).each(function(index){
				//$('#lblLeaveRequest').append('<li>');
				if(this.status.indexOf("Approved")>=0)
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-success" >');
				else if(this.status.indexOf("Rejected")>=0)
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-danger" >');
				else if(this.status.indexOf("Created")>=0)
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-info" >');
				
				$('#lblLeaveRequest').append(this.leaveType + " : " + this.leaveReason + " From " + this.leaveStartDate + " To "+this.leaveEndDate + " ");				
				$('#lblLeaveRequest').append('<br/>');
				$('#lblLeaveRequest').append('Status ' + this.status);		
				$('#lblLeaveRequest').append('</div>');
				$('#lblLeaveRequest').append('<br/>');
			});
			//$('#lblTimesheet').val(data.companyName);			
		},
		 error: function(jqXHR, textStatus, errorThrown) {
			 //window.location="login.zul";
	         //alert(jqXHR);
	         //alert(textStatus); // this comes back as "error"
	         alert(errorThrown); // this comes back as "undefined"
	         
	     }	
		});*/
	
	$('#lblLeaveRequest').text('loading.......');
	$.ajax({
		url: 'rest/HRServices/getUserTaskList',
		dataType: 'json',
		success: function (data) {
			$('#lblLeaveRequest').text('');
			$(data).each(function(index){
				//$('#lblLeaveRequest').append('<li>');
				if(this.statusName.indexOf("Closed")>=0)
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-danger" >');				
				else if(this.statusName.indexOf("Created")>=0)
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-info" >');
				else
					$('#lblLeaveRequest').append('<div class="fa fa-circle text-success" >');
				
				$('#lblLeaveRequest').append(this.creationDateStr + " : " + this.taskName + " Expected Date to finish : "+this.expectedDatetofinishStr + " ");				
				$('#lblLeaveRequest').append('<br/>');
				$('#lblLeaveRequest').append('Priority ' + this.priorityNAme);		
				$('#lblLeaveRequest').append('</div>');
				$('#lblLeaveRequest').append('<br/>');
			});
			//$('#lblTimesheet').val(data.companyName);			
		},
		 error: function(jqXHR, textStatus, errorThrown) {
			 //window.location="login.zul";
	         //alert(jqXHR);
	         //alert(textStatus); // this comes back as "error"
	         //alert(errorThrown); // this comes back as "undefined"
	         
	     }	
		});
	
	
	
	
	
});

