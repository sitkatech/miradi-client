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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;

public class PlanningViewFutureStatusTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewFutureStatusTableModel(Project projectToUse, RowColumnBaseObjectProvider adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(Indicator.getObjectType(), columnTags[column]);
	}
	
	public Object getValueAt(int row, int column)
	{
		BaseObject objectForRow = getBaseObjectForRowColumn(row, column);
		if (!Indicator.is(objectForRow))
			return "";
		
		return objectForRow.getData(columnTags[column]);
	}
	
	public final static String[] columnTags = {Indicator.TAG_FUTURE_STATUS_DATE, Indicator.TAG_FUTURE_STATUS_SUMMARY};
}
