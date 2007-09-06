/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class PlanningViewResourceTableModel extends AbstractTableModel
{
	public PlanningViewResourceTableModel(Project projectToUse)
	{
		project = projectToUse;
		assignmentRefs = new ORefList();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		ORef selectedRef = hierarchyToSelectedRef[0];
		if (selectedRef.getObjectType() != Task.getObjectType())
			return;
		
		Task task = (Task) project.findObject(selectedRef);
		assignmentRefs = getAssignmentsForTask(task);
	}
	
	private ORefList getAssignmentsForTask(Task taskToUse)
	{
		if (taskToUse == null)
			return new ORefList();
		
		return taskToUse.getAssignmentRefs();
	}
	
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	public int getRowCount()
	{
		return assignmentRefs.size();
	}

	public Object getValueAt(int row, int column)
	{
		//FIXME planning - remove code and make it work
		ORef assignmentRef = assignmentRefs.get(row);
		Assignment assignment = (Assignment) project.findObject(assignmentRef);
		return assignment;
	}
	
	Project project;
	private ORefList assignmentRefs;
	
	private static final int COLUMN_COUNT = 4;
}
