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

package org.miradi.dialogs.treetables;

import java.util.HashMap;

import javax.swing.table.TableColumn;

import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

public class MiradiTableColumn extends TableColumn
{
	public MiradiTableColumn(int columnIndex, HashMap<String, Integer> defaultColumnTagToWidthMap, String columnTag)
	{
		super(columnIndex, getDefaultColumnWidth(columnTag, defaultColumnTagToWidthMap));
	}
	
	private static int getDefaultColumnWidth(String columnTag, HashMap<String, Integer> defaultColumnTagToWidthMap)
	{
		if (defaultColumnTagToWidthMap.containsKey(columnTag))
			return defaultColumnTagToWidthMap.get(columnTag).intValue();
		
		return TableWithColumnWidthAndSequenceSaver.NORLMAL_WIDTH;
	}
	
	@Override
	public int getWidth()
	{
		return super.getWidth();
	}
}
