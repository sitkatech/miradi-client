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

import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

public class StringMapCodeListFieldComponent extends AbstractCodeListComponent
{
	public StringMapCodeListFieldComponent(Project projectToUse, ChoiceQuestion questionToUse, int columnCount, ListSelectionListener listener)
	{
		super(questionToUse, columnCount, listener);
		
		project = projectToUse;
	}

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
		CodeList codes = getSelectedCodes();
		setSameToolTipForAllCheckBoxes();

		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER);
		StringMap existingMap = tableSettings.getTableSettingsMap();
		existingMap.add(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY, codes.toString());
		
		return existingMap.toString();
	}

	public void setText(String stringMapAsString)
	{
		CodeList codes = createCodeListFromString(stringMapAsString);
		createCheckBoxes(codes);
	}

	private CodeList createCodeListFromString(String StringMapAsString)
	{
		try
		{
			StringMap stringMap = new StringMap(StringMapAsString);
			String codeListAsString = stringMap.get(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
			
			return new CodeList(codeListAsString);
		}
		catch(ParseException e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	public static final String WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY = "WorkPlanBudgetColumnCodeListKey";
}
