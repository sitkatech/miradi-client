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

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperTableModelInterface;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

abstract public class PlanningViewAbstractTreeTableSyncedTableModel extends EditableObjectTableModel implements PlanningUpperTableModelInterface, ChoiceItemTableModel
{
	public PlanningViewAbstractTreeTableSyncedTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse) throws Exception
	{
		super(projectToUse);
		
		project = projectToUse;
		objectProvider = providerToUse;
	}
	
	public int getRowCount()
	{
		return objectProvider.getRowCount();
	}
	
	public String getColumnTag(int column)
	{
		return getColumnName(column);
	}
		
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return objectProvider.getBaseObjectForRowColumn(row, column);
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		return getBaseObjectForRowColumn(row, 0);
	}
	
	@Override
	public int getProportionShares(int row)
	{
		return objectProvider.getProportionShares(row);
	}

	@Override
	public boolean areBudgetValuesAllocated(int row)
	{
		return objectProvider.areBudgetValuesAllocated(row);
	}
	
	public boolean isCurrencyColumn(int column)
	{
		return false;
	}
	
	public boolean isChoiceItemColumn(int column)
	{
		return false;
	}
	
	public boolean isProgressColumn(int column)
	{
		return false;
	}
	
	public boolean isWorkUnitColumn(int column)
	{
		return false;
	}
	
	public String getColumnGroupCode(int modelColumn)
	{
		return getColumnName(modelColumn);
	}
	
	protected Project project;
	private RowColumnBaseObjectProvider objectProvider;
}
