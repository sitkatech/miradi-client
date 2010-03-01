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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ResourceTypeQuestion;

public class ProjectResourcePoolExporter extends BaseObjectPoolExporter
{
	public ProjectResourcePoolExporter(WcsXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, PROJECT_RESOURCE, ProjectResource.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		writeCodeElementSameAsTag(baseObject, ProjectResource.TAG_RESOURCE_TYPE, new ResourceTypeQuestion());
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_GIVEN_NAME);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_SUR_NAME);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_INITIALS);
		writeCodeListElement(ProjectResource.TAG_ROLE_CODES, baseObject, ProjectResource.TAG_ROLE_CODES);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_ORGANIZATION);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_POSITION);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_LOCATION);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_PHONE_NUMBER);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_PHONE_NUMBER_HOME);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_PHONE_NUMBER_OTHER);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_EMAIL);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_ALTERNATIVE_EMAIL);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_IM_ADDRESS);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_IM_SERVICE);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_DATE_UPDATED);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_COST_PER_UNIT);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_COMMENTS);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_CUSTOM_FIELD_1);
		writeOptionalElementWithSameTag(baseObject, ProjectResource.TAG_CUSTOM_FIELD_2);
	}
}
