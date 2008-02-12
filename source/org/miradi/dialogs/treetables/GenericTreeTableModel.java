/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.treetables;

import javax.swing.tree.TreePath;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.ColumnTagProvider;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public abstract class GenericTreeTableModel extends AbstractTreeTableModel implements ColumnTagProvider
{
	
	public GenericTreeTableModel(Object root)
	{
		super(root);
	}

	public TreePath getPathToRoot()
	{
		return new TreePath(getRoot());
	}
	
	protected void rebuildNode()
	{
		try
		{
			getRootNode().rebuild();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	 //TODO remove this method and anything up the call chain that isn't being used
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
	
	TreeTableNode getRootNode()
	{
		return (TreeTableNode)getRoot();
	}

	public void rebuildEntireTree()
	{
		rebuildNode();
		fireTreeStructureChanged(getRoot(), new Object[] {getPathToRoot()}, null, null);
	}
	
	public void repaintObjectRow(ORef ref)
	{
		TreePath pathToRoot = getPathToRoot();
		TreePath pathToRepaint = findObject(pathToRoot, ref.getObjectType(), ref.getObjectId());
		if(pathToRepaint == null)
			return;
		
		TreeTableNode nodeToRepaint = (TreeTableNode)pathToRepaint.getLastPathComponent();
		TreePath pathToParent = pathToRepaint.getParentPath();
		TreeTableNode parentNode = (TreeTableNode)pathToParent.getLastPathComponent();
		int[] childIndex = new int[] {parentNode.getIndex(nodeToRepaint)};
		Object[] childObject = new Object[] {nodeToRepaint};
		fireTreeNodesChanged(nodeToRepaint, pathToParent.getPath(), childIndex, childObject);
	}
	
	public TreePath findObject(TreePath pathToStartSearch, ORef ref)
	{
		return findObject(pathToStartSearch, ref.getObjectType(), ref.getObjectId());
	}

	public TreePath findObject(TreePath pathToStartSearch, int objectType, BaseId objectId)
	{
		TreeTableNode nodeToSearch = (TreeTableNode)pathToStartSearch.getLastPathComponent();

		if(nodeToSearch.getType() == objectType)
		{
			if (nodeToSearch.getObjectReference()==null)
				return pathToStartSearch;
			if (nodeToSearch.getObjectReference().getObjectId().equals(objectId))
				return pathToStartSearch;
		}

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
	
	public Class getColumnClass(int column)
	{
		if(column == 0)
			return TreeTableModel.class;
		return String.class;
	}
	
	public Object getValueAt(Object rawNode, int column)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		return node.getValueAt(column);
	}
	
	public Object getChild(Object rawNode, int index)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		return node.getChild(index);
	}

	public int getChildCount(Object rawNode)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		return node.getChildCount();
	}
	
	public static final String DEFAULT_COLUMN = "Item";
}
