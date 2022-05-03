/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.DateEditorComponent;
import org.miradi.utils.DateEditorComponentInsideTable;

import javax.swing.*;
import java.awt.*;

public class DateTableCellEditorOrRendererFactory extends PopupEditableCellEditorOrRendererFactory
{
	public DateTableCellEditorOrRendererFactory(RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider)
	{
	    super(objectProvider, fontProvider);

	    dateComponent = new DateEditorComponentInsideTable();
	}
	
	@Override
	public void dispose()
	{
		if(dateComponent != null)
			dateComponent.dispose();
		dateComponent = null;
		
		super.dispose();
	}

	@Override
	protected Component getConfiguredComponent(JTable table, Object value, int row, int column)
	{
		if (value != null)
		{
			TaglessChoiceItem choiceItem = (TaglessChoiceItem) value;
			dateComponent.setText(choiceItem.getLabel());
		}
		return dateComponent;
	}

	@Override
	public Object getCellEditorValue()
	{
		return dateComponent.getText();
	}

	private DateEditorComponent dateComponent;
}
