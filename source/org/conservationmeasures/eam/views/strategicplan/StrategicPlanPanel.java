/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanPanel extends JPanel implements TreeSelectionListener
{
	static public StrategicPlanPanel createForProject(MainWindow mainWindowToUse) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForProject(mainWindowToUse.getProject()));
	}
	
	static public StrategicPlanPanel createForStrategy(MainWindow mainWindowToUse, ConceptualModelIntervention intervention) throws Exception
	{
		return new StrategicPlanPanel(mainWindowToUse, StrategicPlanTreeTableModel.createForStrategy(intervention));
	}
	
	private StrategicPlanPanel(MainWindow mainWindowToUse, StrategicPlanTreeTableModel modelToUse) throws Exception
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		model = modelToUse;
		tree = new JTreeTable(model);
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.getTree().addTreeSelectionListener(this);
		add(new JScrollPane(tree), BorderLayout.CENTER);		
		add(createButtonBox(mainWindow.getActions()), BorderLayout.AFTER_LAST_LINE);
		tree.getTree().addSelectionRow(0);
	}
	
	public StratPlanObject getSelectedObject()
	{
		StratPlanObject selected = (StratPlanObject)tree.getTree().getLastSelectedPathComponent();
		return selected;
	}


	
	private Box createButtonBox(Actions actions)
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton(actions.get(ActionInsertActivity.class));
		UiButton editButton = new UiButton(EAM.text("Button|Edit"));
		UiButton deleteButton = new UiButton(EAM.text("Button|Delete"));
//		addButton.addActionListener(new AddButtonHandler(getProject(), tree));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
	public void valueChanged(TreeSelectionEvent e)
	{
		mainWindow.getActions().updateActionStates();
	}

	MainWindow mainWindow;
	JTreeTable tree;
	TreeTableModel model;
}

class AddButtonHandler implements ActionListener
{

	AddButtonHandler(Project projectToUse, JTreeTable treeToUse)
	{
		project = projectToUse;
		tree = treeToUse;
	}
	
	public DefaultTreeModel getModel()
	{
		return (DefaultTreeModel)tree.getModel();
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				attemptToAdd();
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Could not create activity");
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error");
		}
		
	}
	
	void attemptToAdd() throws Exception
	{
//		int type = ObjectType.TASK;
//		CommandCreateObject create = new CommandCreateObject(type);
//		project.executeCommand(create);
//		Task newTask = project.getTaskPool().find(create.getCreatedId());
//		
//		Task rootTask = project.getRootTask();
		
		// use CommandSetObjectData.createInsertCommand here!
//		IdList subtaskIds = rootTask.getSubtaskIdList();
//		subtaskIds.add(newTask.getId());
//		CommandSetObjectData addSubtask = new CommandSetObjectData(type, rootTask.getId(), Task.TAG_SUBTASK_IDS, subtaskIds.toString());
//		project.executeCommand(addSubtask);
//		
//		TaskTreeNode newNode = new TaskTreeNode(newTask);
//		DefaultMutableTreeNode strategy = (DefaultMutableTreeNode)getModel().getChild(getModel().getRoot(), 0);
//		getModel().insertNodeInto(newNode, strategy, strategy.getChildCount());
//		tree.expandPath(new TreePath(getModel().getPathToRoot(strategy)));
	
	}
	
	Project project;
	JTreeTable tree;
}