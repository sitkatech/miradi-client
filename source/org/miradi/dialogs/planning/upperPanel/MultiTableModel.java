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

package org.miradi.dialogs.planning.upperPanel;

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.questions.ChoiceItem;

public class MultiTableModel extends AbstractTableModel implements ChoiceItemTableModel 
{
	public MultiTableModel()
	{
		models = new Vector();
	}
	
	public void removeAllModels()
	{
		models.clear();
	}

	public void addModel(ChoiceItemTableModel modelToAdd)
	{
		models.add(modelToAdd);
		modelToAdd.addTableModelListener(new EventPropagator());
		fireTableStructureChanged();
	}
	
	class EventPropagator implements TableModelListener
	{
		public void tableChanged(TableModelEvent e)
		{
			fireTableChanged(e);
		}
	}
	
	public int getColumnCount()
	{
		int count = 0;
		for(TableModel model : models)
			count += model.getColumnCount();

		return count;
	}

	public int getRowCount()
	{
		if(models.size() == 0)
			return 0;
		
		return models.get(0).getRowCount();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return getChoiceItemAt(rowIndex, columnIndex);
	}

	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		return findTable(column).getChoiceItemAt(row, findColumnWithinSubTable(column));
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return findTable(columnIndex).getColumnClass(findColumnWithinSubTable(columnIndex));
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return findTable(columnIndex).getColumnName(findColumnWithinSubTable(columnIndex));
	}

	ChoiceItemTableModel findTable(int column)
	{
		int originalColumn = column;
		for(ChoiceItemTableModel model : models)
		{
			if(column < model.getColumnCount())
				return model;
			column -= model.getColumnCount();
		}
		throw new RuntimeException("MultiTable.findTable: Table column out of bounds: " + originalColumn);
	}
	
	int findColumnWithinSubTable(int column)
	{
		int originalColumn = column;
		for(TableModel model : models)
		{
			if(column < model.getColumnCount())
				return column;
			column -= model.getColumnCount();
		}
		
		throw new RuntimeException("MultiTable.findColumnWithinSubTable: Table column out of bounds: " + originalColumn);
	}

	private Vector<ChoiceItemTableModel> models;
}
