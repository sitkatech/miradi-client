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

package org.miradi.dialogfields;

import java.util.Vector;

import org.miradi.dialogfields.RollupReportsObjectsChooserField.ComboBoxChangeHandler;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.project.Project;
import org.miradi.questions.RollupReportsObjectTypeQuestion;
import org.miradi.utils.CodeList;

public class RollupReportsEditorComponent extends MiradiPanel
{
	public RollupReportsEditorComponent(Project projectToUse)
	{
		editors = new Vector<SingleChoiceItemEditor>();
		RollupReportsObjectTypeQuestion question = new RollupReportsObjectTypeQuestion();
		final int UNSPECIFIED_CHOICE_COUNT = 1;
		int NUMBER_OF_LEVELS = question.size() - UNSPECIFIED_CHOICE_COUNT;
		for (int index = 0; index < NUMBER_OF_LEVELS; ++index)
		{
			SingleChoiceItemEditor levelEditor = new SingleChoiceItemEditor(question);
			editors.add(levelEditor);
			add(levelEditor);
		}
	}
	
	public String getText() throws Exception
	{
		CodeList allCodes = new CodeList();
		for(SingleChoiceItemEditor levelEditor : editors)
		{
			allCodes.add(levelEditor.getText());
		}

		return allCodes.toString();
	}

	public void setText(String codes) throws Exception
	{
		CodeList allCodes = new CodeList(codes);
		for (int index = 0; index < allCodes.size(); ++index)
		{
			SingleChoiceItemEditor levelEditor = editors.get(index);
			String singleLevelCode = allCodes.get(index);
			levelEditor.setText(singleLevelCode);
		}
	}
	
	public void addActionListener(ComboBoxChangeHandler comboChangeHandler)
	{
		for(SingleChoiceItemEditor levelEditor : editors)
		{
			levelEditor.addActionListener(comboChangeHandler);
		}
	}
	
	private Vector<SingleChoiceItemEditor> editors;
}
