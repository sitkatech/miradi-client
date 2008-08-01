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

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.AbstractTableExporter;

public class TreeTableModelExporter extends AbstractTableExporter
{
	public TreeTableModelExporter(Project projectToUse, GenericTreeTableModel modelToUse) throws Exception
	{
		project = projectToUse;
		model = modelToUse;
		rowObjectRefs = model.getFullyExpandedRefList();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getBaseObjectForRow(row);
	}

	public BaseObject getBaseObjectForRow(int row)
	{
		ORef rowObjectRef = rowObjectRefs.get(row);
		if (rowObjectRef.isInvalid())
			return null;
		return getProject().findObject(rowObjectRef);
	}

	public int getDepth(int row)
	{
		return 0;
	}

	public String getHeaderFor(int column)
	{
		return getModel().getColumnName(column);
	}

	public int getMaxDepthCount()
	{
		return 0;
	}

	public int getRowCount()
	{
		return rowObjectRefs.size();
	}

	@Override
	public int getColumnCount()
	{
		return 0;
	}

	@Override
	public String getIconAt(int row, int column)
	{
		return null;
	}

	@Override
	public int getRowType(int row)
	{
		return 0;
	}

	@Override
	public String getTextAt(int row, int column)
	{
		return getModel().getValueAt(getBaseObjectForRow(row), column).toString();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private GenericTreeTableModel getModel()
	{
		return model;
	}
	
	private GenericTreeTableModel model;
	private ORefList rowObjectRefs;
	private Project project;

}
