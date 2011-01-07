/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.PlanningTreeTargetPositionQuestion;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class ProjectSummaryPlanningImporter extends AbstractXmpzObjectImporter
{
	public ProjectSummaryPlanningImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.PROJECT_SUMMARY_PLANNING);
	}

	@Override
	public void importElement() throws Exception
	{
		Node projectSummaryPlanningNode = getImporter().getNode(getImporter().getRootNode(), XmpzXmlConstants.PROJECT_SUMMARY_PLANNING);
		
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_START_DATE);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_EXPECTED_END_DATE);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_WORKPLAN_START_DATE);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_WORKPLAN_END_DATE);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_FISCAL_YEAR_START);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_PLANNING_COMMENTS);

		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_CURRENCY_TYPE);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_CURRENCY_SYMBOL);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		importField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		
		importCodeField(projectSummaryPlanningNode, getMetadataRef(), ProjectMetadata.TAG_PLANNING_TREE_TARGET_NODE_POSITION, new PlanningTreeTargetPositionQuestion());
	}
}
