/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

abstract public class MainThreatTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
		directThreatRows =  getProject().getCausePool().getDirectThreats();
		targets = getProject().getTargetPool().getTargets();
	}
	
	public Factor getDirectThreat(int row)
	{
		return directThreatRows[row];
	}
	
	public int getRowCount()
	{
		return directThreatRows.length;
	}

	public Project getProject()
	{
		return project;
	}

	public ORef getLinkRef(ORef fromRef, ORef toRef)
	{
		return getProject().getFactorLinkPool().getLinkedRef(fromRef, toRef);
	}
	
	protected String convertIntToString(int calculatedValue)
	{
		if (calculatedValue == 0)
			return "";
		
		return Integer.toString(calculatedValue);
	}
	
	private Project project;
	protected Factor[] directThreatRows;
	protected Target[] targets;
}
