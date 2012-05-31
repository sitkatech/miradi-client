/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import java.util.Vector;

import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ViewData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.CostAllocationRuleSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.utils.Translation;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class Xmpz2XmlSchemaCreator implements Xmpz2XmlConstants
{
	public static void main(String[] args) throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();

		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(System.out);
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator(writer);
		
		creator.writeRncSchema();
	}

	public Xmpz2XmlSchemaCreator(Xmpz2SchemaWriter writerToUse) throws Exception
	{
		writer = writerToUse;
		project = new Project();
		baseObjectSchemaWriters = getTopLevelBaseObjectSchemas();
	}

	public void writeRncSchema() throws Exception
	{
		writeHeader();
		writeConservationProjectElement();
		writeBaseObjectSchemaElements();
	}

	private void writeHeader()
	{
		getSchemaWriter().writeNamespace(NAME_SPACE);
		getSchemaWriter().defineAlias("start", CONSERVATION_PROJECT + ".element");
	}

	private void writeConservationProjectElement()
	{
		getSchemaWriter().startElementDefinition(CONSERVATION_PROJECT);

		Vector<String> elementNames = new Vector<String>();
		elementNames.add(createElementName(PROJECT_SUMMARY));
		elementNames.add(createElementName(PROJECT_SUMMARY_SCOPE));
		elementNames.add(createElementName(PROJECT_SUMMARY_LOCATION));
		elementNames.add(createElementName(PROJECT_SUMMARY_PLANNING));
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			String poolName = baseObjectSchemaWriter.getPoolName();
			elementNames.add(createElementName(poolName));
		}
		
		getSchemaWriter().writeContentsList(elementNames);
		
		getSchemaWriter().endElementDefinition(CONSERVATION_PROJECT);
		getSchemaWriter().flush();
	}
	
	private void writeBaseObjectSchemaElements() throws Exception
	{
		for(BaseObjectSchemaWriter baseObjectSchemaWriter : baseObjectSchemaWriters)
		{
			writeBaseObjectSchema(baseObjectSchemaWriter);
		}		
	}

	private void writeBaseObjectSchema(BaseObjectSchemaWriter baseObjectSchemaWriter) throws Exception
	{
		writeBaseObjectSchemaHeader(baseObjectSchemaWriter);
		getSchemaWriter().startBlock();
		writeElementContent(baseObjectSchemaWriter);
		getSchemaWriter().endBlock();
	}

	private void writeElementContent(BaseObjectSchemaWriter baseObjectSchemaWriter) throws Exception
	{
		baseObjectSchemaWriter.writeFields(getSchemaWriter());
		getSchemaWriter().println();
	}
	
	public void writeIdAttribute()
	{
		getSchemaWriter().printIndented("attribute " + ID + " "+ "{xsd:integer} &");
	}

	private void writeBaseObjectSchemaHeader(BaseObjectSchemaWriter baseObjectSchemaWriter)
	{
		String baseObjectName = baseObjectSchemaWriter.getXmpz2ElementName();
		String baseObjectPoolName = baseObjectSchemaWriter.getPoolName();
		
		String result = XmpzXmlConstants.ELEMENT_NAME + XmpzXmlConstants.PREFIX + baseObjectPoolName + " { " + createElementName(baseObjectName) + "* }";
		getSchemaWriter().defineAlias(createElementName(baseObjectPoolName), result);
		getSchemaWriter().defineAlias(createElementName(baseObjectName), "element miradi:" + baseObjectName);
	}

	private String createElementName(String elementName)
	{
		return elementName + ".element";
	}

	private Vector<BaseObjectSchemaWriter> getTopLevelBaseObjectSchemas()
	{
		Vector<BaseObjectSchemaWriter> schemaWriters = new Vector<BaseObjectSchemaWriter>();
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (!isPoolDirectlyInXmpz2(objectType))
				continue;

			BaseObjectPool pool = (BaseObjectPool) getProject().getPool(objectType);
			if(pool == null)
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			schemaWriters.add(new BaseObjectSchemaWriter(this, baseObjectSchema));
		}
		
		return schemaWriters;
	}
	
	private boolean isPoolDirectlyInXmpz2(int objectType)
	{
		if (FactorLink.is(objectType))
			return false;
		
		if (ViewData.is(objectType))
			return false;
		
		if (ProjectMetadata.is(objectType))
			return false;
		
		 if (CostAllocationRuleSchema.getObjectType() == objectType)
			 return false;
		 
		  if (ThreatStressRating.is(objectType))
			  return false;
		  
		  if (WcpaProjectDataSchema.getObjectType() == objectType)
			  return false;
		  
		  if (Xenodata.is(objectType))
			  return false;
		  
		  if (TableSettings.is(objectType))
			  return false;
		  
		  if (ThreatRatingCommentsData.is(objectType))
			  return false;
		  
		return !Xmpz2XmlImporter.isCustomImport(objectType);
	}
	
	public Xmpz2SchemaWriter getSchemaWriter()
	{
		return writer;
	}

	public Project getProject()
	{
		return project;
	}

	private Project project;
	private Xmpz2SchemaWriter writer;
	private Vector<BaseObjectSchemaWriter> baseObjectSchemaWriters;
}
