/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.event.CaretEvent;

import org.martus.util.MultiCalendar;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.main.EAM;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class CustomDateChooser extends JDateChooser
{
	public CustomDateChooser(ObjectDataInputField objectDataInputFieldToUse)
	{
		super(new DateEditor(objectDataInputFieldToUse));
		
		setDateFormatString(CustomDateChooser.CUSTOM_DATE_FORMAT);
		setDateChooserPreferredSizeWithPadding();
		
		calendarButton.addMouseListener(new CustomMouseListener());
		jcalendar.getMonthChooser().addPropertyChangeListener(new MonthChangeListener());
		jcalendar.getYearChooser().addPropertyChangeListener(new YearChangeListener());
		setFont(EAM.getMainWindow().getUserDataPanelFont());
	}
	
	public void clear()
	{
		((DateEditor) getDateEditor()).setDate(null);
	}
	
	public String getDateAsString()
	{
		return convertDateToIsoString(getDate());
	}

	private static String convertDateToIsoString(Date date)
	{
		if (date == null)
			return "";
		
		MultiCalendar calendar = new MultiCalendar();
		calendar.setTime(date);
		return calendar.toIsoDateString();
	}
		
	private void setDateChooserPreferredSizeWithPadding()
	{
		Dimension preferredDimension = getPreferredSize();
		//FIXME: Why do we need EXTRA PADDING....I beleive the JDataChooser for this third party is not inlcuding the icon width in its pref
		preferredDimension.width = preferredDimension.width + EXTRA_PADDING;
		setMinimumSize(preferredDimension);
		setPreferredSize(preferredDimension);
	}
		
	class MonthChangeListener implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			setDate(jcalendar.getDate());
		}
	}
	
	class YearChangeListener implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent evt)
		{
			setDate(jcalendar.getDate());
		}
	}
	
	class CustomMouseListener extends MouseAdapter
	{
		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
			if (isVisibleAndPressed)
			{
				dateSelected = true;
				popup.setVisible(false);
				setDate(newCalendar.getTime());
				isVisibleAndPressed = false;		
			}
		}
		
		public void mousePressed(MouseEvent e)
		{
			super.mousePressed(e);
			if (popup.isVisible())
			{
				newCalendar = jcalendar.getCalendar();
				isVisibleAndPressed = true;
			}
		}
		Calendar newCalendar;
		boolean isVisibleAndPressed;	
	}
	
	static class DateEditor extends JTextFieldDateEditor
	{
		public DateEditor(ObjectDataInputField objectDataInputFieldToUse)
		{
			super();
			
			objectDataInputField = objectDataInputFieldToUse;
		}
		
		public void setDate(Date newDate)
		{
			// NOTE: funky case where user clicks on the date icon, 
			// and it invokes setDate BEFORE focus has transferred 
			// to this field. So save the old field first
			objectDataInputField.saveFocusedFieldPendingEdits();
			
			super.setDate(newDate);
			setForeground(Color.blue);
		}

		public void focusLost(FocusEvent arg0)
		{
			super.focusLost(arg0);
			setForeground(Color.BLUE);
			objectDataInputField.forceSave();
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

		private ObjectDataInputField objectDataInputField;
	}
		
	public static final String CUSTOM_DATE_FORMAT = "MM/dd/yyyy";
	private static final int EXTRA_PADDING = 20;
}
