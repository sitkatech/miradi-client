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
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport, final Xmpz2XmlWriter outToUse)
	{
		super(projectToExport);
		
		out = outToUse;
		objectTypeToExporterMap = new HashMap<Integer, BaseObjectExporter>(); 
		fillTypeToExporterMap();
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		exportProjectSummary();
		exportThreatRatings();
		exportPools();
		exportExtraData();
		exportQuarantinedFileContents();
		getWriter().writeMainElementEnd();
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
		getWriter().writeElement(DELETED_ORPHANS_ELEMENT_NAME, getProject().getQuarantineFileContents());
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
			exportBaseObjects(sortedRefList, objectType);
		}
	}

	private void exportBaseObjects(ORefList sortedRefList, final int objectType) throws Exception
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

	private void fillTypeToExporterMap()
	{
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
		addExporterToMap(new TargetExporter(getWriter()));
		addExporterToMap(new HumanWelfareTargetExporter(getWriter()));
		addExporterToMap(new TaggedObjectSetExporter(getWriter()));
		addExporterToMap(new IucnRedlistSpeciesExporter(getWriter()));
		addExporterToMap(new BudgetCategoryOneExporter(getWriter()));
		addExporterToMap(new BudgetCategoryTwoExporter(getWriter()));
		
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
