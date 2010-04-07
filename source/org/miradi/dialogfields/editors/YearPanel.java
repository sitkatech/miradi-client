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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;

import com.toedter.calendar.JYearChooser;

public class YearPanel extends OneRowPanel
{
	public YearPanel(int fiscalYearStartMonthToUse, String panelTitle)
	{
		fiscalYearStartMonth = fiscalYearStartMonthToUse;
		yearChooser = new JYearChooser();
		add(new PanelTitleLabel(panelTitle));
		add(new PanelTitleLabel(getFiscalYearLabel()));
		add(yearChooser);
	}

	private String getFiscalYearLabel()
	{
		if (fiscalYearStartMonth == 1)
			return "";
		
		return EAM.text("FY");
	}
	
	public DateUnit getDate()
	{
		MultiCalendar year = MultiCalendar.createFromGregorianYearMonthDay(yearChooser.getYear(), fiscalYearStartMonth, 1);
		return DateUnit.createFiscalYear(year.getGregorianYear(), fiscalYearStartMonth);
	}
		
	private JYearChooser yearChooser;
	private int fiscalYearStartMonth;
}
