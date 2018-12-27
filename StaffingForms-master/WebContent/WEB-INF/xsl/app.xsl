<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">  
<xsl:output method="xml"/>   

<xsl:template match="/">
  <fo:root>
    <fo:layout-master-set>
      
        <!-- first page layout -->
      <fo:simple-page-master 
                master-name="first-page"
                page-height="11in" page-width="8.5in"
                margin-top="0.3in" margin-bottom="0.3in" 
                margin-left="0.3in" margin-right="0.3in">
          <fo:region-body/>
      </fo:simple-page-master>
        <!-- middle pages layout -->
      <fo:simple-page-master 
                master-name="middle-pages"
                page-height="11in" page-width="8.5in"
                margin-top="0.3in" margin-bottom="0.3in" 
                margin-left="0.3in" margin-right="0.3in">
          <fo:region-body margin-top="0.41in"/>
          <fo:region-before extent="0.4in"/>
        
      </fo:simple-page-master>
        
        <!-- page arrangment -->
      <fo:page-sequence-master 
                master-name="app-pages">
          <fo:repeatable-page-master-alternatives>
            <fo:conditional-page-master-reference
                 page-position="first" 
                 master-reference="first-page"/>
            <fo:conditional-page-master-reference
                 page-position="rest" 
                 master-reference="middle-pages"/>
           </fo:repeatable-page-master-alternatives>
      </fo:page-sequence-master>
    </fo:layout-master-set>
      
      <xsl:apply-templates/>
      
  </fo:root>
</xsl:template>

