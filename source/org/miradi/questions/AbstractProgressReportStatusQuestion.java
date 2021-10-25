/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.questions;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

import java.awt.*;

abstract public class AbstractProgressReportStatusQuestion extends StaticChoiceQuestionSortableByNaturalOrder
{
	public AbstractProgressReportStatusQuestion()
	{
		super();
	}
	
	@Override
	protected ChoiceItem[] createChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(NOT_SPECIFIED, getNotSpecifiedLabel(), Color.WHITE),
				new ChoiceItemWithDynamicColor(NOT_KNOWN_CODE, getNotKnownLabel(), AppPreferences.TAG_COLOR_NOT_KNOWN),
				new ChoiceItemWithDynamicColor(PLANNED_CODE, getPlannedLabel(), AppPreferences.TAG_COLOR_PLANNED),
				new ChoiceItemWithDynamicColor(MAJOR_ISSUES_CODE, getMajorIssuesLabel(), AppPreferences.TAG_COLOR_ALERT),
				new ChoiceItemWithDynamicColor(MINOR_ISSUES_CODE, getMinorIssuesLabel(), AppPreferences.TAG_COLOR_CAUTION),
				new ChoiceItemWithDynamicColor(ON_TRACK_CODE, getOnTrackLabel(), AppPreferences.TAG_COLOR_OK),
				new ChoiceItemWithDynamicColor(COMPLETED_CODE, getCompletedLabel(), AppPreferences.TAG_COLOR_GREAT),
				new ChoiceItemWithDynamicColor(ABANDONED_CODE, getAbandonedLabel(), AppPreferences.TAG_COLOR_ABANDONED),
		};
	}

	protected String getNotSpecifiedLabel()
	{
		return EAM.text("Not Specified");
	}

	abstract protected String getNotKnownLabel();

	abstract protected String getAbandonedLabel();
	
	abstract protected String getCompletedLabel();

	abstract protected String getOnTrackLabel();

	abstract protected String getMinorIssuesLabel();

	abstract protected String getMajorIssuesLabel();

	abstract protected String getPlannedLabel();

	public static final String NOT_SPECIFIED = "";
	public static final String NOT_KNOWN_CODE = "NotKnown";
	public static final String PLANNED_CODE = "Planned";
	public static final String MAJOR_ISSUES_CODE = "MajorIssues";
	public static final String MINOR_ISSUES_CODE = "MinorIssues";
	public static final String ON_TRACK_CODE = "OnTrack";
	public static final String COMPLETED_CODE = "Completed";
	public static final String ABANDONED_CODE = "Abandoned";
}
