/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.questions.ProjectSharingQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.schemas.FieldSchemaChoice;
import org.miradi.schemas.ProjectMetadataSchema;


public class ProjectSummarySchema extends AbstractProjectSummarySchema
{
	public ProjectSummarySchema() throws Exception
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();

		ProjectMetadataSchema schema = new ProjectMetadataSchema();
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_NAME));
		addFieldSchema(createFieldSchemaRequiredChoice(TncProjectData.TAG_PROJECT_SHARING_CODE, ProjectSharingQuestion.class));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_LANGUAGE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_URL));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_PROJECT_DESCRIPTION));

		createExtendedProgressReportSchema();

		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_THREAT_RATING_MODE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_FACTOR_MODE));
		addFieldSchema(schema.getFieldSchema(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT));
		addFieldSchema(new FieldSchemaChoice(OVERALL_PROJECT_THREAT_RATING, getQuestion(ThreatRatingQuestion.class)));
		addFieldSchema(new FieldSchemaChoice(OVERALL_PROJECT_VIABILITY_RATING, getQuestion(StatusQuestion.class)));
		addFieldSchema(new FieldSchemaChoice(OVERALL_PROJECT_VIABILITY_FUTURE_RATING, getQuestion(StatusQuestion.class)));
	}

	@Override
	public String getObjectName()
	{
		return PROJECT_SUMMARY;
	}
}
