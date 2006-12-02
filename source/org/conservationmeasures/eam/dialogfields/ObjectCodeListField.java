/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.icons.RatingIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.martus.swing.UiList;

public class ObjectCodeListField extends ObjectDataInputField
{
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ResourceRoleQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse.getTag());
		list = new UiList(questionToUse.getChoices());
		list.addListSelectionListener(new ComboChangeHandler());
		addFocusListener();
	}
	
	public JComponent getComponent()
	{
		return list;
	}

	public String getText()
	{
		Object[] selected = list.getSelectedValues();
		if(selected.length==0)
			return "";
		CodeList codeList = new CodeList();
		for (int i=0; i<selected.length; ++i)
		{
			codeList.add(((ChoiceItem)selected[i]).getCode());
		}
		return codeList.toString();
	}

	public void setText(String codes)
	{
		try
		{
			CodeList codeList = new CodeList(codes);
			for(int i = 0; i < list.getModel().getSize(); ++i)
			{
				ChoiceItem choiceItem = (ChoiceItem)list.getModel().getElementAt(i);
				if (codeList.contains(choiceItem.getCode()))
					list.addSelectionInterval(i,i);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Internal Error"));
		}
		
	}
	
	public void updateEditableState()
	{
		list.setEnabled(isValidObject());
		if(isValidObject())
		{
			list.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
			list.setBackground(EAM.EDITABLE_BACKGROUND_COLOR);
		}
		else
		{
			list.setForeground(EAM.READONLY_FOREGROUND_COLOR);
			list.setBackground(EAM.READONLY_BACKGROUND_COLOR);
			
		}
	}
	
	class RatingChoiceRenderer extends DefaultListCellRenderer
	{
		public Component getListCellRendererComponent(JList listToUse, Object value, int index, boolean isSelected, boolean cellHasFocus) 
		{
			Component cell = super.getListCellRendererComponent(listToUse, value, index, isSelected,	cellHasFocus);
			ChoiceItem thisOption = (ChoiceItem)value;
			setIcon(RatingIcon.createFromChoice(thisOption));
			return cell;
		}
	}
	
	class ComboChangeHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent arg0)
		{
			setNeedsSave();
			saveIfNeeded();
		}
	}

	UiList list;
}
