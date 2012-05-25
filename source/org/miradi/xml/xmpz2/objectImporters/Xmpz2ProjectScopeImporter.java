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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class Xmpz2ProjectScopeImporter extends BaseObjectImporter
{
	public Xmpz2ProjectScopeImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, null);
	}

	public void importFields() throws Exception
	{
		Node projectSummaryNode = getImporter().getNode(getImporter().getRootNode(), XmpzXmlConstants.PROJECT_SUMMARY_SCOPE);
		
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_SCOPE);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_VISION);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_SCOPE_COMMENTS);		
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_AREA);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_HUMAN_POPULATION);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_SOCIAL_CONTEXT);		
		getImporter().importCodeListField(projectSummaryNode, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadataRef(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES);
		importProjectMetadataField(projectSummaryNode, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_LEGAL_STATUS);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_LEGISLATIVE);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_ACCESS_INFORMATION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_VISITATION_INFORMATION);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_CURRENT_LAND_USES);
		importWcpaField(projectSummaryNode, WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		getImporter().importStringField(projectSummaryNode, PROJECT_SUMMARY_SCOPE, getMetadataRef(), tag);
	}
	
	private void importWcpaField(Node projectSummaryNode, String tag) throws Exception
	{
		getImporter().importStringField(projectSummaryNode, PROJECT_SUMMARY_SCOPE, getWcpaProjectDataRef(), tag);
	}
}
