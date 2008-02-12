/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
