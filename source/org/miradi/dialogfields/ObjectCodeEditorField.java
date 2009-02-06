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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.MiradiScrollPane;

public class ObjectCodeEditorField extends ObjectDataInputField implements ListSelectionListener
{
	public ObjectCodeEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse, 3);
	}
	
	public ObjectCodeEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		codeListEditor = new CodeListComponent(questionToUse, columnCount, this);
		component = new MiradiScrollPane(codeListEditor);
		Dimension preferredSize = component.getPreferredSize();
		final int ARBITRARY_REASONABLE_MAX_WIDTH = 800;
		final int ARBITRARY_REASONABLE_MAX_HEIGHT = 600;
		int width = Math.min(preferredSize.width, ARBITRARY_REASONABLE_MAX_WIDTH);
		int height = Math.min(preferredSize.height, ARBITRARY_REASONABLE_MAX_HEIGHT);
		component.getViewport().setPreferredSize(new Dimension(width, height));
	}
	
	public JComponent getComponent()
	{
		return component;
	}

	public String getText()
	{
		return codeListEditor.getText();
	}

	public void setText(String codes)
	{
		codeListEditor.setText(codes);
	}
	
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
	
	CodeListComponent codeListEditor;
	MiradiScrollPane component; 
}
