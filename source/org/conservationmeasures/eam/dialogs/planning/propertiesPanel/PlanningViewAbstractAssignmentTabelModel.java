/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.text.DecimalFormat;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

abstract public class PlanningViewAbstractAssignmentTabelModel extends EditableObjectTableModel
{
	public PlanningViewAbstractAssignmentTabelModel(Project projectToUse)
	{
		super(projectToUse);
		assignmentRefs = new ORefList();
		currencyFormatter = getProject().getCurrencyFormatter();
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
	
	public void dataWasChanged()
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

	protected DecimalFormat currencyFormatter;
}
