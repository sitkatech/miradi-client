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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.main.VersionConstants;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.StressSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.TaxonomyAssociationSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.utils.XmlUtilities2;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter;
import org.miradi.xml.xmpz2.objectExporters.SingletonObjectExporter;

public class Xmpz2XmlExporter extends XmlExporter implements Xmpz2XmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport)
	{
		super(projectToExport);
		
		objectTypeToExporterMap = new ObjectTypeToExporterMap(); 
	}

	@Override
	public void exportProject(UnicodeXmlWriter outToUse) throws Exception
	{
		out = createWriter(outToUse);
		objectTypeToExporterMap.fillTypeToExporterMap(getWriter());
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		exportExporterDetails();
		exportProjectSummary();
		exportMiradiShareProjectData();
		exportThreatRatings();
		exportPools();
		exportTaxonomyAssociations();
		exportExtraData();
		exportQuarantinedFileContents();
		getWriter().writeMainElementEnd();
	}

	private Xmpz2XmlWriter createWriter(UnicodeWriter outToUse) throws Exception
	{
		return new Xmpz2XmlWriter(getProject(), outToUse);
	}
	
	private void exportExporterDetails() throws Exception
	{
		getWriter().writeStartElement(EXPORT_DETAILS);
		getWriter().writeElement(EXPORTER_NAME, VersionConstants.getMiradiAppName());
		getWriter().writeElement(EXPORTER_VERSION, VersionConstants.getVersion());
		getWriter().writeElement(EXPORT_TIME, getCurrentTime());
		getWriter().writeEndElement(EXPORT_DETAILS);
	}

	protected String getCurrentTime()
	{
		DateFormat formater = DateFormat.getDateTimeInstance();
		return formater.format(Calendar.getInstance().getTime());
	}

	private void exportProjectSummary() throws Exception
	{
		new SingletonObjectExporter(getWriter()).writeBaseObjectDataSchemaElement();
	}
	
	private void exportMiradiShareProjectData()
	{
		
	}
	
	private void exportThreatRatings() throws Exception
	{
		new SimpleThreatRatingExporter(getWriter()).writeThreatRatings();
		new StressBasedThreatRatingExporter(getWriter()).writeThreatRatings();
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
	
	public static HashMap<Integer,String> createTaxonomyAssociationTypeToPoolNameMap()
	{
		HashMap<Integer, String> typeToPoolNameMap = new HashMap<Integer, String>();
		typeToPoolNameMap.put(MiradiShareProjectDataSchema.getObjectType(), MIRADI_SHARE__PROJECT_DATA_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(TargetSchema.getObjectType(), BIODIVERSITY_TARGET_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(HumanWelfareTargetSchema.getObjectType(), HUMAN_WELLBEING_TARGET_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(CauseSchema.getObjectType(), CAUSE_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(StrategySchema.getObjectType(), STRATEGY_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(ResultsChainDiagramSchema.getObjectType(), RESULTS_CHAIN_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(ThreatReductionResultSchema.getObjectType(), THREAT_REDUCTION_RESULT_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(GoalSchema.getObjectType(), GOAL_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(KeyEcologicalAttributeSchema.getObjectType(), KEY_ECOLOGICAL_ATTRIBUTE_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(IndicatorSchema.getObjectType(), INDICATOR_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(ObjectiveSchema.getObjectType(), OBJECTIVE_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(StressSchema.getObjectType(), STRESS_TAXONOMY_ASSOCIATION_POOL);
		typeToPoolNameMap.put(TaskSchema.getObjectType(), TASK_TAXONOMY_ASSOCIATION_POOL);
		
		return typeToPoolNameMap;
	}
	
	public static String getTaxonomyAssociationPoolNameForType(int type)
	{
		return createTaxonomyAssociationTypeToPoolNameMap().get(type);
	}
	
	private void exportTaxonomyAssociations() throws Exception
	{
		HashMap<Integer, String> taxonomyAssociationTypeToPoolMap = createTaxonomyAssociationTypeToPoolNameMap();
		Set<Integer> types = taxonomyAssociationTypeToPoolMap.keySet();
		for(Integer taxonomyAssociationParentType : types)
		{
			String poolName = taxonomyAssociationTypeToPoolMap.get(taxonomyAssociationParentType);
			exportTaxonomyAssociationsForType(taxonomyAssociationParentType, poolName);
		}
	}

	private void exportTaxonomyAssociationsForType(int taxonomyAssociationParentType, String poolName) throws Exception
	{
		TaxonomyAssociationExporter baseObjectExporter = new TaxonomyAssociationExporter(getWriter(), TaxonomyAssociationSchema.getObjectType());
		TaxonomyAssociationPool pool = (TaxonomyAssociationPool) getProject().getPool(TaxonomyAssociationSchema.getObjectType());
		Vector<TaxonomyAssociation> taxonomyAssociations = pool.findTaxonomyAssociationsForBaseObjectType(taxonomyAssociationParentType);
		getWriter().writeStartElement(poolName);
		for(TaxonomyAssociation taxonomyAssociation : taxonomyAssociations)
		{
			baseObjectExporter.writeBaseObjectDataSchemaElement(taxonomyAssociation);
		}
		getWriter().writeEndElement(poolName);
	}

	private ObjectTypeToExporterMap getObjectTypeToExporterMap()
	{
		return objectTypeToExporterMap;
	}
	
	private Xmpz2XmlWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlWriter out;
	private ObjectTypeToExporterMap objectTypeToExporterMap;
}
