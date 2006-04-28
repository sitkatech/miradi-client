package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiLabel;

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
		UiLabel label = new UiLabel("Strategic Plan View"); 
		add(label, BorderLayout.CENTER);
		System.out.println("becomeActive SP called");
	}

	public void becomeInactive() throws Exception
	{
	}
}
