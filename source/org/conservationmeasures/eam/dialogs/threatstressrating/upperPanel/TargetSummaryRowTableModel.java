/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

public class TargetSummaryRowTableModel extends MainThreatTableModel
{
	public TargetSummaryRowTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	public String getColumnTag(int column)
	{
		return "";
	}
	
	public int getRowCount()
	{
		return 1;
	}

	public int getColumnCount()
	{
		return targetColumns.length;
	}

	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getCalculatedTargetSummaryRatingValue(column);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);
	}
	
	private String getCalculatedTargetSummaryRatingValue(int column)
	{
		try
		{
			int calculatedValue = calculateThreatSummaryRatingValue(targetColumns[column]);
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}

	public int calculateThreatSummaryRatingValue(Factor target) throws Exception
	{
		return frameWork.get2PrimeSummaryRatingValue(target);
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return targetColumns[column];
	}
}
