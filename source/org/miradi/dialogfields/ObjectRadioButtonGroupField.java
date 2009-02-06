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
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.miradi.ids.BaseId;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ObjectRadioButtonGroupField extends ObjectDataInputField
{
	public ObjectRadioButtonGroupField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		group = new ButtonGroup();
		panel = new OneRowPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		buttonsByCode = new HashMap();
		
		for(int i = 0; i < questionToUse.size(); ++i)
		{
			ChoiceItem choiceItem = questionToUse.getChoices()[i];
			JRadioButton button = new JRadioButton(choiceItem.getLabel());
			button.addActionListener(new ButtonPressHandler(choiceItem.getCode()));
			group.add(button);
			panel.add(button);
			buttonsByCode.put(choiceItem.getCode(), button);
		}
		addFocusListener();
		setText("");
	}

	public JComponent getComponent()
	{
		return panel;
	}

	public String getText()
	{
		for(String code: buttonsByCode.keySet())
		{
			JRadioButton button = buttonsByCode.get(code);
			if(button.isSelected())
			{
				return code;
			}
		}
		return "";
	}

	public void setText(String newValue)
	{
		JRadioButton button = buttonsByCode.get(newValue);
		button.setSelected(true);
	}
	
	void buttonWasPressed(String newCode)
	{
		saveSelection();
	}
	
	public void updateEditableState()
	{
		for(JRadioButton button : buttonsByCode.values())
		{
			button.setEnabled(isValidObject());
			
			// NOTE: Radio buttons never use the EDITABLE color
			button.setForeground(EAM.READONLY_FOREGROUND_COLOR);
			button.setBackground(AppPreferences.getDataPanelBackgroundColor());
		}
	}

	public void saveSelection()
	{
		forceSave();
	}
	
	class ButtonPressHandler implements ActionListener
	{
		public ButtonPressHandler(String codeToUse)
		{
			code = codeToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			buttonWasPressed(code);
		}

		private String code;

	}

	private JPanel panel;
	private ButtonGroup group;
	HashMap<String,JRadioButton> buttonsByCode;
}
