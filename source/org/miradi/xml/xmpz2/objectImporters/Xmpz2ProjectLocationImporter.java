/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.CountriesQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class Xmpz2ProjectLocationImporter extends AbstractXmpz2ObjectImporter
{
	public Xmpz2ProjectLocationImporter(Xmpz2XmlImporter xmpz2XmlImporter)
	{
		super(xmpz2XmlImporter);
	}
	
	public void importFields() throws Exception
	{
		Node projectSummaryLocationNode = getImporter().getNamedChildNode(getImporter().getRootNode(), PROJECT_SUMMARY_LOCATION);
		
		importGeospatialLocationField(projectSummaryLocationNode);		
		getImporter().importCodeListField(projectSummaryLocationNode, PROJECT_SUMMARY_LOCATION, getMetadataRef(), ProjectMetadata.TAG_COUNTRIES, StaticQuestionManager.getQuestion(CountriesQuestion.class));
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_STATE_AND_PROVINCES);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_MUNICIPALITIES);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LOCATION_DETAIL);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		importProjectMetadataField(projectSummaryLocationNode, ProjectMetadata.TAG_LOCATION_COMMENTS);
	}
	
	private void importGeospatialLocationField(Node projectSummaryLocationNode) throws Exception
	{
		Node locationNode = getImporter().getNamedChildNode(projectSummaryLocationNode, PROJECT_SUMMARY_LOCATION + PROJECT_LOCATION);
		if(locationNode == null)
			return;
		
		Node gespatialLocationNode = getImporter().getNamedChildNode(locationNode, GEOSPATIAL_LOCATION);
		if(gespatialLocationNode == null)
			return;
		
		getImporter().importField(gespatialLocationNode, LATITUDE, getMetadataRef(), ProjectMetadata.TAG_PROJECT_LATITUDE);
		getImporter().importField(gespatialLocationNode, LONGITUDE, getMetadataRef(), ProjectMetadata.TAG_PROJECT_LONGITUDE);
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		importField(projectSummaryNode, PROJECT_SUMMARY_LOCATION, getMetadataRef(), tag);
	}
}
