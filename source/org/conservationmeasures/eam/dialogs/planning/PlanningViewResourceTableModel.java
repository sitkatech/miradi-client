/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ProjectResource;
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
		if(hierarchyToSelectedRef.length == 0)
			return;
		
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
	
	private ORef getAssignmentForRow(int row)
	{
		return assignmentRefs.get(row);
	}
		
	public boolean isCellEditable(int row, int column)
	{
		return ! isResourceCostColumn(column);
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
		return getCellValue(row, column);
	}
	
	private Object getCellValue(int row, int column)
	{
		ORef assignmentRef = getAssignmentForRow(row);
		Assignment assignment = (Assignment) project.findObject(assignmentRef);
		if (isResourceColumn(column))
			return getResource(assignment);
		
		if (isFundingSourceColumn(column))
			return getFundingSource(assignment);
		
		if (isAccountingCodeColumn(column))
			return getAccountingCode(assignment);
		
		if (isResourceCostColumn(column))
			return getResourceCost(assignment);
		
		return null;
	}

	public void setValueAt(Object value, int row, int column)
	{
		ORef assignmentRefForRow = getAssignmentForRow(row);
		setResourceCell(value, assignmentRefForRow, column);
	}
	
	private void setResourceCell(Object value, ORef assignmentRefForRow, int column)
	{
		if (! isResourceColumn(column))
			return;

		ProjectResource projectResource = (ProjectResource)value;
		setResource(projectResource, assignmentRefForRow);
	}

	public void setResource(ProjectResource projectResource, ORef assignmentRef)
	{
		try
		{
			BaseId resourceId = projectResource.getId();
			Command command = new CommandSetObjectData(assignmentRef, Assignment.TAG_ASSIGNMENT_RESOURCE_ID, resourceId.toString());
			project.executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
	
	private BaseObject getResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		return getObjectLabel(resourceRef);
	}
	
	private BaseObject getFundingSource(Assignment assignment)
	{
		ORef fundingSourceRef = assignment.getFundingSourceRef();
		return getObjectLabel(fundingSourceRef);
	}
	
	private BaseObject getAccountingCode(Assignment assignment)
	{
		ORef accountingCodeRef = assignment.getAccountingCodeRef();
		return getObjectLabel(accountingCodeRef);
	}
	
	private String getResourceCost(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		ProjectResource resource = (ProjectResource) project.findObject(resourceRef);
		double cost = resource.getCostPerUnit();
		return Double.toString(cost);
	}

	private BaseObject getObjectLabel(ORef ref)
	{
		return project.findObject(ref);
	}

	public boolean isResourceColumn(int column)
	{
		return RESOURCE_COLUMM == column;
	}
	
	public boolean isFundingSourceColumn(int column)
	{
		return FUNDING_SOURCE_COLUMN == column;
	}
	
	public boolean isAccountingCodeColumn(int column)
	{
		return ACCOUNTING_CODE_COLUMN == column;
	}
	
	public boolean isResourceCostColumn(int column)
	{
		return RESOURCE_COST_COLUMN == column;
	}
	
	public Project getProject()
	{
		return project;
	}	
	
	private Project project;
	private ORefList assignmentRefs;
	
	private static final int COLUMN_COUNT = 4;
	
	private static final int RESOURCE_COLUMM = 0;
	private static final int ACCOUNTING_CODE_COLUMN = 1;
	private static final int FUNDING_SOURCE_COLUMN = 2;
	private static final int RESOURCE_COST_COLUMN = 3;
}
