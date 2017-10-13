/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

public class ObjectRadioButtonGroupField extends ObjectDataInputField
{
	public ObjectRadioButtonGroupField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse);

		question = questionToUse;
		group = new ButtonGroup();
		panel = new OneRowPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		buttonsByCode = new HashMap<String, JRadioButton>();
		listSelectionListeners = new Vector<ListSelectionListener>();

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

	public void addListSelectionListener(ListSelectionListener listSelectionListenerToAdd)
	{
		if (!listSelectionListeners.contains(listSelectionListenerToAdd))
			listSelectionListeners.add(listSelectionListenerToAdd);
	}

	public void removeListSelectionListener(ListSelectionListener listSelectionListenerToRemove)
	{
		listSelectionListeners.remove(listSelectionListenerToRemove);
	}

	@Override
	public JComponent getComponent()
	{
		return panel;
	}

	@Override
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

	@Override
	public void setText(String newValue)
	{
		JRadioButton button = buttonsByCode.get(newValue);
		button.setSelected(true);
	}
	
	void buttonWasPressed(String newCode)
	{
		saveSelection();

		for(ListSelectionListener listSelectionListener : listSelectionListeners)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(newCode);
			ChoiceItemListSelectionEvent event = new ChoiceItemListSelectionEvent(choiceItem, 0, 0, false);
			listSelectionListener.valueChanged(event);
		}
	}
	
	@Override
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

	ChoiceQuestion question;
	private JPanel panel;
	private ButtonGroup group;
	HashMap<String,JRadioButton> buttonsByCode;
	private Vector<ListSelectionListener> listSelectionListeners;
}
