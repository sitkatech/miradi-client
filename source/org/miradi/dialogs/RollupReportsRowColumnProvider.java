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

package org.miradi.dialogs;

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.objects.AbstractBudgetCategoryObject;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.CategoryOne;
import org.miradi.objects.CategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;

public class RollupReportsRowColumnProvider implements RowColumnProvider
{ 
	public CodeList getColumnListToShow()
	{
		CodeList columnCodes = new CodeList();
		columnCodes.add(AbstractBudgetCategoryObject.TAG_CODE);		
		columnCodes.add(CustomPlanningColumnsQuestion.META_ROLLUP_REPORTS_WORK_UNITS_COLUMN_CODE);
		
		return columnCodes;
	}

	public CodeList getRowListToShow()
	{
		return new CodeList(new String[] {
				ProjectResource.OBJECT_NAME,
				AccountingCode.OBJECT_NAME,
				FundingSource.OBJECT_NAME,
				CategoryOne.OBJECT_NAME,
				CategoryTwo.OBJECT_NAME,
		});
	}
}
