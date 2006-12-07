/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiTable;

public class ObjectTable extends UiTable implements ObjectPicker
{
	public ObjectTable(ObjectTableModel modelToUse)
	{
		super(modelToUse);
		currentSortColumn = -1;
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JTableHeader columnHeader = getTableHeader();
		columnHeader.setReorderingAllowed(true);
		ColumnSortListener sortListener = new ColumnSortListener(this);
		columnHeader.addMouseListener(sortListener);
		
		resizeTable(4);
	}
	
	public ObjectTableModel getObjectTableModel()
	{
		return (ObjectTableModel)getModel();
	}
	
	public EAMObject[] getSelectedObjects()
	{
		int[] rows = getSelectedRows();
		EAMObject[] objects = new EAMObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = getObjectFromRow(rows[i]);
		return objects;
	}

	private EAMObject getObjectFromRow(int row)
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

	int currentSortColumn;
}
