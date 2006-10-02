/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionModifyIndicator;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.umbrella.CreateIndicator;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MonitoringView extends UmbrellaView
{
	public MonitoringView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MonitoringToolBar(mainWindowToUse.getActions()));

		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		tabs.addChangeListener(new TabChangeListener());
		
		addMonitoringPlanDoersToMap();
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Monitoring ";
	}

	public void becomeActive() throws Exception
	{
		int mostRecentTabIndex = currentTab;
		
		ignoreTabChanges = true;
		try
		{
			tabs.removeAll();
	
			monitoringPanel = new MonitoringPanel(getProject());
			indicatorManagementPanel = new IndicatorManagementPanel(this);
			tabs.add(EAM.text("Monitoring Plan"), monitoringPanel);
			tabs.add(EAM.text("Indicators"), indicatorManagementPanel);
			
			tabs.setSelectedIndex(mostRecentTabIndex);
		}
		finally
		{
			ignoreTabChanges = false;
		}
	}

	public void becomeInactive() throws Exception
	{
		monitoringPanel = null;
		indicatorManagementPanel = null;
	}

	public IndicatorManagementPanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	private void addMonitoringPlanDoersToMap()
	{
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionModifyIndicator.class, new ModifyIndicator());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());

		
	}
	
	private void setTab(int newTab)
	{
		tabs.setSelectedIndex(newTab);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		try
		{
			if(cmd.getObjectType() != ObjectType.VIEW_DATA)
				return;
			if(!cmd.getObjectId().equals(getViewData().getId()))
				return;
			if(!cmd.getFieldTag().equals(ViewData.TAG_CURRENT_TAB))
				return;
			EAM.logVerbose("MonitoringView.commandExecuted: " + cmd.toString());
			setTab(new Integer(cmd.getDataValue()).intValue());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		try
		{
			if(cmd.getObjectType() != ObjectType.VIEW_DATA)
				return;
			if(!cmd.getObjectId().equals(getViewData().getId()))
				return;
			if(!cmd.getFieldTag().equals(ViewData.TAG_CURRENT_TAB))
				return;
			EAM.logVerbose("MonitoringView.commandUndone: " + cmd.toString());
			setTab(new Integer(cmd.getPreviousDataValue()).intValue());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			int newTab = tabs.getSelectedIndex();
			if(!ignoreTabChanges)
				recordTabChangeCommand(newTab);

			currentTab = newTab;
			getMainWindow().getActions().updateActionStates();
		}

		private void recordTabChangeCommand(int newTab)
		{
			EAM.logVerbose("TabChangeListener.stateChanged");
			closeActivePropertiesDialog();
			try
			{
				Command tabChangeCommand = createTabChangeCommand(newTab);
				getViewData().setCurrentTab(newTab);
				if(!getProject().isExecutingACommand())
				{
					getProject().recordCommand(tabChangeCommand);
					EAM.logVerbose("TabChangeListener.stateChanged recorded command");
				}
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Unexpected error");
			}
		}
		
		Command createTabChangeCommand(int newTab) throws Exception
		{
			CommandSetObjectData cmd = new CommandSetObjectData(ObjectType.VIEW_DATA, getViewData().getId(), ViewData.TAG_CURRENT_TAB, Integer.toString(newTab));
			cmd.setPreviousDataValue(Integer.toString(currentTab));
			return cmd;
		}

	}
	
	JTabbedPane tabs;
	int currentTab;
	boolean ignoreTabChanges;
	
	MonitoringPanel monitoringPanel;
	IndicatorManagementPanel indicatorManagementPanel;
}
