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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

public class StrategicPlanPanel extends JPanel
{
	public StrategicPlanPanel(Project projectToUse) throws Exception
	{
		super(new BorderLayout());
		project = projectToUse;
		add(createActivityTree(), BorderLayout.CENTER);		
		add(createButtonBox(), BorderLayout.AFTER_LAST_LINE);

	}
	
	private JTree createActivityTree() throws Exception
	{
		Task rootTask = getProject().getRootTask();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Activities");
		DefaultMutableTreeNode strategyNode = new DefaultMutableTreeNode("Strategy");
		rootNode.add(strategyNode);

		for(int i = 0; i < rootTask.getSubtaskCount(); ++i)
		{
			int subtaskId = rootTask.getSubtaskId(i);
			Task subtask = getProject().getTaskPool().find(subtaskId);
			TaskTreeNode node = new TaskTreeNode(subtask);
			strategyNode.add(node);
		}
		
		model = new DefaultTreeModel(rootNode);
		tree = new JTree(model);
		tree.setRootVisible(false);
		tree.expandPath(new TreePath(model.getPathToRoot(strategyNode)));
		return tree;
	}

	private Box createButtonBox()
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton("Button|Add");
		UiButton editButton = new UiButton("Button|Edit");
		UiButton deleteButton = new UiButton("Button|Delete");
		addButton.addActionListener(new AddButtonHandler(getProject(), tree));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
	Project getProject()
	{
		return project;
	}
	
	Project project;
	JTree tree;
	DefaultTreeModel model;
}

class AddButtonHandler implements ActionListener
{

	AddButtonHandler(Project projectToUse, JTree treeToUse)
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
			tree.expandPath(new TreePath(getModel().getPathToRoot(strategy)));
			
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Could not create activity");
		}
		
	}
	
	Project project;
	JTree tree;
}