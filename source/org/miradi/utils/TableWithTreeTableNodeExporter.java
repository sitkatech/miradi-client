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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.TaglessChoiceItem;

public class TableWithTreeTableNodeExporter extends AbstractTableExporter
{
	public TableWithTreeTableNodeExporter(TableWithTreeTableNodes tableToExprt)
	{
		table = tableToExprt;
	}
	
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return table.getBaseObjectForRowColumn(row, 0);
	}

	@Override
	public int getColumnCount()
	{
		return table.getColumnCount();
	}

	@Override
	public int getDepth(int row, int column)
	{
		return 0;
	}

	@Override
	public String getHeaderFor(int column)
	{
		return table.getColumnName(column);
	}

	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		return new TaglessChoiceItem(getSafeValue(table.getValueAt(row, column)), null);
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}

	@Override
	public int getRowCount()
	{
		return table.getRowCount();
	}

	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getTextAt(int row, int column)
	{
		return getSafeValue(table.getValueAt(row, column));
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		EAM.logError("getAllRefs is not implemented");
		return new ORefList();
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		EAM.logError("getAllTypes is not implemented");
		return new Vector<Integer>();
	}

	private TableWithTreeTableNodes table;
}
