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

package org.miradi.xml.xmpz;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;


public class ProjectResourceImporter extends AbstractBaseObjectPoolImporter
{
	public ProjectResourceImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.PROJECT_RESOURCE, ProjectResource.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		importCodeField(node, destinationRef, ProjectResource.TAG_RESOURCE_TYPE, new ResourceTypeQuestion());			
		importField(node, destinationRef, ProjectResource.TAG_GIVEN_NAME);
		importField(node, destinationRef, ProjectResource.TAG_SUR_NAME);
		importField(node, destinationRef, ProjectResource.TAG_INITIALS);
		importCodeListField(node, destinationRef, ProjectResource.TAG_ROLE_CODES);
		importField(node, destinationRef, ProjectResource.TAG_ORGANIZATION);
		importField(node, destinationRef, ProjectResource.TAG_POSITION);
		importField(node, destinationRef, ProjectResource.TAG_LOCATION);
		importField(node, destinationRef, ProjectResource.TAG_PHONE_NUMBER);
		importField(node, destinationRef, ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		importField(node, destinationRef, ProjectResource.TAG_PHONE_NUMBER_HOME);
		importField(node, destinationRef, ProjectResource.TAG_PHONE_NUMBER_OTHER);
		importField(node, destinationRef, ProjectResource.TAG_EMAIL);
		importField(node, destinationRef, ProjectResource.TAG_ALTERNATIVE_EMAIL);
		importField(node, destinationRef, ProjectResource.TAG_IM_ADDRESS);
		importField(node, destinationRef, ProjectResource.TAG_IM_SERVICE);
		importField(node, destinationRef, ProjectResource.TAG_DATE_UPDATED);
		importField(node, destinationRef, ProjectResource.TAG_COST_PER_UNIT);
		importField(node, destinationRef, ProjectResource.TAG_COMMENTS);
		importField(node, destinationRef, ProjectResource.TAG_CUSTOM_FIELD_1);
		importField(node, destinationRef, ProjectResource.TAG_CUSTOM_FIELD_2);
	}
}
