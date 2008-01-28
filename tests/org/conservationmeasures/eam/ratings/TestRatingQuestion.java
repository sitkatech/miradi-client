/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.StaticChoiceQuestion;

public class TestRatingQuestion extends EAMTestCase
{
	public TestRatingQuestion(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		ChoiceItem[] choices = {
			new ChoiceItem("", "None", Color.BLACK),
			new ChoiceItem("1", "Low", Color.GREEN),
		};
		ChoiceQuestion question = new StaticChoiceQuestion("Feasibility", choices);
		assertEquals("Feasibility", question.getTag());
		assertEquals(2, question.getChoices().length);
	}
}
