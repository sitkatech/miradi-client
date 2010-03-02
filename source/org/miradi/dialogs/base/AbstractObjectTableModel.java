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

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.threatrating.upperPanel.TableModelChoiceItemComparator;
import org.miradi.dialogs.threatrating.upperPanel.TableModelStringComparator;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.ColumnTagProvider;

abstract public class AbstractObjectTableModel extends AbstractTableModel  implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	public AbstractObjectTableModel(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		return null;
	}
	
	public boolean isChoiceItemColumn(int column)
	{
		return getColumnQuestion(column) != null;
	}
	
	public boolean isCodeListColumn(int column)
	{
		return false;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public void setNewRowOrder(Integer[] existingRowIndexesInNewOrder)
	{
		ORefList newList = new ORefList();
		for(int i = 0; i < existingRowIndexesInNewOrder.length; ++i)
		{
			int nextExistingRowIndex = existingRowIndexesInNewOrder[i].intValue();
			newList.add(getRowObjectRefs().get(nextExistingRowIndex));
		}
		setRowObjectRefs(newList);
	}
	
	public Comparator createComparator(int sortColumn)
	{
		if (isChoiceItemColumn(sortColumn))
			return new TableModelChoiceItemComparator(this, sortColumn, getColumnQuestion(sortColumn));
		
		return new TableModelStringComparator(this, sortColumn);
	}

	abstract public String getUniqueTableModelIdentifier();
	
	abstract public void setRowObjectRefs(ORefList objectRowRefs);
	
	abstract protected ORefList getRowObjectRefs();

	private Project project;
}
