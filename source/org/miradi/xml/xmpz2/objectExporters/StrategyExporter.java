/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class StrategyExporter extends BaseObjectWithLeaderResourceFieldExporter
{
	public StrategyExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, StrategySchema.getObjectType());
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		final Strategy strategy = (Strategy) baseObject;
		writeMethodRefs(baseObjectSchema, strategy);
		writeOptionalCalculatedTimePeriodCosts(strategy, baseObjectSchema);
		
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion(), strategy.getTaxonomyCode());
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion(), strategy.getChoiceItemData(Strategy.TAG_IMPACT_RATING).getCode());
		getWriter().writeNonOptionalCodeElement(baseObjectSchema.getObjectName(), Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion(), strategy.getChoiceItemData(Strategy.TAG_FEASIBILITY_RATING).getCode());		
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		
		if (tag.equals(Strategy.TAG_TAXONOMY_CODE))
			return true;
		
		if (tag.equals(Strategy.TAG_IMPACT_RATING))
			return true;
		
		if (tag.equals(Strategy.TAG_FEASIBILITY_RATING))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void writeMethodRefs(BaseObjectSchema baseObjectSchema, final Strategy strategy) throws Exception
	{
		getWriter().writeReflist(baseObjectSchema.getObjectName() + ORDERED_ACTIVITY_IDS, ACTIVITY, strategy.getActivityRefs());
	}	
}
