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

import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTableModel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class StringMapProjectResourceFilterEditorField extends AbstractCodeListEditorField
{
	public StringMapProjectResourceFilterEditorField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse, 1);
	}

	@Override
	protected AbstractCodeListComponent createCodeListComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return null;
	}
	
	//FIXME urgent setText and getText need to deal with refLists instead of codeLists right now,  refs are stored
	// as codes inside string map
	public String getText()
	{
		try
		{
			return getStringMapAsString();
		}
		catch (Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
			return "";
		}
	}

	private String getStringMapAsString() throws Exception
	{
		CodeList codes = new CodeList(super.getText());

		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER);
		StringMap existingMap = tableSettings.getTableSettingsMap();
		existingMap.add(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY, codes.toString());
		
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
			String codeListAsString = stringMap.get(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);
			
			return new CodeList(codeListAsString);
		}
		catch(ParseException e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
			return new CodeList();
		}
	}
}
