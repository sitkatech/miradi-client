/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

abstract public class MainThreatTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public MainThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
		directThreatRows =  getProject().getCausePool().getDirectThreats();
	}
	
	public int getRowCount()
	{
		return directThreatRows.length;
	}

	public Project getProject()
	{
		return project;
	}

	private Project project;
	protected Factor[] directThreatRows;
}
