/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.treetables;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JTable;

import org.miradi.main.MainWindow;
import org.miradi.utils.RowHeightListener;
import org.miradi.utils.TableRowHeightSaver;
import org.miradi.utils.TableWithRowHeightManagement;

import com.java.sun.jtreetable.TreeTableModel;

abstract public class TreeTableWithRowHeightSaver extends PanelTreeTable implements TableWithRowHeightManagement
{
	public TreeTableWithRowHeightSaver(MainWindow mainWindowToUse, TreeTableModel treeTableModel)
	{
		super(mainWindowToUse, treeTableModel);

		rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(getMainWindow(), this, getUniqueTableIdentifier());
		
		addComponentListener(new ComponentEventHandler());
	}

	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public void rebuildTableCompletely() throws Exception
	{
		super.rebuildTableCompletely();
		adjustRowHeights();

	}

	public void addRowHeightListener(RowHeightListener listener)
	{
		rowHeightSaver.addRowHeightListener(listener);
	}
	
	@Override
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(getMainWindow() == null)
			return;
		
		if(getMainWindow().isRowHeightModeAutomatic())
			return;
		
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(rowHeight);
	}
	
	@Override
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(row, rowHeight);
	}
	
	public JTable asTable()
	{
		return this;
	}
	
	private void adjustRowHeights()
	{
		if(getMainWindow().isRowHeightModeManual())
			return;
		
		for(int row = 0; row < getRowCount(); ++row)
		{
			int height = getPreferredRowHeight(row);
			setRowHeight(row, height);
		}
		getTree().setRowHeight(-1);
	}

	class ComponentEventHandler implements ComponentListener
	{
		public void componentShown(ComponentEvent e)
		{
			adjustRowHeights();
		}
		
		public void componentHidden(ComponentEvent e)
		{
		}

		public void componentMoved(ComponentEvent e)
		{
		}

		public void componentResized(ComponentEvent e)
		{
			adjustRowHeights();
		}

	}
	
	abstract public String getUniqueTableIdentifier();
	
	private TableRowHeightSaver rowHeightSaver;
}
