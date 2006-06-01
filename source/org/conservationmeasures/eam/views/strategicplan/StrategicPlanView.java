package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
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

		strategicPlanPanel = StrategicPlanPanel.createForProject(getMainWindow());
		tabs.add(EAM.text("Strategic Plan"), strategicPlanPanel);
		resourcePanel = new ResourceManagementPanel(getMainWindow());
		tabs.add(EAM.text("Resources"), resourcePanel);
	}

	public void becomeInactive() throws Exception
	{
		if(strategicPlanPanel != null)
		{
			strategicPlanPanel.close();
		}
	}
	
	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return strategicPlanPanel;
	}
	
	public ResourceManagementPanel getResourcePanel()
	{
		return resourcePanel;
	}
	
	public StratPlanObject getSelectedObject()
	{
		if(strategicPlanPanel == null)
			return null;
		return strategicPlanPanel.getSelectedObject();
	}
	
	public ModifyActivity getModifyActivityDoer()
	{
		return modifyActivityDoer;
	}
	
	public ModifyResource getModifyResourceDoer()
	{
		return modifyResourceDoer;
	}

	private void addStrategicPlanDoersToMap()
	{
		modifyActivityDoer = new ModifyActivity(this);
		modifyResourceDoer = new ModifyResource(this);
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity(this));
		addDoerToMap(ActionModifyActivity.class, modifyActivityDoer);
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity(this));
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionModifyResource.class, getModifyResourceDoer());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource(this));
	}
	
	JTabbedPane tabs;
	StrategicPlanPanel strategicPlanPanel;
	ResourceManagementPanel resourcePanel;
	ModifyActivity modifyActivityDoer;
	ModifyResource modifyResourceDoer;
}

