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
package org.miradi.questions;

import java.awt.Color;

import org.miradi.main.EAM;

public class ProgressReportStatusQuestion extends StaticChoiceQuestion
{
	public ProgressReportStatusQuestion()
	{
		super(getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(NOT_SPECIFIED, EAM.text("Not Specified"), Color.WHITE),
				new ChoiceItem(PLANNED_CODE, EAM.text("Scheduled for future implementation"), Color.WHITE),
				new ChoiceItem(MAJOR_ISSUES_CODE, EAM.text("Major Issues - Ongoing, major issues that need attention"), COLOR_ALERT),
				new ChoiceItem(MINOR_ISSUES_CODE, EAM.text("Minor Issues - Ongoing, has minor issues that need attention"), COLOR_CAUTION),
				new ChoiceItem(ON_TRACK_CODE, EAM.text("On-Track - Ongoing, generally on track"), COLOR_OK),
				new ChoiceItem(COMPLETED_CODE, EAM.text("Completed - Successfully accomplished"), COLOR_GREAT),
				new ChoiceItem(ABANDONED_CODE, EAM.text("Abandoned - No longer relevant or useful"), Color.WHITE),
		};
	}
	
	public static final	String NOT_SPECIFIED = "";
	public static final String PLANNED_CODE = "Planned";
	public static final String MAJOR_ISSUES_CODE = "MajorIssues";
	public static final String MINOR_ISSUES_CODE = "MinorIssues";
	public static final String ON_TRACK_CODE = "OnTrack";
	public static final String COMPLETED_CODE = "Completed";
	public static final String ABANDONED_CODE = "Abandoned";

}
