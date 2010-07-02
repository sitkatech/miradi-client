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

import org.miradi.main.EAM;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.CategoryOne;
import org.miradi.objects.CategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;

public class RollupReportsObjectTypeQuestion extends StaticChoiceQuestion
{
	public RollupReportsObjectTypeQuestion()
	{
		super();
	}
	
	@Override
	protected ChoiceItem[] createChoices()
	{
		return new ChoiceItem[]{
			new ChoiceItem(UNSPECIFIED_CODE, EAM.text("Unspecified")),
			new ChoiceItem(ProjectResource.getObjectType(), EAM.text("Resource")),
			new ChoiceItem(FundingSource.getObjectType(), EAM.text("Funding Source")),
			new ChoiceItem(AccountingCode.getObjectType(), EAM.text("Accounting Code")),
			new ChoiceItem(CategoryOne.getObjectType(), EAM.text("Budget Category One")),
			new ChoiceItem(CategoryTwo.getObjectType(), EAM.text("Budget Category Two")),
		};
	}
	
	public static final String UNSPECIFIED_CODE = "";
}
