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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class StringMapBudgetColumnCodeListEditorField extends AbstractStringMapCodeListEditorField
{
	public StringMapBudgetColumnCodeListEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
	}

	@Override
	protected String getStringMapAsString() throws Exception
	{
		AbstractStringKeyMap existingMap = createEmptyStringKeyMap();
		existingMap.put(getMapCode(), getComponentText());
		
		return existingMap.toString();
	}
	
	private AbstractStringKeyMap createEmptyStringKeyMap() throws Exception
	{
		return new StringStringMap(getProject().getObjectData(getORef(), getTag()));
	}

	@Override
	public void setText(String stringMapAsString)
	{
		CodeList codes = createCodeListFromString(stringMapAsString);
		super.setText(codes.toString());
	}

	private CodeList createCodeListFromString(String StringMapAsString)
	{
		try
		{
			AbstractStringKeyMap stringMap = new StringStringMap(StringMapAsString);
			String codeListAsString = stringMap.get(getMapCode());
			
			return new CodeList(codeListAsString);
		}
		catch(ParseException e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
			return new CodeList();
		}
	}
}
