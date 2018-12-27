<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
<!--                 <fo:simple-page-master master-name="first-page"> -->
<!--                     <fo:region-body margin-top="0.3in" margin-bottom="0.3in"  -->
<!--                      margin-left="0.3in" margin-right="0.3in"/> -->
<!--                      <fo:region-body margin-top="0.41in"/> -->
<!--                      <fo:region-before extent="0.4in"/> -->
<!--                 </fo:simple-page-master> -->
                
                <fo:simple-page-master 
                master-name="first-page"
                page-height="8.3in" page-width="11.7in"
                margin-top="0.3in" margin-bottom="0.3in" 
                margin-left="0.3in" margin-right="0.3in" >
                <fo:region-body/>
                </fo:simple-page-master>
                
                <fo:simple-page-master 
                master-name="middle-pages"
                page-height="8.3in" page-width="11.7in"
                margin-top="0.3in" margin-bottom="0.3in" 
                margin-left="0.3in" margin-right="0.3in">
                <fo:region-body margin-top="0.41in"/>
                <fo:region-before extent="0.4in"/>
                </fo:simple-page-master>
          

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
    
            <fo:page-sequence master-reference="app-pages">
               
           <fo:static-content 
            flow-name="xsl-region-before" 
             
            font-family="Courier" 
            line-height="11pt" 
            text-align="start">
      <fo:table table-layout="fixed" border-collapse="collapse" width="100%">
<!--         <fo:table-column column-width="2.95in"/> -->
<!--         <fo:table-column column-width="4.95in"/> -->
        <fo:table-column column-width="33.3%"/>
        <fo:table-column column-width="33.3%"/>
        <fo:table-column column-width="33.3%"/>
      <fo:table-body><fo:table-row>
          <fo:table-cell><fo:block text-align="left" font-size="9pt">
            Home Depot U.S.A., Inc
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="center" font-weight="bold" font-size="13pt">
            Daily Interview Roster
          </fo:block></fo:table-cell>
          <fo:table-cell ><fo:block text-align="right" font-size="9pt">
            Printed: <xsl:value-of select="DailyInterviewRoster/PrintedDate"/>
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
<!--         <fo:table-column column-width="2.63in"/> -->
<!--         <fo:table-column column-width="2.63in"/> -->
<!--         <fo:table-column column-width="2.63in"/> -->
<fo:table-column column-width="33.3%"/>
        <fo:table-column column-width="33.3%"/>
        <fo:table-column column-width="33.3%"/>
      <fo:table-body><fo:table-row>
          <fo:table-cell><fo:block text-align="left">
            Home Depot U.S.A., Inc
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="center" font-weight="bold" font-size="10pt">
            Daily Interview Roster
          </fo:block></fo:table-cell>
          <fo:table-cell><fo:block text-align="right">
            Printed: <xsl:value-of select="DailyInterviewRoster/PrintedDate"/>
          </fo:block></fo:table-cell>
      </fo:table-row></fo:table-body>
      </fo:table>
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="2.0pt" leader-length="100%"/>
      </fo:block>
      <!-- **************************************************************************** -->
