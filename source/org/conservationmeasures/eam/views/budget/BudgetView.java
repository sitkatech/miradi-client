package org.conservationmeasures.eam.views.budget;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

public class BudgetView extends UmbrellaView
{

	public BudgetView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new BudgetToolBar(mainWindowToUse.getActions()));
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(new BudgetComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.BUDGET_VIEW_NAME;
	}

	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}

}

	class BudgetComponent extends JLabel
	{
		public BudgetComponent()
		{
			super(new ResourceImageIcon("images/Budget.png"));
		}
	}