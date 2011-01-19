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
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.ProjectSharingQuestion;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProjectSummaryImporter extends AbstractXmpzObjectImporter
{
	public ProjectSummaryImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.PROJECT_SUMMARY);
	}
	
	@Override
	public void importElement() throws Exception
	{
		Node projectSumaryNode = getImporter().getNode(getImporter().getRootNode(), XmpzXmlConstants.PROJECT_SUMMARY);
				
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_NAME);
		writeShareOutsideOrganizationElement(projectSumaryNode);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_LANGUAGE);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_URL);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_PROJECT_STATUS);
		importProjectMetadataField(projectSumaryNode, ProjectMetadata.TAG_NEXT_STEPS);
		importExternalProjectId(projectSumaryNode);
		importCodeField(projectSumaryNode, getMetadataRef(), ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion());
	}
	
	private void writeShareOutsideOrganizationElement(Node projectSumaryNode) throws Exception
	{
		Node shareOutsideTncNode = getImporter().getNode(projectSumaryNode, getPoolName() + XmlSchemaCreator.PROJECT_SHARE_OUTSIDE_ORGANIZATION);
		String isShareWithAnyOneCode = ProjectSharingQuestion.SHARE_ONLY_INSIDE_ORGANIZATION;
		if (getImporter().isTrue(shareOutsideTncNode.getTextContent()))
			isShareWithAnyOneCode = ProjectSharingQuestion.SHARE_WITH_ANYONE;
		
		getImporter().setData(getTncProjectDataRef(), TncProjectData.TAG_PROJECT_SHARING_CODE, isShareWithAnyOneCode);
	}	

	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, getMetadataRef(), tag);
	}
	
	private void importExternalProjectId(Node projectSumaryNode) throws Exception
	{
		Node projectIdNode = getImporter().getNode(projectSumaryNode, getPoolName() + Xenodata.TAG_PROJECT_ID);
		NodeList projectIdNodes = getImporter().getNodes(projectIdNode, new String[]{EXTERNAL_PROJECT_ID_ELEMENT_NAME, });
		StringRefMap stringRefMap = new StringRefMap();
		for (int index = 0; index < projectIdNodes.getLength(); ++index)
		{
			Node node = projectIdNodes.item(index);
			
			Node externalAppNode = getImporter().getNode(node, EXTERNAL_APP_ELEMENT_NAME);
			String externalAppThatAssignedId = externalAppNode.getTextContent();
			
			Node externalProjectIdNode = getImporter().getNode(node, XmpzXmlConstants.PROJECT_ID);
			String externalProjectId = externalProjectIdNode.getTextContent();
			
			ORef xenodataRef = getProject().createObject(Xenodata.getObjectType());
			getImporter().setData(xenodataRef, Xenodata.TAG_PROJECT_ID, externalProjectId);
			stringRefMap.add(externalAppThatAssignedId, xenodataRef);
		}
		
		getImporter().setData(getMetadataRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, stringRefMap.toString());
	}
}

