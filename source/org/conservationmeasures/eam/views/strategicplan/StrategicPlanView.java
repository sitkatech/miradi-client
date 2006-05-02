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
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiButton;

public class StrategicPlanView extends UmbrellaView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new StrategicPlanToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());

	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Strategic Plan";
	}

	public void becomeActive() throws Exception
	{
		removeAll();
		JPanel strategicPanel = new JPanel(new BorderLayout());
		strategicPanel.add(createActivityTree(), BorderLayout.CENTER);		
		strategicPanel.add(createButtonBox(), BorderLayout.AFTER_LAST_LINE);

		add(strategicPanel, BorderLayout.CENTER);
		
	}

	public void becomeInactive() throws Exception
	{
	}

	private JTree createActivityTree() throws Exception
	{
		Task rootTask = getProject().getRootTask();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Activities");
		//tree.setRootVisible(false);

		for(int i = 0; i < rootTask.getSubtaskCount(); ++i)
		{
			int subtaskId = rootTask.getSubtaskId(i);
			Task subtask = getProject().getTaskPool().find(subtaskId);
			TaskTreeNode node = new TaskTreeNode(subtask);
			rootNode.add(node);
		}
		
		model = new DefaultTreeModel(rootNode);
		tree = new JTree(model);
		return tree;
	}

	private Box createButtonBox()
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton("Add");
		UiButton editButton = new UiButton("Edit");
		UiButton deleteButton = new UiButton("Delete");
		addButton.addActionListener(new AddButtonHandler(getProject(), tree));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		
		return buttonBox;
	}
	
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
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)getModel().getRoot();
			getModel().insertNodeInto(newNode, root, root.getChildCount());
			tree.expandPath(new TreePath(getModel().getPathToRoot(root)));
			
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