<!--       <fo:block font-size="7pt" line-height="9pt"> -->
<!--         The Home Depot is an equal opportunity employer. The company will not under any circumstances discriminate against an -->
<!--         Associate or Applicant with regard to race, age, sex, national origin, religion or disability. -->
<!--       </fo:block> -->
<!--       <fo:block font-weight="bold" font-size="7pt" line-height="9pt"> -->
<!--         WE WORK IN A SMOKE FREE ENVIRONMENT. -->
<!--       </fo:block> -->
      
      <fo:block font-size="7pt" line-height="9pt"></fo:block> <fo:block font-size="7pt" line-height="9pt"></fo:block>
      
         
                    <fo:block font-size="10pt" font-family="Times">
                        <xsl:apply-templates select="DailyInterviewRoster"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="DailyInterviewRoster">
    
    <fo:block font-size="9pt" line-height="9pt" padding-before="0.2in"> Hiring Event Name: <fo:inline font-weight="bold"> <xsl:value-of select="HiringEventName" /></fo:inline></fo:block>
       
       <fo:block>
       Location Name:<fo:inline font-weight="bold"><xsl:value-of select="Location"/>  </fo:inline>
        Address:<fo:inline font-weight="bold"><xsl:value-of select="Address"/>  </fo:inline>
      </fo:block>
      <fo:block>
        City:<fo:inline font-weight="bold" ><xsl:value-of select="City"/>  </fo:inline>                     
        State:<fo:inline font-weight="bold" ><xsl:value-of select="StateCd"/>  </fo:inline>  
        Zip:<fo:inline font-weight="bold" ><xsl:value-of select="ZipCd"/>  </fo:inline>
      </fo:block>
      
      <fo:table>
      <fo:table-body>
      <fo:table-row>
       <fo:table-cell>
      <fo:block padding-before="0.2in"> </fo:block> 
      </fo:table-cell>
       </fo:table-row>
       </fo:table-body>
       </fo:table>
            
      <fo:block font-size="9pt" line-height="9pt" padding-before="0.1in" >
        The Daily Interview Roster details of the applicants are displayed below in the interview time slot order.
      </fo:block> 
      <!-- **************************************************************************** -->
      <fo:block text-align="center"> 
         <fo:leader leader-pattern="rule" rule-thickness="1.0pt" leader-length="100%"/> 
      </fo:block>
   <fo:table start-indent="12pt">
   
   <fo:table-body>
   
  
       
    <xsl:for-each select="Interview">
   
        <fo:table-row>
       <fo:table-cell>
      <fo:block padding-before="0.2in"> </fo:block> 
      </fo:table-cell>
       </fo:table-row>
       
       <fo:table-row start-indent="0pt">
       <fo:table-cell>
      <fo:block font-size="9pt" text-decoration="underline" >InterviewDetails - <xsl:value-of select="@TimeSlot" />
       </fo:block> 
      </fo:table-cell>
       </fo:table-row>
       
       <fo:table-row>
       <fo:table-cell>
      <fo:block padding-before="0.1in"> </fo:block> 
      </fo:table-cell>
       </fo:table-row>
       
     <fo:table-row>
       <fo:table-cell>
    
        <fo:table width="100%" table-layout="fixed" start-indent="0pt" font-size="6pt" font-family="Times" text-align="right"
                 border-style="solid" border-top-width="0.3mm" border-bottom-width="0.3mm" border-left-width="0.3mm" border-right-width="0.3mm" >

            <fo:table-column column-number="1" column-width="5%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="2" column-width="5%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="3" column-width="7%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="4" column-width="4%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="5" column-width="6%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="6" column-width="5%" border-style="solid" border-width="0.2mm"/>            
            <fo:table-column column-number="7" column-width="7%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="8" column-width="10%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="9" column-width="5%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="10" column-width="6%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="11" column-width="25%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="12" column-width="5%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="13" column-width="5%" border-style="solid" border-width="0.2mm"/>
            <fo:table-column column-number="14" column-width="5%" border-style="solid" border-width="0.2mm"/>            
            
            
            <fo:table-header font-weight="bold" background-color="#F0F0F0" border-style="solid" border-bottom-width="0.3mm">
                <fo:table-row>
                    
                    <fo:table-cell column-number="1">
                            <fo:block text-align="center">
                                <xsl:value-of select="'Date'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="2">
                            <fo:block text-align="center">
                                <xsl:value-of select="'Time'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="3">
                            <fo:block text-align="center">
                                <xsl:value-of select="'RequisitionNumber'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="4">
                            <fo:block text-align="center">
                                <xsl:value-of select="'StoreNo'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="5">
                            <fo:block text-align="center">
                                <xsl:value-of select="'DepartmentNo'" />
                            </fo:block>
                        </fo:table-cell>
                         <fo:table-cell column-number="6">
                            <fo:block text-align="center">
                                <xsl:value-of select="'JobCode'" />
                            </fo:block>
                        </fo:table-cell>                        
                        <fo:table-cell column-number="7">
                            <fo:block text-align="center">
                                <xsl:value-of select="'RSCPhnScrnNo'" />
                            </fo:block>
                        </fo:table-cell>
                        
                        <fo:table-cell column-number="8">
                            <fo:block text-align="center">
                                <xsl:value-of select="'CandidateName'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="9">
                            <fo:block text-align="center">
                                <xsl:value-of select="'CandRefNbr'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="10">
                            <fo:block text-align="center">
                                <xsl:value-of select="'CandidatePhNo'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="11" wrap-option="wrap">
                            <fo:block text-align="center">
                                <xsl:value-of select="'CandidateEmail'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="12">
                            <fo:block text-align="center">
                                <xsl:value-of select="'HiringStore'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="13">
                            <fo:block text-align="center">
                                <xsl:value-of select="'StoreDeptNo'" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="14">
                            <fo:block text-align="center">
                                <xsl:value-of select="'StoreJobCode'" />
                            </fo:block>
                        </fo:table-cell>                        
                        

                </fo:table-row>
            </fo:table-header>

            <fo:table-body>
                <xsl:for-each select="candidate">
                    <fo:table-row>
                    
                     <fo:table-cell column-number="1">
                            <fo:block text-align="center">
                                <xsl:value-of select="Date" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="2">
                            <fo:block text-align="center">
                                <xsl:value-of select="Time" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="3">
                            <fo:block text-align="center">
                                <xsl:value-of select="RequisitionNumber" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="4">
                            <fo:block text-align="center">
                                <xsl:value-of select="StoreNo" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="5">
                            <fo:block text-align="center">
                                <xsl:value-of select="DepartmentNo" />
                            </fo:block>
                        </fo:table-cell>
                         <fo:table-cell column-number="6">
                            <fo:block text-align="center">
                                <xsl:value-of select="JobCode" />
                            </fo:block>
                        </fo:table-cell>                        
                        <fo:table-cell column-number="7">
                            <fo:block text-align="center">
                                <xsl:value-of select="RSCPhoneScreenNo" />
                            </fo:block>
                        </fo:table-cell>
                        
                        <fo:table-cell column-number="8">
                            <fo:block text-align="center">
                                <xsl:value-of select="CandidateName" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="9">
                            <fo:block text-align="center">
                                <xsl:value-of select="CandRefNbr" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="10">
                            <fo:block text-align="center">
                                <xsl:value-of select="CandidatePhNo" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="11">
                            <fo:block text-align="left">
                                <xsl:value-of select="CandidateEmail" />
                            </fo:block>
                        </fo:table-cell>
                         <fo:table-cell column-number="12">
                            <fo:block text-align="center">
                                <xsl:value-of select="HiringStore" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="13">
                            <fo:block text-align="center">
                                <xsl:value-of select="HiringStoreDeptNo" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell column-number="14">
                            <fo:block text-align="center">
                                <xsl:value-of select="HiringStoreJobCode" />
                            </fo:block>
                        </fo:table-cell>                        

                    </fo:table-row>
                                       
                    
                    
                    
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
        </fo:table-cell>
        </fo:table-row>
        
       
       <fo:table-row>
       <fo:table-cell>
      <fo:block padding-before="0.4in"> </fo:block> 
      </fo:table-cell>
       </fo:table-row>
        
