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
package org.miradi.dialogs.treetables;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.SavableField;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ViewData;
import org.miradi.utils.EAMTreeTableModelAdapter;

abstract public class TreeTableWithStateSaving extends ObjectTreeTable implements TreeExpansionListener, CommandExecutedListener
{
	public TreeTableWithStateSaving(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModel)
	{
		super(mainWindowToUse, treeTableModel);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(mainWindowToUse.getProject(), treeTableModel, tree);
		
		getProject().addCommandExecutedListener(this);
		tree.addTreeExpansionListener(this);
	}
	
	public void dispose()
	{
		tree.removeTreeExpansionListener(this);
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (isRebuildTreeDueToSettingsChangeCommand(event))
				rebuildTableCompletely();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public static boolean isRebuildTreeDueToSettingsChangeCommand(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_SEQUENCE_CODES))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_WIDTHS))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_ROW_HEIGHT))
			return true;
			
		return false;
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
		addToExpandedList(expandedList, ref);
		saveExpandedPath(expandedList);
	}

	private void addToExpandedList(ORefList expandedList, ORef refToAdd)
	{
		if(!expandedList.contains(refToAdd))
			expandedList.add(refToAdd);
	}
	
	private void removeFromExpandedList(ORefList expandedList, ORef refToRemove)
	{
		if(expandedList.contains(refToRemove))
			expandedList.remove(refToRemove);
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
			int fallbackRow = getSelectedRow();
			TreePath selectedPath = tree.getSelectionPath();
			ORef selectedRef = getObjectRefFromPath(selectedPath);
			
			TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
			TreePath rootPath = new TreePath(root);
			if(recursiveChangeNodeExpansionState(expandedNodeRefs, rootPath))
			{
				treeTableModelAdapter.fireTableDataChanged();
				selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef, fallbackRow);
			}
		}
		finally
		{
			ignoreNotifications = false;
			updateAutomaticRowHeights();
		}
	}

	private ORef getObjectRefFromPath(TreePath path)
	{
		if(path == null)
			return ORef.INVALID;
		
		TreeTableNode node = (TreeTableNode)path.getLastPathComponent();
		if(node == null)
			return ORef.INVALID;
		
		return node.getObjectReference();
	}
	
	public void expandAll() throws Exception
	{
		ORefSet fullExpandedRefs = getTreeTableModel().getFullyExpandedRefSet();
		updateTreeExpansionState(fullExpandedRefs.toRefList());
	}
	
	public void collapseAll(ViewData viewData) throws Exception
	{
		clearSelection();
		updateTreeExpansionState(new ORefList());
	}

	public void updateTreeExpansionState(ORefList expandedRefs) throws Exception
	{
		TableSettings tableSettings = getTableSettingsForTreeTable();
		CommandSetObjectData cmd = new CommandSetObjectData(tableSettings, TableSettings.TAG_TREE_EXPANSION_LIST, expandedRefs);
		getProject().executeCommand(cmd);
	}
	
	public void ensureObjectVisible(ORef ref)
	{
		try
		{
			addObjectToExpandedList(ref);
			super.ensureObjectVisible(ref);
			selectObjectAfterSwingClearsItDueToTreeStructureChange(ref, 0);
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred making the new object visible");
		}
	}
	
	private boolean recursiveChangeNodeExpansionState(ORefList objRefListToUse, TreePath thisPath)
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ORef topLevelObjRef = topLevelObject.getObjectReference();
		
		boolean isInExpandedList = objRefListToUse.contains(topLevelObjRef);
		boolean isAlwaysExpanded = topLevelObject.isAlwaysExpanded();
		boolean shouldBeExpanded = isAlwaysExpanded || isInExpandedList;
		
		boolean needsToChange = (tree.isExpanded(thisPath) != shouldBeExpanded);
		
		if(!shouldBeExpanded)
		{
			if(needsToChange)
				tree.collapsePath(thisPath);
		}
		else
		{
			if(needsToChange)
				tree.expandPath(thisPath);
		
			for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
			{
				TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
				TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
				recursiveChangeNodeExpansionState(objRefListToUse, secondLevelPath);
			}
		}
		
		return needsToChange;
	}

	public void treeCollapsed(TreeExpansionEvent event)
	{
		swingTreeExpansionWasChanged(event.getPath());
	}

	public void treeExpanded(TreeExpansionEvent event)
	{
		swingTreeExpansionWasChanged(event.getPath());
	}
	
	private void swingTreeExpansionWasChanged(TreePath path)
	{
		if(ignoreNotifications)
			return;
	
		try
		{
			ORef ref = getObjectRefFromPath(path);
			int fallbackRow = tree.getRowForPath(path);

			ORefList newExpansionRefs = getExpandedNodeList();
			if(tree.isExpanded(path))
				addToExpandedList(newExpansionRefs, ref);
			else
				removeFromExpandedList(newExpansionRefs, ref);

			saveExpandedPath(newExpansionRefs);
			selectObjectAfterSwingClearsItDueToTreeStructureChange(ref, fallbackRow);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred saving tree expansion state");
		}
	}

	private void saveExpandedPath(ORefList newObjRefList) throws Exception
	{
		SavableField.saveFocusedFieldPendingEdits();

		TableSettings tableSettings = getTableSettingsForTreeTable();
		CommandSetObjectData cmd = new CommandSetObjectData(tableSettings, TableSettings.TAG_TREE_EXPANSION_LIST, newObjRefList);
		getProject().executeCommand(cmd);
	}

	private ORefList getExpandedNodeList() throws Exception
	{
		TableSettings tableSettings = getTableSettingsForTreeTable();	
		return new ORefList(tableSettings.getData(TableSettings.TAG_TREE_EXPANSION_LIST));
	}
	
	private TableSettings getTableSettingsForTreeTable() throws Exception
	{
		return TableSettings.findOrCreate(getProject(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
	}
		
	protected EAMTreeTableModelAdapter treeTableModelAdapter;

	private boolean ignoreNotifications;

}
