/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogfields;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ObjectReadonlyChoiceField extends ObjectDataInputField
{
	public ObjectReadonlyChoiceField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse);
		
		question = questionToUse;
		component = new OpaqueLabel("");
		Border lineBorder = new LineBorder(Color.BLACK);
		Border emptyBorder = new EmptyBorder(3, 3, 3, 3);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		component.setBorder(border);
		component.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		component.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		getComponent().setFocusable(false);

	}
	
	@Override
	public void setText(String code)
	{
		ChoiceItem choice = getRatingChoice(code);
		currentCode = code;
		String text = "";
		Icon icon = null;
		Color color = EAM.READONLY_BACKGROUND_COLOR;
		if(choice != null)
		{
			text = choice.getLabel();
			icon = RatingIcon.createFromChoice(choice);
			color = choice.getColor();

		}
		component.setText(text);
		component.setIcon(icon);
		component.setBackground(color);
		component.invalidate();
	}

	@Override
	protected boolean shouldSetBackground()
	{
		return false;
	}

	private ChoiceItem getRatingChoice(String text)
	{
		return question.findChoiceByCode(text);
	}

	@Override
	public JComponent getComponent()
	{
		return component;
	}

	@Override
	public String getText()
	{
		return currentCode;
	}

	class OpaqueLabel extends PanelTitleLabel
	{
		public OpaqueLabel(String text)
		{
			super(text);
			setOpaque(true);
		}
	}

	String currentCode;
	ChoiceQuestion question;
	UiLabel component;
}
