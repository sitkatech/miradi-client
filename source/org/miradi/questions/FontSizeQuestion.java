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

public class FontSizeQuestion extends StaticChoiceQuestion
{
	public FontSizeQuestion()
	{
		super(getSizeChoices());
	}
	
	static ChoiceItem[] getSizeChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("0", "System Default"),
			new ChoiceItem("6", "6"),
			new ChoiceItem("8", "8"),
			new ChoiceItem("10", "10"),
			new ChoiceItem("11", "11"),
			new ChoiceItem("12", "12"),
			new ChoiceItem("14", "14"),
			new ChoiceItem("18", "18"),
			new ChoiceItem("24", "24"),
		};
	}
}
