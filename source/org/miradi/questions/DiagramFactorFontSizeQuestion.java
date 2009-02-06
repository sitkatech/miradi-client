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

import org.miradi.main.EAM;


public class DiagramFactorFontSizeQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontSizeQuestion()
	{
		super(getFontChoices());
	}
	
	static ChoiceItem[] getFontChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem("", EAM.text("Medium (Default)")),
				new ChoiceItem("0.5", EAM.text("Smallest")),
				new ChoiceItem("0.75", EAM.text("Very Small")),
				new ChoiceItem("0.9", EAM.text("Small")),
				new ChoiceItem("1.25", EAM.text("Large")),
				new ChoiceItem("1.75", EAM.text("Very Large")),
				new ChoiceItem("2.5", EAM.text("Largest")),
		};
	}
}
