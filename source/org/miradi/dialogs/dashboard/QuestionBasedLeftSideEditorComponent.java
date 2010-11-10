/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.awt.Color;

import javax.swing.JToggleButton;

import org.miradi.dialogfields.QuestionEditorWithHierarchichalRows;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

import com.jhlabs.awt.GridLayoutPlus;

public class QuestionBasedLeftSideEditorComponent extends QuestionEditorWithHierarchichalRows
{
	public QuestionBasedLeftSideEditorComponent(MainWindow mainWindowToUse, ChoiceQuestion questionToUse)
	{
		super(mainWindowToUse, questionToUse);
	}

	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/1.html";
	}
	
	@Override
	protected MiradiPanel createRowPanel(ChoiceItem choiceItem)
	{
		MiradiPanel rowPanel = new MiradiPanel(new GridLayoutPlus());
		JToggleButton toggleButton = createToggleButton(choiceItem.getLabel());
		toggleButton.setBackground(Color.red);
		toggleButton.addActionListener(new ToggleButtonHandler());		
		SelectableRow selectableRow = new SelectableRow(toggleButton, toggleButton , new StringRowDescriptionProvider(choiceItem.getDescription()));
		selectableRow.addMouseListener(new ClickHandler(selectableRow));
		getSafeSelectableRows().add(selectableRow);
		
		rowPanel.add(toggleButton);
		
		choiceItemToToggleButtonMap.put(choiceItem, toggleButton);
		
		return rowPanel;
	}
}
