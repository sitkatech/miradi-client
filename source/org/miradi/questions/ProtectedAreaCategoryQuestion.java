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


public class ProtectedAreaCategoryQuestion extends StaticChoiceQuestion
{
	public ProtectedAreaCategoryQuestion()
	{
		super(getStatuses());
	}

	static ChoiceItem[] getStatuses()
	{
		return new ChoiceItem[] {
				new ChoiceItem("Ia", "Category Ia: Strict nature reserve/wilderness protection area"),
				new ChoiceItem("Ib", "Category Ib: Wilderness area"),
				new ChoiceItem("II", "Category II: National park"),
				new ChoiceItem("III", "Category III: Natural monument"),
				new ChoiceItem("IV", "Category IV: Habitat/Species Management Area"),
				new ChoiceItem("V", "Category V: Protected Landscape/Seascape"),
				new ChoiceItem("VI", "Category VI: Managed Resource Protected Area"),
		};
	}
}
