/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.main.VersionConstants;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objectpools.AccountingClassificationAssociationPool;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.schemas.AccountingClassificationAssociationSchema;
import org.miradi.schemas.TaxonomyAssociationSchema;
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
		exportThreatRatings();
		exportPools();
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
		DateFormat formatter = DateFormat.getDateTimeInstance();
		return formatter.format(Calendar.getInstance().getTime());
	}

	private void exportProjectSummary() throws Exception
	{
		new SingletonObjectExporter(getWriter()).writeBaseObjectDataSchemaElement();
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
			exportTaxonomyAssociations(objectType);

			if (ResourceAssignment.is(objectType) || ExpenseAssignment.is(objectType))
				exportAccountingClassificationAssociations(objectType);

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
	
	private void exportTaxonomyAssociations(int objectType) throws Exception
	{
		Vector<String> poolNamesForType = TaxonomyHelper.getTaxonomyAssociationPoolNamesForType(objectType);
		for(String poolName : poolNamesForType)
		{
			exportTaxonomyAssociationsForBaseObjectType(poolName);
		}
	}

	private void exportTaxonomyAssociationsForBaseObjectType(String taxonomyAssociationPoolName) throws Exception
	{
		TaxonomyAssociationExporter taxonomyAssociationExporter = new TaxonomyAssociationExporter(getWriter(), TaxonomyAssociationSchema.getObjectType());
		TaxonomyAssociationPool taxonomyAssociationPool = (TaxonomyAssociationPool) getProject().getPool(TaxonomyAssociationSchema.getObjectType());
		Vector<AbstractTaxonomyAssociation> taxonomyAssociationsForPoolName = taxonomyAssociationPool.findTaxonomyAssociationsForPoolName(taxonomyAssociationPoolName);
		getWriter().writeStartElement(taxonomyAssociationPoolName);
		for(AbstractTaxonomyAssociation taxonomyAssociation : taxonomyAssociationsForPoolName)
		{
			TaxonomyAssociation typedTaxonomyAssociation = (TaxonomyAssociation) taxonomyAssociation;
			taxonomyAssociationExporter.writeBaseObjectDataSchemaElement(typedTaxonomyAssociation);
		}
		getWriter().writeEndElement(taxonomyAssociationPoolName);
	}

	private void exportAccountingClassificationAssociations(int objectType) throws Exception
	{
		Vector<String> poolNamesForType = TaxonomyHelper.getAccountingClassificationAssociationPoolNamesForType(objectType);
		for(String poolName : poolNamesForType)
		{
			exportAccountingClassificationAssociationsForBaseObjectType(poolName);
		}
	}

	private void exportAccountingClassificationAssociationsForBaseObjectType(String taxonomyAssociationPoolName) throws Exception
	{
		AccountingClassificationAssociationExporter accountingClassificationAssociationExporter = new AccountingClassificationAssociationExporter(getWriter(), AccountingClassificationAssociationSchema.getObjectType());
		AccountingClassificationAssociationPool accountingClassificationAssociationPool = (AccountingClassificationAssociationPool) getProject().getPool(AccountingClassificationAssociationSchema.getObjectType());
		Vector<AbstractTaxonomyAssociation> accountingClassificationAssociationsForPoolName = accountingClassificationAssociationPool.findTaxonomyAssociationsForPoolName(taxonomyAssociationPoolName);
		getWriter().writeStartElement(taxonomyAssociationPoolName);
		for(AbstractTaxonomyAssociation accountingClassificationAssociation : accountingClassificationAssociationsForPoolName)
		{
			AccountingClassificationAssociation typedaccountingClassificationAssociation = (AccountingClassificationAssociation) accountingClassificationAssociation;
			accountingClassificationAssociationExporter.writeBaseObjectDataSchemaElement(typedaccountingClassificationAssociation);
		}
		getWriter().writeEndElement(taxonomyAssociationPoolName);
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
