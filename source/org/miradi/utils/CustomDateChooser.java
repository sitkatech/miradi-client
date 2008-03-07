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

public class CustomDateChooser extends JDateChooser implements PropertyChangeListener
{
	public CustomDateChooser(ObjectDataInputField objectDataInputFieldToUse)
	{
		super(new DateEditor());
		
		objectDataInputField =  objectDataInputFieldToUse;
		setDateFormatString(CustomDateChooser.CUSTOM_DATE_FORMAT);
		setDateChooserPreferredSizeWithPadding();
		
		calendarButton.addMouseListener(new CustomMouseListener());
		setFont(EAM.getMainWindow().getUserDataPanelFont());
		
		dateEditor.addPropertyChangeListener(DATE_PROPERTY_NAME, this);
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
	
	public void propertyChange(PropertyChangeEvent evt) 
	{
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(DATE_PROPERTY_NAME)) 
		{
			if (objectDataInputField != null)
				objectDataInputField.forceSave();
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
		public DateEditor()
		{
			super();
		}
		
		public void setDate(Date newDate, boolean firePropertyChange)
		{
			super.setDate(newDate, firePropertyChange);
			setForeground(Color.blue);
		}

		public void focusLost(FocusEvent focusEvent)
		{
			checkText();
			setForeground(Color.BLUE);
		}

		//NOTE:  focusLost does not call super, because we can not override checkText (private method)
		// this method is a duplicate of the parent class
		private void checkText() 
		{
			try 
			{
				Date thisDate = dateFormatter.parse(getText());
				setDate(thisDate, true);
			} 
			catch (Exception e) 
			{
				setDate(null, true);
			}
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
		
	public static final String CUSTOM_DATE_FORMAT = "MM/dd/yyyy";
	private static final int EXTRA_PADDING = 20;
	private static final String DATE_PROPERTY_NAME = "date";
	
	private ObjectDataInputField objectDataInputField;
}
