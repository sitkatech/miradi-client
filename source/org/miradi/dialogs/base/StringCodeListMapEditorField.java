/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.base;

import java.text.ParseException;

import org.miradi.dialogfields.AbstractStringMapCodeListEditorField;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringCodeListMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

//FIXME this class has duplication from StringMapBudgetColumnCodeListEditorField.   
public class StringCodeListMapEditorField extends AbstractStringMapCodeListEditorField
{
	public StringCodeListMapEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapKeyCodeToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse);
		
		mapKeyCode = mapKeyCodeToUse;
	}

	@Override
	protected String getStringMapAsString() throws Exception
	{
		StringCodeListMap existingMap = new StringCodeListMap(getProject().getObjectData(getORef(), getTag()));
		existingMap.put(mapKeyCode, getComponentText());
		
		return existingMap.toString();
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
			StringCodeListMap stringMap = new StringCodeListMap(StringMapAsString);
			String codeListAsString = stringMap.get(mapKeyCode);
			
			return new CodeList(codeListAsString);
		}
		catch(ParseException e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	private String mapKeyCode;
}
