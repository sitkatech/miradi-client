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

import java.text.ParseException;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class StringMapBudgetColumnCodeListEditorField extends AbstractChoiceItemListEditorField
{
	public StringMapBudgetColumnCodeListEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse, 1);
	}

	public String getText()
	{
		try
		{
			return getStringMapAsString();
		}
		catch (Exception e)
		{
			EAM.unexpectedErrorDialog();
			EAM.logException(e);
			return "";
		}
	}

	private String getStringMapAsString() throws Exception
	{
		StringMap existingMap = new StringMap(getProject().getObjectData(getORef(), getTag()));
		existingMap.add(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY, getComponentText());
		
		return existingMap.toString();
	}

	public void setText(String stringMapAsString)
	{
		CodeList codes = createCodeListFromString(stringMapAsString);
		super.setText(codes.toString());
	}

	private CodeList createCodeListFromString(String StringMapAsString)
	{
		try
		{
			StringMap stringMap = new StringMap(StringMapAsString);
			String codeListAsString = stringMap.get(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
			
			return new CodeList(codeListAsString);
		}
		catch(ParseException e)
		{
			EAM.unexpectedErrorDialog();
			EAM.logException(e);
			return new CodeList();
		}
	}
}
