/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import org.martus.swing.UiRadioButton;
import org.miradi.ids.BaseId;
import org.miradi.main.AppPreferences;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class RadioButtonsField extends ObjectDataInputField
{
	public RadioButtonsField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		question = questionToUse;
		group = new ButtonGroup();
		buttons = new Vector<UiRadioButton>();
		for(int i = 0; i < question.size(); ++i)
		{
			UiRadioButton button = new UiRadioButton(question.getChoices()[i].getLabel());
			button.setBackground(AppPreferences.getDataPanelBackgroundColor());
			group.add(button);
			buttons.add(button);
			if(question.getCode(i).equals(""))
				button.setSelected(true);
			button.addActionListener(new RadioChangeHandler());
		}
	}
	
	public int size()
	{
		return buttons.size();
	}
	
	public JComponent getComponent(int index)
	{
		return buttons.get(index);
	}

	public JComponent getComponent()
	{
		return null;
	}
	
	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		for(JComponent button : buttons)
		{
			button.setEnabled(editable);
		}
	}

	public String getText()
	{
		for(int i = 0; i < question.size(); ++i)
		{
			if(buttons.get(i).isSelected())
				return question.getCode(i);
		}
		
		return "";
	}

	public void setText(String newValue)
	{
		for(int i = 0; i < question.size(); ++i)
		{
			boolean shouldSelect = false;
			if(question.getCode(i).equals(newValue))
				shouldSelect = true;
			
			buttons.get(i).setSelected(shouldSelect);
		}
	}

	public void saveSelection()
	{
		forceSave();
	}
	
	class RadioChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			saveSelection();
		}
	}
	
	private ChoiceQuestion question;
	private ButtonGroup group;
	private Vector<UiRadioButton> buttons; 
}
