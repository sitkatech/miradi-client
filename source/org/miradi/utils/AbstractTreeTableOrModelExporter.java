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
package org.miradi.utils;

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.PanelTreeTable;
import org.miradi.icons.IconManager;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.rtf.RtfStyleManager;


abstract public class AbstractTreeTableOrModelExporter extends AbstractTableExporter
{
	@Override
	public String getStyleTagAt(int row, int column)
	{
		String columnTag = getModel().getColumnTag(column);
		if (isCommentTag(columnTag))
			return RtfStyleManager.COMMENT_STYLE_TAG;
		
		if (getBaseObjectForRow(row) == null)
			return RtfStyleManager.createTag(getRowType(row));
		
		if (isTreeColumn(column))
			return RtfStyleManager.createTag(getBaseObjectForRow(row));
		
		return "";
	}

	private boolean isCommentTag(String columnTag)
	{
		if (columnTag.equals(Indicator.TAG_DETAIL))
			return true;
		
		if (columnTag.equals(Factor.TAG_TEXT))
			return true;
		
		if (columnTag.equals(Factor.TAG_COMMENT))
			return true;
		
		return false;
	}

	public int getMaxDepthCount()
	{
		int maxRowDepth = 0;
		int rowCount = getRowCount();
		for (int row = 0; row < rowCount; ++row)
		{
			int rowDepth = getDepth(row, 0);
			maxRowDepth = Math.max(maxRowDepth, rowDepth);
		}
		
		return maxRowDepth;
	}
	
	private Icon getIcon(int row, int column)
	{
		if (isTreeColumn(column))
		{
			BaseObject baseObject = getBaseObjectForRow(row);
			if (baseObject != null)
				return IconManager.getImage(baseObject);
			
			int rowType = getRowType(row);
			return IconManager.getImage(rowType);
		}
		//FIXME this needs to return correct cell icon
		return null;
	}
	
	@Override
	public ChoiceItem getChoiceItemAt(int row, int column)
	{
		return new TaglessChoiceItem(getTextAt(row, column), getIcon(row, column));
	}

	protected boolean isTreeColumn(int column)
	{
		return column == PanelTreeTable.TREE_COLUMN_INDEX;
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
	
	abstract public GenericTreeTableModel getModel();
	
	protected static final int TOPLEVEL_ADJUSTMENT = 1;
}
