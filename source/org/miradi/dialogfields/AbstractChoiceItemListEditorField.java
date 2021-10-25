/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

abstract public class AbstractChoiceItemListEditorField extends ObjectDataInputField implements ListSelectionListener
{
	public AbstractChoiceItemListEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		this(projectToUse, refToUse, tagToUse, questionToUse, 3);
	}
	
	public AbstractChoiceItemListEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, refToUse, tagToUse);

		codeListEditor = createCodeListEditor(questionToUse, columnCount);
		codeListEditor.addListSelectionListener(this);
	}

	protected QuestionBasedEditorComponent createCodeListEditor(ChoiceQuestion questionToUse, int columnCount)
	{
		return new QuestionBasedEditorComponent(questionToUse, columnCount);
	}

	@Override
	public JComponent getComponent()
	{
		return codeListEditor;
	}

	@Override
	public String getText()
	{
		return codeListEditor.getText();
	}

	@Override
	public void setText(String codes)
	{
		codeListEditor.setText(codes);
	}

	@Override
	protected boolean shouldBeEditable()
	{
		return allowEdits() && isValidObject();
	}
	
	@Override
	protected boolean shouldSetBackground()
	{
		// NOTE: Radio buttons and checkboxes are transparent, so never set the background
		return false;
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		forceSave();
	}

	public void setDisabledCodes(CodeList codesToDiable)
	{
		codeListEditor.setDisabledCodes(codesToDiable);
	}
	
	public QuestionBasedEditorComponent getCodeListEditor()
	{
		return codeListEditor;
	}
	
	protected QuestionBasedEditorComponent codeListEditor;
}
