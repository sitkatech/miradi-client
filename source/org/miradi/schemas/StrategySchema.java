/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.questions.*;

public class StrategySchema extends EvidenceFactorSchema
{
	public StrategySchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaRequiredChoice(Strategy.TAG_STATUS, StrategyStatusQuestion.class);
		createFieldSchemaIdList(Strategy.TAG_ACTIVITY_IDS, TaskSchema.getObjectType());
		createFieldSchemaChoice(Strategy.TAG_TAXONOMY_CODE, StrategyTaxonomyQuestion.class);
		createFieldSchemaChoice(Strategy.TAG_IMPACT_RATING, StrategyImpactQuestion.class);
		createFieldSchemaChoice(Strategy.TAG_FEASIBILITY_RATING, StrategyFeasibilityQuestion.class);
		createFieldSchemaMultiLineUserText(Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
		createFieldSchemaChoice(Strategy.TAG_EVIDENCE_CONFIDENCE, StrategyEvidenceConfidenceQuestion.class);

		createFieldSchemaOptionalRef(Strategy.TAG_ASSIGNED_LEADER_RESOURCE);
		createBudgetSchemas();
		createProgressReportSchema();
	    writeIndicatorIds();
		writeObjectiveIds();
		createOutputSchema();
		createTaxonomyClassificationSchemaField();
	
		createPseudoFieldSchemaString(Strategy.PSEUDO_TAG_RATING_SUMMARY);
		createPseudoFieldSchemaQuestion(Strategy.PSEUDO_TAG_IMPACT_RATING_VALUE);
		createPseudoFieldSchemaQuestion(Strategy.PSEUDO_TAG_FEASIBILITY_RATING_VALUE);
		createPseudoFieldSchemaQuestion(Strategy.PSEUDO_TAG_RATING_SUMMARY_VALUE);
		createPseudoFieldSchemaQuestion(Strategy.PSEUDO_TAG_TAXONOMY_CODE_VALUE);
		createPseudoFieldSchemaString(Strategy.PSEUDO_TAG_ACTIVITIES);
		createPseudoFieldSchemaRefList(Strategy.PSEUDO_TAG_RELEVANT_GOAL_REFS);
		createPseudoFieldSchemaRefList(Strategy.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS);
		createPseudoFieldSchemaRefList(Strategy.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
	}

	public static int getObjectType()
	{
		return ObjectType.STRATEGY;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "Strategy";
}
