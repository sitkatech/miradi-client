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

import org.miradi.objects.ProjectResource;
import org.miradi.xml.wcs.WcsXmlConstants;

public class ProjectResourceObjectSchemaElement extends BaseObjectSchemaElement
{
	public ProjectResourceObjectSchemaElement()
	{
		super(WcsXmlConstants.PROJECT_RESOURCE);
		
		createCodeField(XmlSchemaCreator.RESOURCE_TYPE_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_RESOURCE_TYPE);
		createTextField(ProjectResource.TAG_GIVEN_NAME);
		createTextField(ProjectResource.TAG_SUR_NAME);
		createTextField(ProjectResource.TAG_INITIALS);
		createCodeListField(XmlSchemaCreator.RESOURCE_ROLE_CODES_ELEMENT_NAME);
		createTextField(ProjectResource.TAG_ORGANIZATION);
		createTextField(ProjectResource.TAG_POSITION);
		createTextField(ProjectResource.TAG_LOCATION);
		createTextField(ProjectResource.TAG_PHONE_NUMBER);
		createTextField(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		createTextField(ProjectResource.TAG_PHONE_NUMBER_HOME);
		createTextField(ProjectResource.TAG_PHONE_NUMBER_OTHER);
		createTextField(ProjectResource.TAG_EMAIL);
		createTextField(ProjectResource.TAG_ALTERNATIVE_EMAIL);
		createTextField(ProjectResource.TAG_IM_ADDRESS);
		createTextField(ProjectResource.TAG_IM_SERVICE);
		createDateField(ProjectResource.TAG_DATE_UPDATED);
		createNumericField(ProjectResource.TAG_COST_PER_UNIT);
		createTextField(ProjectResource.TAG_COMMENTS);
		createTextField(ProjectResource.TAG_CUSTOM_FIELD_1);
		createTextField(ProjectResource.TAG_CUSTOM_FIELD_2);
	}
}
