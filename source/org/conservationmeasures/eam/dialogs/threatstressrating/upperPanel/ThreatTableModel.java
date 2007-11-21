/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

public class ThreatTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public ThreatTableModel(Project projectToUse)
	{
		project = projectToUse;
		directThreatRows =  getProject().getCausePool().getDirectThreats();
	}
	
	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int column)
	{
		return EAM.text("Threats");
	}
	
	public int getRowCount()
	{
		return directThreatRows.length;
	}

	public Object getValueAt(int row, int column)
	{
		return directThreatRows[row];
	}
	
	public Project getProject()
	{
		return project;
	}

	public String getColumnTag(int column)
	{
		return "";
	}

	private Project project;
	private Factor[] directThreatRows;
}
