/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.objects.Factor;
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
		Factor directThreat = directThreatRows[row];
		Target target = targets[column];
		if (getProject().isLinked(directThreat.getFactorId(), target.getFactorId()))
			return "X";
		
		return "";
	}
	
//FIXME finish method	
//	public FactorLinkId getLink(int row, int column)
//	{
//		Factor directThreat = directThreatRows[row];
//		Target target = targets[column];
//		//factorLinkId = getProject().getFactorLinkPool().getLinkedId(directThreat.getFactorId(), target.getFactorId());
//	}
	
	
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
