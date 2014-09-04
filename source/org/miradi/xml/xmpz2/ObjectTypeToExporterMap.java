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

package org.miradi.xml.xmpz2;

import java.util.HashMap;

import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.AudienceSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.OrganizationSchema;
import org.miradi.schemas.OtherNotableSpeciesSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.xml.xmpz2.objectExporters.BudgetCategoryOneExporter;
import org.miradi.xml.xmpz2.objectExporters.BudgetCategoryTwoExporter;
import org.miradi.xml.xmpz2.objectExporters.ConceptualModelDiagramExporter;
import org.miradi.xml.xmpz2.objectExporters.DashboardExporter;
import org.miradi.xml.xmpz2.objectExporters.DiagramFactorExporter;
import org.miradi.xml.xmpz2.objectExporters.DiagramLinkExporter;
import org.miradi.xml.xmpz2.objectExporters.ExpenseAssignmentExporter;
import org.miradi.xml.xmpz2.objectExporters.GoalExporter;
import org.miradi.xml.xmpz2.objectExporters.HumanWelfareTargetExporter;
import org.miradi.xml.xmpz2.objectExporters.IndicatorExporter;
import org.miradi.xml.xmpz2.objectExporters.IucnRedlistSpeciesExporter;
import org.miradi.xml.xmpz2.objectExporters.ObjectTreeTableConfigurationExporter;
import org.miradi.xml.xmpz2.objectExporters.ObjectiveExporter;
import org.miradi.xml.xmpz2.objectExporters.ProjectResourceExporter;
import org.miradi.xml.xmpz2.objectExporters.ResourceAssignmentExporter;
import org.miradi.xml.xmpz2.objectExporters.ResultsChainExporter;
import org.miradi.xml.xmpz2.objectExporters.StrategyExporter;
import org.miradi.xml.xmpz2.objectExporters.TaggedObjectSetExporter;
import org.miradi.xml.xmpz2.objectExporters.TargetExporter;
import org.miradi.xml.xmpz2.objectExporters.TaskExporter;
import org.miradi.xml.xmpz2.objectExporters.ThreatReductionResultExporter;

public class ObjectTypeToExporterMap extends HashMap<Integer, BaseObjectExporter>
{
	public void fillTypeToExporterMap(Xmpz2XmlWriter writerToUse) throws Exception
	{
		writer = writerToUse;
		addExporterToMap(new DashboardExporter(getWriter()));
		addExporterToMap(new IndicatorExporter(getWriter()));
		addExporterToMap(new GoalExporter(getWriter()));
		addExporterToMap(new ObjectiveExporter(getWriter()));
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
		addGenericExporterToMap(IntermediateResultSchema.getObjectType());
		addGenericExporterToMap(TextBoxSchema.getObjectType());
		addGenericExporterToMap(CostAllocationRuleSchema.getObjectType());
		addGenericExporterToMap(MeasurementSchema.getObjectType());
		addGenericExporterToMap(GroupBoxSchema.getObjectType());
		addGenericExporterToMap(SubTargetSchema.getObjectType());
		addGenericExporterToMap(ProgressReportSchema.getObjectType());
		addGenericExporterToMap(OrganizationSchema.getObjectType());
		addGenericExporterToMap(ProgressPercentSchema.getObjectType());
		addGenericExporterToMap(ScopeBoxSchema.getObjectType());
		addGenericExporterToMap(OtherNotableSpeciesSchema.getObjectType());
		addGenericExporterToMap(AudienceSchema.getObjectType());
		addGenericExporterToMap(FutureStatusSchema.getObjectType());
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
