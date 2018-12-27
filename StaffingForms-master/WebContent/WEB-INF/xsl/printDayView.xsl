<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:fo="http://www.w3.org/1999/XSL/Format">

 <xsl:attribute-set name="cell.span4TopLeftRightBorder">
	<xsl:attribute name="number-columns-spanned">4</xsl:attribute>
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.span4TopBorder">
	<xsl:attribute name="number-columns-spanned">4</xsl:attribute>
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0mm</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.span4LeftRightBorder">
	<xsl:attribute name="number-columns-spanned">4</xsl:attribute>
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>	
</xsl:attribute-set>
<xsl:attribute-set name="cell.span4BottomLeftRightBorder">
	<xsl:attribute name="number-columns-spanned">4</xsl:attribute>
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
		<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.timeSlotTop">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
		<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.timeSlot">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
		<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.timeSlotBottom">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
		<xsl:attribute name="background-color">#F0F0F0</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameTopLeft">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-color">black</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameTopLeft">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-color">black</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameTopCenter">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameTopRight">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-color">black</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameLeft">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-color">black</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameCenter">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameRight">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-color">black</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameBottomLeft">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">black</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-color">black</xsl:attribute>
	<xsl:attribute name="border-right-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameBottomCenter">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">black</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.nameBottomRight">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">black</xsl:attribute>
	<xsl:attribute name="border-left-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-right-color">black</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="cell.name">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-left-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-width">0.0mm</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.storeNumberTop">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.storeNumberTopRight">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-top-color">black</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-color">black</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.storeNumber">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.storeNumberBottom">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">black</xsl:attribute>
	<xsl:attribute name="border-left-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-right-color">grey</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="cell.storeNumberRight">
	<xsl:attribute name="padding-left">0.5mm</xsl:attribute>
	<xsl:attribute name="padding-right">0.5mm</xsl:attribute>
	<xsl:attribute name="border-style">solid</xsl:attribute>
	<xsl:attribute name="border-top-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-top-color">grey</xsl:attribute>
	<xsl:attribute name="border-bottom-width">0.3mm</xsl:attribute>
	<xsl:attribute name="border-bottom-color">grey</xsl:attribute>
	<xsl:attribute name="border-left-width">0.0mm</xsl:attribute>
	<xsl:attribute name="border-left-color">grey</xsl:attribute>
	<xsl:attribute name="border-right-width">0.5mm</xsl:attribute>
	<xsl:attribute name="border-right-color">black</xsl:attribute>
</xsl:attribute-set>

<xsl:attribute-set name="block.titleBigLabel">
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="font-size">12pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-top">4.0mm</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="block.titleLabel">
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="font-size">10pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-top">1.0mm</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="block.boxNumberTop">
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="font-weight">bold</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-left">1.0mm</xsl:attribute>
	<xsl:attribute name="margin-top">2mm</xsl:attribute>		
</xsl:attribute-set>
<xsl:attribute-set name="block.boxNumber">
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="color">grey</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-left">1.0mm</xsl:attribute>
	<xsl:attribute name="margin-top">2mm</xsl:attribute>		
</xsl:attribute-set>
<xsl:attribute-set name="block.storeNumber">
	<xsl:attribute name="text-align">right</xsl:attribute>
	<xsl:attribute name="font-size">8pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="color">grey</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-right">0.5mm</xsl:attribute>
	<xsl:attribute name="margin-top">2mm</xsl:attribute>		
</xsl:attribute-set>

<xsl:attribute-set name="block.name">
	<xsl:attribute name="text-align">left</xsl:attribute>
	<xsl:attribute name="font-size">7pt</xsl:attribute>
	<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	<xsl:attribute name="line-height">1mm</xsl:attribute>
	<xsl:attribute name="margin-left">0.0mm</xsl:attribute>
	<xsl:attribute name="margin-top">2mm</xsl:attribute>		
</xsl:attribute-set>
<xsl:template match="dayView">
<!-- labels defined as parameters-->
<xsl:param name="space">&#160;</xsl:param>




