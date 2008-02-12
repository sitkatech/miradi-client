/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.ratings;

import java.awt.Color;

import org.miradi.main.EAMTestCase;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticChoiceQuestion;

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
		ChoiceQuestion question = new StaticChoiceQuestion(choices);
		assertEquals(2, question.getChoices().length);
	}
}
