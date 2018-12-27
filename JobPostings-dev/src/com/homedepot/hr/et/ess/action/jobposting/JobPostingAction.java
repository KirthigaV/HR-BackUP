package com.homedepot.hr.et.ess.action.jobposting;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.homedepot.hr.et.ess.bean.HourlyPostingsDTO;
import com.homedepot.hr.et.ess.bean.ReadOpenStorePositionsDTO;
import com.homedepot.hr.et.ess.dao.GetHRStoreGroup;
import com.homedepot.hr.et.ess.dao.GetOpenStorePositions;
import com.homedepot.hr.et.ess.form.jobposting.JobPostingForm;
import com.homedepot.hr.et.ess.util.ApplicationCallbackHandler;
import com.homedepot.hr.et.ess.util.JPLogUtil;

public class JobPostingAction extends DispatchAction {
	private String CLASS_NM="JobPostingAction";
	private static final String SUBJECT_KEY = "subject";
	private String sysUserID, firstName, lastName, storeID,storeID2,storeID3,locale;
	
	private static final Logger logger = Logger.getLogger( JobPostingAction.class );
	public JobPostingAction() {
		super();
	}

	public ActionForward jsHome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		final String METHODNAME = "jsHome";
		logger.debug("Entering inside:"+METHODNAME);
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		JobPostingForm jobPostingForm = (JobPostingForm) form;
		jobPostingForm.clearMyForm();
		String storeNumber=null;
		
		try{
			Cookie[] cookies = request.getCookies();
			if ( cookies != null )
			{
			for ( int c = 0; c < cookies.length; c++ )
			{
			logger.debug("............cookies[c].getValue()..........."+cookies[c].getValue());
			if ( cookies[c].getName().equalsIgnoreCase("storeNumber") )
			{
				logger.debug("...........FOUND............");
			}
			}
			}
			HttpSession session = request.getSession();
			Subject subject = (Subject)session.getAttribute(SUBJECT_KEY);
			
			if(subject == null){
			LoginContext lc = new LoginContext("login",new ApplicationCallbackHandler(request));
			logger.debug(METHODNAME+";LoginContext:"+lc);
			lc.login();
			subject = lc.getSubject();
			session.setAttribute(SUBJECT_KEY, subject);
			logger.debug(METHODNAME+";subject:"+subject);
			}
			Set principals = null;
			Principal p;

			//Store number
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			storeID3 = p.getName();
			storeNumber=storeID3;
			logger.debug(METHODNAME+";storeID3"+storeID3);
			
			//First Name
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			firstName = p.getName();
			logger.debug(METHODNAME+";firstName"+firstName);
			
			//Last Name
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			lastName = p.getName();
			logger.debug(METHODNAME+";lastName"+lastName);
			
			
		}
		catch (Exception e) {
			
		}
		
		
		jobPostingForm.setStoreIdValue(storeNumber);
		logger.debug(METHODNAME+";storeNumber:"+storeNumber);
		
		forward = mapping.findForward("home");
		logger.debug("Exiting:"+METHODNAME);
		return (forward);
	}
	
	
	public ActionForward hourPostsInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		final String METHODNAME = "hourPostsInit";
		
		logger.debug("Entering inside:"+METHODNAME);
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward();
		
		JobPostingForm jobPostingForm = (JobPostingForm) form;
		
		try{
		//int i=2/0;	
		logger.debug(METHODNAME+";storeNumber:"+jobPostingForm.getStoreIdValue());	
		if(jobPostingForm.getStoreIdValue()==null||jobPostingForm.getStoreIdValue().trim().length()==0){
				jobPostingForm.setJobPosInd(false);
				jobPostingForm.setSearchInd(false);
				//errors.add("hiddenValue",new ActionMessage("error.nostorefound"));
				//saveErrors(request,errors);
				forward = mapping.findForward("success");
				return (forward);
			}
		else{	
		//*************** SET I/P PARAMETERS TO FORM ************************
		jobPostingForm.setStoreLocale("EN_US");
		jobPostingForm.setEndDate(new Date(System.currentTimeMillis()));
		List list=new ArrayList();
		list.add("01");list.add("02");list.add("03");list.add("04");list.add("05");list.add("05");
		list.add("06");	list.add("07");list.add("08");list.add("09");
		jobPostingForm.setConsentDecreeJobCategoryCodeList(list);
		jobPostingForm.setActiveFlag("Y");
		//*************** END I/P PARAMETERS TO FORM ************************
		
		logger.debug(METHODNAME+"; Page no:"+jobPostingForm.getPageNo());
		if("HPpage".equals(jobPostingForm.getPageNo())){
			boolean isValidStore=isValidStore(jobPostingForm.getStoreIdValue());
			logger.debug(METHODNAME+"; isValidStore:"+isValidStore);
			if(!isValidStore){
				jobPostingForm.setJobPosInd(false);
				jobPostingForm.setSearchInd(false);
				errors.add("storeIdValue",new ActionMessage("error.invalidstore"));
				saveErrors(request,errors);
				forward = mapping.findForward("success");
				return (forward);
			}
		}
		
		HourlyPostingsDTO hourlyPostingsDTO=new HourlyPostingsDTO();
		BeanUtils.copyProperties(hourlyPostingsDTO, jobPostingForm);
		
		List<ReadOpenStorePositionsDTO> storeList=new ArrayList();
		
		GetOpenStorePositions getOpenStorePositions=new GetOpenStorePositions();
		
		
			
			storeList=getOpenStorePositions.readOpenStorePositions(hourlyPostingsDTO);	
			forward = mapping.findForward("success");
			
			if(storeList.size()==0){
				jobPostingForm.setJobPosInd(false);
				jobPostingForm.setSearchInd(true);
				jobPostingForm.setHourlyPostingsList(storeList);
				return (forward);
			}else{
				jobPostingForm.setJobPosInd(true);
				jobPostingForm.setHourlyPostingsList(storeList);
			}
			
		}
		
		}
		
		catch(Exception e){
			JPLogUtil.logAppLogError(CLASS_NM, METHODNAME, e, "", 99);
			throw e;
		}
		
		logger.debug("Exiting:"+METHODNAME);
		return (forward);
	}
	
	public boolean isValidStore(String storeID)throws Exception{
		boolean isValidStore=false;
		final String METHODNAME = "isValidStore";
		logger.debug("Entering into:"+METHODNAME);
		GetHRStoreGroup getHRStoreGroup=new GetHRStoreGroup();
		List list=getHRStoreGroup.readHumanResourcesStoreGrp(storeID);
		logger.debug(METHODNAME+";list size:"+list.size());
		if(list!=null&&list.size()!=0){
			
			if(list.get(0).equals("0")){
				isValidStore=false;
			}else{
				isValidStore=true;
			}
			
		}
		logger.debug("Exiting:"+METHODNAME);
		return isValidStore;
	}
}
