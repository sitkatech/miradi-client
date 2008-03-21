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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.EAMTreeTableModelAdapter;

public class TreeTableWithStateSaving extends TreeTableWithIcons implements TreeExpansionListener
{
	public TreeTableWithStateSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(projectToUse, treeTableModel, tree);
		
		tree.addTreeExpansionListener(this);
	}
	
	public void dispose()
	{
		tree.removeTreeExpansionListener(this);
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
		restoreTreeState(getExpandedNodeList());
	}

	public void restoreTreeState(ORefList expandedNodeRefs) throws Exception
	{
		ignoreNotifications = true;
		try
		{
			updateTreeExpansion(expandedNodeRefs);
		}
		finally
		{
			ignoreNotifications = false;
		}
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
	
	void updateTreeExpansion(ORefList expandedList)
	{
		ignoreNotifications = true;
		try
		{
			TreePath selectedPath = tree.getSelectionPath();
			TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
			TreePath rootPath = new TreePath(root);
			recursiveChangeNodeExpansionState(expandedList, rootPath);
			treeTableModelAdapter.fireTableDataChanged();
			tree.addSelectionPath(selectedPath);
		}
		finally
		{
			ignoreNotifications = false;
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
	
	public ORefList getFullyExpandedRefList() throws Exception
	{
		TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
		ORefList fullyExpandedRefList = new ORefList();
		recursivelyGetFullyExpansedRefs(fullyExpandedRefList, root);
		treeTableModelAdapter.fireTableDataChanged();
		
		return fullyExpandedRefList;
	}
	
	private void recursivelyGetFullyExpansedRefs(ORefList objRefListToUse, TreeTableNode node)
	{
		objRefListToUse.add(node.getObjectReference());
		for(int childIndex = 0; childIndex < node.getChildCount(); ++childIndex)
		{
			TreeTableNode childNode = node.getChild(childIndex);
			recursivelyGetFullyExpansedRefs(objRefListToUse, childNode);
		}
	}
	
	protected EAMTreeTableModelAdapter treeTableModelAdapter;

	private boolean ignoreNotifications;

}
