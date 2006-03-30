package org.conservationmeasures.eam.views.task;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

public class TaskView extends UmbrellaView
{
	public TaskView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new TaskToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(new TaskComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Task";
	}

}


class TaskComponent extends JLabel
{
	public TaskComponent()
	{
		super(new ImageIcon("images/Tasks.png"));
	}
}
