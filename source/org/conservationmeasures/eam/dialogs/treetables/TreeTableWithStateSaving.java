/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;

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
		ignoreNotifications = true;
		try
		{
			updateTreeExpansion(getExpandedNodeList());
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
			selectObject(ref);
			super.ensureObjectVisible(ref);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred making the new object visible");
		}
	}
	
	public void selectObject(ORef ref)
	{
		TreePath path = getTreeTableModel().getPathOfNode(ref.getObjectType(), ref.getObjectId());
		tree.setSelectionPath(path);
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

	protected EAMTreeTableModelAdapter treeTableModelAdapter;

	private boolean ignoreNotifications;

}
