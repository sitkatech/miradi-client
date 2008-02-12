/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.martus.swing.UiLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ObjectReadonlyChoiceField extends ObjectDataInputField
{
	public ObjectReadonlyChoiceField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		question = questionToUse;
		
		component = new PanelTitleLabel("");
		Border lineBorder = new LineBorder(Color.BLACK);
		Border emptyBorder = new EmptyBorder(3, 3, 3, 3);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		component.setBorder(border);
		component.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		component.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		getComponent().setFocusable(false);

	}
	
	public void setText(String code)
	{
		ChoiceItem choice = getRatingChoice(code);
		currentCode = code;
		String text = "";
		Icon icon = null;
		if(choice != null)
		{
			text = choice.getLabel();
			icon = RatingIcon.createFromChoice(choice);
		}
		component.setText(text);
		component.setIcon(icon);
		component.invalidate();
	}

	private ChoiceItem getRatingChoice(String text)
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
	ChoiceQuestion question;
	UiLabel component;
}
