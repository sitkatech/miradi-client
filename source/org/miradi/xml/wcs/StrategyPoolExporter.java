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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;

public class StrategyPoolExporter extends FactorPoolExporter
{
	public StrategyPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, STRATEGY, Strategy.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		Strategy strategy = (Strategy) baseObject;
		
		writeObjectiveIds(strategy);
		writeOptionalIds(Strategy.TAG_ACTIVITY_IDS, XmpzXmlConstants.ACTIVITY, strategy.getActivityRefs());
		writeOptionalElementWithSameTag(strategy, Strategy.TAG_STATUS);
		writeCodeElementSameAsTag(strategy, Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion());		
		writeCodeElementSameAsTag(strategy, Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion());
		writeCodeElementSameAsTag(strategy, Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion());
		writeOptionalElementWithSameTag(strategy, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		writeProgressReportIds(strategy);
		writeExpenseAssignmentIds(strategy);
		writeResourceAssignmentIds(strategy);
		writeIndicatorIds(strategy);
	}
}
