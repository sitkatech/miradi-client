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
import org.miradi.objects.TncProjectData;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class TncProjectDataImporter extends SingletonObjectImporter
{
	public TncProjectDataImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new TncProjectDataSchema());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		return true;
	}
	
	public void importFields() throws Exception
	{
		Node tncProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getMetadataRef(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getMetadataRef(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getMetadataRef(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getMetadataRef(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION);
		
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_PROJECT_CITATIONS);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_MAKING_THE_CASE);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_RISKS);
		importTncProjectDataField(tncProjectDataNode, TncProjectData.TAG_CAPACITY_AND_FUNDING);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getTncProjectDataRef(), TncProjectData.TAG_PROJECT_PLACE_TYPES);
		getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getTncProjectDataRef(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES);
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, getMetadataRef(), tag);
	}
	
	private void importTncProjectDataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, getTncProjectDataRef(), tag);
	}
}
