/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

abstract public class AbstractDirectThreatTargetTableModel extends AbstractTableModel
{
	public AbstractDirectThreatTargetTableModel(Project projectToUse)
	{
		project = projectToUse;
		
		resetTargetAndThreats();
	}
	
	public void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns =  getProject().getTargetPool().getTargets();
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		Factor threat = getDirectThreats()[threatIndex];
		Factor target = getTargets()[targetIndex];
		return getProject().areLinked(threat, target);
	}
	
	protected Factor[] getTargets()
	{
		return targetColumns;
	}
	
	protected Factor[] getDirectThreats()
	{
		return threatRows;
	}

	public int getTargetCount()
	{
		return getTargets().length;
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}
	
	public FactorId getThreatId(int threatIndex)
	{
		Factor cmNode = getDirectThreats()[threatIndex];
		return cmNode.getFactorId();
	}

	public Factor getThreatNode(int threatIndex)
	{
		return getDirectThreats()[threatIndex];
	}

	public String getThreatName(int threatIndex)
	{
		return getDirectThreats()[threatIndex].getLabel();
	}
	
	public FactorId getTargetId(int targetIndex)
	{
		return getTargets()[targetIndex].getFactorId();
	}
	
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;	
}