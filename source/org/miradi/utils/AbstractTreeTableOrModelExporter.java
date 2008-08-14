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

import org.miradi.icons.IconManager;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;


abstract public class AbstractTreeTableOrModelExporter extends AbstractTableExporter
{
	public int getMaxDepthCount()
	{
		int maxRowDepth = 0;
		int rowCount = getRowCount();
		for (int row = 0; row < rowCount; ++row)
		{
			int rowDepth = getDepth(row);
			maxRowDepth = Math.max(maxRowDepth, rowDepth);
		}
		
		return maxRowDepth;
	}
	
	@Override
	public Icon getIconAt(int row, int column)
	{
		if (column == 0)
		{
			BaseObject baseObject = getBaseObjectForRow(row);
			if (baseObject != null)
				return IconManager.getImage(baseObject);
			
			int rowType = getRowType(row);
			if (rowType != ObjectType.FAKE)
				return IconManager.getImage(rowType);
		}
		//FIXME this needs to return correct cell icon
		return null;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		ORefList baseObjectsForType = new ORefList();
		for (int row = 0; row < getRowCount(); ++row)
		{
			BaseObject baseObjectForRow = getBaseObjectForRow(row);
			if (baseObjectForRow == null)
				continue;
			
			if (baseObjectForRow.getType() == objectType)
				baseObjectsForType.add(baseObjectForRow.getRef());
		}
		
		return baseObjectsForType;
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		Vector<Integer> rowTypes = new Vector<Integer>();
		for (int row = 0; row < getRowCount(); ++row)
		{
			BaseObject baseObjectForRow = getBaseObjectForRow(row);
			if (baseObjectForRow != null)
				rowTypes.add(baseObjectForRow.getType());
		}
		
		return rowTypes;
	}
	
	protected static final int ROOT_PLUS_TOPLEVEL_ADJUSTMENT = 2;
}
