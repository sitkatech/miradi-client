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
import java.net.URL;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.main.ResourcesHandler;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.utils.Translation;

public class XmlSchemaCreator
{
	public static void main(String[] args) throws Exception
	{
		System.out.print(new XmlSchemaCreator().getXmlRncSchema());
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

	private String getXmlRncSchema() throws Exception
	{
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(MIRADI_XML_RNC_SCHEMA_SKELETON_FILE_NAME);
		if(resourceURL == null)
			throw new Exception("Schema skeleton not found: " + MIRADI_XML_RNC_SCHEMA_SKELETON_FILE_NAME);

		UnicodeReader reader = new UnicodeReader(resourceURL.openStream());
		String rnc = reader.readAll();
		reader.close();
		
		rnc = EAM.substitute(rnc, "%ProjectSummary", buildProjectSummarySchema());

		return rnc.toString();
    }
	
	private String buildProjectSummarySchema() throws Exception
	{
		Vector<String> summary = new Vector<String>();

		ORef ref = project.createObject(ProjectMetadata.getObjectType());
		StringBuffer asString = new StringBuffer();
		String objectName = "ProjectSummary";
		append(asString, objectName + ".element = ");
		append(asString, "  element miradi:" + objectName);
		append(asString, "  {");

		String[] tags = 
		{
			ProjectMetadata.TAG_PROJECT_NAME,
			ProjectMetadata.TAG_PROJECT_SCOPE
		};
		
		for(String tag : tags)
		{
			summary.add(buildFieldSchema(ref, objectName, tag));
		}
		
		for(String thisFieldSchema : summary)
		{
			if(asString.length() > 0)
				append(asString, "&");
			append(asString, thisFieldSchema);
		}

		append(asString, "  }");
		return asString.toString();
	}

	private String buildFieldSchema(ORef ref, String objectName, String tagProjectName)
	{
		return "element miradi:" + objectName + "_" + tagProjectName + " { text }";
	}

	private static void append(StringBuffer rnc, String string)
	{
		rnc.append(string);
		rnc.append("\n");
	}
	
	private static final String MIRADI_XML_RNC_SCHEMA_SKELETON_FILE_NAME = "xml/XmlRncSchemaSkeleton.txt";
	
	private Project project;
}
