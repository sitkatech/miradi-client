/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanningViewWorkPlanTableModel extends AbstractTableModel
{
	public PlanningViewWorkPlanTableModel(Project projectToUse) throws Exception
	{
		project = projectToUse;
		dateRanges = new ProjectCalendar(project).getQuarterlyDateDanges();
		assignmentRefs = new ORefList();
	}	
	
	public ORef getAssignmentRefForRow(int row)
	{
		return assignmentRefs.get(row);
	}
	
	public int getColumnCount()
	{
		return dateRanges.length;
	}

	public int getRowCount()
	{
		return assignmentRefs.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return "";
	}
	
	private Project project;
	private DateRange[] dateRanges;
	private ORefList assignmentRefs;
}
