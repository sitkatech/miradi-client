/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiLabel;

public class ObjectReadonlyRatingField extends ObjectDataInputField
{
	public ObjectReadonlyRatingField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, RatingQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse.getTag());
		question = questionToUse;
		
		component = new UiLabel();
		Border lineBorder = new LineBorder(Color.BLACK);
		Border emptyBorder = new EmptyBorder(3, 3, 3, 3);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		component.setBorder(border);
		component.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		component.setBackground(EAM.READONLY_BACKGROUND_COLOR);

	}
	
	public void setText(String code)
	{
		RatingChoice choice = getRatingChoice(code);
		currentCode = code;
		String text = "";
		Icon icon = null;
		if(choice != null)
		{
			text = choice.getLabel();
			icon = new RatingIcon(choice);
		}
		component.setText(text);
		component.setIcon(icon);
		component.invalidate();
	}

	private RatingChoice getRatingChoice(String text)
	{
		return question.findChoiceByCode(text);
	}

	public JComponent getComponent()
	{
		return component;
	}

	public String getText()
	{
		return currentCode;
	}

	public void updateEditableState()
	{
	}

	String currentCode;
	RatingQuestion question;
	UiLabel component;
}
