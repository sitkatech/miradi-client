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
import org.miradi.objects.Xenodata;
import org.miradi.xml.wcs.XmpzXmlConstants;

class ProjectSummarySchemaElement extends ObjectSchemaElement
{
	public ProjectSummarySchemaElement()
	{
		super(XmpzXmlConstants.PROJECT_SUMMARY);
	
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_NAME);
		createOptionalBooleanField(XmlSchemaCreator.TNC_PROJECT_DATA_SHARE_OUTSIDE_TNC);
		createOptionalCodeField(ProjectMetadata.TAG_PROJECT_LANGUAGE, XmlSchemaCreator.VOCABULARY_LANGUAGE_CODE);
		createOptionalDateField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		createOptionalTextField(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		createOptionalTextField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_URL);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_STATUS);
		createOptionalTextField(ProjectMetadata.TAG_NEXT_STEPS);
		createOptionalCodeField(OVERALL_PROJECT_THREAT_RATING, XmlSchemaCreator.VOCABULARY_THREAT_RATING);
		createOptionalCodeField(OVERALL_PROJECT_VIABILITY_RATING, XmlSchemaCreator.VOCABULARY_TARGET_STATUS);
		createExternalProjectIdField(Xenodata.TAG_PROJECT_ID);
		createCodeField(ProjectMetadata.TAG_THREAT_RATING_MODE, XmlSchemaCreator.VOCABULARY_THREAT_RATING_MODE);
	}
}
