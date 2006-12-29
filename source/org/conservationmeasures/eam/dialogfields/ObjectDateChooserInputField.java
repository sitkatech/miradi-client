/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CustomDateChooser;
import org.martus.util.MultiCalendar;

import com.toedter.calendar.JTextFieldDateEditor;

public class ObjectDateChooserInputField extends ObjectDataInputField
{
	public ObjectDateChooserInputField(Project projectToUse, int type, BaseId id, String tag)
	{
		super(projectToUse, type, id, tag);
		project = projectToUse;
		
		dateChooser = new CustomDateChooser(new DateEditor());
		dateChooser.setDate(getStartDate(tag));
		dateChooser.setDateFormatString("MM/dd/yyyy");

		//TODO remove hardcoded pref and min settings
		Dimension dimension = new Dimension(150, 20);
		dateChooser.setMinimumSize(dimension);
		dateChooser.setPreferredSize(dimension);
	}
		
	private Date getStartDate(String tag)
	{
		String storedDateString = project.getMetadata().getData(tag);
		if (storedDateString.length() <= 0 )
			return null;
		
		MultiCalendar calendar = MultiCalendar.createFromIsoDateString(storedDateString);
		return calendar.getTime();
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
		return convertFormat();
	}
	
	private String convertFormat()
	{
		Date date = dateChooser.getDate();
		if (date == null)
			return "";
		
		MultiCalendar calendar = new MultiCalendar();
		calendar.setTime(date);
		return calendar.toIsoDateString();
	}
	
	public void setText(String newValue)
	{
		if (newValue.length() <= 0 )
		{   
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

	class DateEditor extends JTextFieldDateEditor
	{
		public DateEditor()
		{
			super();
			setEditable(false);
		}

		public void setDate(Date newDate)
		{
			super.setDate(newDate);
			saveDate();	
		}

		private void saveDate()
		{
			if (dateChooser == null)
				return;
			
			if (!dateChooser.isDateSelected())
				return;
			
			setNeedsSave();
			saveIfNeeded();
		}
	}
	
	CustomDateChooser dateChooser;
}


