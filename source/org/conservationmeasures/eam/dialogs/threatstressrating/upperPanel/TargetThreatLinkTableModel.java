/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class TargetThreatLinkTableModel extends MainThreatTableModel
{
	public TargetThreatLinkTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public int getColumnCount()
	{
		return targets.length;
	}
	
	public String getColumnName(int column)
	{
		return targets[column].toString();
	}
	
	public Object getValueAt(int row, int column)
	{
		return getFactorLinkThreatRatingBundle(row, column);
	}
	
	private String getFactorLinkThreatRatingBundle(int row, int column)
	{
		try
		{
			if (!areLinked(row, column))
				return "";
			
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
		return FactorLink.findFactorLink(getProject(), getLinkRef(getDirectThreat(row).getRef(), getTarget(column).getRef()));
	}

	//TODO combine the two xxLinked method below
	private boolean areLinked(int row, int column)
	{
		return getProject().areLinked(getDirectThreat(row).getFactorId(), getTarget(column).getFactorId());
	}
	
	public boolean isLinked(ORef fromRef, ORef toRef)
	{
		return getProject().getFactorLinkPool().isLinked(fromRef, toRef);
	}
	
	public Target getTarget(int modelColumn)
	{
		return targets[modelColumn];
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
