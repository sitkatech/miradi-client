/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.project.Project;

import java.util.HashMap;
import java.util.Map;

public class Schemas
{
	public Schemas(final Project projectToUse)
	{
		schemas = new HashMap<Integer, BaseObjectSchema>();

		registerSchema(new RatingCriterionSchema());
		registerSchema(new ValueOptionSchema());
		registerSchema(new TaskSchema());
		registerSchema(new ViewDataSchema(projectToUse));
		registerSchema(new FactorLinkSchema());
		registerSchema(new ProjectResourceSchema());
		registerSchema(new IndicatorSchema());
		registerSchema(new ObjectiveSchema());
		registerSchema(new GoalSchema());
		registerSchema(new ProjectMetadataSchema());
		registerSchema(new DiagramLinkSchema());
		registerSchema(new ResourceAssignmentSchema());
		registerSchema(new AccountingCodeSchema());
		registerSchema(new FundingSourceSchema());
		registerSchema(new KeyEcologicalAttributeSchema());
		registerSchema(new DiagramFactorSchema());
		registerSchema(new ConceptualModelDiagramSchema());
		registerSchema(new CauseSchema());
		registerSchema(new StrategySchema());
		registerSchema(new TargetSchema());
		registerSchema(new IntermediateResultSchema());
		registerSchema(new ResultsChainDiagramSchema());
		registerSchema(new ThreatReductionResultSchema());
		registerSchema(new TextBoxSchema());
		registerSchema(new ObjectTreeTableConfigurationSchema(projectToUse));
		registerSchema(new WwfProjectDataSchema());
		registerSchema(new CostAllocationRuleSchema());
		registerSchema(new MeasurementSchema());
		registerSchema(new StressSchema());
		registerSchema(new ThreatStressRatingSchema());
		registerSchema(new GroupBoxSchema());
		registerSchema(new SubTargetSchema());
		registerSchema(new ProgressReportSchema());
		registerSchema(new ExtendedProgressReportSchema());
		registerSchema(new ResultReportSchema());
		registerSchema(new RareProjectDataSchema());
		registerSchema(new WcsProjectDataSchema());
		registerSchema(new TncProjectDataSchema());
		registerSchema(new FosProjectDataSchema());
		registerSchema(new OrganizationSchema());
		registerSchema(new WcpaProjectDataSchema());
		registerSchema(new XenodataSchema());
		registerSchema(new ProgressPercentSchema());
		registerSchema(new ReportTemplateSchema());
		registerSchema(new TaggedObjectSetSchema());
		registerSchema(new TableSettingsSchema());
		registerSchema(new ThreatStressRatingDataSchema());
		registerSchema(new ThreatSimpleRatingDataSchema());
		registerSchema(new ScopeBoxSchema());
		registerSchema(new ExpenseAssignmentSchema());
		registerSchema(new HumanWelfareTargetSchema());
		registerSchema(new IucnRedlistSpeciesSchema());
		registerSchema(new OtherNotableSpeciesSchema());
		registerSchema(new AudienceSchema());
		registerSchema(new BudgetCategoryOneSchema());
		registerSchema(new BudgetCategoryTwoSchema());
		registerSchema(new DashboardSchema());
		registerSchema(new XslTemplateSchema());
		registerSchema(new MiradiShareProjectDataSchema());
		registerSchema(new MiradiShareTaxonomySchema());
		registerSchema(new TaxonomyAssociationSchema());
		registerSchema(new FutureStatusSchema());
		registerSchema(new BiophysicalFactorSchema());
		registerSchema(new BiophysicalResultSchema());
		registerSchema(new TimeframeSchema());
		registerSchema(new AccountingClassificationAssociationSchema());
		registerSchema(new MethodSchema());
		registerSchema(new OutputSchema());
	}
	
	public BaseObjectSchema get(final int objectType)
	{
		Object schema = schemas.get(objectType);
		if(schema == null)
			throw new RuntimeException("Unknown schema for object type: " + objectType);
		
		return (BaseObjectSchema)schema;
	}
	
	private void registerSchema(final BaseObjectSchema schema)
	{
		schemas.put(schema.getType(), schema);
	}

	private Map<Integer, BaseObjectSchema> schemas;
}