<xsl:template match="app">
  <fo:page-sequence 
            master-reference="app-pages">
    <!-- header (but not on 1st page) -->
    <fo:static-content 
            flow-name="xsl-region-before" 
            font-size="9pt" 
            font-family="Courier" 
            line-height="11pt" 
            text-align="start">
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
        <fo:table-column column-width="2.95in"/>
        <fo:table-column column-width="4.95in"/>
      <fo:table-body><fo:table-row>
          <fo:table-cell><fo:block text-align="left">
            Applicant name: <fo:inline font-weight="bold"><xsl:value-of select="name"/></fo:inline>
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="right">
            Printed: <xsl:value-of select="currentTimeStamp"/>
          </fo:block></fo:table-cell>
      </fo:table-row></fo:table-body>
      </fo:table>
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
    </fo:static-content>

    <!-- body flow -->
    <fo:flow 
            flow-name="xsl-region-body" 
            font-size="9pt" 
            font-family="Courier" 
            line-height="11pt" 
            text-align="start">
            
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
        <fo:table-column column-width="2.63in"/>
        <fo:table-column column-width="2.63in"/>
        <fo:table-column column-width="2.63in"/>
      <fo:table-body><fo:table-row>
          <fo:table-cell><fo:block text-align="left">
            Home Depot U.S.A., Inc
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="center" font-weight="bold">
            EMPLOYMENT APPLICATION
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="right">
            Printed: <xsl:value-of select="currentTimeStamp"/>
          </fo:block></fo:table-cell>
      </fo:table-row></fo:table-body>
      </fo:table>
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block font-size="7pt" line-height="9pt">
        The Home Depot is an equal opportunity employer. The company will not under any circumstances discriminate against an
        Associate or Applicant with regard to race, age, sex, national origin, religion or disability.
      </fo:block>
      <fo:block font-weight="bold">
        WE WORK IN A SMOKE FREE ENVIRONMENT.
      </fo:block>
      <fo:block padding-before="0.2in">
        Name <fo:inline font-weight="bold"><xsl:value-of select="name"/></fo:inline>
      </fo:block>                                
      <fo:block>
        Candidate Reference #: <fo:inline font-weight="bold"><xsl:value-of select="candRefNbr"/></fo:inline>
      </fo:block>
      <fo:block>
        Phone: <fo:inline font-weight="bold"><xsl:value-of select="areaCode"/> <xsl:value-of select="phnNumber"/></fo:inline>
        Address: <fo:inline font-weight="bold"><xsl:value-of select="address1"/></fo:inline>
      </fo:block>
      <fo:block>
        City: <fo:inline font-weight="bold"><xsl:value-of select="city"/></fo:inline>                     
        State: <fo:inline font-weight="bold"><xsl:value-of select="stateCd"/></fo:inline>  
        Zip: <fo:inline font-weight="bold"><xsl:value-of select="zipCd"/></fo:inline>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block text-align="center"> 
         <fo:leader leader-pattern="rule" rule-thickness="2.0pt" leader-length="100%"/> 
      </fo:block>

      <fo:block>
        What position(s) are you applying for? 
        <fo:inline font-weight="bold"><xsl:value-of select="positionsAppliedFor"/></fo:inline>
      </fo:block>

      <fo:block>
        What stores are you applying for? 
        <fo:inline font-weight="bold"><xsl:value-of select="storesAppliedFor"/></fo:inline>
      </fo:block>
      
      <fo:block>
        Age Eligibility Met?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="minEmpltAgeFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="minEmpltAgeFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      
      <fo:block>
        Are you currently on a leave of absence or lay off from any company?
        <fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="currentLeaveOfAbsenceLayoffFlag = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="currentLeaveOfAbsenceLayoffFlag = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      
      <fo:block>
        Wage Desired:<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <fo:inline font-weight="bold"><xsl:value-of select="wageDesired"/></fo:inline>
      </fo:block>
      
      <fo:block>
        Willing to work<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <fo:inline font-weight="bold"><xsl:value-of select="ftFlg"/></fo:inline> Full Time,
        <fo:inline font-weight="bold"><xsl:value-of select="ptFlg"/></fo:inline> Part Time
      </fo:block>
      
      <fo:block padding-before="0.1in">
        Days candidate is available to work (Y = Available, X = Discuss Availability, N = Not Available):
      </fo:block>
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
          <fo:table-column column-width="0.3in"/>
          <fo:table-column column-width="1in"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="weekdays"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Weekdays</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="saturday"/></fo:block></fo:table-cell>            
            <fo:table-cell><fo:block>Saturday</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="sunday"/></fo:block></fo:table-cell>            
            <fo:table-cell><fo:block>Sunday</fo:block></fo:table-cell>
          </fo:table-row>
        </fo:table-body>    
      </fo:table>      
      
      <fo:block padding-before="0.1in">
        Times candidate is available to work:
      </fo:block>
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
          <fo:table-column column-width="0.3in"/>
          <fo:table-column column-width="2.5in"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="earlyAm"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Early AM (4am - 6am)</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="mornings"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Mornings (6am - noon)</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="afternoons"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Afternoons (noon - 5pm)</fo:block></fo:table-cell>            
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="nights"/></fo:block></fo:table-cell>            
            <fo:table-cell><fo:block>Nights (5pm - 8pm)</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="lateNight"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Late night (8pm - midnight)</fo:block></fo:table-cell>            
          </fo:table-row>                    
          <fo:table-row>
            <fo:table-cell text-align="center"><fo:block font-weight="bold"><xsl:value-of select="overnight"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Overnight (midnight - 5am)</fo:block></fo:table-cell>
          </fo:table-row>  
        </fo:table-body>    
      </fo:table>  
                 
      <xsl:if test="reasonableAccommodationRequested = 'Y'">
	      <fo:block padding-before="0.1in">
    	  </fo:block>                               
	      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
        	  <fo:table-column column-width="6in"/>
	        <fo:table-body> 
    	      <fo:table-row>
            	<fo:table-cell><fo:block>Reasonable accommodation requested - discuss during interview</fo:block></fo:table-cell>            
	          </fo:table-row>
    	    </fo:table-body>
	      </fo:table>
      </xsl:if>             
                                                                     
      <fo:block padding-before="0.1in">
        How long do you plan to work<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="wrkDurationInd = 'T'">
            <fo:inline font-weight="bold">X</fo:inline>
        </xsl:if>
        Temporary,   
        <xsl:if test="wrkDurationInd = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline>
        </xsl:if>
        1 year,   
        <xsl:if test="wrkDurationInd = 'S'">
            <fo:inline font-weight="bold">X</fo:inline>
        </xsl:if>
        Summer,
        <xsl:if test="wrkDurationInd = 'M'">
            <fo:inline font-weight="bold">X</fo:inline>
        </xsl:if>
        More than 1 year
      </fo:block>
      
      <fo:block>
        Are you willing to relocate?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="reloFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="reloFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      
      <fo:block>
        Have you worked for us before?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="prevEmpltFlg = 'Y'">
          <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="prevEmpltFlg = 'N'">
          YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      <fo:block>
        If yes, begin date:<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <fo:inline font-weight="bold"><xsl:value-of select="prevEmpltBgnDt"/></fo:inline>
        end date:
        <fo:inline font-weight="bold"><xsl:value-of select="prevEmpltEndDt"/></fo:inline>
      </fo:block>
      <fo:block>
        Where: <fo:inline font-weight="bold"><xsl:value-of select="prevEmpltLocation"/></fo:inline>
      </fo:block>
      <!-- **************************************************************************** -->

      <fo:block padding-before="0.1in">
        Languages:
      </fo:block>
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
          <fo:table-column column-width="0.2in"/>
          <fo:table-column column-width="2in"/>
          <fo:table-column column-width="0.2in"/>
          <fo:table-column column-width="2in"/>
          <fo:table-column column-width="0.2in"/>
          <fo:table-column column-width="2in"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg1"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>English</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg2"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Spanish</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg3"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>French</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg4"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>German</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg5"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Italian</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg6"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Chinese</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg7"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Tagalog</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg8"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Polish</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg9"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Korean</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg10"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Vietnamese</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg11"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Portuguese</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg12"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Japanese</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg13"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Greek</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg14"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Arabic</fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg15"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Hindi</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold"><xsl:value-of select="langFlg16"/></fo:block></fo:table-cell>
            <fo:table-cell><fo:block>American Sign Language</fo:block></fo:table-cell>
          </fo:table-row>
        </fo:table-body>    
      </fo:table>       
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block>
        Are you legally eligible to work in the U.S.A.?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="empltEligUsFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="empltEligUsFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>

      <fo:block>
        Have you ever been discharged from your work?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="dismissFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="dismissFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>

      <fo:block>
        Do you have any relatives working for Home Depot?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="relativesAtHd = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="relativesAtHd = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      <fo:block>
        If yes, name: <fo:inline font-weight="bold"><xsl:value-of select="relativesAtHdName"/></fo:inline>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block>
        Are you currently employed at this time?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="currentlyEmployedFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="currentlyEmployedFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      
      <fo:block>
       How many work days have you missed during the past 12 months:<fo:leader leader-pattern="space" leader-length="0.25in"/>
       <fo:inline font-weight="bold"><xsl:value-of select="wrkDaysMissed"/></fo:inline>
      </fo:block>
      
    <!--   <fo:block padding-before="0.1in" line-height="10pt">
        Have you ever been convicted of a crime, including guilty and nolo contendere? 
        Please do not include traffic violations, sealed or expunged records. 
        An answer of yes to this question will not automatically disqualify you from 
        consideration for employment. Factors such as the date and seriousness of the 
        offense, and the relationship between the conviction and the duties/responsibilities 
        of the applied for position, will be considered
        <fo:leader leader-pattern="space" leader-length="0.5in"/>
        <xsl:if test="priorConviction = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="priorConviction = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      <fo:block padding-before="0.1in" font-size="7pt">
        * See below if the position you are applying to is located in one of these states:
      </fo:block>
      <fo:block padding-before="0.1in" font-size="7pt" line-height="9pt">
        <fo:inline font-weight="bold">California:</fo:inline> 
        Applicants do not disclose marijuana-related convictions that are more than two-years old or 
        misdemeanor convictions for which probation has been successfully completed or otherwise discharged and the case 
        has been judicially dismissed. 
      </fo:block>
      <fo:block padding-before="0.1in" font-size="7pt">
        <fo:inline font-weight="bold">Hawaii:</fo:inline> 
        Applicants do not need to answer this section. 
      </fo:block>
      <fo:block padding-before="0.1in"  font-size="7pt" line-height="9pt">
        <fo:inline font-weight="bold">Massachusetts:</fo:inline>
		Applicants need not disclose first convictions for misdemeanor drunkeness, simple assault, affray, or 
		disturbance of the peace. Applicants need not disclose misdemeanor convictions where the date of the 
		convictions or the period of any incerceration resulting therefrom (whichever is later) occured 5 or more 
		years prior to the date of this application, unless the applicant was convicted of any offense within the 
		5 years immediately prededing the date of the application. An applicant for employment with a sealed record 
		on file with the commissioner of probation may answer 'no record' with respect to any inquiry here in relative 
		to prior arrests, criminal court appearances or convictions. In addition, any applicant for employment may 
		answer 'no record' with respect to any inquiry here in relative to prior arrests, court appearances and 
		adjudications in all cases of delinquency or as a child in need of service which did not result in complaint 
		transferred to the superior court for criminal prosecution.
      </fo:block>
      <fo:block padding-before="0.1in" font-size="7pt" line-height="9pt">
        <fo:inline font-weight="bold">Washington:</fo:inline>
        Applicants need not to disclose a conviction that occurred more than 10 years before the date of this application, unless you have been in prison for that conviction within the last 10 years.
      </fo:block> -->
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block>
        Have you ever served in the military?<fo:leader leader-pattern="space" leader-length="0.25in"/>
        <xsl:if test="militaryFlg = 'Y'">
            <fo:inline font-weight="bold">X</fo:inline> YES,  NO
        </xsl:if>
        <xsl:if test="militaryFlg = 'N'">
            YES, <fo:inline font-weight="bold">X</fo:inline> NO
        </xsl:if>
      </fo:block>
      <fo:block>
        If yes, please indicate branch: <fo:inline font-weight="bold"><xsl:value-of select="militaryBranch"/></fo:inline>
      </fo:block>
      <fo:block>
        Dates of active service:
        <fo:inline font-weight="bold"><xsl:value-of select="militaryBgnDt"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        To:
        <fo:inline font-weight="bold"><xsl:value-of select="militaryEndDt"/></fo:inline>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
          <fo:table-column column-width="3in"/>
          <fo:table-column column-width="0.75in"/>
          <fo:table-column column-width="0.75in"/>
          <fo:table-column column-width="2.5in"/>
        <fo:table-body>
          <fo:table-row>
            <fo:table-cell><fo:block>List All High School, College,</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Yrs</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Last</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Graduate</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block>University or Technical Training</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Comp</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>Yr Att</fo:block></fo:table-cell>
            <fo:table-cell><fo:block>(Yes/No)</fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block>
              <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="90%"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block>
              <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="90%"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block>
              <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="90%"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block>
              <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="90%"/>
            </fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold">
              1. <xsl:value-of select="schoolName1"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="yearsComplete1"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="lastYrAttended1"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="degreeCert1"/>
            </fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold">
              2. <xsl:value-of select="schoolName2"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="yearsComplete2"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="lastYrAttended2"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="degreeCert2"/>
            </fo:block></fo:table-cell>
          </fo:table-row>
          <fo:table-row>
            <fo:table-cell><fo:block font-weight="bold">
              3. <xsl:value-of select="schoolName3"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="yearsComplete3"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="lastYrAttended3"/>
            </fo:block></fo:table-cell>
            <fo:table-cell><fo:block font-weight="bold">
              <xsl:value-of select="degreeCert3"/>
            </fo:block></fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
      <fo:block>
        (Last Yr. Attended is not required in California)
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
        List below your 3 most recent employers, beginning with the current or most recent one
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block>
        Name: <fo:inline font-weight="bold"><xsl:value-of select="emplrName1"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Addr.: <fo:inline font-weight="bold"><xsl:value-of select="emplrAddr1"/></fo:inline>
      </fo:block>
      <fo:block>
        City: <fo:inline font-weight="bold"><xsl:value-of select="emplrCity1"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        State: <fo:inline font-weight="bold"><xsl:value-of select="emplrStCd1"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="5%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Zip.: <fo:inline font-weight="bold"><xsl:value-of select="emplrZipCd1"/></fo:inline>
      </fo:block>
      <fo:block>
        Phone: <fo:inline font-weight="bold"><xsl:value-of select="emplrPhn1"/></fo:inline>
      </fo:block>
      <fo:block>
        What kind of work did you do? <fo:inline font-weight="bold"><xsl:value-of select="emplrWrkType1"/></fo:inline>
      </fo:block>
      <fo:block>
        Name of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupv1"/></fo:inline>
      </fo:block>
      <fo:block>
        Title of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupvTtl1"/></fo:inline>
      </fo:block>
      <fo:block>
        Why did you leave? <fo:inline font-weight="bold"><xsl:value-of select="emplrReasonLeaving1"/></fo:inline>
      </fo:block>
      <fo:block>
        Starting date: <fo:inline font-weight="bold"><xsl:value-of select="emplrBgnDt1"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Date of leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrEndDt1"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Pay at leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrPayRate1"/></fo:inline>
      </fo:block>
      <!-- *************************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block>
        Name: <fo:inline font-weight="bold"><xsl:value-of select="emplrName2"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Addr.: <fo:inline font-weight="bold"><xsl:value-of select="emplrAddr2"/></fo:inline>
      </fo:block>
      <fo:block>
        City: <fo:inline font-weight="bold"><xsl:value-of select="emplrCity2"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        State: <fo:inline font-weight="bold"><xsl:value-of select="emplrStCd2"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="5%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Zip.: <fo:inline font-weight="bold"><xsl:value-of select="emplrZipCd2"/></fo:inline>
      </fo:block>
      <fo:block>
        Phone: <fo:inline font-weight="bold"><xsl:value-of select="emplrPhn2"/></fo:inline>
      </fo:block>
      <fo:block>
        What kind of work did you do? <fo:inline font-weight="bold"><xsl:value-of select="emplrWrkType2"/></fo:inline>
      </fo:block>
      <fo:block>
        Name of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupv2"/></fo:inline>
      </fo:block>
      <fo:block>
        Title of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupvTtl2"/></fo:inline>
      </fo:block>
      <fo:block>
        Why did you leave? <fo:inline font-weight="bold"><xsl:value-of select="emplrReasonLeaving2"/></fo:inline>
      </fo:block>
      <fo:block>
        Starting date: <fo:inline font-weight="bold"><xsl:value-of select="emplrBgnDt2"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Date of leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrEndDt2"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Pay at leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrPayRate2"/></fo:inline>
      </fo:block>
      <!-- *************************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block>
        Name: <fo:inline font-weight="bold"><xsl:value-of select="emplrName3"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Addr.: <fo:inline font-weight="bold"><xsl:value-of select="emplrAddr3"/></fo:inline>
      </fo:block>
      <fo:block>
        City: <fo:inline font-weight="bold"><xsl:value-of select="emplrCity3"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="25%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        State: <fo:inline font-weight="bold"><xsl:value-of select="emplrStCd3"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="5%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Zip.: <fo:inline font-weight="bold"><xsl:value-of select="emplrZipCd3"/></fo:inline>
      </fo:block>
      <fo:block>
        Phone: <fo:inline font-weight="bold"><xsl:value-of select="emplrPhn3"/></fo:inline>
      </fo:block>
      <fo:block>
        What kind of work did you do? <fo:inline font-weight="bold"><xsl:value-of select="emplrWrkType3"/></fo:inline>
      </fo:block>
      <fo:block>
        Name of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupv3"/></fo:inline>
      </fo:block>
      <fo:block>
        Title of supervisor: <fo:inline font-weight="bold"><xsl:value-of select="emplrSupvTtl3"/></fo:inline>
      </fo:block>
      <fo:block>
        Why did you leave? <fo:inline font-weight="bold"><xsl:value-of select="emplrReasonLeaving3"/></fo:inline>
      </fo:block>
      <fo:block>
        Starting date: <fo:inline font-weight="bold"><xsl:value-of select="emplrBgnDt3"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Date of leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrEndDt3"/></fo:inline>
        <fo:leader leader-pattern="space" leader-length="10%"  leader-pattern-width="8pt" leader-alignment="reference-area"/>
        Pay at leaving: <fo:inline font-weight="bold"><xsl:value-of select="emplrPayRate3"/></fo:inline>
      </fo:block>
      <!-- **************************************************************************** -->
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
      </fo:block>
      <fo:block font-size="12pt" font-weight="bold" padding-before="0.5in">
        IMPORTANT - PLEASE READ AND SIGN THE FOLLOWING:
      </fo:block>
      <fo:block font-size="7pt" line-height="9pt">
        Applicant hereby certifies that the answers to the foregoing questions are true and correct. I agree if the
        information is found to be false in any respect, including omission of information, I will be subject to dismissal without
        notice. I authorize you to investigate all information in this application. I hereby authorize my former employers
        to release information pertaining to my work record, habits and performance. I understand that additional background
        investigation may be necessary for certain positions.
      </fo:block>
      <fo:block padding-before="0.1in"  font-size="7pt" line-height="9pt">
        Should I become an employee of The Home Depot, I understand that my employment will be for no definite term, and 
        that I will have the right to terminate my employment at any time, at my convenience, with or without cause or reason. 
        I further understand that The Home Depot will have the same right. I understand that I am expected to comply with all
        Company rules and regulations, but that such rules do not create a contract between me and the Company. I understand and
        agree that any handbook I receive will not constitute an employment contract, but will be a statement of the Company's
        current policies.
      </fo:block>
      <fo:block padding-before="0.1in" font-size="7pt" line-height="9pt">
        I understand that before any offer of employment is finalized, I will be required to submit a urine drug screen,
        at a Company-selected facility, at the Company's expense. If the test results demonstrate the presence of illegal drugs
        or non-prescribed controlled substances, I understand that I will not be permitted to commence work for the Company, or
        I will be terminated if I have already commenced work.
      </fo:block>
      <fo:block padding-before="0.1in" font-size="10pt">
          SIGNATURE: <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="3in"/>
          DATE: <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.5in"/>
      </fo:block>
      <!-- **************************************************************************** -->
          <!-- footer for last page -->
      <fo:block padding-before="1in"             
      		font-size="13pt" 
            line-height="15pt" >
          <fo:block>
            <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/>
          </fo:block>
          <fo:block font-weight="bold">
            For office use only:
          </fo:block>
          <fo:block>
              Dept:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="2.5in"/>
              Job title:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="2.5in"/>
          </fo:block>
            Start Date:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.5in"/>
            Associate Status:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.5in"/>
            Rate:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="2.5in"/>
          <fo:block>
            Hire Type: <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.5in"/>
            Birth Date:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1in"/>
            Sex:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="0.5in"/>
            Marital Status:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="0.5in"/>
          </fo:block>
          <fo:block>
            Race:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="2.25in"/>
            Drug Test Number:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="2.25in"/>
          </fo:block>
          <fo:block>
            In Case of Emergency contact:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.75in"/>
            Phone:<fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="1.75in"/>
          </fo:block>
      </fo:block>

    </fo:flow>
  </fo:page-sequence>
</xsl:template>     
</xsl:stylesheet>
