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

package org.miradi.dialogs.base;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.MouseClickRowEvent;
import org.miradi.dialogs.dashboard.MouseOverRowEvent;
import org.miradi.dialogs.dashboard.SelectableRow;
import org.miradi.main.EAM;

public class SingleRowSelectionHandler
{
	public SingleRowSelectionHandler()
	{
		dispatcher = new KeyDispatcher();
		selectableRows = getSafeSelectableRows();
		rowSelectionListeners = new Vector<ListSelectionListener>();
	}
	
	public void becomeActive()
	{
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(dispatcher);
	}
	
	public void becomeInactive()
	{
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.removeKeyEventDispatcher(dispatcher);

	}
	
	public void addSelectableRow(Vector<JComponent> selectableComponentsToUse, AbstractLongDescriptionProvider descriptionProviderToUse)
	{
		SelectableRow selectableRow = createSelectableRow(selectableComponentsToUse, descriptionProviderToUse);
		selectableRow.addMouseListener(new ClickHandler(selectableRow));
		getSafeSelectableRows().add(selectableRow);
	}

	private SelectableRow createSelectableRow(Vector<JComponent> selectableComponentsToUse,
			AbstractLongDescriptionProvider descriptionProviderToUse)
	{
		return new SelectableRow(selectableComponentsToUse, descriptionProviderToUse);
	}
	
	public void addSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		rowSelectionListeners.add(rightSideDescriptionPanel);
	}
	
	public void removeSelectionListener(ListSelectionListener rightSideDescriptionPanel)
	{
		rowSelectionListeners.remove(rightSideDescriptionPanel);
	}
	
	private void selectRow(SelectableRow rowToSelect) throws Exception
	{
		clearSelection();
		rowToSelect.selectRow();
	}
	
	private void clearSelection() throws Exception
	{
		for(SelectableRow selectableRow : selectableRows)
		{
			selectableRow.clearSelection();
		}
	}
	
	private void notifyListenersWithViewChangeEvent(SelectableRow selectedRow)
	{
		for (ListSelectionListener panel : rowSelectionListeners)
		{
			panel.valueChanged(new MouseClickRowEvent(panel, selectedRow.getDescriptionProvider()));
		}
	}
	
	private void notifyListenersWithoutViewChangeEvent(SelectableRow selectedRow)
	{
		for (ListSelectionListener panel : rowSelectionListeners)
		{
			panel.valueChanged(new MouseOverRowEvent(panel, selectedRow.getDescriptionProvider()));
		}
	}
	
	protected Vector<SelectableRow> getSafeSelectableRows()
	{
		if (selectableRows == null)
			selectableRows = new Vector<SelectableRow>();

		return selectableRows;
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
			notifyListenersWithViewChangeEvent(selectableComponent);
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			super.mouseEntered(e);
			
			select();
			notifyListenersWithoutViewChangeEvent(selectableComponent);
		}

		protected void select()
		{
			try
			{
				selectRow(selectableComponent);
			}
			catch(Exception exception)
			{
				EAM.logException(exception);
				EAM.unexpectedErrorDialog(exception);
			}
		}

		private SelectableRow selectableComponent;
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
	    				new KeyboardSingleRowSelectionHandler().selectUp();
	    			if (event.getKeyCode() == KeyEvent.VK_DOWN)
	    				new KeyboardSingleRowSelectionHandler().selectDown();
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
	
	private class KeyboardSingleRowSelectionHandler
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
			notifyListenersWithViewChangeEvent(rowToSelect);
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

	private static final int MOVE_UP_DIRECTION_DELTA = -1;
	private static final int MOVE_DOWN_DIRECTION_DELTA = 1;

	protected KeyDispatcher dispatcher;
	protected Vector<SelectableRow> selectableRows;
	private Vector<ListSelectionListener> rowSelectionListeners;
}
