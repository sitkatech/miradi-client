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
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.summary.SummaryView;

abstract public class LeftSidePanelWithSelectableRows extends TwoColumnPanel
{
	public LeftSidePanelWithSelectableRows(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		dispatcher = new KeyDispatcher();
		selectableRows = new Vector<SelectableRow>();
		rowSelectionListeners = new Vector<ListSelectionListener>();
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

	public void addSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		rowSelectionListeners.add(rightSideDescriptionPanel);
	}
	
	public void removeSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		rowSelectionListeners.remove(rightSideDescriptionPanel);
	}
	
	private void notifyListeners(SelectableRow selectedRow)
	{
		for (ListSelectionListener panel : rowSelectionListeners)
		{
			panel.valueChanged(new RowSelectionEvent(panel, selectedRow.getDescriptionFileName(), selectedRow.getWizardStepName()));
		}
	}
	
	protected void createSubHeaderRow(String leftColumnTranslatedText, String rightPanelHtmlFileName, String wizardStepName)
	{
		String rightColumnTranslatedText = "";
		createSubHeaderRow(leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, wizardStepName);
	}

	protected void createSubHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String rightPanelHtmlFileName, String wizardStepName)
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		createSubHeaderRow(leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, firstColumnBox, wizardStepName);
	}
	
	protected SelectableRow createDataRow( String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String wizardStepName)
	{
		Box firstColumnBox = createBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox, wizardStepName);
	}
	
	protected SelectableRow createHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, String wizardStepName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		leftLabel.setFont(font);

		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		Box firstColumnBox = createBox();
		SelectableRow headerRow = createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
		notifyListeners(headerRow);
		
		return headerRow;
	}
	
	private SelectableRow createSubHeaderRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String wizardStepName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		Font font = leftLabel.getFont();
		font = font.deriveFont(Font.BOLD);
		leftLabel.setFont(font);
		
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
	}
	
	private SelectableRow createRow(String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox, String wizardStepName)
	{
		PanelTitleLabel leftLabel = new PanelTitleLabel(leftColumnTranslatedText);
		PanelTitleLabel rightLabel = new PanelTitleLabel(rightColumnTranslatedText);
		return createRow(descriptionFileName, firstColumnBox, wizardStepName, leftLabel, rightLabel);
	}

	private SelectableRow createRow(String descriptionFileName, Box firstColumnBox, String wizardStepName, PanelTitleLabel leftLabel, PanelTitleLabel rightLabel)
	{
		firstColumnBox.add(leftLabel, BorderLayout.BEFORE_FIRST_LINE);
		
		Box secondColumnBox = createBox();
		
		secondColumnBox.add(rightLabel);
		
		SelectableRow selectableRow = new SelectableRow(firstColumnBox, secondColumnBox, descriptionFileName, wizardStepName);
		selectableRow.addMouseListener(new ClickHandler(selectableRow));
		selectableRows.add(selectableRow);
		
		add(firstColumnBox);
		add(secondColumnBox);
		
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
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	protected String getDiagramOverviewStepName()
	{
		return getMainWindow().getWizardManager().getOverviewStepName(DiagramView.getViewName());
	}
	
	protected String getSummaryOverviewStepName()
	{
		return getMainWindow().getWizardManager().getOverviewStepName(SummaryView.getViewName());
	}
	
	protected void selectRow(SelectableRow rowToSelect) throws Exception
	{
		clearSelection();
		rowToSelect.selectRow();
		notifyListeners(rowToSelect);
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
			selectRow(rowToSelect);
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
				selectRow(selectableComponent);
				notifyListeners(selectableComponent);
			}
			catch(Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}

		private SelectableRow selectableComponent;
	}
	
	abstract protected String getMainDescriptionFileName();
	
	private MainWindow mainWindow;
	private Vector<SelectableRow> selectableRows;
	private KeyDispatcher dispatcher;
	private Vector<ListSelectionListener> rowSelectionListeners;
	
	private static final int INDENT_PER_LEVEL = 25;
	private static final int MOVE_UP_DIRECTION_DELTA = -1;
	private static final int MOVE_DOWN_DIRECTION_DELTA = 1;
}
