/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.xml.conpro.export;

import java.io.FileInputStream;
import java.io.IOException;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.utils.CodeList;
import org.miradi.xml.XmlExporter;

public class ConproXmlExporter extends XmlExporter
{
	public ConproXmlExporter(Project project)
	{
		super(project);
	}

	@Override
	protected void exportProject(UnicodeWriter out) throws Exception
	{
		out.writeln("<?xml version='1.0'?>");
		out.writeln("<conservation_project xmlns='http://services.tnc.org/schema/conservation-project/0.1'>");
		
		writeoutDocumentExchangeElement(out);
		writeoutProjectSummaryElement(out);
		
		out.writeln("</conservation_project>");
	}

	private void writeoutProjectSummaryElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<project_summary share_outside_organization='false'>");
	
			writeElement(out, "name", XmlUtilities.getXmlEncoded(getProjectMetadata().getProjectName()));
			
			writeOptionalElement(out, "start_date", getProjectMetadata(), ProjectMetadata.TAG_START_DATE);
			writeOptionalAreaSize(out);
			writeOptionalLocation(out);
			
			writeOptionalElement(out, "description_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
			writeOptionalElement(out, "goal_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
			writeOptionalElement(out, "planning_team_comment", getProjectMetadata(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
			writeOptionalElement(out, "lessons_learned", getProjectMetadata(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
			
			//FIXME elements not found in namespace.
			//out.writeln("<stressless_threat_rank/>");
			//out.writeln("<project_threat_rank/>");
			//out.writeln("<project_viability_rank/>");
			writeTeamMembers(out);
			writeEcoregionCodes(out);
			writeCodeListElements(out, "country_code", getProjectMetadata(), ProjectMetadata.TAG_COUNTRIES);
			writeCodeListElements(out, "ou_code", getProjectMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
			
			out.writeln("<exporter_name/>");
			out.writeln("<exporter_version/>");
			out.writeln("<data_export_date>" + new MultiCalendar().toIsoDateString() + "</data_export_date>");
			
		out.writeln("</project_summary>");
	}

	private void writeEcoregionCodes(UnicodeWriter out) throws Exception
	{
		CodeList allTncEcoRegionCodes = new CodeList();
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncFreshwaterEcoRegion());
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncMarineEcoRegion());
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncTerrestrialEcoRegion());
		
		writeCodeListElements(out, "ecoregion_code", allTncEcoRegionCodes);
	}
	
	private void writeOptionalAreaSize(UnicodeWriter out) throws IOException
	{
		double sizeInHectaresAsInt = getProjectMetadata().getSizeInHectaresAsDouble();
		if (sizeInHectaresAsInt == 0)
			return;
		
		out.write("<area_size unit='hectares'>");
		out.write(Integer.toString((int)sizeInHectaresAsInt));
		out.writeln("</area_size>");
	}

	private void writeOptionalLocation(UnicodeWriter out) throws IOException, Exception
	{
		float latitudeAsFloat = getProjectMetadata().getLatitudeAsFloat();
		float longitudeAsFloat = getProjectMetadata().getLongitudeAsFloat();
		if (latitudeAsFloat == 0 && longitudeAsFloat == 0)
			return;
		
		out.writeln("<geospatial_location type='point'>");
		writeOptionalFloatElement(out, "latitude", latitudeAsFloat);
		writeOptionalFloatElement(out, "longitude", longitudeAsFloat);
		out.writeln("</geospatial_location>");
	}
	
	private void writeTeamMembers(UnicodeWriter out) throws Exception
	{
		//FIXME this is to avoid writing team member for now.  Person is giving trouble
		if (true)
			return;
			
		ORefList teamMemberRefs = getProject().getResourcePool().getTeamMemberRefs();
		for (int memberIndex = 0; memberIndex < teamMemberRefs.size(); ++memberIndex)
		{
			ProjectResource member = ProjectResource.find(getProject(), teamMemberRefs.get(memberIndex));
			out.writeln("<team_member>");
			
			
			writeMemberRoles(out, member);
			out.writeln("<foaf:Person xmlns:foaf='http://xmlns.com/foaf/spec/'/>");
			//out.writeln("</Person>");
			out.writeln("</team_member>");
		}
	}

	private void writeMemberRoles(UnicodeWriter out, ProjectResource member) throws Exception
	{
		ChoiceQuestion question = getProject().getQuestion(ResourceRoleQuestion.class);
		writeElement(out, "role", question.findChoiceByCode(ResourceRoleQuestion.TeamMemberRoleCode).getLabel());
		if (member.isTeamLead())
			writeElement(out, "role", question.findChoiceByCode(ResourceRoleQuestion.TeamLeaderCode).getLabel());
	}
	
	private void writeOptionalFloatElement(UnicodeWriter out, String elementName, float value) throws Exception
	{
		if (value == 0)
			return;
		
		writeOptionalElement(out, elementName, Float.toString(value));
	}

	private void writeCodeListElements(UnicodeWriter out, String elementName, BaseObject object, String tag) throws Exception
	{
		writeCodeListElements(out, elementName, object.getCodeList(tag));
	}
	
	private void writeCodeListElements(UnicodeWriter out, String elementName, CodeList codeList) throws Exception
	{
		for (int codeIndex = 0; codeIndex < codeList.size(); ++codeIndex)
		{
			writeElement(out, elementName, codeList.get(codeIndex));
		}
	}
	
	private void writeElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		out.write("<" + elementName + ">");
		out.write(XmlUtilities.getXmlEncoded(data));
		out.writeln("</" + elementName + ">");
	}

	private void writeOptionalElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		if (data.length() == 0)
			return;
		
		out.write("<" + elementName + ">");
		out.write(XmlUtilities.getXmlEncoded(data));
		out.writeln("</" + elementName + ">");
	}
	
	private void writeOptionalElement(UnicodeWriter out, String elementName, BaseObject object, String fieldTag) throws Exception
	{
		writeOptionalElement(out, elementName, object.getData(fieldTag));
	}

	private ProjectMetadata getProjectMetadata()
	{
		return getProject().getMetadata();
	}

	private void writeoutDocumentExchangeElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<document_exchange status='success'/>");
	}
	
	public static void main(String[] commandLineArguments) throws Exception
	{	
		Project newProject = getOpenedProject(commandLineArguments);
		try
		{
			new ConproXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			new testSampleXml().validate(new FileInputStream(getXmlDestination(commandLineArguments)));
			System.out.println("Export Conpro xml complete");
		}
		finally
		{
			newProject.close();
		}
	}
}