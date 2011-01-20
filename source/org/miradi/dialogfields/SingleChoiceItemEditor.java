/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class SingleChoiceItemEditor extends ChoiceItemComboBox
{
	public SingleChoiceItemEditor(ChoiceQuestion question)
	{
		super(question);
	}

	public String getText()
	{
		ChoiceItem selected = (ChoiceItem)getSelectedItem();
		if(selected == null)
			return "";

		return selected.getCode();
	}

	public void setText(String code) throws Exception
	{
		for(int index = 0; index < getItemCount(); ++index)
		{
			ChoiceItem choice = (ChoiceItem)getItemAt(index);
			if (code.equals(choice.getCode()))
			{
				setSelectedIndex(index);
				return;
			}
		}
		
		setSelectedIndex(-1);
	}

	@Override
	public void setEnabled(boolean isEnabled)
	{
		super.setEnabled(isEnabled);

		setEnabled(isEnabled);
		if(isEnabled)
		{
			setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
			setBackground(EAM.EDITABLE_BACKGROUND_COLOR);
		}
		else
		{
			setForeground(EAM.READONLY_FOREGROUND_COLOR);
			setBackground(EAM.READONLY_BACKGROUND_COLOR);
		}
	}
}
