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
import org.miradi.dialogfields.QuestionBasedEditorComponent;
import org.miradi.dialogfields.RadioButtonEditorComponent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class DashboardProgressEditorField extends AbstractStringMapCodeListEditorField
{
	public DashboardProgressEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapCodeToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse);
		
		mapCode = mapCodeToUse;
	}
	
	@Override
	protected QuestionBasedEditorComponent createCodeListEditor(ChoiceQuestion questionToUse, int columnCount)
	{
		return new RadioButtonEditorComponent(questionToUse);
	}
	
	@Override
	protected String getStringMapAsString() throws Exception
	{
		Project project = getProject();
		ORef oRef = getORef();
		StringChoiceMap existingMap = new StringChoiceMap(project.getObjectData(oRef, getTag()));
		existingMap.put(mapCode, extractSingleCode());
		
		return existingMap.toString();
	}
	
	private String extractSingleCode()
	{
		try
		{
			CodeList codes = new CodeList(getComponentText());
			if (!codes.isEmpty())
				return codes.firstElement();
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
		
		return "";
	}

	@Override
	public void setText(String stringMapAsString)
	{
		try
		{
			StringChoiceMap stringChoiceMap = new StringChoiceMap(stringMapAsString);
			String code = stringChoiceMap.get(mapCode);
			codeListEditor.setText(code);
		}
		catch(ParseException e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
	}
	
	private String mapCode;
}
