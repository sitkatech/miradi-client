package org.conservationmeasures.eam.views.task;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewTask;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class TaskToolBar extends JToolBar
{
	public TaskToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewTask.class));
	}

}
