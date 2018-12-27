# HR-Staffing/RetailStaffingAdmin

# Context Root: /RetailStaffing #

This is a Java Custom Grid Application that is used by the RSC to Attach Applicants via the QP, Conduct Phone Screens, and Schedule Interviews 

Current In Production as of 11/14/2016 Evening CHG0237572
	Removed the Previous Addess Info on the CPD Form.  Prod Issue
	RFC Puppet Module:retailstaffingadmin Puppet Version:94_0_8	
		hd-aa-CrAppSvr_hrhrRetailStaffing-6.9-1.el5.x86_64 
		hd-tcgrid-hr-RetailStaffing-jvsvr-94.0.10-1.x86_64	
	
Current In Production as of 11/14/2016  CHG0236539
	Moved to Puppet Deployment
	Removed the Skills section from Applicant Profile
	Added new LDAP groups for Interline Security Model.
	Applicant Profile --> Application History:  Removed the Last Updated Timestamp from Store and Position grids
	External QP Screen: Relabeled Last Updated to Last Applied
	Internal QP Screen: Removed Last Updated column
	Added Middle Name and Previous Address to CPD Form.
	Added Interline Phone Screen Banners	
	RFC Puppet Module:retailstaffingadmin Puppet Version:94_0_7	
	SWP_TomcatGrid-AppSvr-hrhrRetailStaffing-Rel-94.0 
		hd-aa-CrAppSvr_hrhrRetailStaffing-6.9-1.el5.x86_64 
		hd-tcgrid-hr-RetailStaffing-jvsvr-94.0.9-1.x86_64
		
Current In Production as of 09/22/2016
	SWP_TomcatGrid-AppSvr-hrhrRetailStaffing-Rel-93.5.5-1 
		hd-aa-CrAppSvr_hrhrRetailStaffing-6.9-1.el5.x86_64 
		hd-tcgrid-hr-RetailStaffing-jvsvr-93.5.5-1.x86_64

Puppet Git Repo https://github.homedepot.com/ose-automation-puppet/thdgrid-retailstaffingadmin

NOTE: When in github click Raw in order to see all the Data Sources.

Data Sources AD:

`
<Context allowLinking="true" crossContext="true">
  <WatchedResource>WEB-INF/web.xml</WatchedResource>
  <Resource type="javax.sql.DataSource" name="jdbc/Sessions" auth="Container" driverClassName="oracle.jdbc.driver.OracleDriver" url="jdbc:oracle:thin:@//cplinph1.homedepot.com:1521/DAD053TA" username="TAUSR02" password="javabatch_ad4u" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/TA.AA.DAO-ADMIN.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="ad3tb01" password="k3isopx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.005" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="adehr01" password="ze2sxz1" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/MM.RD.VNDR-MNT.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="adwmm01" password="ip2ls1a" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="1000"/>
  <Resource type="javax.sql.DataSource" name="jdbc/WorkforceEmploymentQualifications.2" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="ad2lm01" password="dk2ldkv" maxActive="18" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.032" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="adehr01" password="ze2sxz1" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/HR.HR.SELF-SVC-FUNC.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="adchr01" password="csw2scxa" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/TA.AA.GRID-APPL-PLC-ADMIN.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="ST2TB01" password="DtlNpr7Bj8w=" maxActive="10" maxIdle="3" maxWait="30000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
</Context>
`

Data Sources QA:

`
<Context antiJARLocking="false" antiResourceLocking="false">
  <WatchedResource>WEB-INF/web.xml</WatchedResource>
  <Resource type="javax.sql.DataSource" name="jdbc/Sessions" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="adsta01" password="jxi3728p" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/TA.AA.DAO-ADMIN.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="ad3tb01" password="k3isopx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.005" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="QAEHR01" password="wqve3dx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/MM.RD.VNDR-MNT.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://IBDYNPX0.SYSPLEX.HOMEDEPOT.COM:5122/NP1" username="qawmm01" password="i4dg5wg " maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="1000"/>
  <Resource type="javax.sql.DataSource" name="jdbc/WorkforceEmploymentQualifications.2" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="QAEHR01" password="wqve3dx" maxActive="18" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.032" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="qaehr01" password="wqve3dx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.001" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="qaehr01" password="wqve3dx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/DB2Z.PR1.007" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="qaehr01" password="wqve3dx" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/>
  <Resource type="javax.sql.DataSource" name="jdbc/WorkforceSuccessionPlanning.1" auth="Container" driverClassName="com.ibm.db2.jcc.DB2Driver" url="jdbc:db2://ibdynpx0.sysplex.homedepot.com:5122/NP1" username="QA4HR01" password="p3n6c51t" maxActive="10" maxIdle="30" maxWait="10000" logAbandoned="true" removeAbandoned="true" removeAbandonedTimeout="60"/> 
</Context>
`
 
