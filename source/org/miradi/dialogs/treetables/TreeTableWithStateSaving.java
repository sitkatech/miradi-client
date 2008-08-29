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
package org.miradi.dialogs.treetables;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ViewData;
import org.miradi.utils.EAMTreeTableModelAdapter;

abstract public class TreeTableWithStateSaving extends ObjectTreeTable implements TreeExpansionListener
{
	public TreeTableWithStateSaving(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModel)
	{
		super(mainWindowToUse, treeTableModel);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(mainWindowToUse.getProject(), treeTableModel, tree);
		
		tree.addTreeExpansionListener(this);
	}
	
	public void dispose()
	{
		tree.removeTreeExpansionListener(this);
	}
	
	@Override
	public void updateAutomaticRowHeights()
	{
		if(ignoreNotifications)
			return;
		
		super.updateAutomaticRowHeights();
	}
	
	public void addObjectToExpandedList(ORef ref) throws Exception
	{
		ORefList expandedList = getExpandedNodeList();
		if(expandedList.contains(ref))
			return;
		
		expandedList.add(ref);
		saveExpandedPath(expandedList);
	}
	
	public void restoreTreeState() throws Exception
	{
		if(ignoreNotifications)
			return;
		
		restoreTreeState(getExpandedNodeList());
	}

	public void restoreTreeState(ORefList expandedNodeRefs) throws Exception
	{
		ignoreNotifications = true;
		try
		{
			TreePath selectedPath = tree.getSelectionPath();
			TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
			TreePath rootPath = new TreePath(root);
			recursiveChangeNodeExpansionState(expandedNodeRefs, rootPath);
			treeTableModelAdapter.fireTableDataChanged();
			tree.addSelectionPath(selectedPath);
		}
		finally
		{
			ignoreNotifications = false;
		}
	}
	
	public void expandAll(ViewData viewData) throws Exception
	{
		ORefList fullExpandedRefs = getTreeTableModel().getFullyExpandedRefList();
		updateTreeExpansionState(viewData, fullExpandedRefs);
	}
	
	public void collapseAll(ViewData viewData) throws Exception
	{
		clearSelection();
		updateTreeExpansionState(viewData, new ORefList());
	}

	public void updateTreeExpansionState(ViewData viewData, ORefList expandedRefs) throws Exception
	{
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getRef() ,ViewData.TAG_CURRENT_EXPANSION_LIST, expandedRefs.toString());
		getProject().executeCommand(cmd);
	}
	
	public void ensureObjectVisible(ORef ref)
	{
		try
		{
			addObjectToExpandedList(ref);
			selectObjectAfterSwingClearsItDueToTreeStructureChange(ref, 0);
			
			// TODO: This isn't working...probably also needs to be run in a thread like select
			super.ensureObjectVisible(ref);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred making the new object visible");
		}
	}
	
	private void recursiveChangeNodeExpansionState(ORefList objRefListToUse, TreePath thisPath)
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ORef topLevelObjRef = topLevelObject.getObjectReference();
		
		boolean isInExpandedList = objRefListToUse.contains(topLevelObjRef);
		boolean isAlwaysExpanded = topLevelObject.isAlwaysExpanded();
		boolean isExpanded = isAlwaysExpanded || isInExpandedList;
		if(! isExpanded)
		{
			tree.collapsePath(thisPath);
			return;
		}
		
		tree.expandPath(thisPath);
		
		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
			recursiveChangeNodeExpansionState(objRefListToUse, secondLevelPath);
		}
	}

	public void treeCollapsed(TreeExpansionEvent event)
	{
		swingTreeExpansionWasChanged();
	}

	public void treeExpanded(TreeExpansionEvent event)
	{
		swingTreeExpansionWasChanged();
	}
	
	private void swingTreeExpansionWasChanged()
	{
		if(ignoreNotifications)
			return;
	
		try
		{
			int rowCount = tree.getRowCount();
			ORefList newExpansionRefs = getExpandedNodeList();
			for (int i = 0; i < rowCount; i ++)
			{
				TreePath treePath = tree.getPathForRow(i);
				if (tree.isExpanded(treePath))
				{
					TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
					ORef nodeRef = node.getObjectReference();
					if (nodeRef != null && !newExpansionRefs.contains(nodeRef))
						newExpansionRefs.add(nodeRef);
				}
				
				if (tree.isCollapsed(treePath))
				{
					TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
					ORef nodeRef = node.getObjectReference();
					if (newExpansionRefs.contains(nodeRef) && isParentNode(node))
						newExpansionRefs.remove(nodeRef);
				}
			}
			saveExpandedPath(newExpansionRefs);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred saving tree expansion state");
		}
	}

	private boolean isParentNode(TreeTableNode node)
	{
		return node.getChildCount() != 0;
	}

	private void saveExpandedPath(ORefList newObjRefList) throws Exception
	{
		ObjectDataInputField.saveFocusedFieldPendingEdits();
		ViewData viewData = project.getViewData(project.getCurrentView());		
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, newObjRefList.toString());
		project.executeCommand(cmd);
	}
	
	private ORefList getExpandedNodeList() throws Exception
	{
		ViewData viewData = project.getViewData(project.getCurrentView());
		ORefList objRefList= new ORefList(viewData.getData(ViewData.TAG_CURRENT_EXPANSION_LIST));
		return objRefList;
	}
	
	protected EAMTreeTableModelAdapter treeTableModelAdapter;

	private boolean ignoreNotifications;

}
