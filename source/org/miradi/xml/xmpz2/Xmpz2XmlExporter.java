/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.AudienceSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.DashboardSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.IucnRedlistSpeciesSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.ObjectTreeTableConfigurationSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.OrganizationSchema;
import org.miradi.schemas.OtherNotableSpeciesSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ReportTemplateSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.schemas.XslTemplateSchema;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport, final Xmpz2XmlUnicodeWriter outToUse)
	{
		super(projectToExport);
		
		out = outToUse;
		objectTypeToExporterMap = createTypeToExporterMap();
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		new ProjectMetadataExporter(getWriter()).writeBaseObjectDataSchemaElement();
		new ThreatRatingExporter(getWriter()).writeThreatRatings();
		exportPools();
		new ExtraDataExporter(getProject(), getWriter()).exportExtraData();
		getWriter().writeElement(DELETED_ORPHANS_ELEMENT_NAME, getProject().getQuarantineFileContents());
		getWriter().writeMainElementEnd();
	}

	private void exportPools() throws Exception
	{
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			EAMObjectPool pool = getProject().getPool(objectType);
			final BaseObjectExporter baseObjectExporter = getBaseObjectExporter(objectType);
			if (baseObjectExporter == null)
				continue;
			
			final String containerName = baseObjectExporter.getExporterContainerName(objectType);
			final String poolName = getWriter().createPoolElementName(containerName);
			ORefList sortedRefList = pool.getSortedRefList();
			if (sortedRefList.hasRefs())
				exportBaseObjects(poolName, sortedRefList, baseObjectExporter);
		}
	}

	private void exportBaseObjects(final String poolName, ORefList sortedRefList, final BaseObjectExporter baseObjectExporter) throws Exception
	{
		getWriter().writeStartElement(poolName);
		for(ORef ref : sortedRefList)
		{
			BaseObject baseObject = BaseObject.find(getProject(), ref);
			baseObjectExporter.writeBaseObjectDataSchemaElement(baseObject);
		}
		getWriter().writeEndElement(poolName);
	}

	private HashMap<Integer, BaseObjectExporter> createTypeToExporterMap()
	{
		HashMap<Integer, BaseObjectExporter> map = new HashMap<Integer, BaseObjectExporter>();
		map.put(DashboardSchema.getObjectType(), new DashboardExporter(getWriter()));
		map.put(IndicatorSchema.getObjectType(), new IndicatorExporter(getWriter()));
		map.put(GoalSchema.getObjectType(), new DesireExporter(getWriter()));
		map.put(ObjectiveSchema.getObjectType(), new DesireExporter(getWriter()));
		map.put(ResourceAssignmentSchema.getObjectType(), new ResourceAssignmentExporter(getWriter()));
		map.put(ExpenseAssignmentSchema.getObjectType(), new ExpenseAssignmentExporter(getWriter()));
		map.put(TaskSchema.getObjectType(), new TaskExporter(getWriter()));
		map.put(ProjectResourceSchema.getObjectType(), new ProjectResourceExporter(getWriter()));
		map.put(DiagramLinkSchema.getObjectType(), new DiagramLinkExporter(getWriter()));
		map.put(ConceptualModelDiagramSchema.getObjectType(), new ConceptualModelDiagramExporter(getWriter()));
		map.put(ResultsChainDiagramSchema.getObjectType(), new ResultsChainExporter(getWriter()));
		map.put(DiagramFactorSchema.getObjectType(), new DiagramFactorExporter(getWriter()));
		map.put(StrategySchema.getObjectType(), new StrategyExporter(getWriter()));
		map.put(TargetSchema.getObjectType(), new TargetExporter(getWriter()));
		map.put(HumanWelfareTargetSchema.getObjectType(), new HumanWelfareTargetExporter(getWriter()));
		map.put(TaggedObjectSetSchema.getObjectType(), new TaggedObjectSetExporter(getWriter()));
		map.put(IucnRedlistSpeciesSchema.getObjectType(), new IucnRedlistSpeciesExporter(getWriter()));
		map.put(BudgetCategoryOneSchema.getObjectType(), new BudgetCategoryOneExporter(getWriter()));
		map.put(BudgetCategoryTwoSchema.getObjectType(), new BudgetCategoryTwoExporter(getWriter()));
		
		map.put(AccountingCodeSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(FundingSourceSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(KeyEcologicalAttributeSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(CauseSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(IntermediateResultSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ThreatReductionResultSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(TextBoxSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ObjectTreeTableConfigurationSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(CostAllocationRuleSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(MeasurementSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(StressSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(GroupBoxSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(SubTargetSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ProgressReportSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(OrganizationSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ProgressPercentSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ReportTemplateSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(ScopeBoxSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(OtherNotableSpeciesSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(AudienceSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		map.put(XslTemplateSchema.getObjectType(), new BaseObjectExporter(getWriter()));
		
		return map;
	}
	
	private BaseObjectExporter getBaseObjectExporter(final int objectType)
	{
		return objectTypeToExporterMap.get(objectType);
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlUnicodeWriter out;
	private HashMap<Integer, BaseObjectExporter> objectTypeToExporterMap;
}
