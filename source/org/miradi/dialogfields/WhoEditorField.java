/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditorWithMainScrollPane;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProjectResourceQuestion;
import org.miradi.utils.CodeList;

import javax.swing.*;

abstract public class WhoEditorField extends ObjectDataField implements ReadonlyPanelAndPopupEditorProvider
{
	public WhoEditorField(MainWindow mainWindow, ORef refToUse)
	{
		super(mainWindow.getProject(), refToUse);

		readonlyPanelWithPopupEditor = new ReadonlyPanelWithPopupEditorWithMainScrollPane(this, getPanelTitle(), new ProjectResourceQuestion(getProject()));
	}
	
	@Override
	public void updateFromObject()
	{
		updateReadonlyField();
		updateEditableButtonState();
	}
	
	private void updateReadonlyField()
	{
		readonlyPanelWithPopupEditor.setText("");
		if (super.isInvalidObject())
			return;

		BaseObject baseObject = BaseObject.find(getProject(), getORef());
		CodeList whoTotals = getWhoTotalsCodeList(baseObject);
		readonlyPanelWithPopupEditor.setText(whoTotals.toString());
	}

	abstract protected CodeList getWhoTotalsCodeList(BaseObject baseObject);

	abstract protected boolean isWhoCellEditable();

	abstract protected  DisposablePanel createEditorPanel(BaseObject baseObject, ProjectResourceQuestion question);

	abstract protected String getPanelTitle();

	private void updateEditableButtonState()
	{
		readonlyPanelWithPopupEditor.setEnabled(false);
		if (super.isInvalidObject())
			return;

		readonlyPanelWithPopupEditor.setEnabled(isWhoCellEditable());
	}
	
	@Override
	public JComponent getComponent()
	{
		return readonlyPanelWithPopupEditor;
	}

	@Override
	public void saveIfNeeded()
	{
	}
	
	@Override
	public String getTag()
	{
		return "";
	}
	
	public DisposablePanel createEditorPanel() throws Exception
	{
		BaseObject baseObjectForRow = BaseObject.find(getProject(), getORef());
		final ProjectResourceQuestion question = new ProjectResourceQuestion(getProject());
		return createEditorPanel(baseObjectForRow, question);
	}

	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyMultiChoiceComponent(questionToUse, columnCount);
	}
	
	private ReadonlyPanelWithPopupEditor readonlyPanelWithPopupEditor;
}
