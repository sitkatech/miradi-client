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

import org.miradi.dialogs.base.CodeListEditorPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class EditableCodeListField extends AbstractEditableCodeListField
{
	public EditableCodeListField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, columnCount);		
	}
	
	@Override
	protected DisposablePanel createEditorPanel() throws Exception
	{
		return new CodeListEditorPanel(getProject(), getORef(), getTag(), question, 1);
	}	

	@Override
	protected AbstractReadOnlyComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadOnlyCodeListComponent(questionToUse.getChoices(), columnCount);
	}
}	
