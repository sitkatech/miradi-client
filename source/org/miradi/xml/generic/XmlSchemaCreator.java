/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.generic;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import org.miradi.main.Miradi;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.utils.Translation;

public class XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		new XmlSchemaCreator().printXmlRncSchema(new PrintWriter(System.out));
	}

	public XmlSchemaCreator() throws Exception
	{
		Miradi.addThirdPartyJarsToClasspath();
		Translation.initialize();
		
		File tempDirectory = File.createTempFile("$$$Miradi-XmlSchemaCreator", null);
		tempDirectory.delete();
		tempDirectory.mkdirs();
		project = new Project();
		project.setLocalDataLocation(tempDirectory);
		project.createOrOpen("XmlSchemaCreator");
	}

	private void printXmlRncSchema(PrintWriter writer) throws Exception
	{
		ProjectElement rootElement = new ProjectElement();
		writer.println("start = " + rootElement.getProjectElementName());
		rootElement.output(writer);
		writer.flush();
    }

	static class Element
	{
		public void output(PrintWriter writer) throws IOException
		{
			writer.print("element ");
		}

		protected String getDotElement(String elementName)
		{
			return elementName + ".element";
		}
		
	}
	
	static class ProjectElement extends Element
	{
		public ProjectElement()
		{
			objectTypes = new Vector<ObjectElement>();
			
			objectTypes.add(new ProjectSummaryElement());
		}
		
		public void output(PrintWriter writer) throws IOException
		{
			writer.println(getDotElement(getProjectElementName()) + " = ");
			super.output(writer);
			writer.println("miradi:" + getProjectElementName());
			writer.println("{");
			for(ObjectElement objectElement: objectTypes)
			{
				writer.println(getDotElement(objectElement.getObjectTypeName()) + "&");
			}
			writer.println("}");
			writer.println();
			
			for(ObjectElement objectElement: objectTypes)
			{
				objectElement.output(writer);
			}
			
		}
		
		private String getProjectElementName()
		{
			return "conservation_project";
		}

		private Vector<ObjectElement> objectTypes;
	}
	
	static class ObjectElement extends Element
	{
		public ObjectElement(String objectTypeNameToUse)
		{
			objectTypeName = objectTypeNameToUse;
			fields = new Vector<FieldElement>();
		}
		
		public void createTextField(String fieldNameToUse)
		{
			FieldElement field = new TextFieldElement(getObjectTypeName(), fieldNameToUse);
			fields.add(field);
		}

		public String getObjectTypeName()
		{
			return objectTypeName;
		}

		@Override
		public void output(PrintWriter writer) throws IOException
		{
			writer.println(getDotElement(getObjectTypeName()) + " = ");
			super.output(writer);
			writer.print("miradi:" + getObjectTypeName());
			writer.println();
			
			writer.println("{");
			for(FieldElement fieldElement : fields)
			{
				fieldElement.output(writer);
				writer.println("&");
			}
			writer.println("}");
		}
		
		private String objectTypeName;
		private Vector<FieldElement> fields;
	}
	
	static class FieldElement extends Element
	{
		protected FieldElement(String objectTypeNameToUse, String fieldNameToUse)
		{
			objectTypeName = objectTypeNameToUse;
			fieldName = fieldNameToUse;
		}
		
		@Override
		public void output(PrintWriter writer) throws IOException
		{
			super.output(writer);
			writer.write(getFullName());
		}
		
		private String getFullName()
		{
			return "miradi:" + getObjectTypeName() + getFieldName();
		}

		private String getObjectTypeName()
		{
			return objectTypeName;
		}

		private String getFieldName()
		{
			return fieldName;
		}

		private String objectTypeName;
		private String fieldName;
	}
	
	static class TextFieldElement extends FieldElement
	{
		TextFieldElement(String objectTypeNameToUse, String fieldNameToUse)
		{
			super(objectTypeNameToUse, fieldNameToUse);
		}

		public void output(PrintWriter writer) throws IOException
		{
			super.output(writer);
			writer.write(" { text }");
		}
	}

	static class ProjectSummaryElement extends ObjectElement
	{
		public ProjectSummaryElement()
		{
			super("ProjectSummary");
			createTextField(ProjectMetadata.TAG_PROJECT_NAME);
			createTextField(ProjectMetadata.TAG_PROJECT_SCOPE);
		}
	}
	
	private Project project;
}
