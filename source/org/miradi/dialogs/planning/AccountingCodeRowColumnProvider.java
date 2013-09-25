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

package org.miradi.dialogs.planning;

import org.miradi.objects.AbstractBudgetCategoryObject;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.CodeList;

public class AccountingCodeRowColumnProvider extends AccountingCodeCoreRowColumnProvider
{
	public AccountingCodeRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public CodeList getColumnCodesToShow()
	{
		CodeList columnCodes = new CodeList();
		columnCodes.add(AbstractBudgetCategoryObject.TAG_CODE);
		
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE);
		
		return columnCodes;
	}
}
