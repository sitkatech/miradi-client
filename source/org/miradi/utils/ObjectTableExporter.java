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
package org.miradi.utils;

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectTable;
import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.rtf.RtfStyleManager;

public class ObjectTableExporter extends AbstractTableExporter
{
	public ObjectTableExporter(ObjectTable tableToUse)
	{
		tableToExport = tableToUse;
	}

	@Override
	public int getRowCount()
	{
		return tableToExport.getRowCount();
	}

	@Override
	public int getColumnCount()
	{
		return tableToExport.getColumnCount();
	}

	@Override
	public int getDepth(int row)
	{
		return 0;
	}

	@Override
	public String getHeaderFor(int column)
	{
		return tableToExport.getColumnName(column);
	}

	@Override
	public Icon getIconAt(int row, int column)
	{
		//FIXME is there a better way to get the choice time rather than instanceof
		Object value = tableToExport.getValueAt(row, column);
		if (value instanceof ChoiceItem)
			return ((ChoiceItem)value).getIcon();
			
		return null;
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	//FIXME This method needs to be changed so that it accepts column
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		final int FIRST_COLUMN = 0;
		return tableToExport.getBaseObjectForRowColumn(row, FIRST_COLUMN);
	}
	
	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getTextAt(int row, int column)
	{
		Object value = tableToExport.getValueAt(row, column);
		ChoiceQuestion question = getChoiceQuestion(column);
		if (tableToExport.getObjectTableModel().isCodeListColumn(column))
			return createExportableCodeList((CodeList) value, question);
		
		return getSafeValue(value);
	}

	@Override
	public String getStyleTagAt(int row, int column)
	{
		return RtfStyleManager.createTag(getBaseObjectForRow(row));
	}
	
	private ChoiceQuestion getChoiceQuestion(int column)
	{
		int modelColumn = tableToExport.convertColumnIndexToModel(column);
		ChoiceQuestion question = getObjectTableModel().getColumnQuestion(modelColumn);
		return question;
	}

	@Override
	public ORefList getAllRefs(int objectType)
	{
		ORefList allObjectRefs = new ORefList();
		for (int row = 0; row < getRowCount(); ++row)
		{
			allObjectRefs.add(getBaseObjectForRow(row).getRef());
		}
		
		return allObjectRefs;
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		Vector<Integer> rowTypes = new Vector<Integer>();
		rowTypes.add(getRowType(0));
		
		return rowTypes;
	}
	
	private ObjectTableModel getObjectTableModel()
	{
		return tableToExport.getObjectTableModel();
	}
		
	private ObjectTable tableToExport;
}
