/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.martus.swing.UiRadioButton;

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
			UiRadioButton button = new UiRadioButton("");
			group.add(button);
			buttons.add(button);
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
		setNeedsSave();
		saveIfNeeded();
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
