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

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
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
		out.writeln("<conservation_project>");
		
		writeoutDocumentExchangeElement(out);
		writeoutProjectSummaryElement(out);
		out.writeln("<project>");
		out.writeln("</project>");
		
		out.writeln("</conservation_project>");
	}

	private void writeoutProjectSummaryElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<project_summary share_outside_organization='false'>");
		
			out.write("</project_id context=''>");
			
			out.write("<parent_project_id context=''>");
			out.writeln("</parent_project_id>");
			
			out.write("<name>");
			out.write(XmlUtilities.getXmlEncoded(getProjectMetadata().getProjectName()));
			out.writeln("</name>");
			
			
			writeOptionalElement(out, "start_date", getProjectMetadata(), ProjectMetadata.TAG_START_DATE);
			out.write("<area_size unit='hectares'>");
			out.write(XmlUtilities.getXmlEncoded(getProjectMetadata().getData(ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES)));
			out.writeln("</area_size>");
			
			out.writeln("<location>");
			out.writeln("<geospatial_location vocabulary_geospatial_type='point'>");
			out.write("<latitude>");
			out.write(XmlUtilities.getXmlEncoded(getProjectMetadata().getLatitude()));
			out.writeln("</latitude>");
			out.write("<longitude>");
			out.write(XmlUtilities.getXmlEncoded(getProjectMetadata().getLongitude()));
			out.writeln("</longitude>");
			out.writeln("</geospatial_location>");
			out.writeln("</location>");
			
			writeOptionalElement(out, "description_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
			writeOptionalElement(out, "goal_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
			writeOptionalElement(out, "planning_team_comment", getProjectMetadata(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
			writeOptionalElement(out, "lessons_learned", getProjectMetadata(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
			
			out.writeln("</stressless_threat_rank>");
			out.writeln("</project_threat_rank>");
			out.writeln("</project_viability_rank>");
			
			writeTeamMemmers(out);
			
		out.writeln("</project_summary>");
	}
	
	private void writeTeamMemmers(UnicodeWriter out) throws Exception
	{
		ORefList teamMemberRefs = getProject().getResourcePool().getTeamMemberRefs();
		for (int memberIndex = 0; memberIndex < teamMemberRefs.size(); ++memberIndex)
		{
			ProjectResource member = ProjectResource.find(getProject(), teamMemberRefs.get(memberIndex));
			out.writeln("<team_member>");
			writeMemberRoles(out, member);
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
	
	private void writeElement(UnicodeWriter out, String elementName, String data) throws Exception
	{
		if (data.length() == 0)
			return;
		
		out.write("<" + elementName + ">");
		out.write(XmlUtilities.getXmlEncoded(data));
		out.writeln("</" + elementName + ">");
	}

	private void writeOptionalElement(UnicodeWriter out, String elementName, BaseObject object, String fieldTag) throws Exception
	{
		String data = object.getData(fieldTag);
		if (data.length() == 0)
			return;
		
		out.write("<" + elementName + ">");
		out.write(XmlUtilities.getXmlEncoded(data));
		out.writeln("</" + elementName + ">");
	}

	private ProjectMetadata getProjectMetadata()
	{
		return getProject().getMetadata();
	}

	private void writeoutDocumentExchangeElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<document_exchange status='success'>");
		String errorMessages = "";
		if (errorMessages.length() > 0)
		{
			out.writeln("</error_msg>");
		}
		out.writeln("</document_exchange>");
	}
	
	public static void main(String[] commandLineArguments) throws Exception
	{	
		Project newProject = getOpenedProject(commandLineArguments);
		try
		{
			new ConproXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			System.out.println("Export Conpro xml complete");
		}
		finally
		{
			newProject.close();
		}
	}
}