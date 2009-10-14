/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.xml.wcs.WcsXmlConstants;

public class TncProjectDataSchemaElement extends ObjectSchemaElement
{
	public TncProjectDataSchemaElement()
	{
		super(WcsXmlConstants.TNC_PROJECT_DATA);
		
		createOptionalTextField(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		createTncProjectIdField(Xenodata.TAG_PROJECT_ID);
		createOptionalBooleanField(TncProjectData.TAG_PROJECT_SHARING_CODE);
		createOptionalTextField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		createCodeListField(XmlSchemaCreator.TNC_PROJECT_PLACE_TYPES);
		createCodeListField(XmlSchemaCreator.TNC_ORGANIZATIONAL_PRIORITIES);
		createOptionalTextField(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		createOptionalTextField(TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		createCodeListField(XmlSchemaCreator.TNC_OPERATING_UNITS);
		createCodeListField(XmlSchemaCreator.TNC_TERRESTRIAL_ECO_REGION);
		createCodeListField(XmlSchemaCreator.TNC_MARINE_ECO_REGION);
		createCodeListField(XmlSchemaCreator.TNC_FRESHWATER_ECO_REGION);
		createOptionalTextField(ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
	}
}
