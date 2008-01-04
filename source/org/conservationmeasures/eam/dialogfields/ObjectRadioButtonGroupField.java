/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ObjectRadioButtonGroupField extends ObjectDataInputField
{
	public ObjectRadioButtonGroupField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse.getTag());
		group = new ButtonGroup();
		panel = new OneRowPanel();
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
			if(isValidObject())
			{
				button.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
			}
			else
			{
				button.setForeground(EAM.READONLY_FOREGROUND_COLOR);
			}
		}
	}

	public void saveSelection()
	{
		setNeedsSave();
		saveIfNeeded();
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
