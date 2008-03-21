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

import org.miradi.icons.IndicatorIcon;
import org.miradi.utils.MiradiResourceImageIcon;


public class TrendQuestion extends StaticChoiceQuestion
{
	public TrendQuestion()
	{
		super(getTrends());
	}

	static ChoiceItem[] getTrends()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", "Not Specified", new IndicatorIcon()),
				new ChoiceItem("Unknown", "Unknown", new MiradiResourceImageIcon("images/arrows/va_unknown16.png")),
				new ChoiceItem("StrongIncrease", "Strong Increase", new MiradiResourceImageIcon("images/arrows/va_strongup16.png")),
				new ChoiceItem("MildIncrease", "Mild Increase", new MiradiResourceImageIcon("images/arrows/va_mildup16.png")),
				new ChoiceItem("Flat", "Flat", new MiradiResourceImageIcon("images/arrows/va_flat16.png")),
				new ChoiceItem("MildDecrease", "Mild Decrease", new MiradiResourceImageIcon("images/arrows/va_milddown16.png")),
				new ChoiceItem("StrongDecrease", "Strong Decrease", new MiradiResourceImageIcon("images/arrows/va_strongdown16.png")),
		};
	}
}