<!--        <fo:table-row> -->
<!--        <fo:table-cell> -->
        
<!--         <fo:table width="100%" table-layout="fixed" start-indent="0pt" -->
<!--                     font-size="6pt" font-family="Times" text-align="right" -->
<!--                     border-style="solid" border-top-width="0.3mm" border-bottom-width="0.3mm" border-left-width="0.3mm" border-right-width="0.3mm"> -->

<!--             <fo:table-column column-number="1" column-width="10%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="2" column-width="7%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="3" column-width="10%" border-style="solid" border-width="0.2mm" /> -->
<!--             <fo:table-column column-number="4" column-width="16%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="5" column-width="10%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="6" column-width="10%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="7" column-width="7%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="8" column-width="10%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="9" column-width="10%" border-style="solid" border-width="0.2mm"/> -->
<!--             <fo:table-column column-number="10" column-width="10%" border-style="solid" border-width="0.2mm"/> -->

<!--             <fo:table-header font-weight="bold" background-color="#F0F0F0" border-style="solid" border-bottom-width="0.3mm"> -->
<!--                 <fo:table-row > -->
                    
<!--                         <fo:table-cell column-number="1"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'CandidateName'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="2"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'SSN'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="3"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'CandidatePhNo'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="4"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'Email'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="5"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'ReqNumber'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="6"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'StoreNo'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="7"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'DeptNo'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                          <fo:table-cell column-number="8"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'JobCode'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                          <fo:table-cell column-number="9"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'Date'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="10"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="'Time'" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                 </fo:table-row> -->
<!--             </fo:table-header> -->

<!--             <fo:table-body> -->
<!--                 <xsl:for-each select="candidate"> -->
<!--                     <fo:table-row> -->
                       
<!--                         <fo:table-cell column-number="1"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="CandidateName" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="2"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="SSN" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="3"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="CandidatePhNo" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="4"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="CandidateEmail" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="5"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="RequisitionNumber" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="6"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="StoreNo" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="7"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="DepartmentNo" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                          <fo:table-cell column-number="8"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="JobCode" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                          <fo:table-cell column-number="9"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="Date" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                         <fo:table-cell column-number="10"> -->
<!--                             <fo:block text-align="left"> -->
<!--                                 <xsl:value-of select="Time" /> -->
<!--                             </fo:block> -->
<!--                         </fo:table-cell> -->
<!--                     </fo:table-row> -->
<!--                     </xsl:for-each> -->
<!--                     </fo:table-body> -->
<!--                     </fo:table> -->
                    
<!--                   </fo:table-cell> -->
<!--                 </fo:table-row> -->
                
<!--        <fo:table-row> -->
<!--        <fo:table-cell> -->
<!--       <fo:block padding-before="0.4in"> </fo:block>  -->
<!--       </fo:table-cell> -->
<!--        </fo:table-row> -->
        
              </xsl:for-each>
        </fo:table-body>
        </fo:table>
                    
    </xsl:template>
</xsl:stylesheet>