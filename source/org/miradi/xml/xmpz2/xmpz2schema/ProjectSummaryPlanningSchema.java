/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.ProjectMetadata;
import org.miradi.schemas.ProjectMetadataSchema;

public class ProjectSummaryPlanningSchema extends AbstractProjectSummarySchema
{
	@Override
	protected void fillFieldSchemas()
	{
		ProjectMetadataSchema schema = new ProjectMetadataSchema();
		
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_START_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_EXPECTED_END_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_WORKPLAN_START_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_WORKPLAN_END_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_FISCAL_YEAR_START));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PLANNING_COMMENTS));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_CURRENCY_TYPE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_CURRENCY_SYMBOL));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_KEY_FUNDING_SOURCES));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_FINANCIAL_COMMENTS));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY));
	}
	
	@Override
	public String getObjectName()
	{
		return PROJECT_SUMMARY_PLANNING;
	}
}
