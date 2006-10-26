package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

public class WorkPlanView extends UmbrellaView
{
	public WorkPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new WorkPlanToolBar(mainWindowToUse.getActions()));
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(new TaskComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "WorkPlan";
	}

	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}

}


class TaskComponent extends JLabel
{
	public TaskComponent()
	{
		super(new ResourceImageIcon("images/Tasks.png"));
	}
}
