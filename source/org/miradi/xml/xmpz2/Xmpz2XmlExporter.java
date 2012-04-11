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
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.DashboardSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IucnRedlistSpeciesSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport, final Xmpz2XmlUnicodeWriter outToUse)
	{
		super(projectToExport);
		
		out = outToUse;
		createTypeToExporterMap();
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
			EAMObjectPool pool = getBaseObjectPoolToExport(objectType);
			if(pool != null)
			{
				final BaseObjectExporter baseObjectExporter = getBaseObjectExporter(objectType);
				final String poolName = baseObjectExporter.getPoolName(objectType);
				ORefList sortedRefList = pool.getSortedRefList();
				if (sortedRefList.hasRefs())
					exportBaseObjects(poolName, sortedRefList, baseObjectExporter);
			}
		}
	}

	private EAMObjectPool getBaseObjectPoolToExport(final int objectType)
	{
		if (objectType == ObjectType.RATING_CRITERION)
			return null;
		
		if (objectType ==  ObjectType.VALUE_OPTION)
			return null;
		
		if (TableSettings.is(objectType))
			return null;
		
		if (ThreatRatingCommentsData.is(objectType))
			return null;
		
		if (ViewData.is(objectType))
			return null;
		
		if (ProjectMetadata.is(objectType))
			return null;

		if (TncProjectDataSchema.getObjectType() == objectType)
			return null;
		
		if (WwfProjectDataSchema.getObjectType() == objectType)
			return null;
		
		if (WcsProjectDataSchema.getObjectType() == objectType)
			return null;
		
		if (RareProjectDataSchema.getObjectType() == objectType)
			return null;

		if (FosProjectDataSchema.getObjectType() == objectType)
			return null;
		
		if (WcpaProjectDataSchema.getObjectType() == objectType)
			return null;
		
		if (FactorLinkSchema.getObjectType() == objectType)
			return null;
		
		if (ThreatStressRating.is(objectType))
			return null;
		
		if (Xenodata.is(objectType))
			return null;
		
		return getProject().getPool(objectType);
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

	private void createTypeToExporterMap()
	{
		objectTypeToExporterMap = new HashMap<Integer, BaseObjectExporter>();
		objectTypeToExporterMap.put(DashboardSchema.getObjectType(), new DashboardExporter(getWriter()));
		objectTypeToExporterMap.put(IndicatorSchema.getObjectType(), new IndicatorExporter(getWriter()));
		objectTypeToExporterMap.put(GoalSchema.getObjectType(), new DesireExporter(getWriter()));
		objectTypeToExporterMap.put(ObjectiveSchema.getObjectType(), new DesireExporter(getWriter()));
		objectTypeToExporterMap.put(ResourceAssignmentSchema.getObjectType(), new ResourceAssignmentExporter(getWriter()));
		objectTypeToExporterMap.put(ExpenseAssignmentSchema.getObjectType(), new ExpenseAssignmentExporter(getWriter()));
		objectTypeToExporterMap.put(TaskSchema.getObjectType(), new TaskExporter(getWriter()));
		objectTypeToExporterMap.put(ProjectResourceSchema.getObjectType(), new ProjectResourceExporter(getWriter()));
		objectTypeToExporterMap.put(DiagramLinkSchema.getObjectType(), new DiagramLinkExporter(getWriter()));
		objectTypeToExporterMap.put(ConceptualModelDiagramSchema.getObjectType(), new ConceptualModelDiagramExporter(getWriter()));
		objectTypeToExporterMap.put(ResultsChainDiagramSchema.getObjectType(), new ResultsChainExporter(getWriter()));
		objectTypeToExporterMap.put(DiagramFactorSchema.getObjectType(), new DiagramFactorExporter(getWriter()));
		objectTypeToExporterMap.put(StrategySchema.getObjectType(), new StrategyExporter(getWriter()));
		objectTypeToExporterMap.put(TargetSchema.getObjectType(), new TargetExporter(getWriter()));
		objectTypeToExporterMap.put(HumanWelfareTargetSchema.getObjectType(), new HumanWelfareTargetExporter(getWriter()));
		objectTypeToExporterMap.put(TaggedObjectSetSchema.getObjectType(), new TaggedObjectSetExporter(getWriter()));
		objectTypeToExporterMap.put(IucnRedlistSpeciesSchema.getObjectType(), new IucnRedlistSpeciesExporter(getWriter()));
		objectTypeToExporterMap.put(BudgetCategoryOneSchema.getObjectType(), new BudgetCategoryOneExporter(getWriter()));
		objectTypeToExporterMap.put(BudgetCategoryTwoSchema.getObjectType(), new BudgetCategoryTwoExporter(getWriter()));
	}
	
	private BaseObjectExporter getBaseObjectExporter(final int objectType)
	{
		if (objectTypeToExporterMap.containsKey(objectType))
			return objectTypeToExporterMap.get(objectType);
		
		return new BaseObjectExporter(getWriter());
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlUnicodeWriter out;
	private HashMap<Integer, BaseObjectExporter> objectTypeToExporterMap;
}
