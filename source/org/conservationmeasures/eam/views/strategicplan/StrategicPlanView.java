package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.ActionModifyResource;
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
		resourcePanel = new ResourcePanel(getMainWindow());
		tabs.add(EAM.text("Resources"), resourcePanel);
	}

	public void becomeInactive() throws Exception
	{
		if(panel != null)
		{
			panel.close();
		}
	}
	
	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return panel;
	}
	
	public ResourcePanel getResourcePanel()
	{
		return resourcePanel;
	}
	
	public StratPlanObject getSelectedObject()
	{
		if(panel == null)
			return null;
		return panel.getSelectedObject();
	}
	
	public ModifyResource getModifyResourceDoer()
	{
		return modifyResourceDoer;
	}

	private void addStrategicPlanDoersToMap()
	{
		modifyResourceDoer = new ModifyResource(this);
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity(this));
		addDoerToMap(ActionModifyActivity.class, new ModifyActivity(this));
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionModifyResource.class, getModifyResourceDoer());
	}
	
	JTabbedPane tabs;
	StrategicPlanPanel panel;
	ResourcePanel resourcePanel;
	ModifyResource modifyResourceDoer;
}

