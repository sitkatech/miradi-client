/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.miradi.dialogs.fieldComponents.RadioButtonWithChoiceItemProvider;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class SingleSelectionEditorComponentWithHierarchies extends AbstractEditorComponentWithHierarchies
{
	public SingleSelectionEditorComponentWithHierarchies(ChoiceQuestion questionToUse)
	{
		super(questionToUse);
		
		initializeWithDefaultChoice(questionToUse);
	}

	private void initializeWithDefaultChoice(ChoiceQuestion questionToUse)
	{
		setText(new CodeList(new String[]{""}).toString());
	}
	
	@Override
	protected JToggleButton createToggleButton(ChoiceItem choiceItem)
	{
		JRadioButton radioButton = new RadioButtonWithChoiceItemProvider(choiceItem);
		groupRadioButton(radioButton);
		
		return radioButton;
	}

	private void groupRadioButton(JRadioButton radioButton)
	{
		if (group == null)
			group = new ButtonGroup();
	
		group.add(radioButton);
	}
	
	@Override
	protected boolean isRootChoiceItemSelectable()
	{
		return true;
	}

	private ButtonGroup group; 
}
