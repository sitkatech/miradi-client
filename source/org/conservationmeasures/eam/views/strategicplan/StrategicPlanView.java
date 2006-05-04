package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class StrategicPlanView extends UmbrellaView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new StrategicPlanToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		
		addStrategicPlanDoersToMap();

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

		panel = StrategicPlanPanel.createForProject(getMainWindow());
		add(panel, BorderLayout.CENTER);
	}

	public void becomeInactive() throws Exception
	{
	}
	
	public StratPlanObject getSelectedObject()
	{
		if(panel == null)
			return null;
		return panel.getSelectedObject();
	}

	private void addStrategicPlanDoersToMap()
	{
		addDoerToMap(ActionInsertActivity.class, new InsertActivity(this));
	}
	
	StrategicPlanPanel panel;
}

