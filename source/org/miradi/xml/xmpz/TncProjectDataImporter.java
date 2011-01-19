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
import org.miradi.objects.TncProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class TncProjectDataImporter extends AbstractXmpzObjectImporter
{
	public TncProjectDataImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.TNC_PROJECT_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node tncProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		importField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		importField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		importCodeListField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_PROJECT_PLACE_TYPES);
		importCodeListField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES);
		importField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		importField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		importCodeListField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
		importCodeListField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION);
		importCodeListField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION);
		importCodeListField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION);
		importField(tncProjectDataNode, getMetadataRef(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		
		importField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		importField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		importField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_PROJECT_CITATIONS);
		importField(tncProjectDataNode, getTncProjectDataRef(), TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
	}	
}
