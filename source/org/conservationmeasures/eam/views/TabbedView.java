/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTabbedPane;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

abstract public class TabbedView extends UmbrellaView
{
	public TabbedView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);

		tabs = new PanelTabbedPane();
		tabs.addChangeListener(new TabChangeListener());
		tabs.setFocusable(false);
		tabs.addMouseListener(new MouseHandler());
		
		tabPanels = new HashMap<String, MiradiTabContentsPanelInterface>();
	}

	public abstract void createTabs() throws Exception;
	public abstract void deleteTabs() throws Exception;
	
	public JPopupMenu getTabPopupMenu()
	{
		return null;
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();
		add(tabs, BorderLayout.CENTER);
		
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
			setTab(desiredTab);
		}
		catch (Exception e)
		{
			EAM.panic(e);
		}
		finally
		{
			ignoreTabChanges = false;
		}

		forceLayoutSoSplittersWork();
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
	
	public String getSelectedTabName()
	{
		return tabs.getTitleAt(getSelectedTabIndex());
	}
	
	public MiradiTabContentsPanelInterface getSelectedTabPanel()
	{
		return getTabPanel(getSelectedTabName());
	}
	
	public void addScrollingTab(MiradiTabContentsPanelInterface contents)
	{
		addTab(contents, new FastScrollPane(contents.getComponent()));
	}

	public void addNonScrollingTab(MiradiTabContentsPanelInterface contents)
	{
		addTab(contents, contents.getComponent());
	}
	
	private void addTab(MiradiTabContentsPanelInterface contents, Component tabComponent)
	{
		String tabName = contents.getTabName();
		tabPanels.put(tabName, contents);
		tabs.addTab(tabName, contents.getIcon(), tabComponent);
	}
	
	public MiradiTabContentsPanelInterface getTabPanel(String tabName)
	{
		return tabPanels.get(tabName);
	}
	
	
	public void addTab(String name, Component contents)
	{
		addTab(name, null, contents);
	}

	public void addTab(String name, Icon icon, Component contents)
	{
		tabs.addTab(name, icon, contents);
	}
	
	public void setTab(int newTab)
	{
		tabs.setSelectedIndex(newTab);
		tabWasSelected();
	}
	
	public void setCurrentSelectedTitle(String text)
	{
		int selectedTabIndex = getSelectedTabIndex();
		setTabTitle(text, selectedTabIndex);
	}

	public void setTabTitle(String text, int selectedTabIndex)
	{
		tabs.setTitleAt(selectedTabIndex, text);
	}
	
	public void tabWasSelected()
	{
		getMainWindow().updateActionStates();
		getMainWindow().updateToolBar();
	}
	
	public Component getTabContents(int index)
	{
		return tabs.getComponent(index);
	}
	
	public Component getCurrentTabContents()
	{
		return tabs.getSelectedComponent();
	}
	
	@Override
	public boolean isImageAvailable()
	{
		MiradiTabContentsPanelInterface panel = getSelectedTabPanel();
		if(panel == null)
			return false;
		
		return panel.isImageAvailable();
	}
	
	@Override
	public BufferedImage getImage() throws Exception
	{
		MiradiTabContentsPanelInterface panel = getSelectedTabPanel();
		if(panel == null)
			return null;
		
		return panel.getImage();
	}
	
	@Override
	public boolean isExportableTableAvailable()
	{
		MiradiTabContentsPanelInterface panel = getSelectedTabPanel();
		if(panel == null)
			return false;
		
		return panel.isExportableTableAvailable();
	}

	@Override
	public ExportableTableInterface getExportableTable() throws Exception
	{
		MiradiTabContentsPanelInterface panel = getSelectedTabPanel();
		if(panel == null)
			return null;
		
		return panel.getExportableTable();
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		MiradiTabContentsPanelInterface panel = getSelectedTabPanel();
		if(panel == null)
			return null;
		
		return (JComponent) panel.getComponent();
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
			setTab(getViewData().getCurrentTab());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		
	}

	void handleRightClick(MouseEvent event)
	{
		int tab = tabs.indexAtLocation(event.getX(), event.getY());
		if(tab < 0)
			return;
		
		JPopupMenu menu = getTabPopupMenu();
		if(menu == null)
			return;
		
		getMainWindow().updateActionsAndStatusBar();
		menu.show(tabs, event.getX(), event.getY());
	}
	
	public void prepareForTabSwitch()
	{
		ObjectDataInputField.saveFocusedFieldPendingEdits();
		closeActivePropertiesDialog();
	}

	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			prepareForTabSwitch();
			int newTab = tabs.getSelectedIndex();
			if(!ignoreTabChanges)
				recordTabChangeCommand(newTab);

			currentTab = newTab;
			if(!ignoreTabChanges)
				tabWasSelected();
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
	
	public class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}

		public void mouseReleased(MouseEvent event)
		{
			if(event.isPopupTrigger())
				doRightClickMenu(event);
		}
		
		private void doRightClickMenu(MouseEvent event)
		{
			handleRightClick(event);
		}
	}

	private JTabbedPane tabs;
	private int currentTab;
	private boolean ignoreTabChanges;
	private HashMap<String, MiradiTabContentsPanelInterface> tabPanels;
}
