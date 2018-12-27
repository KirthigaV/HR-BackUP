<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <xsl:template match="/">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

      <fo:layout-master-set>

        <fo:simple-page-master master-name="A4"
                page-height="11in" page-width="8.5in"
                margin-top="0.1in" margin-bottom="0.3in" 
                margin-left="0.3in" margin-right="0.3in">
          <fo:region-body margin-top="0.2in" margin-bottom="0.4in"/>
        </fo:simple-page-master>

      </fo:layout-master-set>

      <xsl:apply-templates/>
      
  </fo:root>
</xsl:template>


<xsl:template match="app">

      <fo:page-sequence master-reference="A4">



        <fo:flow flow-name="xsl-region-body">


          <fo:table table-layout="fixed">
			<fo:table-column column-width="2.63in"/>
			<fo:table-column column-width="2.63in"/>		
			<fo:table-column column-width="2.63in"/>	
			<fo:table-header>
					<fo:table-cell number-columns-spanned="3">
						<fo:block ></fo:block>
					</fo:table-cell>
			</fo:table-header>

            <fo:table-body>
		
			<fo:table-row font-size="12pt">
			  	<fo:table-cell>
			    	<fo:block font-weight="bold">HOME DEPOT U.S.A., Inc.</fo:block>
			  	</fo:table-cell>
			  	<fo:table-cell >
			    	<fo:block font-weight="bold" text-align="center">Associate Profile</fo:block>
			  	</fo:table-cell>
			  	<fo:table-cell>
			    	<fo:block font-weight="bold"></fo:block>
			  	</fo:table-cell>
			 </fo:table-row>
            </fo:table-body>
          </fo:table>

      	<fo:block>
        	<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="100%"/>
	</fo:block>


      	<fo:block>
        	Name: <fo:inline ><xsl:value-of select="name"/> </fo:inline>
      	</fo:block>

      	<fo:block>
        	Candidate Reference #: <fo:inline ><xsl:value-of select="candRefNbr"/> </fo:inline>
      	</fo:block>

	<fo:block>
		&#160;
	</fo:block>

       	<fo:block>
        	Current Store: <fo:inline ><xsl:value-of select="currentStore"/> </fo:inline> &#160; &#160;
		Current Dept: <fo:inline ><xsl:value-of select="currentDept"/> </fo:inline> &#160; &#160;
		Current Title: <fo:inline ><xsl:value-of select="currentTitle"/> </fo:inline> 
      	</fo:block>

      	<fo:block>
        	Current Status: <fo:inline ><xsl:value-of select="currentStatus"/> </fo:inline> &#160; &#160;
		PT Hire Date: <fo:inline ><xsl:value-of select="hireDt"/> </fo:inline>
      	</fo:block>

      	<fo:block>
        	<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="100%"/>
	</fo:block>


      	<fo:block>
        	Job Preferences: 
      	</fo:block>

	<xsl:if test="string-length(AssoJobPref) = 0">
      		<fo:block font-weight="bold">
        		No job preferences currently selected.
	      	</fo:block>
	</xsl:if>

	<xsl:if test="string-length(AssoJobPref) != 0">
      		<fo:block>
        		<fo:inline ><xsl:value-of select="AssoJobPref"/> </fo:inline>
	      	</fo:block>
	</xsl:if>

	<fo:block>
		&#160;
	</fo:block>
	
     	<fo:block>
        	<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="100%"/>
	</fo:block>

      	<fo:block>
        	Review Results: 
      	</fo:block>


	<xsl:if test="string-length(reviewScore1) = 0">
      		<fo:block font-weight="bold">
        		No reviews done yet for this associate.
	      	</fo:block>
	</xsl:if>

	<xsl:if test="string-length(reviewScore1) != 0">
      		<fo:table table-layout="fixed" >
		        <fo:table-column column-width="1.5in"/>
		        <fo:table-column column-width="1.5in"/>
			<fo:table-header>
					<fo:table-cell>
						<fo:block ></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block ></fo:block>
					</fo:table-cell>
			</fo:table-header>
			<fo:table-body>
			<fo:table-row>
	        		<fo:table-cell>
					<fo:block text-align="center">
            					DATE
          				</fo:block>
				</fo:table-cell>
          			<fo:table-cell>
					<fo:block text-align="center">
            					RESULTS
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
			<fo:table-row>
          			<fo:table-cell>
					<fo:block text-align="center">
            					<xsl:value-of select="reviewDt1"/>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="center">
		            			<xsl:value-of select="reviewScore1"/>
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
			<fo:table-row>
          			<fo:table-cell>
					<fo:block text-align="center">
            					<xsl:value-of select="reviewDt2"/>
	          			</fo:block>
				</fo:table-cell>
			        <fo:table-cell>
					<fo:block text-align="center">
		            			<xsl:value-of select="reviewScore2"/>
	          			</fo:block>
				</fo:table-cell>
      			</fo:table-row>
		</fo:table-body>
      		</fo:table>
	</xsl:if>


      	<fo:block>
        	<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="100%"/>
	</fo:block>


      	<fo:block>
        	Previous Positions: 
      	</fo:block>

	<xsl:if test="string-length(frmrDept1) = 0">
      		<fo:block font-weight="bold">
        		Associate has only held current position.
	      	</fo:block>
	</xsl:if>

<xsl:if test="string-length(frmrDept1) != 0">
      		<fo:table table-layout="fixed" >
		        <fo:table-column column-width="1in"/>
		        <fo:table-column column-width="1.5in"/>
			<fo:table-column column-width="1in"/>
			<fo:table-column column-width="1in"/>
      		
			<fo:table-header>
					<fo:table-cell number-columns-spanned="4">
						<fo:block ></fo:block>
					</fo:table-cell>
			</fo:table-header>
			
			<fo:table-body>
		
			<fo:table-row>
          			<fo:table-cell>
					<fo:block>
						Dept: <fo:inline ><xsl:value-of select="frmrDept1"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			Position: <fo:inline ><xsl:value-of select="frmrJobCd1"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrFromDt1"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrToDt1"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
		
		    <xsl:if test="string-length(frmrDept2) != 0"> 
			<fo:table-row>
          			<fo:table-cell>
					<fo:block>
						Dept: <fo:inline ><xsl:value-of select="frmrDept2"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			Position: <fo:inline ><xsl:value-of select="frmrJobCd2"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrFromDt2"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrToDt2"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
		     </xsl:if> 
		   <xsl:if test="string-length(frmrDept3) != 0"> 
		   			<fo:table-row>
          			<fo:table-cell>
					<fo:block>
						Dept: <fo:inline ><xsl:value-of select="frmrDept3"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			Position: <fo:inline ><xsl:value-of select="frmrJobCd3"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrFromDt3"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrToDt3"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
		    </xsl:if> 
		    <xsl:if test="string-length(frmrDept4) != 0"> 
			<fo:table-row>
          			<fo:table-cell>
					<fo:block>
						Dept: <fo:inline ><xsl:value-of select="frmrDept4"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			Position: <fo:inline ><xsl:value-of select="frmrJobCd4"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrFromDt4"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
		        	<fo:table-cell>
					<fo:block text-align="left">
		            			<fo:inline ><xsl:value-of select="frmrToDt4"/> </fo:inline>
          				</fo:block>
				</fo:table-cell>
      			</fo:table-row>
		    </xsl:if> 

		</fo:table-body>
      		</fo:table>
</xsl:if>



	</fo:flow>

      </fo:page-sequence>


  </xsl:template>



</xsl:stylesheet>