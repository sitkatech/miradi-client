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

package org.miradi.xml.generic;

import org.miradi.objects.ProjectMetadata;

public class ProjectSummaryPlanningSchemaElement extends ObjectSchemaElement
{
	public ProjectSummaryPlanningSchemaElement()
	{
		super("ProjectSummaryPlanning");
		
		createDateField(ProjectMetadata.TAG_START_DATE);
		createDateField(ProjectMetadata.TAG_EXPECTED_END_DATE);
		createDateField(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		createDateField(ProjectMetadata.TAG_WORKPLAN_END_DATE);
//		TAG_WORKPLAN_TIME_UNIT, getQuestion(BudgetTimePeriodQuestion.class));
//		TAG_FISCAL_YEAR_START
//		TAG_PLANNING_COMMENTS);

		// TAG_CURRENCY_TYPE
		// TAG_CURRENCY_SYMBOL
		// TAG_CURRENCY_DECIMAL_PLACES
		// TAG_TOTAL_BUDGET_FOR_FUNDING
		// TAG_BUDGET_SECURED_PERCENT
//		TAG_KEY_FUNDING_SOURCES);
//		TAG_FINANCIAL_COMMENTS);
		
	}

}
