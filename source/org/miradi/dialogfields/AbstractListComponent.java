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

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JCheckBox;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractListComponent extends DisposablePanel implements ItemListener
{	
	public AbstractListComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, 1);
	}
	
	public AbstractListComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		setLayout(new BasicGridLayout(0,columnCount));
		question = questionToUse;
		rebuildCheckBoxes();
	}

	protected void rebuildCheckBoxes()
	{
		removeAll();
		addAdditinalComponent();
		ChoiceItem[] choices = getQuestion().getChoices();
		choiceItems = new ChoiceItem[choices.length];
		checkBoxes = new PanelCheckBox[choices.length];
		
		for (int i=0; i<choices.length; ++i)
		{
			JCheckBox checkBox = new PanelCheckBox(choices[i].getLabel());
			checkBox.addItemListener(this);
			choiceItems[i] = choices[i];
			checkBoxes[i] = checkBox;
			Icon icon = choiceItems[i].getIcon();
			if (icon != null)
				add(new PanelTitleLabel(icon));
			
			add(checkBox);
		}
	
		revalidate();
		repaint();
	}
	
	protected void addAdditinalComponent()
	{
	}

	public void itemStateChanged(ItemEvent event)
	{
	    if (event.getStateChange() == ItemEvent.SELECTED ||	event.getStateChange() == ItemEvent.DESELECTED)
	    {
	    	PanelCheckBox item = (PanelCheckBox) event.getItem();
	    	ChoiceItem choiceItem = getQuestion().findChoiceByLabel(item.getText());
	    	try
	    	{
	    		valueChanged(choiceItem, item.isSelected());
	    	}
	    	catch (Exception e)
	    	{
	    		EAM.logException(e);
	    		//TODO does this need to notify user with error dialog?
	    	}
	    }
	}

	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		for (int checkBoxIndex = 0; checkBoxIndex<checkBoxes.length; ++checkBoxIndex)
		{
			updateEditableState(checkBoxes[checkBoxIndex],isValidObject);
		}
	}

	public void updateEditableState(JCheckBox checkBox, boolean isValidObject)
	{
			checkBox.setEnabled(isValidObject);
			Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
			Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
			if(!isValidObject)
			{
				fg = EAM.READONLY_FOREGROUND_COLOR;
				bg = EAM.READONLY_BACKGROUND_COLOR;
			}
			checkBox.setForeground(fg);
			checkBox.setBackground(bg);
	
	}
	
	protected ChoiceQuestion getQuestion()
	{
		return question;
	}
	
	abstract protected void valueChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception;
	
	protected JCheckBox checkBoxes[];
	protected ChoiceItem choiceItems[];
	private ChoiceQuestion question;
}
