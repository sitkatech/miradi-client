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
package org.miradi.dialogs.base;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.MultiTableHorizontalScrollController;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableRowSortController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.utils.TableWithRowHeightSaver;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class MultiTablePanel extends DisposablePanel implements ObjectPicker
{
	public MultiTablePanel(MainWindow mainWindowToUse)
	{
		super(new BorderLayout());
		
		mainWindow = mainWindowToUse;
		selectionController = new MultipleTableSelectionController();
		verticalController = new MultiTableVerticalScrollController();
		horizontalController = new MultiTableHorizontalScrollController();
		rowHeightController = new MultiTableRowHeightController(getMainWindow());
		rowSortController = new MultiTableRowSortController();
		
		setBackground(mainWindowToUse.getAppPreferences().getDataPanelBackgroundColor());
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	protected void addRowSortControlledTable(TableWithRowHeightSaver tableToSort)
	{
		rowSortController.addTableToSort(tableToSort);
	}
	
	protected void addRowHeightControlledTable(TableWithRowHeightSaver tableToAdd)
	{
		rowHeightController.addTable(tableToAdd);
	}
	
	protected void addToVerticalController(JScrollPane scroller)
	{
		verticalController.addScrollPane(scroller);
	}
	
	protected void addToHorizontalController(JScrollPane scroller)
	{
		horizontalController.addScrollPane(scroller);
	}
	
	public BaseObject[] getSelectedObjects()
	{
		return null;
	}

	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}

	public ORefList getSelectionHierarchy()
	{
		return null;
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
	}
	
	public void addSelectionChangeListener(ListSelectionListener listener)
	{
	}
	
	public void expandAll() throws Exception
	{
	}
	
	public void collapseAll(ViewData viewData) throws Exception
	{	
	}

	public boolean isActive()
	{
		return isActive;
	}
	
	public void becomeActive()
	{
		isActive = true;
	}

	public void becomeInactive()
	{
		isActive = false;
	}

	public void clearSelection()
	{
	}

	public void ensureObjectVisible(ORef ref)
	{
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
	}
	
	public final static int ABOUT_ONE_LINE = 20;
	public final static int ROW_HEIGHT = 2 * ABOUT_ONE_LINE;


	private MainWindow mainWindow;
	protected MultipleTableSelectionController selectionController;
	protected MultiTableVerticalScrollController verticalController;
	protected MultiTableHorizontalScrollController horizontalController;
	private MultiTableRowHeightController rowHeightController;
	private MultiTableRowSortController rowSortController;
	private boolean isActive;
}
