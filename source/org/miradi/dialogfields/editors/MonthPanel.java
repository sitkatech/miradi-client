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
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneRowPanel;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.project.ProjectCalendar;

public class MonthPanel extends OneRowPanel
{
	public MonthPanel(ProjectCalendar projectCalendar, DateUnit dateUnit, String title)
	{
		yearChooser = new CalendarYearPanel(projectCalendar, getSuperYear(projectCalendar.getFiscalYearFirstMonth(), dateUnit));
		monthChooser = new MonthComboBox(dateUnit);

		add(new PanelTitleLabel(title));
		add(yearChooser);
		add(monthChooser);
	}

	private DateUnit getSuperYear(int fiscalYearFirstMonth, DateUnit dateUnit)
	{
		if (dateUnit != null && dateUnit.isMonth())
			return DateUnit.createFiscalYear(dateUnit.getYear(), fiscalYearFirstMonth);
		
		return dateUnit;
	}
	
	public DateUnit getDate()
	{
		MultiCalendar month = MultiCalendar.createFromGregorianYearMonthDay(yearChooser.getYear(), monthChooser.getMonth(), 1);
		return DateUnit.createMonthDateUnit(month.toIsoDateString()); 
	}
	
	private MonthComboBox monthChooser;
	private YearPanel yearChooser;
}
