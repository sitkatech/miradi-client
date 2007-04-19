/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.dialogs.ObjectPoolManagementPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.conservationmeasures.eam.wizard.WizardPanel;

abstract public class TabbedView extends UmbrellaView
{
	public TabbedView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);

		tabs = new JTabbedPane();
		tabs.addChangeListener(new TabChangeListener());
		tabs.setFocusable(false);
	}

	public abstract void createTabs() throws Exception;
	public abstract void deleteTabs() throws Exception;
	public abstract WizardPanel createWizardPanel() throws Exception;
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();
		bigSplitter = new ViewSplitPane(this, getMainWindow(), getProject().getCurrentView(),createWizardPanel(), tabs);
		add(bigSplitter, BorderLayout.CENTER);
		
		ignoreTabChanges = true;
		try
		{
			createTabs();
			int desiredTab = getViewData().getCurrentTab();
			EAM.logVerbose("Selecting tab " + desiredTab);
			if(desiredTab >= tabs.getTabCount())
			{
				EAM.logDebug("Ignoring setting tab selection, setting selection to 0");
				desiredTab = 0;
			}
			tabs.setSelectedIndex(desiredTab);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unexpected error switching view");
		}
		finally
		{
			ignoreTabChanges = false;
			validate();
		}
	}

	public void becomeInactive() throws Exception
	{
		ignoreTabChanges = true;
		try
		{
			deleteTabs();
			tabs.removeAll();
		}
		finally
		{
			ignoreTabChanges = false;
		}
		super.becomeInactive();
	}
	
	public void removeTab(int index)
	{
		tabs.removeTabAt(index);
	}
	
	public int getTabCount()
	{
		return tabs.getTabCount();
	}
	
	public int getSelectedTabIndex()
	{
		return tabs.getSelectedIndex();
	}
	
	public void addTab(String name, Component contents)
	{
		tabs.add(name, contents);
	}

	public void addTab(String name, Icon icon, Component contents)
	{
		tabs.addTab(name, icon, contents);
	}
	
	public void setTab(int newTab)
	{
		tabs.setSelectedIndex(newTab);
	}
	
	public Component getTabContents(int index)
	{
		return tabs.getComponent(index);
	}
	
	public Component getCurrentTabContents()
	{
		return tabs.getSelectedComponent();
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
			EAM.logVerbose("TabbedView.commandExecuted: " + cmd.toString());
			setTab(new Integer(cmd.getDataValue()).intValue());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		
	}

	public void addNonScrollableTab(ObjectPoolManagementPanel panel)
	{
		addTab(panel.getPanelDescription(), panel.getIcon(), panel);
	}
	
	public void addNonScrollableTab(ModelessDialogPanel panel)
	{
		addTab(panel.getPanelDescription(), panel);
	}
	
	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			closeActivePropertiesDialog();

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
				getProject().getDatabase().writeObject(getViewData());
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
	
	JSplitPane bigSplitter;

	JTabbedPane tabs;
	int currentTab;
	boolean ignoreTabChanges;
	
}
