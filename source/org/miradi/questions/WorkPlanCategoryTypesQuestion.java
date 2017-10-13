/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.ProjectResourceSchema;

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
			new ChoiceItem(ProjectResourceSchema.getObjectType(), EAM.text("People"), new ProjectResourceIcon()),
			new ChoiceItem(AccountingCodeSchema.getObjectType(), EAM.text("Accounting Code"), new AccountingCodeIcon()),
			new ChoiceItem(FundingSourceSchema.getObjectType(), EAM.text("Funding Source"), new FundingSourceIcon()),
			new ChoiceItem(BudgetCategoryOneSchema.getObjectType(), EAM.text("Budget Category One"), new BudgetCategoryOneIcon()),
			new ChoiceItem(BudgetCategoryTwoSchema.getObjectType(), EAM.text("Budget Category Two"), new BudgetCategoryTwoIcon()),
		};
	}
	
	public static final String UNSPECIFIED_CODE = "";
}
