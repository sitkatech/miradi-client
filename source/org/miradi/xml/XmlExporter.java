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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public abstract class XmlExporter
{
	public XmlExporter(Project projectToExport)
	{
		project = projectToExport;
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
	
	public Project getProject()
	{
		return project;
	}
	
	protected void writeStartElementWithAttribute(UnicodeWriter out, String startElementName, String attributeName, int attributeValue) throws IOException
	{
		writeStartElementWithAttribute(out, startElementName, attributeName, Integer.toString(attributeValue));
	}
	
	public void writeStartElementWithAttribute(UnicodeWriter out, String startElementName, String attributeName, String attributeValue) throws IOException
	{
		out.write("<" + startElementName + " " + attributeName + "=\"" + attributeValue + "\">");
	}
	
	public void writeStartElementWithTwoAttributes(UnicodeWriter out, String startElementName, String attributeName1, int attributeValue1, String attributeName2, int attributeValue2) throws IOException
	{
		writeStartElementWithTwoAttributes(out, startElementName, attributeName1, Integer.toString(attributeValue1), attributeName2, Integer.toString(attributeValue2));
	}
	
	public void writeStartElementWithTwoAttributes(UnicodeWriter out, String startElementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2) throws IOException
	{
		out.write("<" + startElementName + " " + attributeName1 + "=\"" + attributeValue1 + "\" " + attributeName2 + "=\"" + attributeValue2 + "\">");
	}
	
	protected void writeStartElement(UnicodeWriter out, String startElementName) throws IOException
	{
		out.writeln("<" + startElementName + ">");
	}
	
	public void writeEndElement(UnicodeWriter out, String endElementName) throws IOException
	{
		out.writeln("</" + endElementName + ">");
	}
	
	public void writeElement(UnicodeWriter out, String elementName, int data) throws Exception
	{
		writeElement(out, elementName, Integer.toString(data));
	}
	
	public void writeElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		out.write("<" + elementName + ">");
		writeXmlEncodedData(out, data);
		out.write("</" + elementName + ">");
		out.writeln();
	}

	public void writeOptionalElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		if (data == null || data.length() == 0)
			return;
		
		writeElement(out, elementName, data);
	}
	
	public void writeXmlEncodedData(UnicodeWriter out, String data) throws IOException
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
		for (int codeIndex = 0; codeIndex < codeList.size(); ++codeIndex)
		{
			writeElement(out, elementName, codeList.get(codeIndex));
		}
		out.writeln("</" + parentElementName + ">");
	}
	
	protected WcpaProjectData getWcpaProjectData()
	{
		ORef wcpaProjectDataRef = getProject().getSingletonObjectRef(WcpaProjectData.getObjectType());
		return WcpaProjectData.find(getProject(), wcpaProjectDataRef);
	}
	
	protected TncProjectData getTncProjectData()
	{
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectData.getObjectType());
		return TncProjectData.find(getProject(), tncProjectDataRef);
	}
	
	protected WwfProjectData getWwfProjectData()
	{
		ORef wwfProjectDataRef = getProject().getSingletonObjectRef(WwfProjectData.getObjectType());
		return WwfProjectData.find(getProject(), wwfProjectDataRef);
	}

	protected WcsProjectData getWcsProjectData()
	{
		ORef wwfProjectDataRef = getProject().getSingletonObjectRef(WcsProjectData.getObjectType());
		return WcsProjectData.find(getProject(), wwfProjectDataRef);
	}
	
	protected RareProjectData getRareProjectData()
	{
		ORef rareProjectDataRef = getProject().getSingletonObjectRef(RareProjectData.getObjectType());
		return RareProjectData.find(getProject(), rareProjectDataRef);
	}
	
	protected FosProjectData getFosProjectData()
	{
		ORef fosProjectDataRef = getProject().getSingletonObjectRef(FosProjectData.getObjectType());
		return FosProjectData.find(getProject(), fosProjectDataRef);
	}
	
	abstract public void exportProject(UnicodeWriter out) throws Exception;
	
	private Project project;
}
