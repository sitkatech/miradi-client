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
package org.miradi.dialogfields;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.martus.swing.UiCheckBox;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ObjectCheckBoxField extends ObjectDataInputField
{
	public ObjectCheckBoxField(Project projectToUse, int objectType, BaseId objectId, String tag, String valueWhenChecked, String valueWhenUnchecked)
	{
		super(projectToUse, objectType, objectId, tag);
		checkBox = new PanelCheckBox();
		checkBox.addItemListener(new StatusChangeHandler());
		addFocusListener();
		checkedValue = valueWhenChecked;
		uncheckedValue = valueWhenUnchecked;
	}

	public JComponent getComponent()
	{
		return checkBox;
	}

	public String getText()
	{
		if (checkBox.isSelected())
			return checkedValue;
		return uncheckedValue;
	}

	public void setText(String code)
	{
		checkBox.setSelected(code.equals(checkedValue));
	}

	public void updateEditableState()
	{
		checkBox.setEnabled(isValidObject());
		if(isValidObject())
		{
			checkBox.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
		}
		else
		{
			checkBox.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		}
	}
	
	class StatusChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			forceSave();
		}
	}
	
	UiCheckBox checkBox;
	String checkedValue;
	String uncheckedValue;
}
