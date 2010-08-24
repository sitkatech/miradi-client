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

import java.awt.Color;

import org.miradi.main.TestCaseWithProject;
import org.miradi.utils.CodeList;

public class TestChoiceQuestion extends TestCaseWithProject
{
	public TestChoiceQuestion(String name)
	{
		super(name);
	}
	
	public void testFindChoiceByLabel()
	{
		DiagramFactorFontColorQuestion fontColorQuestion = new DiagramFactorFontColorQuestion();
		assertEquals("wrong choice?", null, fontColorQuestion.findChoiceByLabel("invalidLabel"));
		assertEquals(new ChoiceItem("#FFFFFF", "White", Color.WHITE), fontColorQuestion.findChoiceByLabel("White"));
	}
	
	public void testGetAllCodes()
	{
		StatusQuestion question = new StatusQuestion();
		assertEquals("wrong code count?", 5, question.getAllCodes().size());
		CodeList allCodes = question.getAllCodes();
		assertContains("Could not find code for poor?", StatusQuestion.POOR, allCodes.toVector());
		assertContains("Could not find code for fair?", StatusQuestion.FAIR, allCodes.toVector());
		assertContains("Could not find code for good?", StatusQuestion.GOOD, allCodes.toVector());
		assertContains("Could not find code for very good?", StatusQuestion.VERY_GOOD, allCodes.toVector());
	}
}
