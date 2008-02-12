/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.miradi.main.EAM;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

public class CustomDateChooser extends JDateChooser
{
	public CustomDateChooser(IDateEditor dateEditor)
	{
		super(dateEditor);
		calendarButton.addMouseListener(new CustomMouseListener());
		jcalendar.getMonthChooser().addPropertyChangeListener(new MonthChangeListener());
		jcalendar.getYearChooser().addPropertyChangeListener(new YearChangeListener());
		setFont(EAM.getMainWindow().getUserDataPanelFont());
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
}
