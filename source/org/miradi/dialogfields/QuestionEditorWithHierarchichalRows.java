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

package org.miradi.dialogfields;

import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.dashboard.RowSelectionEvent;
import org.miradi.dialogs.dashboard.SelectableRow;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceItemWithChildren;
import org.miradi.questions.ChoiceQuestion;

public class QuestionEditorWithHierarchichalRows extends QuestionBasedEditorComponent
{
	public QuestionEditorWithHierarchichalRows(MainWindow mainWindowToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		mainWindow = mainWindowToUse;
		mainWindow = mainWindowToUse;
		dispatcher = new KeyDispatcher();
		selectableRows = getSafeSelectableRows();
		rowSelectionListeners = new Vector<ListSelectionListener>();
		setBackground(AppPreferences.getDataPanelBackgroundColor());
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
	
	protected void notifyListeners(SelectableRow selectedRow)
	{
		for (ListSelectionListener panel : rowSelectionListeners)
		{
			panel.valueChanged(new RowSelectionEvent(panel, selectedRow.getDescriptionProvider()));
		}
	}
	
	protected Vector<SelectableRow> getSafeSelectableRows()
	{
		if (selectableRows == null)
			selectableRows = new Vector<SelectableRow>();

		return selectableRows;
	}
	
	@Override
	protected MiradiPanel createRowPanel(ChoiceItem choiceItem)
	{
		TwoColumnPanel mainRowsPanel = new TwoColumnPanel();
		mainRowsPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		try
		{
			ChoiceItemWithChildren castedChoiceItem = (ChoiceItemWithChildren) choiceItem;
			createHeaderSelectableRow(mainRowsPanel, castedChoiceItem);

			addSubHeaderRows(mainRowsPanel, castedChoiceItem);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
		
		MiradiPanel wrapperPanel = new MiradiPanel();
		wrapperPanel.add(mainRowsPanel);
		return wrapperPanel;
	}

	protected void addSubHeaderRows(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren parentChoiceItem) throws Exception
	{
		Vector<ChoiceItem> children = parentChoiceItem.getChildren();
		for(ChoiceItem childChoiceItem : children)
		{
			ChoiceItemWithChildren thisChoiceItem = (ChoiceItemWithChildren) childChoiceItem;
			createSubHeaderSelectableRow(mainRowsPanel, thisChoiceItem);
			addLeafRows(mainRowsPanel, thisChoiceItem);
		}
	}

	private void addLeafRows(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren parentChoiceItem) throws Exception
	{
		Vector<ChoiceItem> children = parentChoiceItem.getChildren();
		for(ChoiceItem childChoiceItem : children)
		{
			ChoiceItemWithChildren thisChoiceItem = (ChoiceItemWithChildren) childChoiceItem;
			createLeafSelectableRow(mainRowsPanel, thisChoiceItem);
		}		
	}
	
	private void createLeafSelectableRow(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren choiceItem) throws Exception
	{
	 	final int LEAF_INDENT_COUNT = 2;
		
		createSelectableRow(mainRowsPanel, choiceItem, LEAF_INDENT_COUNT, getLeafTitleFont());
	}

	private void createSubHeaderSelectableRow(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren choiceItem) throws Exception
	{
		final int SUBHEADER_INDENT_COUNT = 1;
		
		createSelectableRow(mainRowsPanel, choiceItem, SUBHEADER_INDENT_COUNT, createSubHeaderFont());
	}

	private void createHeaderSelectableRow(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren choiceItem) throws Exception
	{
		final int HEADER_INDENT_COUNT = 0;
		
		createSelectableRow(mainRowsPanel, choiceItem, HEADER_INDENT_COUNT, createHeaderTitleFont());
	}

	private void createSelectableRow(TwoColumnPanel mainRowsPanel, ChoiceItemWithChildren choiceItem, final int indentCount, Font font)
	{
		JComponent leftComponent = createToggleButton(choiceItem);
		leftComponent.setFont(font);
		
		PanelTitleLabel rightComponent = new PanelTitleLabel(choiceItem.getRightLabel());
		rightComponent.setFont(font);
		
		Box box = createHorizontalBoxWithIndents(indentCount);
		box.add(leftComponent);
		mainRowsPanel.add(box);
		mainRowsPanel.add(rightComponent);
		
		SelectableRow selectableRow = new SelectableRow(leftComponent, rightComponent, choiceItem.getProvider());
		selectableRow.addMouseListener(new ClickHandler(selectableRow));
		getSafeSelectableRows().add(selectableRow);
	}
	
	private JComponent createToggleButton(ChoiceItemWithChildren choiceItem)
	{
		if (choiceItem.isSelectable())
			return createToggleButton(choiceItem.getLabel());
		
		return new PanelTitleLabel(choiceItem.getLabel());
	}

	protected Box createHorizontalBoxWithIndents(int indentCount)
	{
		Box box = Box.createHorizontalBox();
		for (int index = 0; index < indentCount; ++index)
		{
			box.add(Box.createHorizontalStrut(INDENT_PER_LEVEL));
		}
		
		return box;
	}
	
	private Font createHeaderTitleFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		font = font.deriveFont((float)(font.getSize() * 1.5));
		
		return font;
	}
	
	private Font createSubHeaderFont()
	{
		Font font = getRawFont();
		font = font.deriveFont(Font.BOLD);
		
		return font;
	}
	
	private Font getLeafTitleFont()
	{
		return getRawFont();
	}

	private Font getRawFont()
	{
		return new PanelTitleLabel().getFont();
	}
	
	protected String getMainDescriptionFileName()
	{
		return "dashboard/1.html";
	}
	
	protected void selectRow(SelectableRow rowToSelect) throws Exception
	{
		clearSelection();
		rowToSelect.selectRow();
		notifyListeners(rowToSelect);
	}
	
	private void clearSelection() throws Exception
	{
		for(SelectableRow selectableRow : selectableRows)
		{
			selectableRow.clearSelection();
		}
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
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
	
	protected class ClickHandler extends MouseAdapter
	{
		public ClickHandler(SelectableRow selectableComponentToUse)
		{
			selectableComponent = selectableComponentToUse;
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			select();
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			super.mouseEntered(e);
			
			select();
		}

		protected void select()
		{
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
	
	private MainWindow mainWindow;
	protected Vector<SelectableRow> selectableRows;
	protected KeyDispatcher dispatcher;
	private Vector<ListSelectionListener> rowSelectionListeners;
	
	protected static final int INDENT_PER_LEVEL = 25;
	private static final int MOVE_UP_DIRECTION_DELTA = -1;
	private static final int MOVE_DOWN_DIRECTION_DELTA = 1;
}
