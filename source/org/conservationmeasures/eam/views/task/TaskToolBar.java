package org.conservationmeasures.eam.views.task;

import org.conservationmeasures.eam.actions.ActionViewTask;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class TaskToolBar extends EAMToolBar
{
	public TaskToolBar(Actions actions)
	{
		super(actions, ActionViewTask.class);
	}

}
