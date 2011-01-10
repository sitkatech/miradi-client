<?xml version="1.0"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:miradi="http://xml.miradi.org/schema/ConservationProject/49" 
	version="1.0">
<xsl:output method="html" indent="yes"/>

<xsl:template match="miradi:ConservationProject">
  <html>
  <body>
        <xsl:apply-templates select="miradi:ProjectSummary" />
        <xsl:apply-templates select="miradi:StrategyPool"/>
  </body>
  </html>
</xsl:template>

<xsl:template match="miradi:ProjectSummary">
        <xsl:value-of select="miradi:ProjectSummaryProjectName"/>
</xsl:template>

<xsl:template match="miradi:StrategyPool">
   <table border="1" width="50%">
   <thead>
        <tr>
           <td><b>Strategy</b></td>
           <td><b>Relevant Objectives</b></td>
        </tr>
  </thead> 
     <xsl:for-each select="./miradi:Strategy">
       <tr>
         <td>
		<xsl:value-of select="miradi:StrategyName"/>
	</td>
         
	<td>        
	<xsl:apply-templates name="RelevantObjectivesForStrategy" select="//miradi:Objective">
		<xsl:with-param name="StrategyId" select="@Id"/>
	</xsl:apply-templates>
	</td>
        </tr>
      </xsl:for-each>
    </table>
</xsl:template>

<xsl:template name="RelevantObjectivesForStrategy" match="miradi:Objective">
	<xsl:param name="StrategyId" />
		<xsl:if test="./miradi:ObjectiveRelevantStrategyIds/miradi:StrategyId = $StrategyId">
		<xsl:text>
		</xsl:text> <br/>
		<xsl:value-of select="miradi:ObjectiveId" />:
		<xsl:value-of select="miradi:ObjectiveName" />
		</xsl:if>
</xsl:template>

</xsl:stylesheet>

