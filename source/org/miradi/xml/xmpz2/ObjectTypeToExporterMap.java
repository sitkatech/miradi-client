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

package org.miradi.xml.xmpz2;

import org.miradi.schemas.*;
import org.miradi.xml.xmpz2.objectExporters.*;

import java.util.HashMap;

public class ObjectTypeToExporterMap extends HashMap<Integer, BaseObjectExporter>
{
	public void fillTypeToExporterMap(Xmpz2XmlWriter writerToUse) throws Exception
	{
		writer = writerToUse;
		addExporterToMap(new DashboardExporter(getWriter()));
		addExporterToMap(new IndicatorExporter(getWriter()));
		addExporterToMap(new GoalExporter(getWriter()));
		addExporterToMap(new ObjectiveExporter(getWriter()));
		addExporterToMap(new TimeframeExporter(getWriter()));
		addExporterToMap(new ResourceAssignmentExporter(getWriter()));
		addExporterToMap(new ExpenseAssignmentExporter(getWriter()));
		addExporterToMap(new TaskExporter(getWriter()));
		addExporterToMap(new ProjectResourceExporter(getWriter()));
		addExporterToMap(new DiagramLinkExporter(getWriter()));
		addExporterToMap(new ConceptualModelDiagramExporter(getWriter()));
		addExporterToMap(new ResultsChainExporter(getWriter()));
		addExporterToMap(new DiagramFactorExporter(getWriter()));
		addExporterToMap(new StrategyExporter(getWriter()));
		addExporterToMap(new ThreatReductionResultExporter(getWriter()));
		addExporterToMap(new TargetExporter(getWriter()));
		addExporterToMap(new HumanWelfareTargetExporter(getWriter()));
		addExporterToMap(new BiophysicalFactorExporter(getWriter()));
		addExporterToMap(new CauseExporter(getWriter()));
		addExporterToMap(new TaggedObjectSetExporter(getWriter()));
		addExporterToMap(new IucnRedlistSpeciesExporter(getWriter()));
		addExporterToMap(new BudgetCategoryOneExporter(getWriter()));
		addExporterToMap(new BudgetCategoryTwoExporter(getWriter()));
		addExporterToMap(new ObjectTreeTableConfigurationExporter(getWriter()));
		addExporterToMap(new StressExporter(getWriter()));
		addExporterToMap(new MiradiShareTaxonomyExporter(getWriter()));
		
		addGenericExporterToMap(AccountingCodeSchema.getObjectType());
		addGenericExporterToMap(FundingSourceSchema.getObjectType());
		addGenericExporterToMap(KeyEcologicalAttributeSchema.getObjectType());
		addGenericExporterToMap(BiophysicalResultSchema.getObjectType());
		addGenericExporterToMap(IntermediateResultSchema.getObjectType());
		addGenericExporterToMap(TextBoxSchema.getObjectType());
		addGenericExporterToMap(CostAllocationRuleSchema.getObjectType());
		addGenericExporterToMap(MeasurementSchema.getObjectType());
		addGenericExporterToMap(GroupBoxSchema.getObjectType());
		addGenericExporterToMap(SubTargetSchema.getObjectType());
		addGenericExporterToMap(ProgressReportSchema.getObjectType());
		addGenericExporterToMap(ExtendedProgressReportSchema.getObjectType());
		addGenericExporterToMap(ResultReportSchema.getObjectType());
		addGenericExporterToMap(OrganizationSchema.getObjectType());
		addGenericExporterToMap(ProgressPercentSchema.getObjectType());
		addGenericExporterToMap(ScopeBoxSchema.getObjectType());
		addGenericExporterToMap(OtherNotableSpeciesSchema.getObjectType());
		addGenericExporterToMap(AudienceSchema.getObjectType());
		addGenericExporterToMap(FutureStatusSchema.getObjectType());
		addGenericExporterToMap(MethodSchema.getObjectType());
		addGenericExporterToMap(OutputSchema.getObjectType());
	}
	
	private void addGenericExporterToMap(final int objectType) throws Exception
	{
		addExporterToMap(new BaseObjectExporter(getWriter(), objectType));
	}
	
	private void addExporterToMap(final BaseObjectExporter baseObjectExporter) throws Exception
	{
		if (containsKey(baseObjectExporter.getObjectType()))
			throw new Exception("Attempting to override existing baseObject exporter for type:" + baseObjectExporter.getObjectType());
		
		put(baseObjectExporter.getObjectType(), baseObjectExporter);
	}
	
	private Xmpz2XmlWriter getWriter()
	{
		return writer;
	}

	private Xmpz2XmlWriter writer;
}
