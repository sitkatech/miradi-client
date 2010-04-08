/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields.editors;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.FillerLabel;

public class QuarterDateUnitStartAndEndCard extends DateUnitStartAndEndCard
{
	public QuarterDateUnitStartAndEndCard(ProjectCalendar projectCalendar, DateUnitRange dateUnitRange)
	{
		startQuarterPanel = new QuarterPanel(projectCalendar, dateUnitRange.getStartDateUnit(), getStartText());
		endQuarterPanel = new QuarterPanel(projectCalendar, dateUnitRange.getEndDateUnit(), getEndText());

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
	protected DateUnit getStartDate()
	{
		return startQuarterPanel.getDateUnit();
	}
	
	@Override
	public String getPanelDescription()
	{
		return WhenEditorComponent.QUARTER_ITEM;
	}
	
	private QuarterPanel startQuarterPanel;
	private QuarterPanel endQuarterPanel;
}
