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

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.TagToElementNameMap;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ProjectResourceImporter extends AbstractXmpzObjectImporter
{
	public ProjectResourceImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse);
	}
	
	@Override
	public void importElement() throws Exception
	{
		String poolName = WcsXmlConstants.PROJECT_RESOURCE  + WcsXmlConstants.POOL_ELEMENT_TAG;
		
		NodeList nodes = getImporter().getNodes(getImporter().getRootNode(), new String[]{poolName, WcsXmlConstants.PROJECT_RESOURCE, });
		for (int index = 0; index < nodes.getLength(); ++index)
		{
			Node projectResourceNode = nodes.item(index);
			String intIdAsString = getImporter().getAttributeValue(projectResourceNode, WcsXmlConstants.ID);
			ORef projectResourceRef = getProject().createObject(ProjectResource.getObjectType(), new BaseId(intIdAsString));
			importCodeField(projectResourceNode, WcsXmlConstants.PROJECT_RESOURCE, projectResourceRef, ProjectResource.TAG_RESOURCE_TYPE, new ResourceTypeQuestion());
			
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_GIVEN_NAME);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_SUR_NAME);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_INITIALS);
			importCodeListElement(projectResourceNode, WcsXmlConstants.PROJECT_RESOURCE, projectResourceRef, ProjectResource.TAG_ROLE_CODES);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_ORGANIZATION);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_POSITION);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_LOCATION);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_PHONE_NUMBER);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_PHONE_NUMBER_MOBILE);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_PHONE_NUMBER_HOME);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_PHONE_NUMBER_OTHER);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_EMAIL);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_ALTERNATIVE_EMAIL);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_IM_ADDRESS);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_IM_SERVICE);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_DATE_UPDATED);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_COST_PER_UNIT);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_COMMENTS);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_CUSTOM_FIELD_1);
			importSummaryField(projectResourceNode, projectResourceRef, ProjectResource.TAG_CUSTOM_FIELD_2);
		}
	}
	
	private void importCodeField(Node node, String elementContainerName, ORef ref, String tag, ChoiceQuestion question) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(elementContainerName, tag);
		getImporter().importCodeField(node, elementContainerName + elementName, ref, tag, question);
	}
	
	private void importCodeListElement(Node node, String elementContainerName, ORef ref, String tag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(elementContainerName, tag);
		NodeList codeNodes = getImporter().getNodes(node, new String[]{elementContainerName + elementName + WcsXmlConstants.CONTAINER_ELEMENT_TAG, XmlSchemaCreator.CODE_ELEMENT_NAME});
		CodeList codesToImport = new CodeList();
		for (int index = 0; index < codeNodes.getLength(); ++index)
		{
			Node codeNode = codeNodes.item(index);
			String code = getImporter().getSafeNodeContent(codeNode);
			codesToImport.add(code);
		}
		
		getImporter().setData(ref, tag, codesToImport.toString());
	}
	
	private void importSummaryField(Node projectSumaryNode,	ORef ref, String tag) throws Exception
	{
		TagToElementNameMap map = new TagToElementNameMap();
		String elementName = map.findElementName(WcsXmlConstants.PROJECT_RESOURCE, tag);
		importField(projectSumaryNode, WcsXmlConstants.PROJECT_RESOURCE + elementName, ref, tag);
	}
}
