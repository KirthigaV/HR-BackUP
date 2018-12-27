function close_Current_Window_After_Yes() {
	var confirmation=window.confirm("The web page you are viewing is trying to close the window. Do you want to close this window?")
	if (confirmation) {					
		window.opener = top;
		window.close();
	}
}

function goHome(actionItem) {
	document.jobPostingForm.action="/JobPostings/js_initialize.do?method=goHome";	
	document.jobPostingForm.submit();	
}

function printHourlyPostings() {	
	document.jobPostingForm.action="/JobPostings/js_initialize.do?method=printHourlyPostings";	
	document.jobPostingForm.submit();
}				

function search() {	
	document.jobPostingForm.action="/JobPostings/js_initialize.do?method=selectSalariedPostings";
	document.jobPostingForm.submit();		
}

function goToYourStore() {	
	document.jobPostingForm.action="/JobPostings/js_initialize.do?method=selectHourlyPostings";
	document.jobPostingForm.submit();		
}

function goToCareerDepot() {	
	document.jobPostingForm.action="/JobPostings/js_initialize.do?method=goToCareerDepot&CareerDepotURL=http://hrportal.homedepot.com/en_US/HR_Desktop_Portal/KnowledgeBase/US/Staffing/Recruiting/mc_careerdepot_chklst.htm";
	document.jobPostingForm.submit();		
}