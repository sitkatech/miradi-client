/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogfields.editors;

import org.miradi.dialogfields.TimeframeEditorField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.AbstractDateUnitTypeQuestion;
import org.miradi.utils.FillerLabel;

import java.awt.*;

public class CalendarYearDateUnitStartAndEndCard extends DateUnitStartAndEndCard
{
	public CalendarYearDateUnitStartAndEndCard(ProjectCalendar projectCalendar, StartEndDateUnitProvider startEndDateUnitProvider)
	{
		startYearPanel = new YearPanel(projectCalendar, startEndDateUnitProvider.getStartDateUnit(), getStartText());
		endYearPanel = new YearPanel(projectCalendar, startEndDateUnitProvider.getEndDateUnit(), getEndText());
		
		add(new PanelTitleLabel(EAM.text("Year Selection: ")));
		add(new FillerLabel());
		add(startYearPanel);
		add(endYearPanel);
	}
	
	@Override
	protected DateUnit getEndDate()
	{
		return endYearPanel.getDateUnit();
	}

	@Override
	protected DateUnit getStartDate()
	{
		return startYearPanel.getDateUnit();
	}

	@Override
	public String getPanelDescription()
	{
		return AbstractDateUnitTypeQuestion.YEAR_CODE;
	}

	@Override
	public void setStartEndDateUnitProvider(ProjectCalendar projectCalendarToUse,StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startYearPanel.setProjectCalendar(projectCalendarToUse);
		startYearPanel.setDateUnit(startEndDateUnitProviderToUse.getStartDateUnit());
		endYearPanel.setProjectCalendar(projectCalendarToUse);
		endYearPanel.setDateUnit(startEndDateUnitProviderToUse.getEndDateUnit());
	}

	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (startYearPanel != null)
			startYearPanel.setBackground(bg);
		if (endYearPanel != null)
			endYearPanel.setBackground(bg);
	}

	@Override
	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		if (startYearPanel != null)
			startYearPanel.addActionListener(editorFieldChangeHandlerToUse);
		if (endYearPanel != null)
			endYearPanel.addActionListener(editorFieldChangeHandlerToUse);
	}

	private YearPanel startYearPanel;
	private YearPanel endYearPanel;
}
