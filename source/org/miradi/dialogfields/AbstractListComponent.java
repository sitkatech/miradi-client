/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractListComponent extends JPanel implements ItemListener
{
	public AbstractListComponent(ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		setLayout(new BasicGridLayout(0,columnCount));
		listSelectionListener = listener;
		ChoiceItem[] choices = questionToUse.getChoices();
		choiceItems = new ChoiceItem[choices.length];
		checkBoxes = new PanelCheckBox[choices.length];
		
		for (int i=0; i<choices.length; ++i)
		{
			JCheckBox checkBox = new PanelCheckBox(choices[i].getLabel());
			checkBox.addItemListener(this);
			choiceItems[i] = choices[i];
			checkBoxes[i] = checkBox;
			add(checkBox);
		}
	}
	
	public void itemStateChanged(ItemEvent e)
	{
	    if (e.getStateChange() == ItemEvent.SELECTED || 
	    	e.getStateChange() == ItemEvent.DESELECTED)
	    {
	    	valueChanged();
	    }
	}

	public void valueChanged()
	{
		if (!skipNotice)
		{
			ListSelectionEvent event = new ListSelectionEvent("DUMMY EVENT",0,0, false);
			listSelectionListener.valueChanged(event);
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

	abstract public String getText();
	
	abstract public void setText(String codesToUse); 
	
	protected JCheckBox checkBoxes[];
	protected ChoiceItem choiceItems[];
	protected ListSelectionListener listSelectionListener;
	protected boolean skipNotice;
}
