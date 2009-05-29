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

import java.awt.Color;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.questions.ColumnConfigurationQuestion;
import org.miradi.utils.OptionalDouble;

public class WorkUnitsTableModel extends AssignmentDateUnitsTableModel
{
	public WorkUnitsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse, providerToUse);
	}
	
	@Override
	public boolean isWorkUnitColumn(int column)
	{
		return true;
	}
	
	@Override
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getWorkUnitsBackgroundColor(dateUnit);
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return ColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE;
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return baseObject.getWorkUnits(dateUnit);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	@Override
	protected boolean isAssignmentForModel(Assignment assignment)
	{
		return ResourceAssignment.is(assignment);
	}

	@Override
	protected boolean hasValue(Assignment assignment, DateUnit dateUnit) throws Exception
	{
		return getOptionalDoubleData(assignment, dateUnit).hasValue();
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "WorkUnitsTableModel";
}
