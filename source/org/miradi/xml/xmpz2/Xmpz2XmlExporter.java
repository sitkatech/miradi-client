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

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Dashboard;
import org.miradi.objects.Desire;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlExporter extends XmlExporter implements XmpzXmlConstants
{
	public Xmpz2XmlExporter(Project projectToExport, final Xmpz2XmlUnicodeWriter outToUse)
	{
		super(projectToExport);
		
		out = outToUse;
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		getWriter().writeXmlHeader();
		getWriter().writeMainElementStart();
		new ProjectMetadataExporter(getWriter()).writeBaseObjectDataSchemaElement();
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
				final String poolName = getPoolName(objectType);
				ORefList sortedRefList = pool.getSortedRefList();
				if (sortedRefList.hasRefs())
					exportBaseObjects(poolName, sortedRefList);
			}
		}
	}

	private String getPoolName(int objectType)
	{
		if (ProjectMetadata.is(objectType))
			return PROJECT_SUMMARY;
		
		if (ConceptualModelDiagram.is(objectType))
			return getWriter().createPoolElementName(CONCEPTUAL_MODEL);
		
		final String internalObjectTypeName = getProject().getObjectManager().getInternalObjectTypeName(objectType);
		return getWriter().createPoolElementName(internalObjectTypeName);
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

		return getProject().getPool(objectType);
	}
	
	private void exportBaseObjects(final String poolName, ORefList sortedRefList) throws Exception
	{
		getWriter().writeStartElement(poolName);
		for(ORef ref : sortedRefList)
		{
			BaseObject baseObject = BaseObject.find(getProject(), ref);
			createBaseObjectExporter(baseObject).writeBaseObjectDataSchemaElement(baseObject);
		}
		getWriter().writeEndElement(poolName);
	}

	private BaseObjectExporter createBaseObjectExporter(final BaseObject baseObject)
	{
		if (Dashboard.is(baseObject))
			return new DashboardExporter(getWriter());
		
		if (Indicator.is(baseObject))
			return new IndicatorExporter(getWriter());
		
		if (Desire.isDesire(baseObject.getRef()))
			return new DesireExporter(getWriter());
		
		if (ResourceAssignment.is(baseObject))
			return new ResourceAssignmentExporter(getWriter());
		
		if (ExpenseAssignment.is(baseObject))
			return new ExpenseAssignmentExporter(getWriter());
		
		return new BaseObjectExporter(getWriter());
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return out;
	}

	private Xmpz2XmlUnicodeWriter out;
}
