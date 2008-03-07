/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
			startDateChooser.cleanup();
		
		if (endDateChooser != null);
			endDateChooser.cleanup();
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

		if (startDate.after(endDate))
			return new DateRange(endDate, startDate).toJson().toString();
		
		return new DateRange(startDate, endDate).toJson().toString();
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
