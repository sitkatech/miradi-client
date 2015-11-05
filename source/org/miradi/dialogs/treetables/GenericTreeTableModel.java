/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.treetables;

import java.util.Vector;

import javax.swing.tree.TreePath;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.utils.ModelColumnTagProvider;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public abstract class GenericTreeTableModel extends AbstractTreeTableModel implements ModelColumnTagProvider
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

	protected TreeTableNode getRootNode()
	{
		return (TreeTableNode)getRoot();
	}

	public void rebuildEntireTree()
	{
		rebuildNode();
		reloadNodesWithoutRebuildingNodes();
	}

	public void reloadNodesWithoutRebuildingNodes()
	{
		fireTreeStructureChanged(getRoot(), new Object[] {getPathToRoot()}, null, null);
	}
	
	public TreePath findFirstMatchingTreePath(ORef ref)
	{
		Vector<TreePath> treePaths = findTreePaths(ref);
		if (treePaths.isEmpty())
			return null;
		
		return treePaths.get(0);
	}
	
	public Vector<TreePath> findTreePaths(ORef ref)
	{
		return findTreePaths(getPathToRoot(), ref);
	}
	
	private Vector<TreePath> findTreePaths(TreePath pathToStartSearch, ORef ref)
	{
		TreeTableNode nodeToSearch = (TreeTableNode)pathToStartSearch.getLastPathComponent();
		if(nodeToSearch.getType() == ref.getObjectType())
		{
			ORef nodeRef = nodeToSearch.getObjectReference();
			if (nodeRef == null)
			{
				return createSingleTreePathVector(pathToStartSearch);
			}
			if (nodeRef.equals(ref))
			{
				return createSingleTreePathVector(pathToStartSearch);
			}
		}

		Vector<TreePath> treePaths = new Vector<TreePath>();
		for(int index = 0; index < nodeToSearch.getChildCount(); ++index)
		{
			TreeTableNode thisChild = nodeToSearch.getChild(index);
			TreePath childPath = pathToStartSearch.pathByAddingChild(thisChild);
			Vector<TreePath> foundTreePaths = findTreePaths(childPath, ref);
			treePaths.addAll(foundTreePaths);
		}
		
		return treePaths;
	}

	private Vector<TreePath> createSingleTreePathVector(TreePath pathToStartSearch)
	{
		Vector<TreePath> treePaths = new Vector<TreePath>();
		treePaths.add(pathToStartSearch);
		
		return treePaths;
	}
	
	@Override
	public Class getColumnClass(int column)
	{
		if(column == PanelTreeTable.TREE_COLUMN_INDEX)
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
	
	public ORefList getFullyExpandedRefListExcludingLeafNodes() throws Exception
	{
		ORefList fullyExpandedObjectRefs = new ORefList();
		Vector<TreePath> fullExpandedNodeList = getFullyExpandedTreePathListExcludingLeafNodes();
		for(TreePath treePath : fullExpandedNodeList)
		{
			TreeTableNode node = (TreeTableNode) treePath.getLastPathComponent();
			ORef ref = node.getObjectReference();
			fullyExpandedObjectRefs.add(ref);
		}
		
		return fullyExpandedObjectRefs;
	}
	
	public Vector<ORefList> getFullyExpandedHierarchyRefListListExcludingLeafNodes() throws Exception
	{
		return getExpandedHierarchies(getFullyExpandedTreePathListExcludingLeafNodes());
	}
	
	public Vector<ORefList> getFullyExpandedHierarchyRefListListIncludingLeafNodes() throws Exception
	{
		return getExpandedHierarchies(getFullyExpandedTreePathListIncludingLeafNodes());
	}

	private Vector<ORefList> getExpandedHierarchies(Vector<TreePath> fullExpandedNodeList)
	{
		Vector<ORefList> fullyExpandedObjectRefs = new Vector<ORefList>();
		for(TreePath treePath : fullExpandedNodeList)
		{
			fullyExpandedObjectRefs.add(convertTreePathToRefList(treePath));
		}
		
		return fullyExpandedObjectRefs;
	}
	
	public ORefList convertTreePathToRefList(TreePath treePath)
	{
		ORefList selectionHierarchyNodeRefs = new ORefList();
		for(int i = treePath.getPathCount() - 1; i >=0 ; --i)
		{			
			TreeTableNode node = (TreeTableNode) treePath.getPathComponent(i);
			selectionHierarchyNodeRefs.add(node.getObjectReference());
		}
		
		return selectionHierarchyNodeRefs;	
	}
	
	public Vector<TreePath> getFullyExpandedTreePathListIncludingLeafNodes() throws Exception
	{
		return getExpandedTreePathList(new IncludingLeafNodesTreeExpander());
	}
	
	public Vector<TreePath> getFullyExpandedTreePathListExcludingLeafNodes() throws Exception
	{
		return getExpandedTreePathList(new ExcludingLeafNodesTreeExpander());
	}

	private Vector<TreePath> getExpandedTreePathList(final AbstractTreeExpander abstractTreeExpander) throws Exception
	{
		return abstractTreeExpander.getFullyExpandedTreePathList(getPathToRoot());
	}

	abstract public String getUniqueTreeTableModelIdentifier();

	public static final String DEFAULT_COLUMN = "Item";
	public static final int SINGLE_COLUMN_COUNT = 1;
}
