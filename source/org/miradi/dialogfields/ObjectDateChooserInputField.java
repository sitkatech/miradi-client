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

import javax.swing.JComponent;

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CustomDateChooser;

public class ObjectDateChooserInputField extends ObjectDataInputField
{
	public ObjectDateChooserInputField(Project projectToUse, int type, BaseId id, String tag)
	{
		super(projectToUse, type, id, tag);
		project = projectToUse;
		
		dateChooser = new CustomDateChooser(this);
	}
	
	public void dispose()
	{
		super.dispose();
		if (dateChooser != null)
			dateChooser.dispose();
		dateChooser = null;
	}

	public String getPanelDescription()
	{
		return EAM.text("Date Chooser");
	}

	public JComponent getComponent()
	{
		return dateChooser;
	}

	public String getText()
	{
		return dateChooser.getDateAsString();
	}

	public void setText(String newValue)
	{
		if (newValue.length() <= 0 )
		{   
			dateChooser.setDate(null);		
			clearNeedsSave();
			return;
		}

		MultiCalendar calendar = MultiCalendar.createFromIsoDateString(newValue);
		dateChooser.setDate(calendar.getTime());
		clearNeedsSave();
	}
	
	public void updateEditableState()
	{
		dateChooser.setEnabled(isValidObject());
	}

	private CustomDateChooser dateChooser;
}


