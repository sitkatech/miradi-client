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

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class StrategyPoolImporter extends FactorPoolImporter
{
	public StrategyPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.STRATEGY, Strategy.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);

		importObjectiveIds(node, destinationRef);
		
		importIds(node, destinationRef, Strategy.TAG_ACTIVITY_IDS, Task.getObjectType(), WcsXmlConstants.ACTIVITY);
		importField(node, destinationRef, Strategy.TAG_STATUS);
		importCodeField(node, destinationRef, Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion());
		importCodeField(node, destinationRef, Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion());
		importCodeField(node, destinationRef, Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion());
		importField(node, destinationRef, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		importProgressReportRefs(node, destinationRef);
		importExpenseAssignmentRefs(node, destinationRef);
		importResourceAssignmentIds(node, destinationRef);
		importIndicatorIds(node, destinationRef);
	}	
}
