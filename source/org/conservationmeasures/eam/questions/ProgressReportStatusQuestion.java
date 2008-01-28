/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

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
