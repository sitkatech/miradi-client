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

import org.martus.util.MultiCalendar;

public class YearRangeCard extends DateRangePanel
{
	public YearRangeCard()
	{
		startYearPanel = new YearPanel(getStartText());
		endYearPanel = new YearPanel(getEndText());
		
		add(startYearPanel);
		add(endYearPanel);
	}
	
	@Override
	protected MultiCalendar getEndDate()
	{
		return startYearPanel.getDate();
	}

	@Override
	protected MultiCalendar getStartDate()
	{
		return endYearPanel.getDate();
	}

	@Override
	public String getPanelDescription()
	{
		return WhenEditorComponent.YEAR_ITEM;
	}
	
	private YearPanel startYearPanel;
	private YearPanel endYearPanel;
}
