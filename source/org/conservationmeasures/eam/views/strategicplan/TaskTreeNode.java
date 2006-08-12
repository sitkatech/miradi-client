package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.tree.DefaultMutableTreeNode;

import org.conservationmeasures.eam.objects.Task;

public class TaskTreeNode extends DefaultMutableTreeNode
{
	TaskTreeNode(Task taskToUse)
	{
		task = taskToUse;
	}
	
	public String toString()
	{
		return Integer.toString(task.getId().asInt());
	}
	
	Task task;
}
