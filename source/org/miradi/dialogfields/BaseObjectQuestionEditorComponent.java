/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class BaseObjectQuestionEditorComponent extends AbstractQuestionBasedComponent
{
	public BaseObjectQuestionEditorComponent(final BaseObject parentObjectToUse, final String codeListTagToUse, final ChoiceQuestion questionToUse, final CodeList codesToDisableToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		parentObject = parentObjectToUse;
		codeListTag = codeListTagToUse;
		setDisabledCodes(codesToDisableToUse);
		disableCheckBoxes();
		updateToggleButtonSelections(getCodes(parentObject));
	}
	
	@Override
	public void toggleButtonStateChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getCodes(getParentObject());
		final boolean codeExist = currentCodes.contains(choiceItem.getCode());
		final boolean needToRemoveCode = codeExist && !isSelected;
		final boolean needToAddCode = !codeExist && isSelected;
		if (needToRemoveCode)
			currentCodes.removeCode(choiceItem.getCode());
		
		if (needToAddCode)
			currentCodes.add(choiceItem.getCode());
		
		CommandSetObjectData setCommand = new CommandSetObjectData(getParentObject(), getTag(), currentCodes.toString());
		getProject().executeCommand(setCommand);
	}

	private CodeList getCodes(BaseObject baseObject)
	{		
		try
		{
			return baseObject.getCodeList(getTag());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}

	private String getTag()
	{
		return codeListTag;
	}
	
	private BaseObject getParentObject()
	{
		return parentObject;
	}
	
	private Project getProject()
	{
		return getParentObject().getProject();
	}
	
	private BaseObject parentObject;
	private String codeListTag;
}
