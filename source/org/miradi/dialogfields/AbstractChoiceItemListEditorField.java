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

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

abstract public class AbstractChoiceItemListEditorField extends ObjectDataInputField implements ListSelectionListener
{
	public AbstractChoiceItemListEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse, 3);
	}
	
	public AbstractChoiceItemListEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		this(projectToUse, refToUse.getObjectType(), refToUse.getObjectId(), tagToUse, questionToUse, columnCount);
	}
	
	public AbstractChoiceItemListEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);

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
		return getComponentText();
	}

	protected String getComponentText()
	{
		return codeListEditor.getText();
	}

	@Override
	public void setText(String codes)
	{
		codeListEditor.setText(codes);
	}

	@Override
	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		codeListEditor.setEnabled(editable);
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
