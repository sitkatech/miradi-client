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

public class TargetThreatLinkTableModel extends MainThreatTableModel
{
	public TargetThreatLinkTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return targetColumns.length;
	}
	
	public String getColumnName(int column)
	{
		return targetColumns[column].toString();
	}
	
	public Object getValueAt(int row, int column)
	{
		String valueToConvert = getFactorLinkThreatRatingBundle(row, column);
		return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);	
	}

	private String getFactorLinkThreatRatingBundle(int row, int column)
	{
		try
		{
			if (!areLinked(row, column))
				return null;
			
			FactorLink factorLink = getFactorLink(row, column);
			int calculatedValue = factorLink.calculateThreatRatingBundleValue();
			return convertIntToString(calculatedValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}

	private FactorLink getFactorLink(int row, int column)
	{
		return FactorLink.find(getProject(), getLinkRef(getDirectThreat(row), getTarget(column)));
	}

	private boolean areLinked(int row, int column)
	{
		return areLinked(getDirectThreat(row), getTarget(column));
	}
	
	public boolean areLinked(Factor from, Factor to)
	{
		return getProject().getFactorLinkPool().areLinked(from, to);
	}
	
	public String getColumnTag(int column)
	{
		return "";
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		if (areLinked(row, column))
			return getFactorLink(row, column);
			
		return null;
	}
}
