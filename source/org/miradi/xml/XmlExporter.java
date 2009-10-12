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
package org.miradi.xml;

import java.io.File;
import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.Translation;

public abstract class XmlExporter
{
	public XmlExporter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void export(File destination) throws Exception
	{
		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			exportProject(out);
		}
		finally
		{
			out.close();
		}
	}
	
	protected static Project getOpenedProject(String[] commandLineArguments) throws Exception
	{
		if (incorrectArgumentCount(commandLineArguments))
			throw new RuntimeException("Incorrect number of arguments " + commandLineArguments.length);

		File projectDirectory = getProjectDirectory(commandLineArguments);
		if(!ProjectServer.isExistingLocalProject(projectDirectory))
			throw new RuntimeException("Project does not exist:" + projectDirectory);

		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();
		
		Project newProject = new Project();
		newProject.setLocalDataLocation(dataDirectory);
		newProject.createOrOpen(projectName);
		Translation.initialize();
		
		return newProject;
	}	 
	
	public static File getProjectDirectory(String[] commandLineArguments) throws Exception
	{
		return new File(EAM.getHomeDirectory(), commandLineArguments[0]);
	}
	
	public static File getXmlDestination(String[] commandLineArguments) throws Exception
	{
		return new File(commandLineArguments[1]);
	}

	public static boolean incorrectArgumentCount(String[] commandLineArguments)
	{
		return commandLineArguments.length != 2;
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	protected void writeStartElementWithAttribute(UnicodeWriter out, String startElementName, String attributeName, int attributeValue) throws IOException
	{
		writeStartElementWithAttribute(out, startElementName, attributeName, Integer.toString(attributeValue));
	}
	
	protected void writeStartElementWithAttribute(UnicodeWriter out, String startElementName, String attributeName, String attributeValue) throws IOException
	{
		out.write("<" + startElementName + " " + attributeName + "='" + attributeValue + "'>");
	}
	
	protected void writeStartElement(UnicodeWriter out, String startElementName) throws IOException
	{
		out.writeln("<" + startElementName + ">");
	}
	
	protected void writeEndElement(UnicodeWriter out, String endElementName) throws IOException
	{
		out.writeln("</" + endElementName + ">");
	}
	
	protected void writeElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		out.write("<" + elementName + ">");
		writeXmlEncodedData(out, data);
		out.writeln("</" + elementName + ">");
	}

	protected void writeOptionalElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		if (data == null || data.length() == 0)
			return;
		
		writeElement(out, elementName, data);
	}
	
	protected void writeXmlEncodedData(UnicodeWriter out, String data) throws IOException
	{
		out.write(XmlUtilities.getXmlEncoded(data));
	}
	
	protected void writeOptionalElement(UnicodeWriter out, String elementName, BaseObject object, String fieldTag) throws Exception
	{
		writeOptionalElement(out, elementName, object.getData(fieldTag));
	}
	
	protected void writeElement(UnicodeWriter out, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(out, elementName, object.getData(tag));
	}
	
	protected void writeCodeListElements(UnicodeWriter out, String parentElementName, String elementName, CodeList codeList) throws Exception
	{
		out.writeln("<" + parentElementName + ">");
		writeCodeListElements(out, elementName, codeList);
		out.writeln("</" + parentElementName + ">");
	}
	
	private void writeCodeListElements(UnicodeWriter out, String elementName, CodeList codeList) throws Exception
	{
		for (int codeIndex = 0; codeIndex < codeList.size(); ++codeIndex)
		{
			writeElement(out, elementName, codeList.get(codeIndex));
		}
	}

	abstract public void exportProject(UnicodeWriter out) throws Exception;
	
	private Project project;
}
