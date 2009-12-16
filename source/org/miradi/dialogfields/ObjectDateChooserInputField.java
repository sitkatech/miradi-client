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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.DateEditorComponent;

public class ObjectDateChooserInputField extends ObjectDataInputField
{
	public ObjectDateChooserInputField(Project projectToUse, int type, BaseId id, String tag)
	{
		super(projectToUse, type, id, tag);
		
		dateEditor = new DateEditorComponent();
		dateEditor.addFocusListener(this);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();

		if (dateEditor != null)
		{
			dateEditor.dispose();
			dateEditor.removeFocusListener(this);
			dateEditor = null;
		}
	}
	
	@Override
	public boolean needsToBeSaved()
	{
		return dateEditor.needsToBeSaved();
	}

	public String getPanelDescription()
	{
		return EAM.text("Date Chooser");
	}

	@Override
	public JComponent getComponent()
	{
		return dateEditor;
	}

	@Override
	public String getText()
	{
		return dateEditor.getText();
	}

	@Override
	public void setText(String newValue)
	{
		dateEditor.setText(newValue);
	}
	
	@Override
	public void updateEditableState()
	{
		dateEditor.setEnabled(isValidObject());
	}
		
	private DateEditorComponent dateEditor;
}