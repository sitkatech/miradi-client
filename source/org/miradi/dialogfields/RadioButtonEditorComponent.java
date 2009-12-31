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

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.AppPreferences;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class RadioButtonEditorComponent extends QuestionBasedEditorComponent
{
	public RadioButtonEditorComponent(ChoiceQuestion questionToUse, ListSelectionListener listener)
	{
		super(questionToUse, SINGLE_COLUMN, listener);
		
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	@Override
	protected void addAdditionalComponent()
	{
		super.addAdditionalComponent();
		
		PanelTextArea toggleDescriptionArea = new PanelTextArea(getQuestion().getQuestionDescription());
		toggleDescriptionArea.setWrapStyleWord(true);
		toggleDescriptionArea.setLineWrap(true);
		toggleDescriptionArea.setEditable(false);
		toggleDescriptionArea.setBackground(AppPreferences.getControlPanelBackgroundColor());
		add(toggleDescriptionArea, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	@Override
	protected JToggleButton createToggleButton(String label)
	{
		JRadioButton radioButton = new JRadioButton(label);
		groupRadioButton(radioButton);
		
		return radioButton;
	}
	
	@Override
	public void setText(String codeToUse)
	{
		CodeList codeList = new CodeList(new String[]{codeToUse, });
		updateToggleButtonSelections(codeList);
	}

	private void groupRadioButton(JRadioButton radioButton)
	{
		if (group == null)
			group = new ButtonGroup();
	
		group.add(radioButton);
	}

	private ButtonGroup group; 
}
