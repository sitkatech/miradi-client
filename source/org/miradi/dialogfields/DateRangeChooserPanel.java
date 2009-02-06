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

import java.text.ParseException;
import java.util.Date;

import org.martus.util.MultiCalendar;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.AppPreferences;
import org.miradi.utils.CustomDateChooser;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;

public class DateRangeChooserPanel extends MiradiPanel
{
	public DateRangeChooserPanel(ObjectDataInputField objectDataInputFieldToUse)
	{
		super();
		
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		startDateChooser = new CustomDateChooser(objectDataInputFieldToUse);
		endDateChooser = new CustomDateChooser(objectDataInputFieldToUse);
		
		add(startDateChooser);
		add(endDateChooser);
	}
	
	public void dispose()
	{
		if (startDateChooser != null)
			startDateChooser.dispose();
		startDateChooser = null;
		
		if (endDateChooser != null)
			endDateChooser.dispose();
		endDateChooser = null;
	}
	
	public String getDateRange() throws Exception
	{
		Date startRawDate = startDateChooser.getDate();
		Date endRawDate = endDateChooser.getDate();
		
		if (endRawDate == null)
			endRawDate = startRawDate;
		
		if (startRawDate == null)
			startRawDate =  endRawDate;
		
		if (startRawDate == null || endRawDate == null)
			return "";
		
		MultiCalendar startDate = new MultiCalendar(startRawDate);
		MultiCalendar endDate = new MultiCalendar(endRawDate);
		return DateRange.createFromJson(startDate, endDate).toJson().toString();
	}
	
	public void setDateRange(String dateRangeAsSting) throws ParseException, Exception
	{
		if (dateRangeAsSting.length() == 0)
		{
			startDateChooser.clear();
			endDateChooser.clear();		
			return;
		}
		
		DateRange dateRange = new DateRange(new EnhancedJsonObject(dateRangeAsSting));
		startDateChooser.setDate(dateRange.getStartDate().getTime());
		endDateChooser.setDate(dateRange.getEndDate().getTime());
	}
	
	private CustomDateChooser startDateChooser;
	private CustomDateChooser endDateChooser;
}
