/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;

public class ProjectSummaryScopeSchema extends AbstractProjectSummarySchema
{
	@Override
	protected void fillFieldSchemas()
	{
		ProjectMetadataSchema schema = new ProjectMetadataSchema();
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_SCOPE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_VISION));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_SCOPE_COMMENTS));		
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_AREA));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_AREA_NOTES));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_HUMAN_POPULATION));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_SOCIAL_CONTEXT));		
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES));
		
		WcpaProjectDataSchema wcpaProjectDataSchema = new WcpaProjectDataSchema();
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_LEGAL_STATUS));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_LEGISLATIVE));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_CULTURAL_DESCRIPTION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_ACCESS_INFORMATION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_VISITATION_INFORMATION));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_CURRENT_LAND_USES));
		addFieldSchema(wcpaProjectDataSchema.getFieldSchema(WcpaProjectData.TAG_MANAGEMENT_RESOURCES));				
	}

	@Override
	public String getObjectName()
	{
		return PROJECT_SUMMARY_SCOPE;
	}
}
