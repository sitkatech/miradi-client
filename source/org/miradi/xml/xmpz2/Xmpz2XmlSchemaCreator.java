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

package org.miradi.xml.xmpz2;

import java.util.Vector;

import org.miradi.main.Miradi;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.utils.Translation;
import org.miradi.xml.generic.SchemaWriter;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class Xmpz2XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();

		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator();
		SchemaWriter writer = new SchemaWriter(System.out);
		creator.writeRncSchema(writer);
	}

	private Xmpz2XmlSchemaCreator() throws Exception
	{
		project = new Project();
		baseObjectSchemas = getTopLevelBaseObjectSchemas();
	}

	private void writeRncSchema(SchemaWriter writer) throws Exception
	{
		writeHeader(writer);
		writeConservationProjectElement(writer);
	}

	private void writeHeader(SchemaWriter writer)
	{
		writer.writeNamespace(XmpzXmlConstants.NAME_SPACE);
		writer.defineAlias("start", "ConservationProject.element");
	}

	private void writeConservationProjectElement(SchemaWriter writer)
	{
		String name = "ConservationProject";
		writer.startElementDefinition(name);

		Vector<String> elementNames = new Vector<String>();
		for(BaseObjectSchema baseObjectSchema : baseObjectSchemas)
		{
			String member = baseObjectSchema.getXmpz2ElementName() + ".element";
			elementNames.add(member);
		}
		
		writer.writeContentsList(elementNames);
		

		writer.endElementDefinition(name);
		writer.flush();
	}

	private Vector<BaseObjectSchema> getTopLevelBaseObjectSchemas()
	{
		Vector<BaseObjectSchema> schemas = new Vector<BaseObjectSchema>();
		for(int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
		{
			if (!isPoolDirectlyInXmpz2(objectType))
				continue;

			BaseObjectPool pool = (BaseObjectPool) getProject().getPool(objectType);
			if(pool == null)
				continue;
			
			BaseObjectSchema baseObjectSchema = pool.createBaseObjectSchema(getProject());
			schemas.add(baseObjectSchema);
		}
		return schemas;
	}
	
	private boolean isPoolDirectlyInXmpz2(int objectType)
	{
		return !Xmpz2XmlImporter.isCustomImport(objectType);
	}

	public Project getProject()
	{
		return project;
	}

	private Project project;
	private Vector<BaseObjectSchema> baseObjectSchemas;
}
