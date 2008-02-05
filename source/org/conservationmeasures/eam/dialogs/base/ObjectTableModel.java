/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

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
			throw new RuntimeException("TeamModel.getObjectFromRow error");
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

	void setRowObjectRefs(ORefList rowObjectRefsToUse)
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

	abstract public ORefList getLatestRefListFromProject();
	
	protected Project project;
	private int rowObjectType;
	private ORefList rowObjectRefs;
	private String[] columnTags;
}
