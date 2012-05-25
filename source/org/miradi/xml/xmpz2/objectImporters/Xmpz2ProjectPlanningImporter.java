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
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class Xmpz2ProjectPlanningImporter extends AbstractXmpz2ObjectImporter
{
	public Xmpz2ProjectPlanningImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse);
	}

	public void importFields() throws Exception
	{
		Node projectSummaryPlanningNode = getImporter().getNode(getImporter().getRootNode(), PROJECT_SUMMARY_PLANNING);
		
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_START_DATE);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_EXPECTED_END_DATE);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_WORKPLAN_START_DATE);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_WORKPLAN_END_DATE);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_FISCAL_YEAR_START);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_PLANNING_COMMENTS);

		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_CURRENCY_TYPE);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_CURRENCY_SYMBOL);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		importProjectMetadataField(projectSummaryPlanningNode, ProjectMetadata.TAG_FINANCIAL_COMMENTS);
	}
	
	private void importProjectMetadataField(Node projectSummaryNode, String tag) throws Exception
	{
		getImporter().importStringField(projectSummaryNode, PROJECT_SUMMARY_PLANNING, getMetadataRef(), tag);
	}
}
