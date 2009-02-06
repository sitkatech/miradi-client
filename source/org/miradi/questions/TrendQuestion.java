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

import org.miradi.icons.IndicatorIcon;
import org.miradi.main.EAM;
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
				new ChoiceItem("", EAM.text("Not Specified"), new IndicatorIcon()),
				new ChoiceItem("Unknown", EAM.text("Unknown"), new MiradiResourceImageIcon("images/arrows/va_unknown16.png")),
				new ChoiceItem("StrongIncrease", EAM.text("Strong Increase"), new MiradiResourceImageIcon("images/arrows/va_strongup16.png")),
				new ChoiceItem("MildIncrease", EAM.text("Mild Increase"), new MiradiResourceImageIcon("images/arrows/va_mildup16.png")),
				new ChoiceItem("Flat", EAM.text("Flat"), new MiradiResourceImageIcon("images/arrows/va_flat16.png")),
				new ChoiceItem("MildDecrease", EAM.text("Mild Decrease"), new MiradiResourceImageIcon("images/arrows/va_milddown16.png")),
				new ChoiceItem("StrongDecrease", EAM.text("Strong Decrease"), new MiradiResourceImageIcon("images/arrows/va_strongdown16.png")),
		};
	}
}