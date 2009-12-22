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

import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionListener;

import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class SingleItemSelectableCodeListEditorComponent extends CodeListFieldComponent
{
	public SingleItemSelectableCodeListEditorComponent(ChoiceQuestion questionToUse, ListSelectionListener listener)
	{
		super(questionToUse, COLUMN_COUNT, listener);
	}
	
	@Override
	protected boolean isSingleSelection()
	{
		return true;
	}
	
	@Override
	protected JToggleButton createToggleButton(ChoiceItem[] choices, int i)
	{
		return new JRadioButton(choices[i].getLabel());
	}

	@Override
	protected JToggleButton[] createToggleButtons(ChoiceItem[] choices)
	{
		return new JRadioButton[choices.length];
	}
	
	private static final int COLUMN_COUNT = 1;
}
