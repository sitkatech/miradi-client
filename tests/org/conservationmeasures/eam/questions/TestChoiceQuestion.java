/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.awt.Color;

import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objects.DiagramFactor;

public class TestChoiceQuestion extends TestCaseWithProject
{
	public TestChoiceQuestion(String name)
	{
		super(name);
	}
	
	public void testFindChoiceByLabel()
	{
		DiagramFactorColorQuestion fontColorQuestion = new DiagramFactorColorQuestion(DiagramFactor.TAG_FONT_COLOR);
		assertEquals("wrong choice?", null, fontColorQuestion.findChoiceByLabel("invalidLabel"));
		assertEquals(new ChoiceItem("White", "White", Color.WHITE), fontColorQuestion.findChoiceByLabel("White"));
	}
}
