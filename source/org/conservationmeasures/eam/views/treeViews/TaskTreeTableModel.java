/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.treeViews;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TaskTreeTableModel extends GenericTreeTableModel
{
	public TaskTreeTableModel(Object root)
	{
		super(root);
	}

	public int getColumnCount()
	{
		return 0;
	}

	public String getColumnName(int column)
	{
		return null;
	}

	public TreePath getPathOfParent(int objectType, BaseId objectId)
	{
		TreePath path = getPathOfNode(objectType, objectId);
		if(path == null)
			return null;
		return path.getParentPath();
	}

	public TreePath getPathOfNode(int objectType, BaseId objectId)
	{
		return findObject(getPathToRoot(), objectType, objectId);
	}

	public TreePath getPathToRoot()
	{
		return new TreePath(getRootWorkPlanObject());
	}

	TreeTableNode getRootWorkPlanObject()
	{
		return (TreeTableNode)getRoot();
	}

	public void objectiveWasModified()
	{
EAM.logDebug("objectiveWasModified");
		rebuildNode();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);
	}

	public void dataWasChanged(int objectType, BaseId objectId)
	{
		rebuildNode();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);

EAM.logDebug("dataWasChanged");
	}

	private void rebuildNode()
	{
		try
		{
			getRootStratPlanObject().rebuild();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	TreeTableNode getRootStratPlanObject()
	{
		return (TreeTableNode)getRoot();
	}

	public void idListWasChanged(int objectType, BaseId objectId, String newIdListAsString)
	{
		rebuildNode();
		fireTreeStructureChanged(getRoot(), new Object[] {getRoot()}, null, null);

EAM.logDebug("idListWasChanged");
	}

	public TreePath findObject(TreePath pathToStartSearch, int objectType, BaseId objectId)
	{
		TreeTableNode nodeToSearch = (TreeTableNode)pathToStartSearch.getLastPathComponent();
		if(nodeToSearch.getType() == objectType && nodeToSearch.getObjectReference().getObjectId().equals(objectId))
			return pathToStartSearch;

		for(int i = 0; i < nodeToSearch.getChildCount(); ++i)
		{
			TreeTableNode thisChild = nodeToSearch.getChild(i);
			TreePath childPath = pathToStartSearch.pathByAddingChild(thisChild);
			TreePath found = findObject(childPath, objectType, objectId);
			if(found != null)
				return found;
		}
		return null;
	}

}
