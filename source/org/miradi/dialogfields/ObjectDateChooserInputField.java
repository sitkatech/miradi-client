/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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


