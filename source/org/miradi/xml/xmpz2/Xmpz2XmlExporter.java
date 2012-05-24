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
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.GroupBoxSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.ObjectTreeTableConfigurationSchema;
import org.miradi.schemas.OrganizationSchema;
import org.miradi.schemas.OtherNotableSpeciesSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.schemas.ReportTemplateSchema;
import org.miradi.schemas.ScopeBoxSchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TextBoxSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.schemas.XslTemplateSchema;
import org.miradi.utils.XmlUtilities2;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.objectExporters.BudgetCategoryOneExporter;
import org.miradi.xml.xmpz2.objectExporters.BudgetCategoryTwoExporter;
import org.miradi.xml.xmpz2.objectExporters.ConceptualModelDiagramExporter;
import org.miradi.xml.xmpz2.objectExporters.DashboardExporter;
import org.miradi.xml.xmpz2.objectExporters.DiagramFactorExporter;
import org.miradi.xml.xmpz2.objectExporters.DiagramLinkExporter;
import org.miradi.xml.xmpz2.objectExporters.ExpenseAssignmentExporter;
import org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter;
import org.miradi.xml.xmpz2.objectExporters.GoalExporter;
import org.miradi.xml.xmpz2.objectExporters.HumanWelfareTargetExporter;
import org.miradi.xml.xmpz2.objectExporters.IndicatorExporter;
import org.miradi.xml.xmpz2.objectExporters.IucnRedlistSpeciesExporter;
import org.miradi.xml.xmpz2.objectExporters.ObjectiveExporter;
import org.miradi.xml.xmpz2.objectExporters.ProjectMetadataExporter;
import org.miradi.xml.xmpz2.objectExporters.ProjectResourceExporter;
import org.miradi.xml.xmpz2.objectExporters.ResourceAssignmentExporter;
import org.miradi.xml.xmpz2.objectExporters.ResultsChainExporter;
import org.miradi.xml.xmpz2.objectExporters.StrategyExporter;
import org.miradi.xml.xmpz2.objectExporters.TaggedObjectSetExporter;
import org.miradi.xml.xmpz2.objectExporters.TargetExporter;
import org.miradi.xml.xmpz2.objectExporters.TaskExporter;
import org.miradi.xml.xmpz2.objectExporters.ThreatRatingExporter;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport)
	{
		super(projectToExport);
		
		objectTypeToExporterMap = new HashMap<Integer, BaseObjectExporter>(); 
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		out = createWriter(outToUse);
		fillTypeToExporterMap(getWriter());
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		exportProjectSummary();
		exportThreatRatings();
		exportPools();
		exportExtraData();
		exportQuarantinedFileContents();
		getWriter().writeMainElementEnd();
	}

	protected Xmpz2XmlWriter createWriter(UnicodeWriter outToUse) throws Exception
	{
		return new Xmpz2XmlWriter(getProject(), outToUse);
	}

	private void exportProjectSummary() throws Exception
	{
		new ProjectMetadataExporter(getWriter()).writeBaseObjectDataSchemaElement();
	}
	
	private void exportThreatRatings() throws Exception
	{
		new ThreatRatingExporter(getWriter()).writeThreatRatings();
	}
	
	private void exportExtraData() throws Exception
	{
		new ExtraDataExporter(getProject(), getWriter()).exportExtraData();
	}
	
	private void exportQuarantinedFileContents() throws Exception
	{
		String quarantineFileContents = getProject().getQuarantineFileContents();
		quarantineFileContents = XmlUtilities2.getXmlEncoded(quarantineFileContents);
		getWriter().writeElement(DELETED_ORPHANS_ELEMENT_NAME, quarantineFileContents);
	}

	private void exportPools() throws Exception
	{
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (!getObjectTypeToExporterMap().containsKey(objectType))
				continue;

			EAMObjectPool pool = getProject().getPool(objectType);
			if (pool.isEmpty())
				continue;
			
			ORefList sortedRefList = pool.getSortedRefList();
			exportBaseObjects(objectType, sortedRefList);
		}
	}

	private void exportBaseObjects(final int objectType, ORefList sortedRefList) throws Exception
	{
		final BaseObjectExporter baseObjectExporter = getObjectTypeToExporterMap().get(objectType);
		final String containerName = baseObjectExporter.getExporterContainerName(objectType);
		final String poolName = getWriter().createPoolElementName(containerName);
		getWriter().writeStartElement(poolName);
		for(ORef ref : sortedRefList)
		{
			BaseObject baseObject = BaseObject.find(getProject(), ref);
			baseObjectExporter.writeBaseObjectDataSchemaElement(baseObject);
		}
		getWriter().writeEndElement(poolName);
	}

	private void fillTypeToExporterMap(final Xmpz2XmlWriter writerToUse)
	{
		addExporterToMap(new DashboardExporter(writerToUse));
		addExporterToMap(new IndicatorExporter(writerToUse));
		addExporterToMap(new GoalExporter(writerToUse));
		addExporterToMap(new ObjectiveExporter(writerToUse));
		addExporterToMap(new ResourceAssignmentExporter(writerToUse));
		addExporterToMap(new ExpenseAssignmentExporter(writerToUse));
		addExporterToMap(new TaskExporter(writerToUse));
		addExporterToMap(new ProjectResourceExporter(writerToUse));
		addExporterToMap(new DiagramLinkExporter(writerToUse));
		addExporterToMap(new ConceptualModelDiagramExporter(writerToUse));
		addExporterToMap(new ResultsChainExporter(writerToUse));
		addExporterToMap(new DiagramFactorExporter(writerToUse));
		addExporterToMap(new StrategyExporter(writerToUse));
		addExporterToMap(new TargetExporter(writerToUse));
		addExporterToMap(new HumanWelfareTargetExporter(writerToUse));
		addExporterToMap(new TaggedObjectSetExporter(writerToUse));
		addExporterToMap(new IucnRedlistSpeciesExporter(writerToUse));
		addExporterToMap(new BudgetCategoryOneExporter(writerToUse));
		addExporterToMap(new BudgetCategoryTwoExporter(writerToUse));
		
		addGenericExporterToMap(AccountingCodeSchema.getObjectType());
		addGenericExporterToMap(FundingSourceSchema.getObjectType());
		addGenericExporterToMap(KeyEcologicalAttributeSchema.getObjectType());
		addGenericExporterToMap(CauseSchema.getObjectType());
		addGenericExporterToMap(IntermediateResultSchema.getObjectType());
		addGenericExporterToMap(ThreatReductionResultSchema.getObjectType());
		addGenericExporterToMap(TextBoxSchema.getObjectType());
		addGenericExporterToMap(ObjectTreeTableConfigurationSchema.getObjectType());
		addGenericExporterToMap(CostAllocationRuleSchema.getObjectType());
		addGenericExporterToMap(MeasurementSchema.getObjectType());
		addGenericExporterToMap(StressSchema.getObjectType());
		addGenericExporterToMap(GroupBoxSchema.getObjectType());
		addGenericExporterToMap(SubTargetSchema.getObjectType());
		addGenericExporterToMap(ProgressReportSchema.getObjectType());
		addGenericExporterToMap(OrganizationSchema.getObjectType());
		addGenericExporterToMap(ProgressPercentSchema.getObjectType());
		addGenericExporterToMap(ReportTemplateSchema.getObjectType());
		addGenericExporterToMap(ScopeBoxSchema.getObjectType());
		addGenericExporterToMap(OtherNotableSpeciesSchema.getObjectType());
		addGenericExporterToMap(AudienceSchema.getObjectType());
		addGenericExporterToMap(XslTemplateSchema.getObjectType());
	}
	
	private void addGenericExporterToMap(final int objectType)
	{
		addExporterToMap(new BaseObjectExporter(getWriter(), objectType));
	}
	
	private void addExporterToMap(final BaseObjectExporter baseObjectExporter)
	{
		getObjectTypeToExporterMap().put(baseObjectExporter.getObjectType(), baseObjectExporter);
	}
	
	private HashMap<Integer, BaseObjectExporter> getObjectTypeToExporterMap()
	{
		return objectTypeToExporterMap;
	}
	
	private Xmpz2XmlWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlWriter out;
	private HashMap<Integer, BaseObjectExporter> objectTypeToExporterMap;
}
