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

import org.miradi.objecthelpers.ORef;
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
		if (isImportedElseWhere(tag))
			return true;
		
		return super.isCustomImportField(tag);
	}

	private boolean isImportedElseWhere(String tag)
	{
		return tag.equals(TncProjectData.TAG_PROJECT_SHARING_CODE);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importFields();
	}
	
	public void importFields() throws Exception
	{
		Node tncProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		importProjectMetadataField(tncProjectDataNode, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		
		final String[] codelistTagsToImport = new String[]{ProjectMetadata.TAG_TNC_OPERATING_UNITS, 
														   ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, 
														   ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, 
														   ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION};
		
		importCodeListsIntoProjectMetadata(tncProjectDataNode, codelistTagsToImport);
	}
	
	private void importCodeListsIntoProjectMetadata(Node tncProjectDataNode, String[] codelistfieldTagsToImport) throws Exception
	{
		for (int index = 0; index < codelistfieldTagsToImport.length; ++index)
		{
			getImporter().importCodeListField(tncProjectDataNode, TNC_PROJECT_DATA, getMetadataRef(), codelistfieldTagsToImport[index]);
		}
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, getMetadataRef(), tag);
	}
}
