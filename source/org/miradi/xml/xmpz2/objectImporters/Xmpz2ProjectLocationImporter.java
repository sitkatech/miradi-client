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
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class Xmpz2ProjectLocationImporter extends BaseObjectImporter
{
	public Xmpz2ProjectLocationImporter(Xmpz2XmlImporter xmpz2XmlImporter)
	{
		super(xmpz2XmlImporter, null);
	}
	
	public void importFields() throws Exception
	{
		Node projectSummaryLocationNode = getImporter().getNode(getImporter().getRootNode(), PROJECT_SUMMARY_LOCATION);
		
		importGeospatialLocationField(projectSummaryLocationNode);		
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		getImporter().importCodeListField(projectSummaryLocationNode, PROJECT_SUMMARY_LOCATION, getMetadataRef(), ProjectMetadata.TAG_COUNTRIES);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_STATE_AND_PROVINCES);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_MUNICIPALITIES);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LOCATION_DETAIL);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LOCATION_COMMENTS);
	}
	
	private void importGeospatialLocationField(Node projectSummaryLocationNode) throws Exception
	{
		Node locationNode = getImporter().getNode(projectSummaryLocationNode, XmpzXmlConstants.PROJECT_SUMMARY_LOCATION + XmpzXmlConstants.PROJECT_LOCATION);
		if(locationNode == null)
			return;
		
		Node gespatialLocationNode = getImporter().getNode(locationNode, XmpzXmlConstants.GEOSPATIAL_LOCATION);
		if(gespatialLocationNode == null)
			return;
		
		getImporter().importField(gespatialLocationNode, XmpzXmlConstants.LATITUDE, getMetadataRef(), ProjectMetadata.TAG_PROJECT_LATITUDE);
		getImporter().importField(gespatialLocationNode, XmpzXmlConstants.LONGITUDE, getMetadataRef(), ProjectMetadata.TAG_PROJECT_LONGITUDE);
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		getImporter().importStringField(projectSummaryNode, PROJECT_SUMMARY_LOCATION, getMetadataRef(), tag);
	}
}
