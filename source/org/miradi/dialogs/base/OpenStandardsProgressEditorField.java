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

import org.miradi.dialogfields.QuestionBasedEditorComponent;
import org.miradi.dialogfields.RadioButtonEditorComponent;
import org.miradi.dialogfields.StringMapBudgetColumnCodeListEditorField;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class OpenStandardsProgressEditorField extends StringMapBudgetColumnCodeListEditorField
{
	public OpenStandardsProgressEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapCodeToUse)
	{
		super(projectToUse, refToUse.getObjectType(), refToUse.getObjectId(), tagToUse, questionToUse);
		
		mapCode = mapCodeToUse;
	}
	
	@Override
	protected QuestionBasedEditorComponent createCodeListEditor(ChoiceQuestion questionToUse, int columnCount)
	{
		RadioButtonEditorComponent editorComponent = new SingleItemCodeListRadioButtonEditorComponent(questionToUse);
		editorComponent.addListSelectionListener(this);
		
		return editorComponent;
	}
	
	@Override
	protected String getStringMapAsString() throws Exception
	{
		Project project = getProject();
		ORef oRef = getORef();
		StringChoiceMap existingMap = new StringChoiceMap(project.getObjectData(oRef, getTag()));
		existingMap.put(mapCode, getComponentText());
		
		return existingMap.toString();
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
	
	private class SingleItemCodeListRadioButtonEditorComponent extends RadioButtonEditorComponent
	{
		public SingleItemCodeListRadioButtonEditorComponent(ChoiceQuestion questionToUse)
		{
			super(questionToUse);
		}
		
		@Override
		public String getText()
		{
			return extractSingleCode(super.getText());
		}

		private String extractSingleCode(String parentCodes)
		{
			try
			{
				
				CodeList codes = new CodeList(parentCodes);
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
	}

	private String mapCode;
}
