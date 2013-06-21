/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import java.text.ParseException;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;

public class WorkPlanExpenseAmountsTableModel extends AbstractExpenseTableModel
{
	public WorkPlanExpenseAmountsTableModel(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProvider, RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, rowColumnProvider, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return getTreeModelIdentifierAsTag() + "." + UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	@Override
	protected boolean isEditableModel()
	{
		return true;
	}
	
	@Override
	protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef) throws ParseException
	{
		return CommandSetObjectData.createAppendORefCommand(baseObjectForRowColumn, getAssignmentsTag(), assignmentRef);
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "ExpenseAmountsTableModel";
}
