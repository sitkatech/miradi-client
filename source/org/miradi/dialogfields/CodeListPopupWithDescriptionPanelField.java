/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogfields.editors.SplitterPanelWithStaticRightSideTextPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.questions.ChoiceQuestion;

public class CodeListPopupWithDescriptionPanelField extends	AbstractEditableCodeListField
{
	public CodeListPopupWithDescriptionPanelField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse, questionToUse);
		
		mainWindow = mainWindowToUse;
	}
	
	@Override
	public DisposablePanel createEditorPanel() throws Exception
	{
		SingleLevelQuestionEditor leftComponent = new SingleLevelQuestionEditor(mainWindow, question);
		ComponentWrapperObjectDataInputField field = new ComponentWrapperObjectDataInputField(getProject(), getORef(), getTag(), leftComponent);
		OneFieldObjectDataInputPanel leftPanel = new OneFieldObjectDataInputPanel(getProject(), getORef(), getTag(), field);
		if (leftComponent.getQuestion().hasLongDescriptionProvider())
			return new SplitterPanelWithStaticRightSideTextPanel(mainWindow, leftPanel);

		return createScrollingPanel(leftPanel);
	}

	@Override
	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyMultiChoiceComponent(questionToUse, columnCount);
	}
	
	private MainWindow mainWindow;
}