<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

      <fo:layout-master-set>
        <fo:simple-page-master master-name="payStmtMain"
                page-height="11in" page-width="8.5in"
                margin-top="0.3in" margin-bottom="0.3in" 
                margin-left="0.25in" margin-right="0.25in">
               
        	<fo:region-body margin-top="0.25in" margin-bottom="0.25in" ></fo:region-body>
        	<fo:region-before extent=".25in"/>
        	<fo:region-after extent="0.25in"/>
        </fo:simple-page-master>


      </fo:layout-master-set>

      <fo:page-sequence master-reference="payStmtMain" initial-page-number="1" >
	<fo:static-content flow-name="xsl-region-before">
	  <fo:block  font-size="12pt" font-weight="bold" text-align="center" >
            <xsl:value-of select="./error"/>
          </fo:block>
	</fo:static-content>

	<fo:static-content flow-name="xsl-region-after">
		<fo:block text-align="center"><fo:page-number/></fo:block>
	</fo:static-content>


        <fo:flow flow-name="xsl-region-body">

	<fo:table table-layout="fixed" width="100%" >
		<fo:table-column column-width="0.59in"/>
		<fo:table-column column-width="2.48in"/>
		<fo:table-column column-width="2.48in"/>
		<fo:table-column column-width="2.48in"/>
		<fo:table-header>
			<fo:table-row height="8mm">
				<fo:table-cell xsl:use-attribute-sets="cell.span4TopLeftRightBorder">
					<fo:block xsl:use-attribute-sets="block.titleBigLabel">Day View</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row height="4mm">
				<fo:table-cell xsl:use-attribute-sets="cell.span4LeftRightBorder">
					<fo:block xsl:use-attribute-sets="block.titleLabel">
						Interview Date:<xsl:value-of select="$space"/><xsl:value-of select="./date"/><xsl:value-of select="$space"/>/<xsl:value-of select="$space"/>
						Location:<xsl:value-of select="$space"/><xsl:value-of select="./location"/><xsl:value-of select="$space"/>/<xsl:value-of select="$space"/>
						Calendar:<xsl:value-of select="$space"/><xsl:value-of select="./calendar"/></fo:block>
				</fo:table-cell>
			</fo:table-row>				
			<fo:table-row height="5mm">
				<fo:table-cell xsl:use-attribute-sets="cell.span4BottomLeftRightBorder">
					<fo:block xsl:use-attribute-sets="block.titleLabel">Printed on <xsl:value-of select="./printDate"/>.  Please reprint 24 hours prior to interview.</fo:block>
				</fo:table-cell>
			</fo:table-row>
			<fo:table-row height="1mm">
			  	<fo:table-cell number-columns-spanned="4">
		    	<fo:block></fo:block>
			</fo:table-cell>
			</fo:table-row>
		</fo:table-header>
		<fo:table-body>

	       <xsl:apply-templates select="//timeSlot">
	       </xsl:apply-templates>
  			<fo:table-row height="1mm">
				<fo:table-cell xsl:use-attribute-sets="cell.span4TopBorder">
					<fo:block></fo:block>
				</fo:table-cell>
			</fo:table-row>

   </fo:table-body>
	</fo:table>
         
        </fo:flow>

      </fo:page-sequence>

    </fo:root>
</xsl:template>

  <xsl:template match="timeSlot">
    <fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlotTop">
        <fo:block xsl:use-attribute-sets="block.boxNumberTop"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameTopLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name1/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameTopCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name2/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameTopRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name3/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>
	<xsl:if test="name4/@seq &gt; 3 ">
	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name4/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name5/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name6/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>
	 </xsl:if>			  
	<xsl:if test="name7/@seq &gt; 6 ">
	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name7/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name8/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name9/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>	 
	 </xsl:if>			  
	<xsl:if test="name10/@seq &gt; 9 ">
 	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name10/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name11/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name12/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>
	 </xsl:if>			  
	<xsl:if test="name13/@seq &gt; 12 ">
	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name13/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name14/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name15/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>	 
	 </xsl:if>			  
	<xsl:if test="name16/@seq &gt; 15 ">
	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name16/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name17/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name18/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>	 
	 </xsl:if>			  
	<xsl:if test="name19/@seq &gt; 18 ">
	<fo:table-row height="5mm">
      <fo:table-cell xsl:use-attribute-sets="cell.timeSlot">
        <fo:block xsl:use-attribute-sets="block.boxNumber"><xsl:value-of select="time"/></fo:block>
      </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameLeft">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name19/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameCenter">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name20/name"/></fo:block>
		  </fo:table-cell>
		  <fo:table-cell xsl:use-attribute-sets="cell.nameRight">
			<fo:block xsl:use-attribute-sets="block.name"><xsl:value-of select="name21/name"/></fo:block>
		  </fo:table-cell>
     </fo:table-row>	 
	 </xsl:if>			  

	 </xsl:template>

</xsl:stylesheet>