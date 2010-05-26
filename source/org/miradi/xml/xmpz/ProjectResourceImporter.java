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


public class ProjectResourceImporter extends AbstractBaseObjectImporter
{
	public ProjectResourceImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.PROJECT_RESOURCE);
	}
	
	@Override
	public void importElement() throws Exception
	{
		importObject(ProjectResource.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef ref) throws Exception
	{
		importCodeField(node, WcsXmlConstants.PROJECT_RESOURCE, ref, ProjectResource.TAG_RESOURCE_TYPE, new ResourceTypeQuestion());			
		importSummaryField(node, ref, ProjectResource.TAG_GIVEN_NAME);
		importSummaryField(node, ref, ProjectResource.TAG_SUR_NAME);
		importSummaryField(node, ref, ProjectResource.TAG_INITIALS);
		importCodeListField(node, WcsXmlConstants.PROJECT_RESOURCE, ref, ProjectResource.TAG_ROLE_CODES);
		importSummaryField(node, ref, ProjectResource.TAG_ORGANIZATION);
		importSummaryField(node, ref, ProjectResource.TAG_POSITION);
		importSummaryField(node, ref, ProjectResource.TAG_LOCATION);
		importSummaryField(node, ref, ProjectResource.TAG_PHONE_NUMBER);
		importSummaryField(node, ref, ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		importSummaryField(node, ref, ProjectResource.TAG_PHONE_NUMBER_HOME);
		importSummaryField(node, ref, ProjectResource.TAG_PHONE_NUMBER_OTHER);
		importSummaryField(node, ref, ProjectResource.TAG_EMAIL);
		importSummaryField(node, ref, ProjectResource.TAG_ALTERNATIVE_EMAIL);
		importSummaryField(node, ref, ProjectResource.TAG_IM_ADDRESS);
		importSummaryField(node, ref, ProjectResource.TAG_IM_SERVICE);
		importSummaryField(node, ref, ProjectResource.TAG_DATE_UPDATED);
		importSummaryField(node, ref, ProjectResource.TAG_COST_PER_UNIT);
		importSummaryField(node, ref, ProjectResource.TAG_COMMENTS);
		importSummaryField(node, ref, ProjectResource.TAG_CUSTOM_FIELD_1);
		importSummaryField(node, ref, ProjectResource.TAG_CUSTOM_FIELD_2);
	}
	
	private void importSummaryField(Node projectSumaryNode,	ORef ref, String tag) throws Exception
	{
		importField(projectSumaryNode, ref, tag);
	}
}
