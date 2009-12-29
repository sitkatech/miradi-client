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
import javax.swing.JToggleButton;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractQuestionEditorComponent extends DisposablePanel implements ItemListener
{	
	public AbstractQuestionEditorComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, 1);
	}
	
	public AbstractQuestionEditorComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		setLayout(new BasicGridLayout(0,columnCount));
		question = questionToUse;
		rebuildToggleButtonsBoxes();
	}

	protected void rebuildToggleButtonsBoxes()
	{
		removeAll();
		addAdditinalComponent();
		ChoiceItem[] choices = getQuestion().getChoices();
		choiceItems = new ChoiceItem[choices.length];
		toggleButtons = createToggleButtons(choices);
		
		for (int index=0; index<choices.length; ++index)
		{
			JToggleButton toggleButton = createToggleButton(choices[index].getLabel());
			toggleButton.addItemListener(this);
			choiceItems[index] = choices[index];
			toggleButtons[index] = toggleButton;
			Icon icon = choiceItems[index].getIcon();
			if (icon != null)
				add(new PanelTitleLabel(icon));
			
			add(toggleButton);
		}
	
		revalidate();
		repaint();
	}

	protected JToggleButton createToggleButton(String label)
	{
		return new JCheckBox(label);
	}

	protected JToggleButton[] createToggleButtons(ChoiceItem[] choices)
	{
		return new JCheckBox[choices.length];
	}
	
	protected void addAdditinalComponent()
	{
	}

	public void itemStateChanged(ItemEvent event)
	{
	    if (event.getStateChange() == ItemEvent.SELECTED ||	event.getStateChange() == ItemEvent.DESELECTED)
	    {
	    	JToggleButton item = (JToggleButton) event.getItem();
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

	@Override
	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		for (int index = 0; index<toggleButtons.length; ++index)
		{
			updateEditableState(toggleButtons[index],isValidObject);
		}
	}

	public void updateEditableState(JToggleButton toggleButton, boolean isValidObject)
	{
			toggleButton.setEnabled(isValidObject);
			Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
			Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
			if(!isValidObject)
			{
				fg = EAM.READONLY_FOREGROUND_COLOR;
				bg = EAM.READONLY_BACKGROUND_COLOR;
			}
			toggleButton.setForeground(fg);
			toggleButton.setBackground(bg);
	
	}
	
	protected ChoiceQuestion getQuestion()
	{
		return question;
	}
	
	abstract protected void valueChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception;
	
	protected JToggleButton toggleButtons[];
	protected ChoiceItem choiceItems[];
	private ChoiceQuestion question;
}
