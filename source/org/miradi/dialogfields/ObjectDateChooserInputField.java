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
package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.DateEditorComponent;

public class ObjectDateChooserInputField extends ObjectDataInputField
{
	public ObjectDateChooserInputField(Project projectToUse, ORef refToUse, String tag)
	{
		super(projectToUse, refToUse, tag);
		
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
		if(dateEditor == null)
		{
			EAM.logWarning("ODCIF.needsToBeSaved called after dispose");
			EAM.logStackTrace();
			return false;
		}
		return dateEditor.needsToBeSaved();
	}
	
	@Override
	public void clearNeedsSave()
	{
		dateEditor.clearNeedsSaving();
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
	protected boolean shouldBeEditable()
	{
		return isValidObject();
	}
	
	private DateEditorComponent dateEditor;
}