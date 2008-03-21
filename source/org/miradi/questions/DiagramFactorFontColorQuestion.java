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


public class DiagramFactorFontColorQuestion extends StaticChoiceQuestion
{
	public DiagramFactorFontColorQuestion()
	{
		super(getColorChoices());
	}
	
	static ChoiceItem[] getColorChoices()
	{
		return new ChoiceItem[] {
			new ChoiceItem("", "Black (Default)"),
			new ChoiceItem("#4E4848", "Dark Gray"),
			new ChoiceItem("#6D7B8D", "Light Gray"),
			new ChoiceItem("#C85A17", "Brown"),
			new ChoiceItem("#EDE275", "Tan"),
			new ChoiceItem("#FFFFFF", "White"),
			new ChoiceItem("#FF0000", "Red"),
			new ChoiceItem("#FF00FF", "Pink"),
			new ChoiceItem("#FF8040", "Orange"),
			new ChoiceItem("#FFFF00", "Yellow"),
			new ChoiceItem("#254117", "Dark Green"),
			new ChoiceItem("#5FFB17", "Light Green"),
			new ChoiceItem("#0000CC", "Dark Blue"),
			new ChoiceItem("#00CCFF", "Light Blue"),
		};
	}
}
