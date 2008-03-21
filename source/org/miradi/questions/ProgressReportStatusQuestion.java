/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

public class ProgressReportStatusQuestion extends StaticChoiceQuestion
{
	public ProgressReportStatusQuestion()
	{
		super(getChoiceItems());
	}

	static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", Color.WHITE),
				new ChoiceItem("Planned", "Scheduled for future implementation ", Color.WHITE),
				new ChoiceItem("MajorIssues", "Major Issues - Ongoing, major issues that need attention", COLOR_ALERT),
				new ChoiceItem("MinorIssues", "Minor Issues - Ongoing, has minor issues that need attention", COLOR_CAUTION),
				new ChoiceItem("OnTrack", "On-Track - Ongoing, generally on track", COLOR_OK),
				new ChoiceItem("Completed", "Completed - Successfully accomplished", COLOR_GREAT),
				new ChoiceItem("Abandoned", "Abandoned - No longer relevant or useful", Color.WHITE),
		};
	}
}
