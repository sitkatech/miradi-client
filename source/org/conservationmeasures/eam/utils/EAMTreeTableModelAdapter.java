/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.TreeTableModel;

public class EAMTreeTableModelAdapter extends AbstractTableModel //implements CommandExecutedListener
{
	public EAMTreeTableModelAdapter(Project projectToUse, TreeTableStateSaver treeTableStateSaverToUse, TreeTableModel treeTableModelToUse, JTree treeToUse)
	{
		tree = treeToUse;
		treeTableModel = treeTableModelToUse;
		project = projectToUse;
		treeTableStateSaver = treeTableStateSaverToUse; 
		//project.addCommandExecutedListener(this);
		setExpansionListeners(tree);
		setModelListeners(treeTableModel);
	}

	private void setModelListeners(TreeTableModel treeTableModel)
	{
		treeTableModel.addTreeModelListener(new TreeModelHandler());
	}

	private void setExpansionListeners(JTree tree)
	{
		tree.addTreeExpansionListener(new TreeExpansionHandler());
	}

	public int getColumnCount() 
	{
		return treeTableModel.getColumnCount();
	}

	public String getColumnName(int column) 
	{
		return treeTableModel.getColumnName(column);
	}

	public Class getColumnClass(int column) 
	{
		return treeTableModel.getColumnClass(column);
	}

	public int getRowCount() 
	{
		return tree.getRowCount();
	}

	protected Object nodeForRow(int row) 
	{
		TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();         
	}

	public Object getValueAt(int row, int column) 
	{
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}

	public boolean isCellEditable(int row, int column) 
	{
		return treeTableModel.isCellEditable(nodeForRow(row), column); 
	}

	public void setValueAt(Object value, int row, int column) 
	{
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}

	protected void delayedFireTableRowsUpdated() 
	{
		SwingUtilities.invokeLater(new DelayedTableDataUpdatedFirer()); 
	}

	protected void delayedFireTableRowsRemoved() 
	{
		SwingUtilities.invokeLater(new DelayedTableDataChangedFirer());	}

	protected void delayedFireTableRowsInserted() 
	{
		SwingUtilities.invokeLater(new DelayedTableDataChangedFirer());
	}

	protected void delayedFireTableDataChanged() 
	{
		SwingUtilities.invokeLater(new DelayedTableDataUpdatedFirer());
	}
/*
	private void saveTreeExpansionState() throws Exception
	{
		if (!isListening)
			return;

		int rowCount = tree.getRowCount();
		ObjectReferenceList objRefList = new ObjectReferenceList();
		for (int i = 0; i < rowCount; i ++)
		{
			TreePath treePath = tree.getPathForRow(i);
			if (tree.isExpanded(treePath))
			{
				TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
				ObjectReference objectReference = node.getObjectReference();
				if (objectReference != null)
					objRefList.add(objectReference);
			}
		}
		saveExpandedPath(objRefList);
	}

	private void saveExpandedPath(ObjectReferenceList  newObjRefList) throws Exception
	{
		ViewData viewData = project.getViewData(project.getCurrentView());		
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, newObjRefList.toString());
		project.executeCommand(cmd);
	}
	
	void setTreeExpansionState() throws Exception
	{
		ObjectReferenceList objRefList = getExpandedNodeList();
		TreeTableNode root = (TreeTableNode)treeTableModel.getRoot();
		TreePath rootPath = new TreePath(root);
		expandNode(objRefList, rootPath);
	}

	private void expandNode(ObjectReferenceList objRefToUse, TreePath thisPath)
	{
		TreeTableNode topLevelObject = (TreeTableNode)thisPath.getLastPathComponent();
		ObjectReference topLevelObjRef = topLevelObject.getObjectReference();
		if ( ! (objRefToUse.contains(topLevelObjRef) || topLevelObjRef == null))
		{
			tree.collapsePath(thisPath);
			return;
		}
			tree.expandPath(thisPath);
		
		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = thisPath.pathByAddingChild(secondLevelObject);
			expandNode(objRefToUse, secondLevelPath);
		}
	}

	public void restoreTreeState() throws Exception
	{
		isListening = false;
		setTreeExpansionState();
		isListening = true;
	}
	
	private ObjectReferenceList getExpandedNodeList() throws Exception, ParseException
	{
		ViewData viewData = project.getViewData(project.getCurrentView());
		ObjectReferenceList objRefList= new ObjectReferenceList(viewData.getData(ViewData.TAG_CURRENT_EXPANSION_LIST));
		return objRefList;
	}
*/
	private final class TreeModelHandler implements TreeModelListener
	{
		public void treeNodesChanged(TreeModelEvent e) 
		{
			delayedFireTableRowsUpdated();
		}

		public void treeNodesInserted(TreeModelEvent e) 
		{
			delayedFireTableRowsInserted();
		}

		public void treeNodesRemoved(TreeModelEvent e) 
		{
			delayedFireTableRowsRemoved();
		}

		public void treeStructureChanged(TreeModelEvent e) 
		{
			delayedFireTableDataChanged();
		}
	}

	private final class TreeExpansionHandler implements TreeExpansionListener
	{
		public void treeExpanded(TreeExpansionEvent event) 
		{
			treeExpansionStateChanged();
		}

		public void treeCollapsed(TreeExpansionEvent event) 
		{
			treeExpansionStateChanged();
		}

		private void treeExpansionStateChanged()
		{
			try
			{
				treeTableStateSaver.saveTreeExpansionState();
			}
			catch(Exception e)
			{
				EAM.errorDialog(EAM.text("Could not save tree epanded state"));
			}
			fireTableDataChanged();
		}
	}

	private final class DelayedTableDataChangedFirer implements Runnable
	{
		public void run() 
		{
			fireTableDataChanged();
		}
	}

	private final class DelayedTableDataUpdatedFirer implements Runnable
	{
		public void run() 
		{
			fireTableRowsUpdated(0, getRowCount() - 1);
		}
	}
	
	public void restoreTreeState() throws Exception
	{
		treeTableStateSaver.restoreTreeState();
	}
/*
	public void commandExecuted(CommandExecutedEvent event)
	{
		executeTreeStateRestore(event);
	}
	
	public void commandUndone(CommandExecutedEvent event)
	{
		executeTreeStateRestore(event);
	}
	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	private void executeTreeStateRestore(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		try
		{
			restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unexpected Error has occured"));
		}
	}

	boolean isListening = true;
	
*/	
	Project project;
	JTree tree;
	TreeTableModel treeTableModel;
	TreeTableStateSaver treeTableStateSaver;
}
