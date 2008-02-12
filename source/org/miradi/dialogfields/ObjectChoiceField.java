/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.martus.swing.UiComboBox;
import org.miradi.dialogs.fieldComponents.PanelComboBox;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class ObjectChoiceField extends ObjectDataInputField
{
	public ObjectChoiceField(Project projectToUse, int objectType, BaseId objectId, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, tagToUse);
		combo = new PanelComboBox(questionToUse.getChoices());
		addFocusListener();
		combo.addActionListener(createActionHandler());
	}

	ComboChangeHandler createActionHandler()
	{
		return new ComboChangeHandler();
	}
	
	public JComponent getComponent()
	{
		return combo;
	}

	public String getText()
	{
		ChoiceItem selected = (ChoiceItem)combo.getSelectedItem();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	public void setText(String code)
	{
		for(int i = 0; i < combo.getItemCount(); ++i)
		{
			ChoiceItem choice = (ChoiceItem)combo.getItemAt(i);
			if(choice.getCode().equals(code))
			{
				combo.setSelectedIndex(i);
				return;
			}
		}
		combo.setSelectedIndex(-1);
	}

	public void updateEditableState()
	{
		combo.setEnabled(isValidObject());
		if(isValidObject())
		{
			combo.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
			combo.setBackground(EAM.EDITABLE_BACKGROUND_COLOR);
		}
		else
		{
			combo.setForeground(EAM.READONLY_FOREGROUND_COLOR);
			combo.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		}
	}

	public void saveSelection()
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	class ComboChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			saveSelection();
		}
	}
	
	UiComboBox combo;
}
