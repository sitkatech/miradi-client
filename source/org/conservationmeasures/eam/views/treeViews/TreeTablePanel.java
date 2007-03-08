/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.treeViews;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectCollectionPanel;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class TreeTablePanel extends ObjectCollectionPanel  implements TreeSelectionListener, CommandExecutedListener
{
	public TreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse, Class[] classes, int objectType)
	{
		super(mainWindowToUse.getProject(), treeToUse);
		mainWindow = mainWindowToUse;
		tree = treeToUse;
		panelObjectType = objectType;
		
		tree.getTree().addTreeSelectionListener(this);
		restoreTreeExpansionState();
		
		UiScrollPane uiScrollPane = new UiScrollPane(tree);
		add(uiScrollPane, BorderLayout.CENTER);
		
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel buttonBox = new JPanel(layout);
		add(buttonBox,BorderLayout.AFTER_LINE_ENDS);
		addButtonsToBox(classes, buttonBox, mainWindow.getActions());

		tree.getTree().addSelectionRow(0);
		getProject().addCommandExecutedListener(this);
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
		getProject().removeCommandExecutedListener(this);
		tree.dispose();
		super.dispose();
	}
	
	public EAMObject getSelectedObject()
	{
		return getSelectedTreeNode().getObject();
	}

	public TreeTableNode getSelectedTreeNode()
	{
		TreeTableNode selected = (TreeTableNode)tree.getTree().getLastSelectedPathComponent();
		return selected;
	}

	public void selectObject(EAMObject objectToSelect)
	{
		selectObject(objectToSelect.getType(), objectToSelect.getId());
	}

	public void selectObject(int objectType, BaseId objectToSelect)
	{
		TreePath found = model.findObject(model.getPathToRoot(), objectType, objectToSelect);
		if(found == null)
			return;
		tree.getTree().expandPath(found.getParentPath());
		int row = tree.getTree().getRowForPath(found);
		if(row < 0)
		{
			EAM.logWarning("TreeTablePanel.selectObject failed: row -1");
			return;
		}
		tree.clearSelection();
		setSelectedRow(row);
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
	

	public boolean isSetDataCommand(CommandExecutedEvent event)
	{
		return isCommand(event, CommandSetObjectData.COMMAND_NAME);
	}

	public boolean isCreateObjectCommand(CommandExecutedEvent event)
	{
		return isCommand(event, CommandCreateObject.COMMAND_NAME);
	}

	public boolean isDeleteObjectCommand(CommandExecutedEvent event)
	{
		return isCommand(event, CommandDeleteObject.COMMAND_NAME);
	}

	private boolean isCommand(CommandExecutedEvent event, final String string)
	{
		Command rawCommand = event.getCommand();
		return (rawCommand.getCommandName().equals(string));
	}


	public boolean isThisSeDataCommand(CommandExecutedEvent event, int objectType, String tag)
	{
		if(!isSetDataCommand(event))
			return false;

		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		return (cmd.getObjectType() == objectType && cmd.getFieldTag().equals(tag));
	}
	
	
	public void valueChanged(TreeSelectionEvent e)
	{	
		TreeTableNode selectedObject = getSelectedTreeNode();
		if (selectedObject == null)
			return;
		if (propertiesPanel == null)
			return;
		
		BaseId baseId = BaseId.INVALID;
		if (selectedObject.getType() == panelObjectType)
			baseId = getSelectedTreeNode().getObjectReference().getObjectId();
		
		propertiesPanel.setObjectId(baseId);
		mainWindow.getActions().updateActionStates();
	}
	
	
	abstract public void commandExecuted(CommandExecutedEvent event);
	
	

	
	public void setSelectedRow(int currentSelectedRow)
	{
		if(currentSelectedRow < 0)
		{
			tree.clearSelection();
			return;
		}
		
		try
		{
			tree.setRowSelectionInterval(currentSelectedRow, currentSelectedRow);
		}
		catch (Exception e)
		{
			//TODO ingnoring for now.  precheck to ingore and only log real errors.
			EAM.logException(e);
		}
	}

	
	public void setPropertiesPanel(ObjectDataInputPanel propertiesPanelToUse)
	{
		propertiesPanel = propertiesPanelToUse;
	}

	MainWindow mainWindow;
	protected TreeTableWithStateSaving tree;
	protected GenericTreeTableModel model;
	protected ObjectDataInputPanel propertiesPanel;
	int panelObjectType;
}
