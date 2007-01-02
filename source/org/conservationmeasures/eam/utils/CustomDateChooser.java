/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

public class CustomDateChooser extends JDateChooser
{
	public CustomDateChooser(IDateEditor dateEditor)
	{
		super(dateEditor);
		calendarButton.addMouseListener(new MouseListener());
	}
	
	public boolean isDateSelected()
	{
		return dateSelected;
	}
	
	class MouseListener extends MouseAdapter
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
