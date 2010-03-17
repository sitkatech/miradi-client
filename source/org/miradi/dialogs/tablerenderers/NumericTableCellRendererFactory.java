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
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.BaseObject;

public class NumericTableCellRendererFactory extends SingleLineObjectTableCellRendererFactory
{
	public NumericTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		renderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		return renderer;
	}
	
	protected Font possiblyCreateStrikeThroughFont(Font font, DateUnit dateUnit, int row, int column)
	{
		BaseObject baseObject = getObjectProvider().getBaseObjectForRowColumn(row, column);
		if (isSuperseded(baseObject, dateUnit))
			font = createStrikethroughFont(font);
		
		return font;
	}
	
	private boolean isSuperseded(BaseObject baseObject, DateUnit dateUnit)
	{
		try
		{
			return baseObject.isAssignmentSuperseded(dateUnit);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private Font createStrikethroughFont(Font defaultFontToUse)
	{
		Map attributesMap = defaultFontToUse.getAttributes();
		attributesMap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

		return new Font(attributesMap);
	}
}
