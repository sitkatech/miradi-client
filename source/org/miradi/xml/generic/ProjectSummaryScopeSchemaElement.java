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

public class ProjectSummaryScopeSchemaElement extends ObjectSchemaElement
{
	public ProjectSummaryScopeSchemaElement()
	{
		super("ProjectSummaryScope");
		
		createTextField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		createTextField(ProjectMetadata.TAG_PROJECT_SCOPE);
		createTextField(ProjectMetadata.TAG_PROJECT_VISION);
		createTextField(ProjectMetadata.TAG_SCOPE_COMMENTS);		

		createTextField(ProjectMetadata.TAG_PROJECT_AREA);
		createTextField(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		createTextField(ProjectMetadata.TAG_RED_LIST_SPECIES);
		createTextField(ProjectMetadata.TAG_OTHER_NOTABLE_SPECIES);

		createNumericField(ProjectMetadata.TAG_HUMAN_POPULATION);
		createTextField(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		createTextField(ProjectMetadata.TAG_SOCIAL_CONTEXT);

		createCodeListField(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, XmlSchemaCreator.VOCABULARY_PROTECTED_AREA_CATEGORIES);
		createTextField(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		createTextField(WcpaProjectData.TAG_LEGAL_STATUS);
		createTextField(WcpaProjectData.TAG_LEGISLATIVE);
		createTextField(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		createTextField(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		createTextField(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		createTextField(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		createTextField(WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		createTextField(WcpaProjectData.TAG_ACCESS_INFORMATION);
		createTextField(WcpaProjectData.TAG_VISITATION_INFORMATION);
		createTextField(WcpaProjectData.TAG_CURRENT_LAND_USES);
		createTextField(WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
	}
}
