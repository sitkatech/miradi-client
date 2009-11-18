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

import java.util.Vector;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.RefListListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.TableSettings;
import org.miradi.utils.CodeList;
import org.miradi.utils.EAMTreeTableModelAdapter;

abstract public class TreeTableWithStateSaving extends ObjectTreeTable implements TreeExpansionListener, CommandExecutedListener
{
	public TreeTableWithStateSaving(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModel)
	{
		super(mainWindowToUse, treeTableModel);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(mainWindowToUse.getProject(), treeTableModel, tree);
		
		getProject().addCommandExecutedListener(this);
		tree.addTreeExpansionListener(this);
		tree.addTreeWillExpandListener(new TreeWillExpandHandler());
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
	
	private void addObjectToExpandedList(ORef ref) throws Exception
	{
		Vector<ORefList> expandedList = getExpandedNodeList();
		addToExpandedList(expandedList, ref);
		saveExpanded(expandedList);
	}

	private void addToExpandedList(Vector<ORefList> expandedList, ORef refToAdd) throws Exception
	{
		ORefList pathForRef = findHierarchyForRef(refToAdd);
		if(!expandedList.contains(pathForRef))
			expandedList.add(pathForRef);
	}
	
	private void removeFromExpandedList(Vector<ORefList> expandedList, ORef refToRemove) throws Exception
	{
		ORefList pathForRef = findHierarchyForRef(refToRemove);
		if(expandedList.contains(pathForRef))
			expandedList.remove(pathForRef);
	}
	
	public void restoreTreeState() throws Exception
	{
		if(ignoreNotifications)
			return;
		
		restoreTreeState(getExpandedNodeList());
	}

	private void restoreTreeState(Vector<ORefList> expandedNodeRefs) throws Exception
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
		Vector<ORefList> fullExpandedRefs = getTreeTableModel().getFullyExpandedHierarchyRefListList();
		saveExpanded(fullExpandedRefs);
	}
	
	public void collapseAll() throws Exception
	{
		clearSelection();
		saveExpanded(new Vector());
	}

	private ORefList findHierarchyForRef(ORef nodeRef) throws Exception
	{
		for (int row = 0; row < getRowCount(); ++row)
		{
			TreePath pathForRow = getTree().getPathForRow(row);
			ORef refForPath = getObjectRefFromPath(pathForRow);
			if (refForPath.equals(nodeRef))
				return getTreeTableModel().convertPath(pathForRow);
		}			
		
		return new ORefList();
	}
	
	private void saveExpanded(Vector<ORefList> expandedRowsHierarchies) throws Exception
	{
		FieldSaver.savePendingEdits();
		
		CodeList codeList = RefListListData.convertToCodeList(expandedRowsHierarchies);
		TableSettings tableSettings = getTableSettingsForTreeTable();
		CommandSetObjectData cmd = new CommandSetObjectData(tableSettings, TableSettings.TAG_TREE_EXPANSION_LIST, codeList.toString());
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
	
	private boolean recursiveChangeNodeExpansionState(Vector<ORefList> objRefListToUse, TreePath thisPath) throws Exception
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ORef topLevelObjRef = topLevelObject.getObjectReference();
		ORefList heirarchyForRef = findHierarchyForRef(topLevelObjRef);
		
		boolean isInExpandedList = objRefListToUse.contains(heirarchyForRef);
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

			Vector<ORefList> newExpansionRefs = getExpandedNodeList();
			if(tree.isExpanded(path))
				addToExpandedList(newExpansionRefs, ref);
			else
				removeFromExpandedList(newExpansionRefs, ref);

			saveExpanded(newExpansionRefs);
			selectObjectAfterSwingClearsItDueToTreeStructureChange(ref, fallbackRow);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error has occurred saving tree expansion state");
		}
	}

	private Vector<ORefList> getExpandedNodeList() throws Exception
	{
		return getTableSettingsForTreeTable().getExpandedRefListList();
	}
	
	private TableSettings getTableSettingsForTreeTable() throws Exception
	{
		return TableSettings.findOrCreate(getProject(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
	}
	
	class TreeWillExpandHandler implements TreeWillExpandListener
	{
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException
		{
			savePendingEdits();
		}

		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException
		{
			savePendingEdits();
		}
		
		private void savePendingEdits()
		{
			FieldSaver.savePendingEdits();
		}
	}
		
	protected EAMTreeTableModelAdapter treeTableModelAdapter;

	private boolean ignoreNotifications;

}
