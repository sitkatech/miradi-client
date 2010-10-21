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
import java.util.Comparator;

import org.miradi.main.EAM;

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
				new ChoiceItem(NOT_SPECIFIED, EAM.text("Not Specified"), Color.WHITE),
				new ChoiceItem(PLANNED_CODE, getPlannedLabel(), Color.WHITE),
				new ChoiceItem(MAJOR_ISSUES_CODE, getMajorIssuesLabel(), COLOR_ALERT),
				new ChoiceItem(MINOR_ISSUES_CODE, getMinorIssuesLabel(), COLOR_CAUTION),
				new ChoiceItem(ON_TRACK_CODE, getOnTrackLabel(), COLOR_OK),
				new ChoiceItem(COMPLETED_CODE, getCompletedLabel(), COLOR_GREAT),
				new ChoiceItem(ABANDONED_CODE, getAbandonedLabel(), Color.WHITE),
		};
	}
	
	@Override
	public Comparator<ChoiceItem> getComparator()
	{
		return new ByNaturalOrderComparator();
	}
	
	private class ByNaturalOrderComparator implements Comparator<ChoiceItem>
	{
		public int compare(ChoiceItem choiceItem1, ChoiceItem choiceItem2)
		{
			Integer index1 = findIndexByCode(choiceItem1);
			Integer index2 = findIndexByCode(choiceItem2);
				
			return index1.compareTo(index2);
		}
	}
	
	abstract protected String getAbandonedLabel();
	
	abstract protected String getCompletedLabel();

	abstract protected String getOnTrackLabel();

	abstract protected String getMinorIssuesLabel();

	abstract protected String getMajorIssuesLabel();

	abstract protected String getPlannedLabel();

	public static final String NOT_SPECIFIED = "";
	public static final String PLANNED_CODE = "Planned";
	public static final String MAJOR_ISSUES_CODE = "MajorIssues";
	public static final String MINOR_ISSUES_CODE = "MinorIssues";
	public static final String ON_TRACK_CODE = "OnTrack";
	public static final String COMPLETED_CODE = "Completed";
	public static final String ABANDONED_CODE = "Abandoned";
}
