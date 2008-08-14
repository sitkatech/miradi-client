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
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.AbstractTreeTableOrModelExporter;

public class TreeTableModelExporter extends AbstractTreeTableOrModelExporter
{
	public TreeTableModelExporter(Project projectToUse, GenericTreeTableModel modelToUse) throws Exception
	{
		project = projectToUse;
		model = modelToUse;
		rowObjectRefs = model.getFullyExpandedRefList();
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
		BaseObject objectForRow = getBaseObjectForRow(row);
		if (objectForRow == null)
			return 0;
		
		return  getModel().getPathOfNode(objectForRow.getRef()).getPath().length - ROOT_PLUS_TOPLEVEL_ADJUSTMENT;
	}

	public String getHeaderFor(int column)
	{
		return getModel().getColumnName(column);
	}

	public int getRowCount()
	{
		return rowObjectRefs.size();
	}

	@Override
	public int getColumnCount()
	{
		return model.getColumnCount();
	}

	@Override
	public int getRowType(int row)
	{
		BaseObject baseObjectForRow = getBaseObjectForRow(row);
		if (baseObjectForRow == null)
			return ObjectType.FAKE;
		
		TreeTableNode node = (TreeTableNode) getModel().getPathOfNode(baseObjectForRow.getType(), baseObjectForRow.getId()).getLastPathComponent();
		return node.getType();
	}

	@Override
	public String getTextAt(int row, int column)
	{
		BaseObject baseObjectForRow = getBaseObjectForRow(row);
		if (baseObjectForRow == null)
			return "";
		
		TreeTableNode node = (TreeTableNode) getModel().getPathOfNode(baseObjectForRow.getType(), baseObjectForRow.getId()).getLastPathComponent();
		Object value = getModel().getValueAt(node, column);
		return getSafeValue(value);
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
