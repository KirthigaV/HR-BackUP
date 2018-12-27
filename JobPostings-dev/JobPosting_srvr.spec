############ DO NOT MODIFY THIS SECTION ############
%define RPM_BUILD_ROOT $(pwd)
%define ver %(/bin/awk -f /opt/hd/dt/bin/dtk_getVersion.awk ../../build.xml)
%define rel %(/bin/awk -f /opt/hd/dt/bin/dtk_getRelease.awk ../../build.xml)
%define sum %(/bin/echo "$(/opt/isv/svn/bin/svn info $(/bin/pwd)/../..|/bin/grep URL) - $(/opt/isv/svn/bin/svn info $(/bin/pwd)/../..|/bin/grep Rev:|/bin/cut -c14-)")
%define desc %(/bin/cat $(pwd)/../../target/MANIFEST.MF)
####################################################

# RPM package information                                    #
Name: hd-tcgrid-et-JobPosting_srvr
Version: %{ver}
Release: %{rel}
Summary: %{sum}
Group: The Home Depot
 
############ DO NOT MODIFY THIS SECTION ############
License: Non-Commercial
BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}
AutoProv: 0
AutoReq: 0

%description
%{desc}

%prep
%install
mkdir -p %{buildroot}
export PKG_NAME=%{name}
####################################################

#>>>>>--------------------------------------------------<<<<<#
# Add the files to package relative to the Build Directory   #
# and the final file name and location on the runtime        #
# environment                                                #
# NOTE : - each file to package need to be defined relative  #
#          to ../SOURCES                                     #
#        - each final file name and location need to be      #
#          defined relative to %{buildroot}                  #

install -D ../SOURCES/JobPostings.war %{buildroot}/var/opt/tomcat/et/java/wars/JobPostings.war
install -D ../SOURCES/JobPostings_install.ksh %{buildroot}/var/opt/tomcat/et/java/wars/JobPostings_install.ksh
install -D ../SOURCES/JobPostings_uninstall.ksh %{buildroot}/var/opt/tomcat/et/java/wars/JobPostings_uninstall.ksh

#>>>>>--------------------------------------------------<<<<<#

############ DO NOT MODIFY THIS SECTION ############
%build
####################################################

############ DO NOT MODIFY THIS SECTION ############
%files
####################################################

#>>>>>--------------------------------------------------<<<<<#
# Add here attributes to define for each file name deployed  #
# on the runtime environment                                 #

%attr(0755,tomcat,tomcat) /var/opt/tomcat/et/java/wars/JobPostings.war
%attr(0755,tomcat,tomcat) /var/opt/tomcat/et/java/wars/JobPostings_install.ksh
%attr(0755,tomcat,tomcat) /var/opt/tomcat/et/java/wars/JobPostings_uninstall.ksh

#>>>>>--------------------------------------------------<<<<<#

%post
/var/opt/tomcat/et/java/wars/JobPostings_install.ksh

%preun
/var/opt/tomcat/et/java/wars/JobPostings_uninstall.ksh $*

############ DO NOT MODIFY THIS SECTION ############
%clean
####################################################
%changelog
