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

import org.miradi.dialogfields.editors.AbstractTimeframePopupEditorComponent;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;

import javax.swing.*;
import java.awt.*;

abstract public class AbstractTimeframeTableCellPopupEditorOrRendererFactory extends PopupEditableCellEditorOrRendererFactory
{
	public AbstractTimeframeTableCellPopupEditorOrRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider)
	{
		super(objectProvider, fontProvider);
		
		abstractTimeframePopupEditorComponent = createWhenPopupEditorComponent(mainWindowToUse);
	}

	abstract protected AbstractTimeframePopupEditorComponent createWhenPopupEditorComponent(MainWindow mainWindowToUse);

	@Override
	public Object getCellEditorValue()
	{
		return abstractTimeframePopupEditorComponent.getText();
	}

	@Override
	protected Component getConfiguredComponent(JTable table, Object value, int row, int column)
	{
		abstractTimeframePopupEditorComponent.setText(value.toString());
		abstractTimeframePopupEditorComponent.setInvokeButtonEnabled(table.isCellEditable(row, column));
		BaseObject baseObjectForRow = getBaseObjectForRow(row, column);
		abstractTimeframePopupEditorComponent.setBaseObjectForRowLabel(baseObjectForRow);
		
		return abstractTimeframePopupEditorComponent;
	}
	
	private AbstractTimeframePopupEditorComponent abstractTimeframePopupEditorComponent;
}
