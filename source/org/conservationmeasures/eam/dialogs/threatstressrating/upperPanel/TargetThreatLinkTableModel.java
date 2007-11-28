/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class TargetThreatLinkTableModel extends MainThreatTableModel
{
	public TargetThreatLinkTableModel(Project projectToUse)
	{
		super(projectToUse);
		targets = getProject().getTargetPool().getTargets();
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
			Factor directThreat = directThreatRows[row];
			Target target = targets[column];
			if (!getProject().isLinked(directThreat.getFactorId(), target.getFactorId()))
				return "";
			
			FactorLink factorLink = FactorLink.findFactorLink(getProject(), getLinkRef(directThreat.getRef(), target.getRef()));
			return Integer.toString(factorLink.calculateThreatRatingBundleValue());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "ERROR";
		}
	}
	
	public ORef getLinkRef(ORef fromRef, ORef toRef)
	{
		return getProject().getFactorLinkPool().getLinkedRef(fromRef, toRef);
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
		return targets[column].getLabel();
	}
	
	private Target[] targets;
}
