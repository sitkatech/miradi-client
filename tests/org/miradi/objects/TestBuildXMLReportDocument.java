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
package org.miradi.objects;

import java.io.File;
import java.io.PrintStream;
import java.util.Vector;

import org.martus.util.DirectoryUtils;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.IdListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMXmlFileChooser;
import org.miradi.utils.StringMapData;


public class TestBuildXMLReportDocument extends EAMTestCase
{

	public TestBuildXMLReportDocument(String name)
	{
		super(name);
	}


	public void testBuild() throws Exception
	{
		String projectName = "Marine demo 06-10-20";
		
		EAMFileSaveChooser eamFileChooser = new EAMXmlFileChooser(MainWindow.create());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		System.setOut(new PrintStream(chosen));
		
		File tempDirectory = createTempDirectory();
		try
		{
			int BASE_INT = BaseId.INVALID.asInt();
			
			Project project = new Project();
			project.setLocalDataLocation(tempDirectory);
			project.createOrOpen(projectName);
			
			writeXMLVersionLine();
			writeLineReturn();
			writeStartElementWithNamedAttr("Miradi", "project", projectName);
			
			//			processObject(project, new AccountingCode(BaseId.INVALID));
//			processObject(project, new Assignment(BaseId.INVALID, new CreateAssignmentParameter(new TaskId(BASE_INT))));
//			processObject(project, new Strategy(new FactorId(BASE_INT)));
//*			processObject(project, new Target(new FactorId(BASE_INT)));
//			processObject(project, new Cause(new FactorId(BASE_INT)));
			ORef toRef = new ORef(ObjectType.CAUSE, new FactorId( BASE_INT));
			ORef fromRef = new ORef(ObjectType.CAUSE, new FactorId( BASE_INT));
			processObject(project, new FactorLink(project.getObjectManager(), new FactorLinkId(BASE_INT), fromRef, toRef ));
//			processObject(project, new FundingSource(BaseId.INVALID));
//*			processObject(project, new Goal(BaseId.INVALID));
//*			processObject(project, new Indicator(new IndicatorId(BASE_INT)));
//			processObject(project, new Objective(BaseId.INVALID));
//*			processObject(project, new ProjectMetadata(BaseId.INVALID));
//			processObject(project, new Task(BaseId.INVALID, new CreateTaskParameter(new ORef(Task.getObjectType(), BaseId.INVALID))));
//*			processObject(project, new ViewData(BaseId.INVALID));
//*			processObject(project, new KeyEcologicalAttribute(new KeyEcologicalAttributeId(BASE_INT)));
			
			
			writeLineReturn();
			writeEndELement("Miradi");
			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}


	private void processObject(Project project, BaseObject object) throws Exception
	{
		writeLineReturn();
		writeStartELement(object.getClass().getSimpleName());

		writeLineReturn();
		writeLineReturn();
		
		processTags(object, 0);

		writeLineReturn();
		writeLineReturn();
		
		makeFieldDefs();

		writeLineReturn();
		writeLineReturn();
		writeEndELement(object.getClass().getSimpleName());
	}


	private void processTags(BaseObject object, int lineNumber)
	{
		String[] tags = object.getFieldTags();
		for(int i = 0; i < tags.length; ++i)
		{
			if (object.getField(tags[i]) instanceof IdListData)
				continue;
			if (object.getField(tags[i]) instanceof ChoiceData)
				continue;
			if (object.getField(tags[i]) instanceof StringMapData)
				continue;
			
			lineNumber = lineNumber + 10;
			writeData(getTextFieldElement("Label:"+tags[i],lineNumber));
			fieldDefs.add(getTextFieldDefElement("Label:"+tags[i]));
			writeLineReturn();
			writeLineReturn();
			writeData(getTextFieldElement(tags[i],lineNumber));
			fieldDefs.add(getTextFieldDefElement(tags[i]));
			writeLineReturn();
			writeLineReturn();
		}
	}


	private void writeStartELement(String name)
	{
		write("<"+name+">");
	}
	
	private void writeEndELement(String name)
	{
		write("</"+name+">");
	}
	
	private void writeData(String text)
	{
		write(text);
	}
	
	private void writeStartElementWithNamedAttr(String name, String attrName, String attrValue)
	{
		write("<"+name  +  "  " + attrName + "=\"" + attrValue + "\">");
	}
	
	private void writeXMLVersionLine()
	{
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	}
	
	public void write(String text)
	{
		System.out.print(text);
	}
	
	public void writeLineReturn()
	{
		System.out.println();
	}
	
	private void makeFieldDefs()
	{
		for (int i=0; i<fieldDefs.size(); ++i)
		{
			write((String)fieldDefs.get(i));
			writeLineReturn();
		}
		fieldDefs.clear();
	}

	
	private String getTextFieldElement(String name, int lineNumber)
	{
		String newLine = field_line_part1;
		newLine = newLine.replaceAll("KEYKEY", name);
		if (name.startsWith("Label:"))
		{
			newLine = newLine.replaceAll("ISBOLD", "true");
			newLine = newLine.replaceAll("FONTNAME", "Helvetica-Bold");
			newLine = newLine.replaceAll("XPOS", new Integer(10).toString());
		}
		else
		{
			newLine = newLine.replaceAll("ISBOLD", "false");
			newLine = newLine.replaceAll("FONTNAME", "Helvetica");
			newLine = newLine.replaceAll("XPOS", new Integer(170).toString());
		}
		
		newLine = newLine.replaceAll("YPOS", Integer.toString(lineNumber));
		return newLine + name + field_line_part2;
	}
	
	private String getTextFieldDefElement(String name)
	{
		return field_def_part1 + name + field_def_part2;
	}
	
	Vector fieldDefs = new Vector();
	
	private static String field_def_part1 = "<field name=\"";
	private static String field_def_part2 = "\" class=\"java.lang.String\" /> ";
		
	private static String field_line_part1 = "<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >" +
			"<reportElement" +
			" x=\"XPOS\"" +
			" y=\"YPOS\"" +
			" width=\"150\"" +
			" height=\"20\"" +
			" positionType=\"Float\"" +
			" key=\"KEYKEY\"/>" +
			"<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>" +
			"<textElement>" +
			"<font pdfFontName=\"FONTNAME\" isBold=\"ISBOLD\"/>" +
			"</textElement>" +
			"<textFieldExpression   class=\"java.lang.String\"><![CDATA[$F{";
	private static String field_line_part2 =
			"}]]></textFieldExpression>" +
			"</textField>";

}
