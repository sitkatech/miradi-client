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

import javax.swing.JPanel;

import org.martus.util.MultiCalendar;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;

import com.jhlabs.awt.BasicGridLayout;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class MonthPanel extends JPanel
{
	public MonthPanel(String title)
	{
		super(new BasicGridLayout(1, 3));
		
		yearChooser = new JYearChooser();
		monthChooser = new JMonthChooser();

		add(new PanelTitleLabel(title));
		add(yearChooser);
		add(monthChooser);
	}
	
	public MultiCalendar getDate()
	{
		return MultiCalendar.createFromGregorianYearMonthDay(yearChooser.getYear(), monthChooser.getMonth(), 1); 
	}
	
	private JMonthChooser monthChooser;
	private JYearChooser yearChooser;
}
