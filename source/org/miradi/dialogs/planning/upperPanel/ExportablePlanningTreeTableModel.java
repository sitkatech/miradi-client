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

import javax.swing.tree.TreePath;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;

public class ExportablePlanningTreeTableModel extends PlanningTreeTableModel implements RowColumnBaseObjectProvider
{
	public ExportablePlanningTreeTableModel(Project projectToUse, TreeTableNode rootNode, PlanningTreeRowColumnProvider rowColumnProvider, String uniqueTreeTableModelIdentifierToUse) throws Exception
	{
		super(projectToUse, rootNode, rowColumnProvider);
		
		uniqueTreeTableModelIdentifier = uniqueTreeTableModelIdentifierToUse;
		setRowObjectRefs();
	}
	
	private void setRowObjectRefs() throws Exception
	{
		rowObjectRefs = getFullyExpandedRefList();
		removeRootNode();
	}
	
	private void removeRootNode()
	{
		rowObjectRefs.remove(0);
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
	
	public boolean areBudgetValuesAllocated(int row)
	{
		TreeTableNode node = getNodeForRow(row);
		if(node == null)
			return false;
		return node.areBudgetValuesAllocated();
	}

	private TreeTableNode getNodeForRow(int row)
	{
		ORef rowObjectRef = rowObjectRefs.get(row);
		if (rowObjectRef.isInvalid())
			return null;
		
		try
		{
			TreePath path = getFullyExpandedTreePathList().get(row);
			
			return (TreeTableNode) path.getLastPathComponent();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getUniqueTreeTableModelIdentifier()
	{
		return uniqueTreeTableModelIdentifier;
	}
	
	public ORefList getObjectHiearchy(int row, int column)
	{
		throw new RuntimeException("Method is currently unused and has no implementation");
	}
	
	private String uniqueTreeTableModelIdentifier;

	private ORefList rowObjectRefs;
}
