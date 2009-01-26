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

import javax.swing.tree.TreePath;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class ExportablePlanningTreeTableModel extends PlanningTreeTableModel implements RowColumnBaseObjectProvider
{
	public ExportablePlanningTreeTableModel(Project projectToUse, CodeList visibleRowCodesToUse, CodeList visibleColumnCodesToUse) throws Exception
	{
		super(projectToUse, visibleRowCodesToUse, visibleColumnCodesToUse);
		rowObjectRefs = getFullyExpandedRefList();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		ORef rowObjectRef = rowObjectRefs.get(row);
		if (rowObjectRef.isInvalid())
			return null;
		return getProject().findObject(rowObjectRef);
	}

	public int getRowCount()
	{
		return rowObjectRefs.size();
	}
	
	public int getProportionShares(int row)
	{
		TreeTableNode node = getNodeForRow(row);
		if(node == null)
			return 1;
		return node.getProportionShares();
	}

	private TreeTableNode getNodeForRow(int row)
	{
		ORef rowObjectRef = rowObjectRefs.get(row);
		if (rowObjectRef.isInvalid())
			return null;
		TreePath path = findObject(new TreePath(getRootNode()), rowObjectRef);
		TreeTableNode node = (TreeTableNode) path.getLastPathComponent();
		return node;
	}
	
	private ORefList rowObjectRefs;
}
