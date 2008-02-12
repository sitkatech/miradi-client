/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.event.CaretEvent;

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CustomDateChooser;

import com.toedter.calendar.JTextFieldDateEditor;

public class ObjectDateChooserInputField extends ObjectDataInputField
{
	public ObjectDateChooserInputField(Project projectToUse, int type, BaseId id, String tag)
	{
		super(projectToUse, type, id, tag);
		project = projectToUse;
		
		DateEditor dateEditor = new DateEditor();
		dateChooser = new CustomDateChooser(dateEditor);
		dateChooser.setDateFormatString("MM/dd/yyyy");

		setDateChooserPreferredSizeWithPadding();
	}
	
	public void dispose()
	{
		super.dispose();
		if (dateChooser != null)
			dateChooser.cleanup();
	}

	private void setDateChooserPreferredSizeWithPadding()
	{
		Dimension preferredDimension = dateChooser.getPreferredSize();
		//FIXME: Why do we need EXTRA PADDING....I beleive the JDataChooser for this third party is not inlcuding the icon width in its pref
		preferredDimension.width = preferredDimension.width + EXTRA_PADDING;
		dateChooser.setMinimumSize(preferredDimension);
		dateChooser.setPreferredSize(preferredDimension);
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

	class DateEditor extends JTextFieldDateEditor
	{
		public DateEditor()
		{
			super();
		}
		
		public void setDate(Date newDate)
		{
			// NOTE: funky case where user clicks on the date icon, 
			// and it invokes setDate BEFORE focus has transferred 
			// to this field. So save the old field first
			saveFocusedFieldPendingEdits();
			
			super.setDate(newDate);
			setForeground(Color.blue);
			saveDate();	
		}

		private void saveDate()
		{
			if (dateChooser == null)
				return;
			
			setNeedsSave();
			saveIfNeeded();
		}

		public void focusLost(FocusEvent arg0)
		{
			super.focusLost(arg0);
			setForeground(Color.BLUE);
			saveDate();
		}

		public void caretUpdate(CaretEvent event)
		{
			super.caretUpdate(event);
			setForeground(Color.BLUE);
		}
		
		public void setEnabled(boolean b)
		{
			super.setEnabled(b);
			setForeground(Color.blue);
		}

	}
	
	CustomDateChooser dateChooser;
	private static final int EXTRA_PADDING = 20;
}


