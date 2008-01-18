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
		if (valueToConvert != null)
			return convertToChoiceItem(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, valueToConvert);
			
		return null;	
	}

	private String getFactorLinkThreatRatingBundle(int row, int column)
	{
		try
		{
			if (!areLinked(row, column))
				return null;
			
			FactorLink factorLink = getFactorLink(row, column);
			return Integer.toString(factorLink.calculateThreatRatingBundleValue());
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
