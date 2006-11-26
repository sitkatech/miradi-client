/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiComboBox;

public class ObjectRatingField extends ObjectDataInputField
{
	public ObjectRatingField(Project projectToUse, int objectType, BaseId objectId, RatingQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse.getTag());
		combo = new UiComboBox(questionToUse.getChoices());
		combo.setRenderer(new RatingChoiceRenderer());
		combo.addActionListener(new ComboChangeHandler());
		addFocusListener();
	}

	public JComponent getComponent()
	{
		return combo;
	}

	public String getText()
	{
		RatingChoice selected = (RatingChoice)combo.getSelectedItem();
		if(selected == null)
			return "";
		return selected.getCode();
	}

	public void setText(String code)
	{
		for(int i = 0; i < combo.getItemCount(); ++i)
		{
			RatingChoice choice = (RatingChoice)combo.getItemAt(i);
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
	}

	class RatingChoiceRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(list, value, index, isSelected,	cellHasFocus);
			RatingChoice thisOption = (RatingChoice)value;
			setIcon(new RatingIcon(thisOption));
			return cell;
		}
	}
	
	class ComboChangeHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			setNeedsSave();
			saveIfNeeded();
		}
		
	}
	
	UiComboBox combo;
}
