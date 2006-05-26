package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class StrategicPlanView extends UmbrellaView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new StrategicPlanToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		
		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		
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
		tabs.removeAll();

		panel = StrategicPlanPanel.createForProject(getMainWindow());
		tabs.add(EAM.text("Strategic Plan"), panel);
		tabs.add(EAM.text("Resources"), new ResourcePanel(getProject(), getActions()));
	}

	public void becomeInactive() throws Exception
	{
		if(panel != null)
		{
			panel.close();
		}
	}
	
	public StrategicPlanPanel getPanel()
	{
		return panel;
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
		addDoerToMap(ActionModifyActivity.class, new ModifyActivity(this));
		addDoerToMap(ActionCreateResource.class, new CreateResource());
	}
	
	JTabbedPane tabs;
	StrategicPlanPanel panel;
}

