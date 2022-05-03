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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.*;
import org.miradi.schemas.ExtendedProgressReportSchema;
import org.miradi.schemas.FieldSchemaReflist;
import org.miradi.schemas.XenodataSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.xmpz2schema.ProjectSummarySchema;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xmpz2ProjectSummaryImporter extends BaseObjectImporter
{
	public Xmpz2ProjectSummaryImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, null);
	}

	public void importFields() throws Exception
	{
		Node projectSummaryNode = getImporter().getNamedChildNode(getImporter().getRootNode(), PROJECT_SUMMARY);
				
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_NAME);
		writeShareOutsideOrganizationElement(projectSummaryNode);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_LANGUAGE);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_URL);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_DESCRIPTION);

		importExternalProjectId(projectSummaryNode);
				
		getImporter().importCodeField(projectSummaryNode, PROJECT_SUMMARY, getMetadataRef(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE, new TargetModeQuestion());
		getImporter().importCodeField(projectSummaryNode, PROJECT_SUMMARY, getMetadataRef(), ProjectMetadata.TAG_FACTOR_MODE, new FactorModeQuestion());
		getImporter().importCodeField(projectSummaryNode, PROJECT_SUMMARY, getMetadataRef(), ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, StaticQuestionManager.getQuestion(BudgetTimePeriodQuestion.class));
		getImporter().importCodeField(projectSummaryNode, PROJECT_SUMMARY, getMetadataRef(), ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion());

		FieldSchemaReflist progressReportFieldSchema = new FieldSchemaReflist(BaseObject.TAG_EXTENDED_PROGRESS_REPORT_REFS, EXTENDED_PROGRESS_REPORT);
		getImporter().importRefs(projectSummaryNode, getMetadataRef(), new ProjectSummarySchema(), progressReportFieldSchema, ExtendedProgressReportSchema.OBJECT_NAME);
	}
	
	private void writeShareOutsideOrganizationElement(Node projectSummaryNode) throws Exception
	{
		Node shareOutsideTncNode = getImporter().getNamedChildNode(projectSummaryNode, PROJECT_SUMMARY + PROJECT_SHARE_OUTSIDE_ORGANIZATION);
		String readableCode = getImporter().getSafeNodeContent(shareOutsideTncNode);
		String internalCode = StaticQuestionManager.getQuestion(ProjectSharingQuestion.class).convertToInternalCode(readableCode);
		getImporter().setData(getTncProjectDataRef(), TncProjectData.TAG_PROJECT_SHARING_CODE, internalCode);
	}	

	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, PROJECT_SUMMARY, getMetadataRef(), tag);
	}
	
	private void importExternalProjectId(Node projectSummaryNode) throws Exception
	{
		Node projectIdNode = getImporter().getNamedChildNode(projectSummaryNode, PROJECT_SUMMARY + Xenodata.TAG_PROJECT_ID);
		NodeList projectIdNodes = getImporter().getNodes(projectIdNode, new String[]{EXTERNAL_PROJECT_ID_ELEMENT_NAME, });
		StringRefMap stringRefMap = new StringRefMap();
		for (int index = 0; index < projectIdNodes.getLength(); ++index)
		{
			Node node = projectIdNodes.item(index);
			
			Node externalAppNode = getImporter().getNamedChildNode(node, EXTERNAL_APP_ELEMENT_NAME);
			String externalAppThatAssignedId = externalAppNode.getTextContent();
			
			Node externalProjectIdNode = getImporter().getNamedChildNode(node, PROJECT_ID);
			String externalProjectId = externalProjectIdNode.getTextContent();
			
			ORef xenodataRef = getProject().createObject(XenodataSchema.getObjectType());
			getImporter().setData(xenodataRef, Xenodata.TAG_PROJECT_ID, externalProjectId);
			stringRefMap.add(externalAppThatAssignedId, xenodataRef);
		}
		
		getImporter().setData(getMetadataRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, stringRefMap.toJsonString());
	}
}
