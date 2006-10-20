/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestRatingQuestion extends EAMTestCase
{
	public TestRatingQuestion(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		RatingChoice[] choices = {
			new RatingChoice("", "None", Color.BLACK),
			new RatingChoice("1", "Low", Color.GREEN),
		};
		RatingQuestion question = new RatingQuestion("Feasibility", "Feasibility and Cost", choices);
		assertEquals("Feasibility", question.getTag());
		assertEquals("Feasibility and Cost", question.getLabel());
		assertEquals(2, question.getChoices().length);
	}
}
