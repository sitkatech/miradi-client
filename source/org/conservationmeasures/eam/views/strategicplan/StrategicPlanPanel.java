/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

public class StrategicPlanPanel extends JPanel
{
	static public StrategicPlanPanel createForProject(Project projectToUse) throws Exception
	{
		return new StrategicPlanPanel(projectToUse, StrategicPlanTreeTableModel.createForProject(projectToUse));
	}
	
	static public StrategicPlanPanel createForStrategy(Project projectToUse, ConceptualModelIntervention intervention) throws Exception
	{
		return new StrategicPlanPanel(projectToUse, StrategicPlanTreeTableModel.createForStrategy(intervention));
	}
	
	private StrategicPlanPanel(Project projectToUse, StrategicPlanTreeTableModel modelToUse) throws Exception
	{
		super(new BorderLayout());
		model = modelToUse;
		add(new JScrollPane(createActivityTree()), BorderLayout.CENTER);		
		add(createButtonBox(), BorderLayout.AFTER_LAST_LINE);

	}
	
	private Component createActivityTree() throws Exception
	{
		tree = new JTreeTable(model);
//		tree.setRootVisible(false);
//		tree.expandPath(new TreePath(model.getPathToRoot(strategyNode)));
		return tree;
	}

	private Box createButtonBox()
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton(EAM.text("Button|Add"));
		UiButton editButton = new UiButton(EAM.text("Button|Edit"));
		UiButton deleteButton = new UiButton(EAM.text("Button|Delete"));
//		addButton.addActionListener(new AddButtonHandler(getProject(), tree));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
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
		int type = ObjectType.TASK;
		CommandCreateObject create = new CommandCreateObject(type);
		project.executeCommand(create);
		Task newTask = project.getTaskPool().find(create.getCreatedId());
		
		Task rootTask = project.getRootTask();
		IdList subtaskIds = rootTask.getSubtaskIdList();
		subtaskIds.add(newTask.getId());
		CommandSetObjectData addSubtask = new CommandSetObjectData(type, rootTask.getId(), Task.TAG_SUBTASK_IDS, subtaskIds.toString());
		project.executeCommand(addSubtask);
		
		TaskTreeNode newNode = new TaskTreeNode(newTask);
		DefaultMutableTreeNode strategy = (DefaultMutableTreeNode)getModel().getChild(getModel().getRoot(), 0);
		getModel().insertNodeInto(newNode, strategy, strategy.getChildCount());
//		tree.expandPath(new TreePath(getModel().getPathToRoot(strategy)));
	
	}
	
	Project project;
	JTreeTable tree;
}