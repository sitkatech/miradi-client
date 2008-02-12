/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.treetables;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.martus.swing.UiButton;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectCollectionPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.utils.HideableScrollBar;
import org.miradi.utils.MiradiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class TreeTablePanel extends ObjectCollectionPanel  implements TreeSelectionListener
{
	public TreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] classes)
	{
		super(mainWindowToUse.getProject(), treeToUse);
		mainWindow = mainWindowToUse;
		tree = treeToUse;
		
		restoreTreeExpansionState();
		treeTableScrollPane = new ScrollPaneWithHideableScrollBar(tree);
		add(treeTableScrollPane, BorderLayout.CENTER);
		
		GridLayoutPlus layout = new GridLayoutPlus(1, 0);
		JPanel buttonBox = new JPanel(layout);
		add(buttonBox,BorderLayout.AFTER_LAST_LINE);
		addButtonsToBox(classes, buttonBox, mainWindow.getActions());

		tree.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tree.getTree().addSelectionRow(0);
		tree.getTree().addTreeSelectionListener(this);
	}
	
	public TreeTableWithStateSaving getTree()
	{
		return tree;
	}

	protected void restoreTreeExpansionState() 
	{
		try
		{
			tree.restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error restoring tree state"));
		}
	}

	public void dispose()
	{
		tree.dispose();
		super.dispose();
	}
	
	public BaseObject getSelectedObject()
	{
		return getSelectedTreeNode().getObject();
	}

	public TreeTableNode getSelectedTreeNode()
	{
		return (TreeTableNode)tree.getTree().getLastSelectedPathComponent();
	}

	public void selectObject(BaseObject objectToSelect)
	{
		tree.selectObject(objectToSelect.getRef());
	}

	public GenericTreeTableModel getModel()
	{
		return model;
	}
	
	public void addButtonsToBox(Class[] classes, JPanel buttonBox, Actions actions)
	{
		for (int i=0; i<classes.length; ++i)
		addCreateButtonAndAddToBox(classes[i], buttonBox, actions);
	}
	
	private void addCreateButtonAndAddToBox(Class actionClass, JPanel buttonBox, Actions actions)
	{
		UiButton button = createObjectsActionButton(actions.getObjectsAction(actionClass), tree);
		buttonBox.add(button);
	}
	

	public void valueChanged(TreeSelectionEvent e)
	{	
		if (getPropertiesPanel() == null)
			return;
		
		TreePath[] treePaths = tree.getTree().getSelectionPaths();
		if (treePaths==null)
			return;
		
		Object[] selectedObjects = treePaths[0].getPath();
		
		Vector objects = new Vector();
		for (int i=0; i< selectedObjects.length; ++i)
		{
			TreeTableNode node = (TreeTableNode)selectedObjects[i];
			objects.insertElementAt(node.getObjectReference(), 0);
		}
		
		getPropertiesPanel().setObjectRefs((ORef[])objects.toArray(new ORef[0]));
		mainWindow.updateActionStates();
	}
	
	
	//TODO:Is this needed? Is it the right place/mechanism? 
	public void setSelectedObject(ORef ref)
	{
	}
	
	protected ScrollPaneWithHideableScrollBar getTreeTableScrollPane()
	{
		return treeTableScrollPane;
	}
	
	protected boolean isDeleteCommand(CommandExecutedEvent event, int type)
	{
		if (! event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject deleteCommand = (CommandDeleteObject) event.getCommand();
		if (deleteCommand.getObjectType() != type)
			return false;
		
		return true;
	}

	protected boolean isCreateCommand(CommandExecutedEvent event, int type)
	{
		if (! event.isCreateObjectCommand())
			return false;
	
		CommandCreateObject createCommand = (CommandCreateObject) event.getCommand();
		if (createCommand.getObjectType() != type)
			return false;
		
		return true;
	}

	protected boolean isSelectedObjectModification(CommandExecutedEvent event, int typeToCheck)
	{
		if (! event.isSetDataCommand())
			return false;
		
		TreeTableNode node = getSelectedTreeNode();
		if (node == null)
			return false;
		
		BaseObject selectedObject = node.getObject(); 
		if (selectedObject == null)
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int setType = setCommand.getObjectType();
		if(setType == typeToCheck)
			return true;
		
		String setField = setCommand.getFieldTag();
		
		String[] fieldTags = selectedObject.getFieldTags();
		Vector fields = new Vector(Arrays.asList(fieldTags));
	
		boolean sameType = (selectedObject.getType() == setType);
		boolean containsField = (fields.contains(setField));
		return (sameType && containsField);
	}

	protected void repaintToGrowIfTreeIsTaller()
	{
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().repaint();
	}

	public static class ScrollPaneWithHideableScrollBar extends MiradiScrollPane
	{
		public ScrollPaneWithHideableScrollBar(JComponent component)
		{
			super(component);
			hideableScrollBar = new HideableScrollBar();
			setVerticalScrollBar(hideableScrollBar);
		}
		
		public void showVerticalScrollBar()
		{
			hideableScrollBar.visible = true;
		}
		
		public void hideVerticalScrollBar()
		{
			hideableScrollBar.visible = false;
		}

		private HideableScrollBar hideableScrollBar;
	}

	private MainWindow mainWindow;
	protected TreeTableWithStateSaving tree;
	protected GenericTreeTableModel model;
	protected ScrollPaneWithHideableScrollBar treeTableScrollPane;
}
