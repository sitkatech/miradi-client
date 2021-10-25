/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import javax.swing.table.DefaultTableCellRenderer;

import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.utils.NonNegativeIntegerRestrictedDocument;

public class NonNegativeIntegerRestrictedTableCellRendererEditorFactory extends AbstractNumericRestrictedTableCellRendererEditorFactory
{
	public NonNegativeIntegerRestrictedTableCellRendererEditorFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	@Override
	protected PanelTextField createRestrictedNumericTextField()
	{	
		return new NonNegativeIntegerRestrictedTextField();
	}
	
	class NonNegativeIntegerRestrictedTextField extends PanelTextField
	{
		public NonNegativeIntegerRestrictedTextField()
		{
			setDocument(new NonNegativeIntegerRestrictedDocument());
			setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		}
	}
}
