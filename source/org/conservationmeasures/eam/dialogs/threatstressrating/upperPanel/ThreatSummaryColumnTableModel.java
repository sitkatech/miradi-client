/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

public class ThreatSummaryColumnTableModel extends MainThreatTableModel
{
	public ThreatSummaryColumnTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnName(int column)
	{
		return EAM.text("Summary Threat Rating");
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}

	public int getColumnCount()
	{
		return 1;
	}

	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getCalculatedThreatSummaryRatingValue(row);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);
	}
	
	private String getCalculatedThreatSummaryRatingValue(int row)
	{
		try
		{
			int calculatedValue = calculateThreatSummaryRatingValue(threatRows[row]);
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}
	
	public int calculateThreatSummaryRatingValue(Factor directThreat) throws Exception
	{
		return frameWork.get2PrimeSummaryRatingValue(directThreat);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getDirectThreat(row);
	}
}
