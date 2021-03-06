/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.questions.ChoiceItem;

public class MultiTableModel extends AbstractTableModel implements ChoiceItemTableModel 
{
	public MultiTableModel(String subViewModelIdentifierToUse)
	{
		subViewModelIdentifier = subViewModelIdentifierToUse;
		models = new Vector<ChoiceItemTableModel>();
		modelListenerMap = new HashMap<ChoiceItemTableModel, TableModelListener>();
	}
	
	public void removeAllModels()
	{
		for (Map.Entry<ChoiceItemTableModel, TableModelListener> modelToRemove: modelListenerMap.entrySet())
		{
			modelToRemove.getKey().removeTableModelListener(modelToRemove.getValue());
		}

		modelListenerMap.clear();
		models.clear();
	}

	public void addModel(ChoiceItemTableModel modelToAdd)
	{
		models.add(modelToAdd);
		EventPropagator eventPropagator = new EventPropagator();
		modelToAdd.addTableModelListener(eventPropagator);
		modelListenerMap.put(modelToAdd, eventPropagator);

		fireTableStructureChanged();
	}
	
	class EventPropagator implements TableModelListener
	{
		public void tableChanged(TableModelEvent e)
		{
			fireTableChanged(e);
		}
	}
	
	public void updateColumnsToShow() throws Exception
	{
		for(ChoiceItemTableModel model : models)
		{
			model.updateColumnsToShow();
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
		return findModel(column).getChoiceItemAt(row, findColumnWithinSubTable(column));
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return findModel(columnIndex).getColumnClass(findColumnWithinSubTable(columnIndex));
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return findModel(columnIndex).getColumnName(findColumnWithinSubTable(columnIndex));
	}
	
	public String getColumnGroupCode(int modelColumn)
	{
		return findModel(modelColumn).getColumnGroupCode(findColumnWithinSubTable(modelColumn));
	}
	
	public String getTagForCell(int objectType, int modelColumn)
	{
		return findModel(modelColumn).getTagForCell(objectType, findColumnWithinSubTable(modelColumn));
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return findModel(columnIndex).isCellEditable(rowIndex, findColumnWithinSubTable(columnIndex));
	}
		
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
		findModel(columnIndex).setValueAt(value, rowIndex, findColumnWithinSubTable(columnIndex));
	}

	protected ChoiceItemTableModel findModel(int column)
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
	
	public int findColumnWithinSubTable(int column)
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

	public String getUniqueTableModelIdentifier()
	{
		return "MultiTableModel" + subViewModelIdentifier;
	}
	
	private Vector<ChoiceItemTableModel> models;
	private HashMap<ChoiceItemTableModel, TableModelListener> modelListenerMap;
	private String subViewModelIdentifier;
}
