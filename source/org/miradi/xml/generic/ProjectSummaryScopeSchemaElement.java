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
import org.miradi.objects.WcpaProjectData;
import org.miradi.xml.wcs.WcsXmlConstants;

public class ProjectSummaryScopeSchemaElement extends ObjectSchemaElement
{
	public ProjectSummaryScopeSchemaElement()
	{
		super(WcsXmlConstants.PROJECT_SUMMARY_SCOPE);
		
		createOptionalTextField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_SCOPE);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_VISION);
		createOptionalTextField(ProjectMetadata.TAG_SCOPE_COMMENTS);		

		createOptionalTextField(ProjectMetadata.TAG_PROJECT_AREA);
		createOptionalTextField(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		createOptionalTextField(ProjectMetadata.LEGACY_TAG_RED_LIST_SPECIES);
		createOptionalTextField(ProjectMetadata.LEGACY_TAG_OTHER_NOTABLE_SPECIES);

		createOptionalNumericField(ProjectMetadata.TAG_HUMAN_POPULATION);
		createOptionalTextField(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		createOptionalTextField(ProjectMetadata.TAG_SOCIAL_CONTEXT);

		createCodeListField(XmlSchemaCreator.PROTECTED_AREA_CATEGORIES_ELEMENT_NAME);
		createOptionalTextField(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		createOptionalTextField(WcpaProjectData.TAG_LEGAL_STATUS);
		createOptionalTextField(WcpaProjectData.TAG_LEGISLATIVE);
		createOptionalTextField(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		createOptionalTextField(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		createOptionalTextField(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		createOptionalTextField(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		createOptionalTextField(WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		createOptionalTextField(WcpaProjectData.TAG_ACCESS_INFORMATION);
		createOptionalTextField(WcpaProjectData.TAG_VISITATION_INFORMATION);
		createOptionalTextField(WcpaProjectData.TAG_CURRENT_LAND_USES);
		createOptionalTextField(WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
	}
}
