/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.miradi.ids.FactorId;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;

abstract public class AbstractThreatTargetTableModel extends AbstractTableModel
{
	public AbstractThreatTargetTableModel(Project projectToUse)
	{
		project = projectToUse;
		
		resetTargetAndThreats();
	}
	
	public void resetTargetAndThreats()
	{
		threatRows =  getProject().getCausePool().getDirectThreats();
		targetColumns =  getProject().getTargetPool().getTargets();
	}
	
	protected boolean isPopupSupportableCell(int row, int modelColumn)
	{
		return true;
	}
    	
	public Project getProject()
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
	
	public Factor getDirectThreat(int row)
	{
		return threatRows[row];
	}
	
	public Target getTarget(int modelColumn)
	{
		return targetColumns[modelColumn];
	}

	public int getTargetCount()
	{
		return getTargets().length;
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}
	
	public String getThreatName(int threatIndex)
	{
		return getDirectThreats()[threatIndex].getLabel();
	}
	
	public String getTargetName(int targetIndex)
	{
		return getTargets()[targetIndex].getLabel();
	}

	public FactorId getThreatId(int threatIndex)
	{
		return getDirectThreats()[threatIndex].getFactorId();
	}

	public FactorId getTargetId(int targetIndex)
	{
		return getTargets()[targetIndex].getFactorId();
	}
	
	private Project project;
	protected Factor[] threatRows;
	protected Target[] targetColumns;	
}