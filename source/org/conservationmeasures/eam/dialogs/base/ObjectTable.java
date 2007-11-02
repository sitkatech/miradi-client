/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.utils.UiTableWithAlternatingRows;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

abstract public class ObjectTable extends UiTableWithAlternatingRows implements ObjectPicker
{
	public ObjectTable(ObjectTableModel modelToUse)
	{
		super(modelToUse);
		selectionListeners = new Vector();
		currentSortColumn = -1;
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JTableHeader columnHeader = getTableHeader();
		columnHeader.setReorderingAllowed(true);
		ColumnSortListener sortListener = new ColumnSortListener(this);
		columnHeader.addMouseListener(sortListener);
		resizeTable(4);
		setAutoResizeMode(AUTO_RESIZE_OFF);
	}
	
	public void scrollToAndSelectRow(int row)
	{
		if (getRowCount() <= row)
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
		int[] rows = getSelectedRows();
		BaseObject[] objects = new BaseObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = getObjectFromRow(rows[i]);
		return objects;
	}
	
	public ORefList getSelectionHierarchy()
	{
		return new ORefList(getSelectedObjects());
	}

	public void ensureObjectVisible(ORef ref)
	{
		// TODO Auto-generated method stub
		// we should scroll the table as needed to make this 
		// probably-newly-created object visible
	}

	public void addSelectionChangeListener(SelectionChangeListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(SelectionChangeListener listener)
	{
		selectionListeners.remove(listener);
	}

	private BaseObject getObjectFromRow(int row)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	int findRowObject(BaseId id)
	{
		return getObjectTableModel().findRowObject(id);
	}
	
	public void sort(int sortColumn) 
	{
		Comparator comparator = new TableColumnComparator(this, sortColumn);
		Vector rows = new Vector();
		for(int i = 0; i < getRowCount(); ++i)
			rows.add(new Integer(i));

		Vector unsortedRows = (Vector)rows.clone();
		Collections.sort(rows, comparator);
		
		if (sortColumn == currentSortColumn && rows.equals(unsortedRows))
			Collections.reverse(rows);

		getObjectTableModel().setNewRowOrder(((Integer[])rows.toArray(new Integer[0])));
		
		// TODO: Should memorize sort order for each table
		currentSortColumn = sortColumn;
		
		revalidate();
		repaint();
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
			SelectionChangeListener listener = (SelectionChangeListener)selectionListeners.get(i);
			listener.selectionHasChanged();
		}
	}

	static class ColumnSortListener extends MouseAdapter
	{
		public ColumnSortListener(ObjectTable tableToManage)
		{
			table = tableToManage;
		}
		
		public void mouseClicked(MouseEvent e) 
		{
			int clickedColumn = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
			if (clickedColumn < 0)
				return;
			
			int sortColumn = ((JTableHeader)e.getSource()).getTable().convertColumnIndexToModel(clickedColumn);
			sortByTableColumn(sortColumn);
		}

		private void sortByTableColumn(int sortColumn)
		{
			table.sort(sortColumn);
		}

		ObjectTable table;
	}
	
	static class TableColumnComparator extends IgnoreCaseStringComparator
	{
		public TableColumnComparator(ObjectTable tableToUse, int columnToSort)
		{
			table = tableToUse;
			column = columnToSort;
		}
		
		public int compare(Object object1, Object object2)
		{
			Integer row1 = (Integer)object1;
			Integer row2 = (Integer)object2;
			String value1 = (String)table.getValueAt(row1.intValue(), column);
			String value2 = (String)table.getValueAt(row2.intValue(), column);
			return super.compare(value1, value2);
		}

		ObjectTable table;
		int column;
	}

	Vector selectionListeners;
	int currentSortColumn;
}
