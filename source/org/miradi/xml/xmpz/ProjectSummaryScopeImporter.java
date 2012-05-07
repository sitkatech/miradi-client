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

import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class ProjectSummaryScopeImporter extends AbstractXmpzObjectImporter
{
	public ProjectSummaryScopeImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.PROJECT_SUMMARY_SCOPE);
	}

	@Override
	public void importElement() throws Exception
	{
		Node projectSummaryNode = getImporter().getNode(getImporter().getRootNode(), XmpzXmlConstants.PROJECT_SUMMARY_SCOPE);
		
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_PROJECT_SCOPE);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_PROJECT_VISION);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_SCOPE_COMMENTS);		
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_PROJECT_AREA);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_HUMAN_POPULATION);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_SOCIAL_CONTEXT);		
		importCodeListField(projectSummaryNode, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadataRef(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES);
		importField(projectSummaryNode, getMetadataRef(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_LEGAL_STATUS);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_LEGISLATIVE);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_ACCESS_INFORMATION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_VISITATION_INFORMATION);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_CURRENT_LAND_USES);
		importField(projectSummaryNode, getWcpaProjectDataRef(), WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
	}
}
