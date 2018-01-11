/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

public class MonthDateUnitStartAndEndCard extends DateUnitStartAndEndCard
{
	public MonthDateUnitStartAndEndCard(ProjectCalendar projectCalendar, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startMonthPanel = new MonthPanel(projectCalendar, startEndDateUnitProviderToUse.getStartDateUnit(), getStartText());
		endMonthPanel = new MonthPanel(projectCalendar, startEndDateUnitProviderToUse.getEndDateUnit(), getEndText());
		
		add(new PanelTitleLabel(EAM.text("Month Selection: ")));
		add(new FillerLabel());
		add(startMonthPanel);
		add(endMonthPanel);
	}
	
	@Override
	protected DateUnit getStartDate()
	{
		return startMonthPanel.getDateUnit();
	}
	
	@Override
	protected DateUnit getEndDate()
	{
		return endMonthPanel.getDateUnit();
	}

	@Override
	public String getPanelDescription()
	{
		return AbstractDateUnitTypeQuestion.MONTH_CODE;
	}

	@Override
	public void setStartEndDateUnitProvider(ProjectCalendar projectCalendarToUse, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startMonthPanel.setProjectCalendar(projectCalendarToUse);
		startMonthPanel.setDateUnit(startEndDateUnitProviderToUse.getStartDateUnit());
		endMonthPanel.setProjectCalendar(projectCalendarToUse);
		endMonthPanel.setDateUnit(startEndDateUnitProviderToUse.getEndDateUnit());
	}

	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (startMonthPanel != null)
			startMonthPanel.setBackground(bg);
		if (endMonthPanel != null)
			endMonthPanel.setBackground(bg);
	}

	@Override
	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		if (startMonthPanel != null)
			startMonthPanel.addActionListener(editorFieldChangeHandlerToUse);
		if (endMonthPanel != null)
			endMonthPanel.addActionListener(editorFieldChangeHandlerToUse);
	}

	private MonthPanel startMonthPanel;
	private MonthPanel endMonthPanel;
}
