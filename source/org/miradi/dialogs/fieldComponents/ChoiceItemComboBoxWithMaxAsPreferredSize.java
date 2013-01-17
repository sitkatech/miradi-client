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
package org.miradi.dialogs.fieldComponents;

import java.awt.Dimension;

import org.miradi.questions.ChoiceItem;

public class ChoiceItemComboBoxWithMaxAsPreferredSize extends ChoiceItemComboBox
{
	public ChoiceItemComboBoxWithMaxAsPreferredSize(ChoiceItem[] items)
	{
		super(items);
	}

	@Override
	public Dimension getMaximumSize()
	{
		return super.getPreferredSize();
	}

	public void setSelectedCode(String codeToSelect)
	{
		for(int row = 0; row < getItemCount(); ++row)
		{
			ChoiceItem choiceItem = getChoiceItemAt(row);
			if(choiceItem.getCode().equals(codeToSelect))
			{
				setSelectedIndex(row);
				return;
			}
		}
		setSelectedIndex(-1);
	}

	public ChoiceItem getChoiceItemAt(int row)
	{
		return (ChoiceItem)getItemAt(row);
	}
}
