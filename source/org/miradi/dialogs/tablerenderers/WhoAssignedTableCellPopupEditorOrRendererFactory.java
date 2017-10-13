/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogfields.editors.WhoAssignedPopupEditorComponent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;

import javax.swing.*;
import java.awt.*;

public class WhoAssignedTableCellPopupEditorOrRendererFactory extends PopupEditableCellEditorOrRendererFactory
{
	public WhoAssignedTableCellPopupEditorOrRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider, ORefSet resourceRefsFilter)
	{
		super(objectProvider, fontProvider);
		
		whoAssignedPopupEditorComponent = createWhenPopupEditorComponent(mainWindowToUse, resourceRefsFilter);
	}

	private WhoAssignedPopupEditorComponent createWhenPopupEditorComponent(MainWindow mainWindowToUse, ORefSet resourceRefsFilter)
	{
		return new WhoAssignedPopupEditorComponent(mainWindowToUse, resourceRefsFilter);
	}

	@Override
	public Object getCellEditorValue()
	{
		return whoAssignedPopupEditorComponent.getText();
	}

	@Override
	protected Component getConfiguredComponent(JTable table, Object value, int row, int column)
	{
		whoAssignedPopupEditorComponent.setText(value.toString());
		whoAssignedPopupEditorComponent.setInvokeButtonEnabled(table.isCellEditable(row, column));
		BaseObject baseObjectForRow = getBaseObjectForRow(row, column);
		whoAssignedPopupEditorComponent.setBaseObjectForRowLabel(baseObjectForRow);
		
		return whoAssignedPopupEditorComponent;
	}
	
	private WhoAssignedPopupEditorComponent whoAssignedPopupEditorComponent;
}
