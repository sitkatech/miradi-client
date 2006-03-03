package org.conservationmeasures.eam.views.budget;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class BudgetView extends UmbrellaView
{

	public BudgetView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new BudgetToolBar(mainWindowToUse.getActions()));
		add(new BudgetComponent());
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Budget";
	}

}

	class BudgetComponent extends JLabel
	{
		public BudgetComponent()
		{
			super(new ImageIcon(BudgetView.class.getResource("budget.jpg")));
		}
	}