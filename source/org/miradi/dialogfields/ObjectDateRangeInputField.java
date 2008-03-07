/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ObjectDateRangeInputField extends ObjectDataInputField
{
	public ObjectDateRangeInputField(Project projectToUse, int type, BaseId id, String tag)
	{
		super(projectToUse, type, id, tag);
		
		dateRangeChooserPanel = new DateRangeChooserPanel(this);
	}
	
	public void dispose()
	{
		super.dispose();
		if (dateRangeChooserPanel != null)
			dateRangeChooserPanel.dispose();
	}

	public String getPanelDescription()
	{
		return EAM.text("Date Range Chooser");
	}

	public JComponent getComponent()
	{
		return dateRangeChooserPanel;
	}

	public String getText()
	{
		try
		{
			return dateRangeChooserPanel.getDateRange();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	public void setText(String newValue)
	{
		try
		{
			dateRangeChooserPanel.setDateRange(newValue);
			clearNeedsSave();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void updateEditableState()
	{
		dateRangeChooserPanel.setEnabled(isValidObject());
	}

	private DateRangeChooserPanel dateRangeChooserPanel; 
}
