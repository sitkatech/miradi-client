/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectCalendar;
import org.conservationmeasures.eam.utils.DateRange;

public class PlanningViewWorkPlanTableModel extends PlanningViewAbstractAssignmentTabelModel
{
	public PlanningViewWorkPlanTableModel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		dateRanges = new ProjectCalendar(getProject()).getQuarterlyDateDanges();
	}	
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	
	public String getColumnName(int col)
	{
		return dateRanges[col].toString();
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
		return "under dev";
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		
	}
	
	private DateRange[] dateRanges;
}
