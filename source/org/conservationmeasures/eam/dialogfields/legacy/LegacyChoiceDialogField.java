/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields.legacy;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.RatingQuestion;
import org.martus.swing.UiComboBox;

public class LegacyChoiceDialogField extends LegacyDialogField
{
	public LegacyChoiceDialogField(RatingQuestion question)
	{
		super(question.getTag(), question.getLabel());
		combo = new UiComboBox(question.getChoices());
		combo.setRenderer(new RatingChoiceRenderer());
	}
	
	public Component getComponent()
	{
		return combo;
	}
	
	public void selectCode(String code)
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

	public String getText()
	{
		RatingChoice selected = (RatingChoice)combo.getSelectedItem();
		if(selected == null)
			return "";
		return selected.getCode();
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
	
	UiComboBox combo;
}
