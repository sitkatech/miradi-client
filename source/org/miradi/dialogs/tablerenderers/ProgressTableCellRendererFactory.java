/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.tablerenderers;

import org.miradi.objects.*;

import javax.swing.*;
import java.awt.*;

public class ProgressTableCellRendererFactory extends ObjectTableCellEditorOrRendererFactory
{
	public ProgressTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		choiceItemRendererFactory = new ChoiceItemTableCellRendererFactory(providerToUse, fontProviderToUse);
		singleLineRendererFactory = new SingleLineObjectTableCellEditorOrRendererFactory(providerToUse, fontProviderToUse);		
	}

	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{	
		return getRendererFactory(row, tableColumn).getRendererComponent(table, isSelected, hasFocus, row, tableColumn, value);
	}
	
	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return getRendererFactory(row, column).getPreferredHeight(table, row, column, value);
	}	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		return getRendererFactory(row, tableColumn).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
	}
	
	private ObjectTableCellEditorOrRendererFactory getRendererFactory(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row, column);
		if (baseObject != null && isChoiceItemRow(baseObject))
			return choiceItemRendererFactory;
		
		return singleLineRendererFactory;
	}

	private boolean isChoiceItemRow(BaseObject baseObject)
	{
		if (ProjectMetadata.is(baseObject))
			return true;
		
		if (ResultsChainDiagram.is(baseObject))
			return true;

		if (ConceptualModelDiagram.is(baseObject))
			return true;

		if (Strategy.is(baseObject))
			return true;

		if (Indicator.is(baseObject))
			return true;
		
		if (Task.is(baseObject))
			return true;
		
		return false;
	}
	
	private ChoiceItemTableCellRendererFactory choiceItemRendererFactory;
	private SingleLineObjectTableCellEditorOrRendererFactory singleLineRendererFactory;
}
