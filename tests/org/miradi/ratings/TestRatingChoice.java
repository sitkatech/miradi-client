/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.ratings;

import java.awt.Color;

import org.miradi.main.EAMTestCase;
import org.miradi.questions.ChoiceItem;

public class TestRatingChoice extends EAMTestCase
{

	public TestRatingChoice(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		ChoiceItem choice = new ChoiceItem("4", "Very High", Color.RED);
		assertEquals("4", choice.getCode());
		assertEquals("Very High", choice.getLabel());
		assertEquals(Color.RED, choice.getColor());
	}
}
