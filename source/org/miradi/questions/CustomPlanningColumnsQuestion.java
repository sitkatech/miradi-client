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
package org.miradi.questions;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;

public class CustomPlanningColumnsQuestion extends StaticChoiceQuestion
{
	public CustomPlanningColumnsQuestion()
	{
		super(getColumnChoiceItems());
	}

	private static ChoiceItem[] getColumnChoiceItems()
	{
		return new ChoiceItem[] 
		{
				createChoiceItem(Target.TAG_SPECIES_LATIN_NAME),
				createChoiceItem(Indicator.PSEUDO_TAG_METHODS), 
				createChoiceItem(Indicator.PSEUDO_TAG_FACTOR),
				createChoiceItem(Indicator.TAG_PRIORITY),
				createChoiceItem(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE),
				createChoiceItem(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS),
				createChoiceItem(Factor.PSEUDO_TAG_TAXONOMY_CODE_VALUE),
				createChoiceItem(META_WHO_TOTAL),
				createChoiceItem(BaseObject.PSEUDO_TAG_WHEN_TOTAL),
				createChoiceItem(Measurement.META_COLUMN_TAG),
				createChoiceItem(Indicator.META_COLUMN_TAG),
				createChoiceItem(Factor.TAG_COMMENTS),
				createChoiceItem(Desire.TAG_FULL_TEXT),
		};
	}

	public static ChoiceItem createChoiceItem(String tag)
	{
		return new ChoiceItem(tag, EAM.fieldLabel(ObjectType.FAKE, tag));
	}

	public static final String META_RESOURCE_ASSIGNMENT_COLUMN_CODE = "MetaWorkUnitColumnCode";
	public static final String META_EXPENSE_ASSIGNMENT_COLUMN_CODE = "MetaExpenseAmountColumnCode";
	
	public static final String META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE = "MetaFundingSourceExpenseColumnCode";
	public static final String META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE = "MetaFundingSourceBudgetDetailsColumnCode";
	
	public static final String META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE = "MetaAcountingCodeExpenseColumnCode";
	public static final String META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE = "MetaAccountingCodeBudgetDetailsColumnCode";
	
	public static final String META_BUDGET_DETAIL_COLUMN_CODE = "MetaBudgetDetailColumnCode";
	public static final String META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE = "MetaProjectResourceWorkUnitsColumnCode";
	public final static String META_WHO_TOTAL = "MetaWhoTotal";
}
