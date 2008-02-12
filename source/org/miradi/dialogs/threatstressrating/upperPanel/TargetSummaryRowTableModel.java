/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

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
