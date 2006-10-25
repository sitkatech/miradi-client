/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiLabel;

public class RatingLabel extends UiLabel
{
	public RatingLabel(RatingQuestion questionToUse)
	{
		question = questionToUse;
		Border lineBorder = new LineBorder(Color.BLACK);
		Border emptyBorder = new EmptyBorder(3, 3, 1, 1);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		setBorder(border);
		
	}
	
	public void setText(String code)
	{
		RatingChoice choice = getRatingChoice(code);

		String text = "";
		Icon icon = null;
		if(choice != null)
		{
			text = choice.getLabel();
			icon = new RatingIcon(choice);
		}
		super.setText(text);
		super.setIcon(icon);
	}

	private RatingChoice getRatingChoice(String text)
	{
		if(question == null)
			return null;
		return question.findChoiceByCode(text);
	}

	RatingQuestion question;
}
