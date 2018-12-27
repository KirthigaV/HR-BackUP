# HR-Staffing/StaffingForms

# Context Root: /StaffingForms

This is a Java Custom Grid Application that is used to add Interview Availability and Manage Calendars For RSA and IVR

Current In Production as of 11/14/2016 CHG0236400
	Removed the Skills section from both printed applications.
	Removed the Available To Work Date from Printed Application (External)
	Added new LDAP groups for Interline Security Model.
	RFC : Puppet Module:staffingforms Puppet Version:6_0_4		
	SWP_TomcatGrid-AppSvr-hrhrStaffingForms-Rel-6.0  
		hd-aa-CrAppSvr_hrhrStaffingForms-1.2-1.el5.x86_64 
		hd-tcgrid-hr-StaffingForms-jvsvr-6.0.8-1.x86_64
		
Current In Production as of 08/26/2016
	SWP_TomcatGrid-AppSvr_hrhrStaffingAdministration-Rel-3.1.3-1 
		hd-aa-CrAppSvr_hrhrStaffingAdministration-2.3-1.el5.x86_64 
		hd-tcgrid-hr-StaffingForms-jvsvr-5.3.2-1.x86_64

Puppet Git Repo https://github.homedepot.com/ose-automation-puppet/thdgrid-staffingforms

NOTE: When in github click Raw in order to see all the Data Sources.

Data Sources AD:
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

Data Sources QA:
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
 
