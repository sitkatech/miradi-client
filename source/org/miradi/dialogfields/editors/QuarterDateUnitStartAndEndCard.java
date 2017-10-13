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

public class QuarterDateUnitStartAndEndCard extends DateUnitStartAndEndCard
{
	public QuarterDateUnitStartAndEndCard(ProjectCalendar projectCalendar, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startQuarterPanel = new QuarterPanel(projectCalendar, startEndDateUnitProviderToUse.getStartDateUnit(), getStartText());
		endQuarterPanel = new QuarterPanel(projectCalendar, startEndDateUnitProviderToUse.getEndDateUnit(), getEndText());

		add(new PanelTitleLabel(EAM.text("Quarter Selection: ")));
		add(new FillerLabel());
		add(startQuarterPanel);
		add(endQuarterPanel);
	}

	@Override
	protected DateUnit getEndDate()
	{
		return endQuarterPanel.getDateUnit();
	}

	@Override
	public void setStartEndDateUnitProvider(ProjectCalendar projectCalendarToUse, StartEndDateUnitProvider startEndDateUnitProviderToUse)
	{
		startQuarterPanel.setProjectCalendar(projectCalendarToUse);
		startQuarterPanel.setDateUnit(startEndDateUnitProviderToUse.getStartDateUnit());
		endQuarterPanel.setProjectCalendar(projectCalendarToUse);
		endQuarterPanel.setDateUnit(startEndDateUnitProviderToUse.getEndDateUnit());
	}

	@Override
	protected DateUnit getStartDate()
	{
		return startQuarterPanel.getDateUnit();
	}
	
	@Override
	public String getPanelDescription()
	{
		return AbstractDateUnitTypeQuestion.QUARTER_CODE;
	}

	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (startQuarterPanel != null)
			startQuarterPanel.setBackground(bg);
		if (endQuarterPanel != null)
			endQuarterPanel.setBackground(bg);
	}

	@Override
	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		if (startQuarterPanel != null)
			startQuarterPanel.addActionListener(editorFieldChangeHandlerToUse);
		if (endQuarterPanel != null)
			endQuarterPanel.addActionListener(editorFieldChangeHandlerToUse);
	}

	private QuarterPanel startQuarterPanel;
	private QuarterPanel endQuarterPanel;
}
