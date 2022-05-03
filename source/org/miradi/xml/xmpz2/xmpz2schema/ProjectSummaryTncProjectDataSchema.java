/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objects.TncProjectData;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.TncProjectDataSchema;


public class ProjectSummaryTncProjectDataSchema extends AbstractProjectSummarySchema
{
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();

		ProjectMetadataSchema schema = new ProjectMetadataSchema();
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_OPERATING_UNITS)); 
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION)); 
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_MARINE_ECO_REGION)); 
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION));
	
		TncProjectDataSchema tncProjectDataSchema = new TncProjectDataSchema();
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_PROJECT_LEVEL_COMMENTS));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_PROJECT_CITATIONS));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_CAP_STANDARDS_SCORECARD));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_MAKING_THE_CASE));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_RISKS));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_CAPACITY_AND_FUNDING));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_PROJECT_FOCUS));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_PROJECT_SCALE));
		addFieldSchema(tncProjectDataSchema.getFieldSchema(TncProjectData.TAG_FUNDRAISING_PLAN));
	}

	@Override
	public String getObjectName()
	{
		return TNC_PROJECT_DATA;
	}
}
