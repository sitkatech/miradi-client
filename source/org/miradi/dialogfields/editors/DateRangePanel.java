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
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.utils.DateRange;

abstract public class DateRangePanel extends TwoColumnPanel
{
	public DateRange getDateRange() throws Exception
	{
		return new DateRange(getStartDate(), getEndDate());
	}
	
	protected static String getStartText()
	{
		return EAM.text("Start:");
	}
	
	protected static String getEndText()
	{
		return EAM.text("End:");
	}
	
	abstract protected MultiCalendar getStartDate();

	abstract protected MultiCalendar getEndDate();

	abstract public String getPanelDescription();
}
