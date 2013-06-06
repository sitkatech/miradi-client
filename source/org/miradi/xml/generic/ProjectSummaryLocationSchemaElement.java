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
import org.miradi.xml.wcs.XmpzXmlConstants;

public class ProjectSummaryLocationSchemaElement extends ObjectSchemaElement
{
	public ProjectSummaryLocationSchemaElement()
	{
		super(XmpzXmlConstants.PROJECT_SUMMARY_LOCATION);

		createOptionalGeospatialLocationField("ProjectLocation");
		createOptionalCodeListField(XmlSchemaCreator.COUNTRIES);
		createOptionalTextField(ProjectMetadata.TAG_STATE_AND_PROVINCES);
		createOptionalTextField(ProjectMetadata.TAG_MUNICIPALITIES);
		createOptionalTextField(ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		createOptionalTextField(ProjectMetadata.TAG_LOCATION_DETAIL);
		createOptionalTextField(ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		createOptionalTextField(ProjectMetadata.TAG_LOCATION_COMMENTS);
	}
}
