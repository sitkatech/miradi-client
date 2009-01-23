/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.Task;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.Project;

abstract public class PlanningViewAbstractAssignmentTableModel extends EditableObjectTableModel
{
	public PlanningViewAbstractAssignmentTableModel(Project projectToUse)
	{
		super(projectToUse);
		assignmentRefs = new ORefList();
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
	}
	
	public int getRowCount()
	{
		return assignmentRefs.size();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		if(hierarchyToSelectedRef.length == 0)
			return;
		
		ORef selectedRef = hierarchyToSelectedRef[0];
		if (selectedRef.getObjectType() != Task.getObjectType())
			return;
		
		task = (Task) getProject().findObject(selectedRef);
		assignmentRefs = getAssignmentsForTask(task);
	}
			
	public void setTask(Task taskToUse)
	{
		if (isAlreadyCurrentTask(taskToUse))
			return;
			
		task = taskToUse;
		updateAssignmentIdList();	
	}
	
	public void dataWasChanged() throws Exception
	{
		if (isAlreadyCurrentAssignmentIdList())
			return;
		
		updateAssignmentIdList();
	}

	private boolean isAlreadyCurrentTask(Task taskToUse)
	{
		 if(task == null || taskToUse == null)
			 return false;
		 
		 return task.getId().equals(taskToUse.getId());
	}
	
	private boolean isAlreadyCurrentAssignmentIdList()
	{
		return assignmentRefs.equals(getAssignmentsForTask(task));
	}
	
	private void updateAssignmentIdList()
	{
		assignmentRefs = getAssignmentsForTask(task);
		fireTableDataChanged();
	}
		
	private ORefList getAssignmentsForTask(Task taskToUse)
	{
		if (taskToUse == null)
			return new ORefList();
		
		return taskToUse.getAssignmentRefs();
	}
	
	public ORef getAssignmentForRow(int row)
	{
		return assignmentRefs.get(row);
	}
	
	public Assignment getAssignment(int row)
	{
		return (Assignment) getProject().findObject(getAssignmentForRow(row));
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getAssignment(row);
	}
	
	//FIXME planning table - there should be methods that return the raw value,  then that value
	//can be used in budgetmodel to calculate the cost. (cost per unit and units need to return raw values)
	protected Object getResourceCostPerUnit(Assignment assignment)
	{
		ProjectResource resource = findProjectResource(assignment);
		if (resource == null)
			return "";
				
		double cost = resource.getCostPerUnit();
		return currencyFormatter.format(cost);
	}
	
	protected ProjectResource findProjectResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		ProjectResource resource = (ProjectResource) getProject().findObject(resourceRef);
		return resource;
	}
	
	protected ORefList assignmentRefs;
	protected Task task;

	protected CurrencyFormat currencyFormatter;
}
