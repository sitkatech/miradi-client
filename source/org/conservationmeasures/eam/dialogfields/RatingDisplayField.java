/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import org.conservationmeasures.eam.ratings.RatingQuestion;

public class RatingDisplayField extends DialogField
{
	public RatingDisplayField(RatingQuestion questionToUse)
	{
		super("", "");
		labelComponent = new RatingLabel(questionToUse);
	}
	
	public Component getComponent()
	{
		return labelComponent;
	}

	public String getText()
	{
		return null;
	}

	RatingLabel labelComponent;
}
