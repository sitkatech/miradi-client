/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.io.File;
import java.io.PrintStream;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMFileSaveChooser;
import org.conservationmeasures.eam.utils.EAMXmlFileChooser;


public class TestBuildXMLReportDocument extends EAMTestCase
{

	public TestBuildXMLReportDocument(String name)
	{
		super(name);
	}


	public void testBuild() throws Exception
	{
		String projectName = "Marine demo 06-10-20";
		
		EAMFileSaveChooser eamFileChooser = new EAMXmlFileChooser(new MainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) return;
		System.setOut(new PrintStream(chosen));
		
		try
		{
			int BASE_INT = BaseId.INVALID.asInt();
			
			File projectFile = new File(EAM.getHomeDirectory(),projectName);
			Project project = new Project();
			project.createOrOpen(projectFile);
			
			writeXMLVersionLine();
			writeLineReturn();
			writeStartElementWithNamedAttr("Miradi", "project", projectName);

			
			processObject(project, new AccountingCode(BaseId.INVALID));
			processObject(project, new Assignment(BaseId.INVALID, new CreateAssignmentParameter(new TaskId(BASE_INT))));
			processObject(project, new Strategy(new FactorId(BaseId.INVALID.asInt())));
			processObject(project, new Target(new FactorId(BaseId.INVALID.asInt())));
			processObject(project, new Cause(new FactorId(BaseId.INVALID.asInt())));
			processObject(project, new FactorLink(new FactorLinkId(BASE_INT), new FactorId(BASE_INT), new FactorId(BASE_INT)));
			processObject(project, new FundingSource(BaseId.INVALID));
			processObject(project, new Goal(BaseId.INVALID));
			processObject(project, new Indicator(new IndicatorId(BASE_INT)));
			processObject(project, new Objective(BaseId.INVALID));
			processObject(project, new ProjectMetadata(BaseId.INVALID));
			processObject(project, new Task(BaseId.INVALID, new CreateTaskParameter(new ORef(Task.getObjectType(), BaseId.INVALID))));
			processObject(project, new ViewData(BaseId.INVALID));
			
			writeLineReturn();
			writeEndELement("Miradi");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private void processObject(Project project, BaseObject object) throws Exception
	{
		writeLineReturn();
		writeStartELement(object.getClass().getSimpleName());

		writeLineReturn();
		writeLineReturn();
		
		processTags(object);

		writeLineReturn();
		writeLineReturn();
		
		makeFieldDefs();

		writeLineReturn();
		writeLineReturn();
		writeEndELement(object.getClass().getSimpleName());
	}


	private void processTags(BaseObject object)
	{
		String[] tags = object.getFieldTags();
		for(int i = 0; i < tags.length; ++i)
		{
			writeData(getTextFieldElement("Label:"+tags[i]));
			fieldDefs.add(getTextFieldDefElement("Label:"+tags[i]));
			writeLineReturn();
			writeLineReturn();
			writeData(getTextFieldElement(tags[i]));
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
	
	
	private String getTextFieldElement(String name)
	{
		return field_line_part1 + name + field_line_part2;
		
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
			"x=\"0\"" +
			"y=\"0\"" +
			"width=\"0\"" +
			"height=\"0\"" +
			"key=\"textField-1\"/>" +
			"<box topBorder=\"None\" topBorderColor=\"#000000\" leftBorder=\"None\" leftBorderColor=\"#000000\" rightBorder=\"None\" rightBorderColor=\"#000000\" bottomBorder=\"None\" bottomBorderColor=\"#000000\"/>" +
			"<textElement>" +
			"<font/>" +
			"</textElement>" +
			"<textFieldExpression   class=\"java.lang.String\"><![CDATA[$F{";
	private static String field_line_part2 =
			"}]]></textFieldExpression>" +
			"</textField>";
}
