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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.MiradiScrollPane;

import com.jhlabs.awt.BasicGridLayout;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class AbstractQuestionEditorComponent extends DisposablePanel
{	
	public AbstractQuestionEditorComponent(ChoiceQuestion questionToUse)
	{
		this(questionToUse, SINGLE_COLUMN);
	}
	
	public AbstractQuestionEditorComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		setLayout(new BasicGridLayout(0,columnCount));
		question = questionToUse;
		choiceItemToToggleButtonMap = new HashMap<ChoiceItem, JToggleButton>();
		
		rebuildToggleButtonsBoxes();
	}
	
	@Override
	public void dispose()
	{
		removeAll();
		clearChoiceItemToToggleButtonMap();
		
		super.dispose();
	}
	
	private void clearChoiceItemToToggleButtonMap()
	{
		choiceItemToToggleButtonMap.clear();
	}
	
	protected void rebuildToggleButtonsBoxes()
	{
		removeAll();
		addAdditionalComponent();
		ChoiceItem[] choices = getQuestion().getChoices();
		clearChoiceItemToToggleButtonMap();
		MiradiPanel toggleButtonsPanel = new MiradiPanel(new GridLayoutPlus(0, 3)); 
		toggleButtonsPanel.setBackground(getTogglePanelBackgroundColor());
		for (int index = 0; index < choices.length; ++index)
		{
			ChoiceItem choiceItem = choices[index];
			JToggleButton toggleButton = createToggleButton(choiceItem.getLabel());
			toggleButton.setBackground(choiceItem.getColor());
			toggleButton.addActionListener(new ToggleButtonHandler());
			choiceItemToToggleButtonMap.put(choiceItem, toggleButton);
			Icon icon = choiceItem.getIcon();
			toggleButtonsPanel.add(getSafeIconLabel(icon));
			toggleButtonsPanel.add(toggleButton);
			toggleButtonsPanel.add(new PanelTitleLabel(choiceItem.getDescription()));
		}
	
		add(new MiradiScrollPane(toggleButtonsPanel));
		revalidate();
		repaint();
	}

	protected Color getTogglePanelBackgroundColor()
	{
		return AppPreferences.getDataPanelBackgroundColor();
	}

	private PanelTitleLabel getSafeIconLabel(Icon icon)
	{
		if (icon == null)
			return new PanelTitleLabel();
		
		return new PanelTitleLabel(icon);
	}

	protected JToggleButton createToggleButton(String label)
	{
		return new JCheckBox(label);
	}

	protected void addAdditionalComponent()
	{
	}

	@Override
	public void setEnabled(boolean isValidObject)
	{
		super.setEnabled(isValidObject);
		Set<ChoiceItem> choices = choiceItemToToggleButtonMap.keySet();
		for(ChoiceItem choiceItem : choices)
		{
			JToggleButton toggleButton = choiceItemToToggleButtonMap.get(choiceItem);
			updateEditableState(toggleButton,isValidObject);
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
	
	class ToggleButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				JToggleButton item = (JToggleButton) event.getSource();
				ChoiceItem choiceItem = getQuestion().findChoiceByLabel(item.getText());
				valueChanged(choiceItem, item.isSelected());
			}
			catch (Exception e)
			{
				EAM.logException(e);
				//TODO does this need to notify user with error dialog?
			}			
		}
	}
	
	private ChoiceQuestion question;
	protected HashMap<ChoiceItem, JToggleButton> choiceItemToToggleButtonMap;
	protected static final int SINGLE_COLUMN = 1;
}
