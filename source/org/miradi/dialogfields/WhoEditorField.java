/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ReadonlyPanelWithPopupEditor;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.questions.BaseObjectQuestionWithExcludedRef;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProjectResourceQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.CodeList;

public class WhoEditorField extends ObjectDataField implements ReadonlyPanelAndPopupEditorProvider
{
	public WhoEditorField(MainWindow mainWindow, ORef refToUse)
	{
		super(mainWindow.getProject(), refToUse);

		readonlyPanelWithPopupEditor = new ReadonlyPanelWithPopupEditor(this, EAM.text("Select Project Resources"), new ProjectResourceQuestion(getProject()), 1);		
	}
	
	@Override
	public void updateFromObject()
	{
		getComponent().setEnabled(isValidObject());
		if (!isValidObject())
			return;

		BaseObject baseObject = BaseObject.find(getProject(), getORef());
		CodeList whoTotals = WhoCodeListEditorComponent.getWhoTotalCodes(baseObject);
		readonlyPanelWithPopupEditor.setText(whoTotals.toString());
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
		final BaseObjectQuestionWithExcludedRef question = new BaseObjectQuestionWithExcludedRef(getProject(), ProjectResourceSchema.getObjectType(), baseObjectForRow.getLeaderResourceRef());		
		return new WhoCodeListEditorComponent(baseObjectForRow, question);		
	}

	public AbstractReadonlyChoiceComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyMultiChoiceComponent(questionToUse, columnCount);
	}
	
	private ReadonlyPanelWithPopupEditor readonlyPanelWithPopupEditor;
}
