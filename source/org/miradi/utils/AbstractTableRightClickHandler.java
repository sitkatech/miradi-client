/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.miradi.actions.Actions;
import org.miradi.main.MainWindow;

public abstract class AbstractTableRightClickHandler extends MouseAdapter
{
	public AbstractTableRightClickHandler(MainWindow mainWindowToUse, JTable tableToAutoSelectIn)
	{
		mainWindow = mainWindowToUse;
		table = tableToAutoSelectIn;
	}
	
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
	
	public void doRightClickMenu(MouseEvent event)
	{
		Point clickLocation  = event.getPoint();
		Component rawComponent = event.getComponent();
		if(rawComponent == table)
		{
			int rowToSelect = table.rowAtPoint(clickLocation);
			table.getSelectionModel().setSelectionInterval(rowToSelect, rowToSelect);
			
			int columnToSelect = table.columnAtPoint(clickLocation);
			table.getColumnModel().getSelectionModel().setSelectionInterval(columnToSelect, columnToSelect);
		}
		else
		{
			table.clearSelection();
		}

		JPopupMenu popupMenu = new JPopupMenu();
		populateMenu(popupMenu);
		popupMenu.show(rawComponent, (int)clickLocation.getX(), (int)clickLocation.getY());
	}
	
	abstract protected void populateMenu(JPopupMenu popupMenu);

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	public Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	public int getSelectedColumn()
	{
		return table.getSelectedColumn();
	}

	private MainWindow mainWindow;
	private JTable table;
}
