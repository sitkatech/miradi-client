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

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objecthelpers.ObjectReferenceList;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

import com.java.sun.jtreetable.TreeTableModel;

public class EAMTreeTableModelAdapter extends AbstractTableModel implements CommandExecutedListener
{
	public EAMTreeTableModelAdapter(Project projectToUse, TreeTableModel treeTableModelToUse, JTree treeToUse)
	{
		tree = treeToUse;
		treeTableModel = treeTableModelToUse;
		project = projectToUse;

		project.addCommandExecutedListener(this);
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

	private void saveTreeExpansionState() throws Exception
	{
		int rowCount = tree.getRowCount();
		ObjectReferenceList objRefList = new ObjectReferenceList();;
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
		
		if (isListening)
			saveExpandedPath(objRefList);
	}

	private void saveExpandedPath(ObjectReferenceList  newObjRefList) throws Exception
	{
		ViewData viewData = project.getViewData(project.getCurrentView());		
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId() ,ViewData.TAG_CURRENT_EXPANSION_LIST, newObjRefList.toString());
		project.executeCommand(cmd);
	}

	private void expandRootNodes()
	{
		TreeTableNode root = (TreeTableNode)tree.getModel().getRoot();
		TreePath rootPath = new TreePath(root);
		TreeTableNode topLevelObject = (TreeTableNode)rootPath.getLastPathComponent();
		tree.expandPath(rootPath);

		for(int childIndex = 0; childIndex < topLevelObject.getChildCount(); ++childIndex)
		{
			TreeTableNode secondLevelObject = topLevelObject.getChild(childIndex);
			TreePath secondLevelPath = rootPath.pathByAddingChild(secondLevelObject);
			tree.expandPath(secondLevelPath);
		}
	}

	public void restoreTreeState()
	{
		try
		{
			isListening = false;
			expandRootNodes();
			ViewData viewData = project.getViewData(project.getCurrentView());
			ObjectReferenceList objRefList= new ObjectReferenceList(viewData.getData(ViewData.TAG_CURRENT_EXPANSION_LIST));
			int rowCount = tree.getRowCount();
			for (int rowCounter = 0; rowCounter < rowCount; rowCounter ++)
			{
				TreePath treePath = tree.getPathForRow(rowCounter);
				TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
				ObjectReference nodeObjRef = node.getObjectReference();
				if (nodeObjRef != null)
					for (int objRefListCounter  = 0; objRefListCounter < objRefList.size(); objRefListCounter++)
						if (nodeObjRef.equals(objRefList.get(objRefListCounter)))
							tree.expandPath(treePath);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		isListening = true;
	}

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
			try
			{
				saveTreeExpansionState();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
			fireTableDataChanged();
		}

		public void treeCollapsed(TreeExpansionEvent event) 
		{
			try
			{
				saveTreeExpansionState();
			}
			catch(Exception e)
			{
				EAM.logException(e);
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

	public void commandExecuted(CommandExecutedEvent event)
	{
		//FIXME add functionality so that undo and redo work
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
		//FIXME add functionality so that undo and redo work
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		//TODO remove comments and make undo work
		/*
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		//System.out.println("PREVIOUS DATA = "+cmd.getPreviousDataValue());
		//int rowCount = tree.getRowCount();
		//try
		{
			ObjectReferenceList objRefList = new ObjectReferenceList(cmd.getPreviousDataValue());
					for (int i = 0; i < rowCount; i ++)
			{
				TreePath treePath = tree.getPathForRow(i);
				if (treePath != null)
				{
					TreeTableNode node = (TreeTableNode)treePath.getLastPathComponent();
					//System.out.println("node = "+node);
					for (int j  = 0; j < objRefList.size(); j++)
					{
						ObjectReference or = objRefList.get(j);
						System.out.println("or = "+or);

						if (or.equals(node.getObjectReference()) && j != 0)
						{
							//TODO remove commented print
							System.out.println(" COLLPASING NODE ="+new TreePath(node));
							tree.collapsePath(treePath);
						}
					}
				}

			}	
		}
		catch(ParseException e)
		{
		}
		 */
	}

	boolean isListening = true;
	Project project;
	JTree tree;
	TreeTableModel treeTableModel;
}
