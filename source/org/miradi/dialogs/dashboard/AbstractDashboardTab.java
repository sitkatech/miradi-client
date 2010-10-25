/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
 */

package org.miradi.dialogs.dashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.ViewSwitchDoer;

abstract public class AbstractDashboardTab extends ObjectDataInputPanel
{
	public AbstractDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse, Dashboard.getObjectType());

		setLayout(new BorderLayout());
		splitPane = new PersistentHorizontalSplitPane(getMainWindow(), getMainWindow(), getPanelDescription());
		selectableRows = new Vector<SelectableRow>();

		TwoColumnPanel leftPanel = createLeftPanel();
		dispatcher = new KeyDispatcher();
		addLeftPanel(leftPanel);

		createRightPanel();
		addRightPanel();
		add(splitPane);
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(dispatcher);
	}
	
	@Override
	public void becomeInactive()
	{
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.removeKeyEventDispatcher(dispatcher);

		super.becomeInactive();
	}

	private void addRightPanel() throws Exception
	{
		splitPane.setRightComponent(rightSideDescriptionPanel);
	}
	
	protected void addLeftPanel(TwoColumnPanel leftMainPanel)
	{
		splitPane.setLeftComponent(leftMainPanel);
	}
	
	protected void createRightPanel() throws Exception
	{
		rightSideDescriptionPanel = new DashboardRightSideDescriptionPanel(getMainWindow());
		rightSideDescriptionPanel.setRightSidePanelContent(getMainDescriptionFileName());
	}

	protected void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightPanelHtmlFileName, String viewName)
	{
		String rightColumnTranslatedText = "";
		createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, viewName);
	}

	protected void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String rightPanelHtmlFileName, String viewName)
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, firstColumnBox, viewName);
	}
	
	protected SelectableRow createDataRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String viewName)
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox, viewName);
	}
	
	protected SelectableRow createHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String viewName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		leftLabel.setFont(font);

		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		Box firstColumnBox = createBox();
		return createRow(leftMainPanel, descriptionFileName, firstColumnBox, viewName, leftLabel, rightLabel);
	}
	
	private SelectableRow createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String viewName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		leftLabel.setFont(font);
		
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(leftMainPanel, descriptionFileName, firstColumnBox, viewName, leftLabel, rightLabel);
	}
	
	private SelectableRow createRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String viewName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(leftMainPanel, descriptionFileName, firstColumnBox, viewName, leftLabel, rightLabel);
	}

	private SelectableRow createRow(TwoColumnPanel leftMainPanel,	String descriptionFileName, Box firstColumnBox, String viewName, PanelTitleLabel leftLabel, PanelTitleLabel rightLabel)
	{
		firstColumnBox.add(leftLabel, BorderLayout.BEFORE_FIRST_LINE);
		
		Box secondColumnBox = createBox();
		
		secondColumnBox.add(rightLabel);
		
		SelectableRow selectableRow = new SelectableRow(firstColumnBox, secondColumnBox, descriptionFileName, viewName);
		selectableRows.add(selectableRow);
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
		
		return selectableRow;
	}

	private Box createBox()
	{
		Box box = Box.createHorizontalBox();
		
		return box;
	}
	
	protected String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}
	
	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}
	
	private void clearSelection() throws Exception
	{
		for(SelectableRow selectableRow : selectableRows)
		{
			selectableRow.clearSelection();
		}
	}

	private class SingleRowSelectionHandler
	{
		private void selectUp() throws Exception
		{
			select(MOVE_UP_DIRECTION_DELTA);
		}
		
		private void selectDown() throws Exception
		{
			select(MOVE_DOWN_DIRECTION_DELTA);
		}

		private void select(int directionDelta) throws Exception
		{
			SelectableRow currentlySelected = findSelectedRow();
			if(currentlySelected == null)
			{
				selectableRows.firstElement().selectRow();
				return;
			}
			
			SelectableRow rowToSelect = null;
			int indexOfSelectedRow = selectableRows.indexOf(currentlySelected);
			int indexToSelect = indexOfSelectedRow + directionDelta;
			
			indexToSelect = (indexToSelect + selectableRows.size()) % selectableRows.size();
			rowToSelect = selectableRows.get(indexToSelect);
			clearSelection();
			rowToSelect.selectRow();
		}

		private SelectableRow findSelectedRow()
		{
			for(SelectableRow selectableRow : selectableRows)
			{
				if(selectableRow.isSelected())
					return selectableRow;
			}

			return null;
		}
	}
	
	private class KeyDispatcher implements KeyEventDispatcher 
	{
	    public boolean dispatchKeyEvent(KeyEvent event) 
	    {
	    	try
	    	{
	    		if (event.getID() == KeyEvent.KEY_PRESSED) 
	    		{
	    			if (event.getKeyCode() == KeyEvent.VK_UP)
	    				new SingleRowSelectionHandler().selectUp();
	    			if (event.getKeyCode() == KeyEvent.VK_DOWN)
	    				new SingleRowSelectionHandler().selectDown();
	    		}
	    	}
	    	catch (Exception e)
	    	{
	    		EAM.logException(e);
	    		EAM.unexpectedErrorDialog(e);
	    	}
	    	
	    	//TODO:  is this used and what is the correct value to return
	    	return false;
	    }
	}

	private class ClickHandler extends MouseAdapter
	{
		public ClickHandler(SelectableRow selectableComponentToUse)
		{
			selectableComponent = selectableComponentToUse;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			try
			{
				clearSelection();
				selectableComponent.selectRow();
				ViewSwitchDoer.changeView(getMainWindow(), selectableComponent.getViewName());
			}
			catch(Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}
		
		private SelectableRow selectableComponent;
	}
	
	public class SelectableRow
	{
		private SelectableRow(JComponent leftSideToUse, JComponent rightSideToUse, String descriptionFileNameToUse, String viewNameToUse)
		{
			leftSide = leftSideToUse;
			rightSide = rightSideToUse;
			descriptionFileName = descriptionFileNameToUse;
			viewName = viewNameToUse;
			
			leftSide.addMouseListener(new ClickHandler(this));
			rightSide.addMouseListener(new ClickHandler(this));
		}
		
		public String getViewName()
		{
			return viewName;
		}
		
		private void selectRow() throws Exception
		{
			isSelected = true;
			setSelectionBorder(leftSide);
			setSelectionBorder(rightSide);
			rightSideDescriptionPanel.setRightSidePanelContent(descriptionFileName);
		}
		
		private void unSelect()
		{
			isSelected = false;
		}

		public boolean isSelected()
		{
			return isSelected;
		}

		private void setSelectionBorder(JComponent component)
		{
			setBackgroundColor(component, AppPreferences.getWizardBackgroundColor());
		}

		private void clearSelection()
		{
			unSelect();
			setBackgroundColor(leftSide, new JLabel().getBackground());
			setBackgroundColor(rightSide, new JLabel().getBackground());
		}
		
		private void setBackgroundColor(JComponent component, Color backgroundColor)
		{
			component.setOpaque(true);
			component.setBackground(backgroundColor);
		}

		@Override
		public int hashCode()
		{
			return leftSide.hashCode() + rightSide.hashCode();
		}

		@Override
		public boolean equals(Object rawObjet)
		{
			if(!(rawObjet instanceof SelectableRow))
				return false;

			SelectableRow selectableRow = (SelectableRow) rawObjet;
			if(!selectableRow.leftSide.equals(leftSide))
				return false;

			return selectableRow.rightSide.equals(rightSide);
		}

		private JComponent leftSide;
		private JComponent rightSide;
		private boolean isSelected;
		private String descriptionFileName;
		private String viewName;
	}

	abstract protected String getMainDescriptionFileName();

	abstract protected TwoColumnPanel createLeftPanel();

	private Vector<SelectableRow> selectableRows;
	private DashboardRightSideDescriptionPanel rightSideDescriptionPanel;
	private PersistentHorizontalSplitPane splitPane;
	private KeyDispatcher dispatcher;
	private static final int INDENT_PER_LEVEL = 20;
	private static final int MOVE_UP_DIRECTION_DELTA = -1;
	private static final int MOVE_DOWN_DIRECTION_DELTA = 1;
}
