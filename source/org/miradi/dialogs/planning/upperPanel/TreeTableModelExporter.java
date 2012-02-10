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
package org.miradi.dialogs.planning.upperPanel;

import java.util.Vector;

import javax.swing.tree.TreePath;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.AbstractTreeTableOrModelExporter;

public class TreeTableModelExporter extends AbstractTreeTableOrModelExporter
{
	public TreeTableModelExporter(Project projectToUse, GenericTreeTableModel modelToUse) throws Exception
	{
		super(projectToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		
		model = modelToUse;
		fullyExpandedTreePaths = model.getFullyExpandedTreePathList();
		removeRootNode();
	}

	private void removeRootNode()
	{
		fullyExpandedTreePaths.remove(0);
	}
	
	public TreeTableNode getTreeTableNodeForRow(int row)
	{
		TreeTableNode node = (TreeTableNode) fullyExpandedTreePaths.get(row).getLastPathComponent();
		return node;
	}
	
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		TreeTableNode node = getTreeTableNodeForRow(row);
		ORef rowObjectRef = node.getObjectReference();
		if (rowObjectRef.isInvalid())
			return null;
		
		return getProject().findObject(rowObjectRef);
	}

	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		if (isTreeColumn(modelColumn))
		{
			TreePath treePath = fullyExpandedTreePaths.get(row);
			return  treePath.getPath().length - TOPLEVEL_ADJUSTMENT;
		}
		
		return 0;
	}

	@Override
	public String getModelColumnName(int modelColumn)
	{
		return getModel().getColumnName(modelColumn);
	}

	@Override
	public int getRowCount()
	{
		return fullyExpandedTreePaths.size();
	}

	@Override
	public int getColumnCount()
	{
		return model.getColumnCount();
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		return getModelColumnName(modelColumn);
	}

	@Override
	public int getRowType(int row)
	{
		TreeTableNode node = getTreeTableNodeForRow(row);
		return node.getType();
	}
	
	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		TreeTableNode node = getTreeTableNodeForRow(row);
		if (isTreeColumn(modelColumn))
			return node.toRawString();
		
		Object value = getModel().getValueAt(node, modelColumn);
		return getSafeValue(value);
	}
	
	@Override
	public GenericTreeTableModel getModel()
	{
		return model;
	}
	
	private GenericTreeTableModel model;
	private Vector<TreePath> fullyExpandedTreePaths;
}
