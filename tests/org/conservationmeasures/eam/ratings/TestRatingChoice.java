/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.ratings;

import java.awt.Color;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestRatingChoice extends EAMTestCase
{

	public TestRatingChoice(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		RatingChoice choice = new RatingChoice(4, "Very High", Color.RED);
		assertEquals(4, choice.getNumericValue());
		assertEquals("Very High", choice.getLabel());
		assertEquals(Color.RED, choice.getColor());
	}
}
