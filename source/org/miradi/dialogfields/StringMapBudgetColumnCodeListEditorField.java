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

import org.miradi.dialogs.base.AbstractStringCodeListMapEditorField;
import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class StringMapBudgetColumnCodeListEditorField extends AbstractStringCodeListMapEditorField
{
	public StringMapBudgetColumnCodeListEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
	}

	@Override
	protected AbstractStringKeyMap createCurrentStringKeyMap() throws Exception
	{
		return new StringStringMap(getProject().getObjectData(getORef(), getTag()));
	}

	@Override
	protected AbstractStringKeyMap createStringKeyMap(String StringMapAsString) throws Exception
	{
		return new StringStringMap(StringMapAsString);
	}
}
