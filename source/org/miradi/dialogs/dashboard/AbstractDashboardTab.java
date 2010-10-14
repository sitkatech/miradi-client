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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class AbstractDashboardTab extends ObjectDataInputPanel
{
	public AbstractDashboardTab(Project projectToUse) throws Exception
	{
		super(projectToUse, Dashboard.getObjectType());

		setLayout(new BorderLayout());
		splitPane = new PersistentHorizontalSplitPane(getMainWindow(), getMainWindow(), getPanelDescription());
		selectableComponentToContentsFileNameMap = new LinkedHashMap<SelectableRow, String>();

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

	protected void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightPanelHtmlFileName)
	{
		String rightColumnTranslatedText = "";
		createSubHeaderRow(leftMainPanel, leftColumnTranslatedText, rightPanelHtmlFileName, rightColumnTranslatedText);
	}

	protected void createSubHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String rightPanelHtmlFileName)
	{
		Box firstColumnBox = createBorderedBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		SelectableRow selectableRow = createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, rightPanelHtmlFileName, firstColumnBox);
		final Color HEADER_BACKGROUND_COLOR = Color.GREEN.darker();
		selectableRow.setBackgroundColor(HEADER_BACKGROUND_COLOR);
	}
	
	protected SelectableRow createDataRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		Box firstColumnBox = createBorderedBox();
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		firstColumnBox.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		
		return createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox);
	}
	
	protected SelectableRow createHeaderRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName)
	{
		Box firstColumnBox = createBorderedBox();
		
		return createRow(leftMainPanel, leftColumnTranslatedText, rightColumnTranslatedText, descriptionFileName, firstColumnBox);
	}
	
	private SelectableRow createRow(TwoColumnPanel leftMainPanel, String leftColumnTranslatedText, String rightColumnTranslatedText, String descriptionFileName, Box firstColumnBox)
	{
		firstColumnBox.add(new PanelTitleLabel(leftColumnTranslatedText), BorderLayout.BEFORE_FIRST_LINE);
		
		Box secondColumnBox = createBorderedBox();
		secondColumnBox.add(new PanelTitleLabel(rightColumnTranslatedText));
		
		SelectableRow selectableRow = new SelectableRow(firstColumnBox, secondColumnBox);
		selectableComponentToContentsFileNameMap.put(selectableRow, descriptionFileName);
		
		leftMainPanel.add(firstColumnBox);
		leftMainPanel.add(secondColumnBox);
		
		return selectableRow;
	}

	private Box createBorderedBox()
	{
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createEtchedBorder());
		
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
	
	protected void clearSelection() throws Exception
	{
		Set<SelectableRow> selectableRows = selectableComponentToContentsFileNameMap.keySet();
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
			Set<SelectableRow> rowsSet = selectableComponentToContentsFileNameMap.keySet();
			SelectableRow currentlySelected = findSelectedRow();
			Vector<SelectableRow> rows = new Vector<SelectableRow>(rowsSet);
			if(currentlySelected == null)
			{
				rows.firstElement().selectRow();
				return;
			}
			
			SelectableRow rowToSelect = null;
			int indexOfSelectedRow = rows.indexOf(currentlySelected);
			int indexToSelect = 0;
			indexToSelect = indexOfSelectedRow + directionDelta;
			if (indexToSelect >= rows.size())
				indexToSelect = 0;

			 if (indexToSelect < 0 )
				indexToSelect = getLastRowIndex(rows);
			
			rowToSelect = rows.get(indexToSelect);
			clearSelection();
			rowToSelect.selectRow();
		}

		private int getLastRowIndex(Vector<SelectableRow> rows)
		{
			return rows.size() - 1;
		}

		private SelectableRow findSelectedRow()
		{
			Set<SelectableRow> rows = selectableComponentToContentsFileNameMap.keySet();
			for(SelectableRow selectableRow : rows)
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
				
				String resourceFileName = selectableComponentToContentsFileNameMap.get(selectableComponent);
				rightSideDescriptionPanel.setRightSidePanelContent(resourceFileName);
			}
			catch(Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}
	
		private void clearSelection() throws Exception
		{
			Set<SelectableRow> selectableRows = selectableComponentToContentsFileNameMap.keySet();
			for(SelectableRow selectableRow : selectableRows)
			{
				selectableRow.clearSelection();
			}
		}
		
		private SelectableRow selectableComponent;
	}
	
	public class SelectableRow
	{
		private SelectableRow(JComponent leftSideToUse, JComponent rightSideToUse)
		{
			leftSide = leftSideToUse;
			rightSide = rightSideToUse;
			
			leftSide.addMouseListener(new ClickHandler(this));
			rightSide.addMouseListener(new ClickHandler(this));
		}
		
		private void selectRow()
		{
			isSelected = true;
			setSelectionBorder(leftSide);
			setSelectionBorder(rightSide);
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
			component.setBorder(BorderFactory.createEtchedBorder(getSelectionColor(), getSelectionColor()));
		}

		private Color getSelectionColor()
		{
			return Color.BLUE;
		}
		
		private void clearSelection()
		{
			unSelect();
			leftSide.setBorder(BorderFactory.createEtchedBorder());
			rightSide.setBorder(BorderFactory.createEtchedBorder());
		}
		
		private void setBackgroundColor(Color backgroundColor)
		{
			setBackgroundColor(leftSide, backgroundColor);
			setBackgroundColor(rightSide, backgroundColor);
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
	}

	abstract protected String getMainDescriptionFileName();

	abstract protected TwoColumnPanel createLeftPanel();

	private LinkedHashMap<SelectableRow, String> selectableComponentToContentsFileNameMap;
	private DashboardRightSideDescriptionPanel rightSideDescriptionPanel;
	private PersistentHorizontalSplitPane splitPane;
	private KeyDispatcher dispatcher;
	private static final int INDENT_PER_LEVEL = 20;
	private static final int MOVE_UP_DIRECTION_DELTA = -1;
	private static final int MOVE_DOWN_DIRECTION_DELTA = 1;
}
