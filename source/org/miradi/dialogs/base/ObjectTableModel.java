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

import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

import org.miradi.dialogs.threatrating.upperPanel.TableModelStringComparator;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.ColumnTagProvider;

abstract public class ObjectTableModel extends AbstractTableModel implements ColumnTagProvider
{
	public ObjectTableModel(Project projectToUse, int listedItemType, String[] tableColumnTags)
	{
		columnTags = tableColumnTags;
		project = projectToUse;
		rowObjectType = listedItemType;
	}
	
	public int getRowCount()
	{
		return getRowObjectRefs().size();
	}
	
	void setNewRowOrder(Integer[] existingRowIndexesInNewOrder)
	{
		ORefList newList = new ORefList();
		for(int i = 0; i < existingRowIndexesInNewOrder.length; ++i)
		{
			int nextExistingRowIndex = existingRowIndexesInNewOrder[i].intValue();
			newList.add(getRowObjectRefs().get(nextExistingRowIndex));
		}
		setRowObjectRefs(newList);
	}

	public void resetRows()
	{
		setRowObjectRefs(getLatestRefListFromProject());
	}
	
	public BaseObject getObjectFromRow(int row) throws RuntimeException
	{
		try
		{
			ORef rowObjectRef = getRowObjectRefs().get(row);
			BaseObject rowObject = project.findObject(rowObjectRef);
			if(rowObject == null)
				EAM.logWarning("ObjectTableModel.getObjectFromRow: Missing object: " + rowObjectRef);
			return rowObject;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException("TeamModel.getObjectFromRow error. row = " + row);
		}
	}
	
	public int findRowObject(BaseId id)
	{
		for(int row = 0; row < getRowCount(); ++row)
		{
			if(getObjectFromRow(row).getId().equals(id))
				return row;
		}
		
		return -1;
	}

	public int getRowObjectType()
	{
		return rowObjectType;
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			ORef rowObjectRef = getRowObjectRefs().get(row);
			String valueToDisplay = getValueToDisplay(rowObjectRef, getColumnTag(column));
			if (isCodeListColumn(column))
				return new CodeList(valueToDisplay);

			if (isChoiceItemColumn(column))
				return getChoiceItem(column, valueToDisplay);
			
			return valueToDisplay;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "(Error)";
		}
	}

	public String getValueToDisplay(ORef rowObjectRef, String tag)
	{
		return project.getObjectData(rowObjectRef, tag);
	}

	public void rowsWereAddedOrRemoved()
	{
		//NOTE: Assumes one row at a time insert or delete
		ORefList availableRefs = getLatestRefListFromProject();
		ORefList newList = new ORefList();
		int deletedRowIndex = 0;
		for(int row = 0; row < getRowObjectRefs().size(); ++row)
		{
			ORef thisRef = getRowObjectRefs().get(row);
			if(availableRefs.contains(thisRef))
			{
				newList.add(thisRef);
				availableRefs.remove(thisRef);
			}
			else
			{
				deletedRowIndex = row;
			}
		}
		for(int i = 0; i < availableRefs.size(); ++i)
		{
			newList.add(availableRefs.get(i));
		}
		
		int priorCount = getRowObjectRefs().size();
		setRowObjectRefs(newList);
		
		if (newList.size() > priorCount)
			fireTableRowsInserted(newList.size()-1, newList.size()-1);
		else if (newList.size() < priorCount)
			fireTableRowsDeleted(deletedRowIndex, deletedRowIndex);
	}

	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, getColumnTag(column));
	}
	
	public Project getProject()
	{
		return project;
	}

	public void setRowObjectRefs(ORefList rowObjectRefsToUse)
	{
		rowObjectRefs = rowObjectRefsToUse;
	}

	private ORefList getRowObjectRefs()
	{
		if (rowObjectRefs == null)
			resetRows();
			
		return rowObjectRefs;
	}

	public boolean isChoiceItemColumn(int column)
	{
		return getColumnQuestion(column) != null;
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		return null;
	}
	
	public boolean isCodeListColumn(int column)
	{
		return false;
	}
	
	public ChoiceItem getChoiceItem(int column, String dataToDisplay)
	{
		return getColumnQuestion(column).findChoiceByCode(dataToDisplay);
	}
		
	protected Comparator createComparator(int sortColumn)
	{
		return new TableModelStringComparator(this, sortColumn);
	}
	
	abstract public ORefList getLatestRefListFromProject();
	
	protected Project project;
	private int rowObjectType;
	private ORefList rowObjectRefs;
	private String[] columnTags;
}
