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

import org.miradi.main.EAMTestCase;
import org.miradi.questions.ChoiceItem;

public class TestChoiceItem extends EAMTestCase
{
	public TestChoiceItem(String name)
	{
		super(name);
	}
	
	public void testEquals()
	{
		String label1 = "some label";
		String code1 = "SomeCode";
		ChoiceItem choice1 = new ChoiceItem(code1, label1);
		ChoiceItem flippedCodeLabelChoice = new ChoiceItem(label1, code1);
		ChoiceItem nonFlippedCodeLabelChoice = new ChoiceItem(code1, label1);
		
		assertFalse("should not be the same?", choice1.equals(flippedCodeLabelChoice));
		assertTrue("should be the same?", choice1.equals(nonFlippedCodeLabelChoice));
	}
}
