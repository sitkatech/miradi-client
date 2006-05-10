/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ActivityInsertionPoint;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.martus.swing.UiButton;

import com.java.sun.jtreetable.JTreeTable;

public class StrategicPlanPanel extends JPanel implements TreeSelectionListener, CommandExecutedListener
{
	static public StrategicPlanPanel createForProject(MainWindow mainWindowToUse) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForProject(mainWindowToUse.getProject()));
	}
	
	static public StrategicPlanPanel createForStrategy(MainWindow mainWindowToUse, ConceptualModelIntervention intervention) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForStrategy(mainWindowToUse.getProject(), intervention));
	}
	
	private StrategicPlanPanel(MainWindow mainWindowToUse, StrategicPlanTreeTableModel modelToUse) throws Exception
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		model = modelToUse;
		tree = new JTreeTable(model);
		expandTopLevel();
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.getTree().addTreeSelectionListener(this);
		add(new JScrollPane(tree), BorderLayout.CENTER);		
		add(createButtonBox(mainWindow.getActions()), BorderLayout.AFTER_LAST_LINE);
		tree.getTree().addSelectionRow(0);
		
		mainWindow.getProject().addCommandExecutedListener(this);
	}
	
	public void close()
	{
		mainWindow.getProject().removeCommandExecutedListener(this);
	}
	
	public StratPlanObject getSelectedObject()
	{
		StratPlanObject selected = (StratPlanObject)tree.getTree().getLastSelectedPathComponent();
		return selected;
	}
	
	public ActivityInsertionPoint getActivityInsertionPoint()
	{
		TreePath path = tree.getTree().getSelectionPath();
		return new ActivityInsertionPoint(path, 0);
	}

	public StrategicPlanTreeTableModel getModel()
	{
		return model;
	}

	void expandTopLevel()
	{
		StratPlanObject root = model.getRootStratPlanObject();
		TreePath rootPath = new TreePath(root);
		for(int i = 0; i < root.getChildCount(); ++i)
		{
			StratPlanObject topLevelObject = (StratPlanObject)root.getChild(i);
			TreePath thisPath = rootPath.pathByAddingChild(topLevelObject);
			tree.getTree().expandPath(thisPath);
		}
	}
	
	private Box createButtonBox(Actions actions)
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton(actions.get(ActionInsertActivity.class));
		UiButton editButton = new UiButton(actions.get(ActionModifyActivity.class));
		UiButton deleteButton = new UiButton(EAM.text("Button|Delete"));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
	boolean isRelevant(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return false;
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		if(cmd.getObjectType() == ObjectType.MODEL_NODE && cmd.getFieldTag().equals(ConceptualModelIntervention.TAG_ACTIVITY_IDS))
			return true;
		if(cmd.getObjectType() == ObjectType.TASK && cmd.getFieldTag().equals(Task.TAG_SUBTASK_IDS))
			return true;
		return false;
	}
	
	public void valueChanged(TreeSelectionEvent e)
	{
		mainWindow.getActions().updateActionStates();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if(!isRelevant(event))
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		model.idListWasChanged(cmd.getObjectType(), cmd.getObjectId(), cmd.getDataValue());
		TreePath pathToModifiedItem = model.getPath(cmd.getObjectType(), cmd.getObjectId());
		tree.getTree().expandPath(pathToModifiedItem);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		if(!isRelevant(event))
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		model.idListWasChanged(cmd.getObjectType(), cmd.getObjectId(), cmd.getPreviousDataValue());
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	MainWindow mainWindow;
	JTreeTable tree;
	StrategicPlanTreeTableModel model;
}

