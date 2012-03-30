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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;

public class ProjectResourceSchema extends BaseObjectSchema
{
	public static final String OBJECT_NAME = "ProjectResource";

	public ProjectResourceSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaChoice(ProjectResource.TAG_RESOURCE_TYPE, getQuestion(ResourceTypeQuestion.class));
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_INITIALS);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_GIVEN_NAME);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_SUR_NAME);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_POSITION);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_PHONE_NUMBER);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_EMAIL);
		createFieldSchemaNumber(ProjectResource.TAG_COST_PER_UNIT);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_ORGANIZATION);
		createFieldSchemaCodeList(ProjectResource.TAG_ROLE_CODES, getQuestion(ResourceRoleQuestion.class));
		createFieldSchemaMultiLineUserText(ProjectResource.TAG_COMMENTS);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_LOCATION);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_PHONE_NUMBER_HOME);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_PHONE_NUMBER_OTHER);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_ALTERNATIVE_EMAIL);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_IM_ADDRESS);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_IM_SERVICE);
		createFieldSchemaDate(ProjectResource.TAG_DATE_UPDATED);
		createFieldSchemaBoolean(ProjectResource.TAG_IS_CCN_COACH);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_CUSTOM_FIELD_1);
		createFieldSchemaSingleLineUserText(ProjectResource.TAG_CUSTOM_FIELD_2);
	}

	public static int getObjectType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}
}
