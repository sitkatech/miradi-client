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

import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.CodeListRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.ColumnSortListener;
import org.miradi.utils.SortableTable;
import org.miradi.utils.UiTableWithAlternatingRows;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class ObjectTable extends UiTableWithAlternatingRows implements ObjectPicker, RowColumnBaseObjectProvider, SortableTable
{
	public ObjectTable(MainWindow mainWindowToUse, ObjectTableModel modelToUse, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, modelToUse, uniqueTableIdentifierToUse);
		selectionListeners = new Vector();
		currentSortColumn = -1;
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JTableHeader columnHeader = getTableHeader();
		columnHeader.setReorderingAllowed(true);
		ColumnSortListener sortListener = new ColumnSortListener(this);
		columnHeader.addMouseListener(sortListener);
		resizeTable(4);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		
		DefaultFontProvider fontProvider = new DefaultFontProvider(getMainWindow());
		statusQuestionRenderer = new ChoiceItemTableCellRendererFactory(this, fontProvider);
		otherRenderer = new MultiLineObjectTableCellRendererFactory(this, fontProvider);
		codeListRenderer = new CodeListRendererFactory(mainWindowToUse, this, fontProvider);
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		if (getObjectTableModel().isCodeListColumn(modelColumn))
		{
			codeListRenderer.setQuestion(getObjectTableModel().getColumnQuestion(modelColumn));
			return codeListRenderer;
		}
		
		if (getObjectTableModel().isChoiceItemColumn(modelColumn))
		{
			return statusQuestionRenderer;
		}
	
		return otherRenderer;
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	public void scrollToAndSelectRow(int row)
	{
		if(row < 0 || row >= getRowCount())
			return;
		
		Rectangle rect = getCellRect(row, 0, true);
		scrollRectToVisible(rect);
		setRowSelectionInterval(row, row);
	}

	public void setSelectedRow(ORef ref)
	{
		int rowToSelect = getObjectTableModel().findRowObject(ref.getObjectId());
		scrollToAndSelectRow(rowToSelect);
	}
	
	public ObjectTableModel getObjectTableModel()
	{
		return (ObjectTableModel)getModel();
	}
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}
	
	public BaseObject[] getSelectedObjects()
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		Vector<BaseObject> selectedObjects = new Vector();
		for (int i = 0; i < selectedHierarchies.length; ++i)
		{
			ORefList thisSelectionHierarchy = selectedHierarchies[i];
			if (thisSelectionHierarchy.size() == 0)
				continue;
			
			BaseObject foundObject = getProject().findObject(thisSelectionHierarchy.get(0));
			selectedObjects.add(foundObject);		
		}
		
		return selectedObjects.toArray(new BaseObject[0]);
	}
	
	public ORefList getSelectionHierarchy()
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		if(selectedHierarchies.length == 0)
			return new ORefList();
		return selectedHierarchies[0];
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		int[] rows = getSelectedRows();
		ORefList[] selectedHierarchies = new ORefList[rows.length];
		for(int i = 0; i < rows.length; ++i)
		{
			BaseObject objectFromRow = getObjectFromRow(rows[i]);
			ORefList selectedObjectRefs = new ORefList();
			if (objectFromRow != null)
				selectedObjectRefs.add(objectFromRow.getRef());
			
			selectedHierarchies[i] = selectedObjectRefs;
		}
		
		return selectedHierarchies;
	}

	public Project getProject()
	{
		return getObjectTableModel().getProject();
	}
	
	public int getProportionShares(int row)
	{
		return 1;
	}
	
	public boolean areBudgetValuesAllocated(int row)
	{
		return false;
	}

	public void ensureObjectVisible(ORef ref)
	{
		setSelectedRow(ref);
	}

	public void addSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}
	
	public void expandAll(ViewData viewData) throws Exception
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

	private BaseObject getObjectFromRow(int row)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	int findRowObject(BaseId id)
	{
		return getObjectTableModel().findRowObject(id);
	}
	
	public void sort(int sortByTableColumn) 
	{
		Comparator comparator = getComparator(sortByTableColumn);
		Vector rows = new Vector();
		for(int i = 0; i < getRowCount(); ++i)
			rows.add(new Integer(i));

		Vector unsortedRows = (Vector)rows.clone();
		Collections.sort(rows, comparator);
		
		if (sortByTableColumn == currentSortColumn && rows.equals(unsortedRows))
			Collections.reverse(rows);

		getObjectTableModel().setNewRowOrder(((Integer[])rows.toArray(new Integer[0])));
		
		// TODO: Should memorize sort order for each table
		currentSortColumn = sortByTableColumn;
		
		revalidate();
		repaint();
	}

	protected Comparator getComparator(int sortByTableColumn)
	{
		int sortByModelColumn = convertColumnIndexToModel(sortByTableColumn);
		return getObjectTableModel().createComparator(sortByModelColumn);
	}
	
	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	public void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateTableAfterUndo(CommandSetObjectData cmd)
	{
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateIfRowObjectWasModified(int type, BaseId id)
	{
		if(type != getObjectTableModel().getRowObjectType())
			return;
		
		int row = findRowObject(id);
		if(row >= 0)
			getObjectTableModel().fireTableRowsUpdated(row, row);
	}
	
	void updateTableAfterObjectCreated(ORef createdRef)
	{
		
	}
	
	void updateTableAfterObjectDeleted(ORef deletedRef)
	{
		
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		super.valueChanged(e);
		if(selectionListeners == null)
			return;
		
		for(int i = 0; i < selectionListeners.size(); ++i)
		{
			ListSelectionListener listener = (ListSelectionListener)selectionListeners.get(i);
			listener.valueChanged(null);
		}
	}

	private Vector selectionListeners;
	private int currentSortColumn;
	private ChoiceItemTableCellRendererFactory statusQuestionRenderer;
	private BasicTableCellRendererFactory otherRenderer;
	private CodeListRendererFactory codeListRenderer;
	private boolean isActive;
}
