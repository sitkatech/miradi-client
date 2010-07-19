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

package org.miradi.questions;

import org.miradi.icons.AccountingCodeIcon;
import org.miradi.icons.BudgetCategoryOneIcon;
import org.miradi.icons.BudgetCategoryTwoIcon;
import org.miradi.icons.FundingSourceIcon;
import org.miradi.icons.ProjectResourceIcon;
import org.miradi.main.EAM;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;

public class WorkPlanCategoryTypesQuestion extends StaticChoiceQuestion
{
	public WorkPlanCategoryTypesQuestion()
	{
		super();
	}
	
	@Override
	protected ChoiceItem[] createChoices()
	{
		return new ChoiceItem[]{
			new ChoiceItem(UNSPECIFIED_CODE, EAM.text("Unspecified")),
			new ChoiceItem(ProjectResource.getObjectType(), EAM.text("Project Resource"), new ProjectResourceIcon()),
			new ChoiceItem(AccountingCode.getObjectType(), EAM.text("Accounting Code"), new AccountingCodeIcon()),
			new ChoiceItem(FundingSource.getObjectType(), EAM.text("Funding Source"), new FundingSourceIcon()),
			new ChoiceItem(BudgetCategoryOne.getObjectType(), EAM.text("Budget Category One"), new BudgetCategoryOneIcon()),
			new ChoiceItem(BudgetCategoryTwo.getObjectType(), EAM.text("Budget Category Two"), new BudgetCategoryTwoIcon()),
		};
	}
	
	public static final String UNSPECIFIED_CODE = "";
}
