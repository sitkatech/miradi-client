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
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

abstract public class AbstractWorkUnitsTableModel extends AssignmentDateUnitsTableModel
{
	public AbstractWorkUnitsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse,	String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}

	@Override
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getWorkUnitsBackgroundColor(dateUnit);
	}
	
	@Override
	protected String getAssignmentsTag()
	{
		 return BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;
	}
	
	@Override
	protected int getAssignmentType()
	{
		return ResourceAssignment.getObjectType();
	}

	//FIXME urgent - Is this overring method needed.  Does it make sense to rename this method
	// to isDateUnitColumn or isAssignment column, somethign more generic.
	@Override
	public boolean isDateUnitColumn(int column)
	{
		return true;
	}

	@Override
	protected OptionalDouble calculateValue(TimePeriodCosts timePeriodCosts)
	{
		return timePeriodCosts.getTotalWorkUnits();
	}
	
	@Override
	protected boolean isAssignmentForModel(Assignment assignment)
	{
		return ResourceAssignment.is(assignment);
	}
	
	@Override
	public boolean isFullTimeEmployeeFractionAvailable(int row, int modelColumn)
	{
		if (!isCellEditable(row, modelColumn))
			return false;
		
		DateUnit dateUnit = getDateUnit(modelColumn);
		if (dateUnit.isYear())
			return true;
		if (dateUnit.isQuarter())
			return true;
		if (dateUnit.isMonth())
			return true;
			
		return false;
	}
}
