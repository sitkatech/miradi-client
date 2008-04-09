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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
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
		writeOptionalTargets(out);
		
		out.writeln("</conservation_project>");
	}

	private void writeOptionalTargets(UnicodeWriter out) throws Exception
	{
		ORefList targetRefs = getProject().getTargetPool().getRefList();
		if (targetRefs.size() == 0)
			return;
		
		out.write("<targets>");
		for (int refIndex = 0; refIndex < targetRefs.size(); ++refIndex)
		{
			Target target = Target.find(getProject(), targetRefs.get(refIndex));
			out.write("<target id='" + target.getId().asInt() + "'>");
			writeElement(out, "name", target, Target.TAG_LABEL);
			writeOptionalElement(out, "description", target, Target.TAG_TEXT);
			writeOptionalElement(out, "target_viability_comment", target, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			writeOptionalElement(out, "target_viability_rank", translate(target.getBasicTargetStatus()));
			//FIXME cant get this work,  need a way to export each code in list, schema question
			//writeCodeListElements(out, "habitat_code", target.getCodeList(Target.TAG_HABITAT_ASSOCIATION));
			writeStresses(out, target);
			writeThreatStressRatings(out, target);
			out.writeln("</target>");
		}
		out.writeln("</targets>");

	}
	private void writeThreatStressRatings(UnicodeWriter out, Target target) throws Exception
	{
		ORefList factorLinkReferrers = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			if (factorLink.isThreatTargetLink())
			{
				writeThreatStressRatings(out, factorLink);
			}
		}
	}

	private void writeThreatStressRatings(UnicodeWriter out, FactorLink factorLink) throws Exception
	{
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		for (int refIndex = 0; refIndex < threatStressRatingRefs.size(); ++refIndex)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingRefs.get(refIndex));
			out.write("<stress_threat>");
			writeOptionalElement(out, "contrib_rank", threatStressRating.getIrreversibility().getLabel());
			out.write("</stress_threat>");
		}
	}

	private void writeStresses(UnicodeWriter out, Target target) throws Exception
	{
		ORefList stressRefs = target.getStressRefs();
		for (int refIndex = 0; refIndex < stressRefs.size(); ++refIndex)
		{
			out.write("<stresses_target sequence='" + refIndex + "'>");
			Stress stress = Stress.find(getProject(), stressRefs.get(refIndex));
			writeElement(out, "stress_name", stress, Stress.TAG_LABEL);
			writeOptionalElement(out, "stress_severity", translate(stress.getData(Stress.TAG_SEVERITY)));
			writeOptionalElement(out, "stress_scope", translate(stress.getData(Stress.TAG_SCOPE)));
			writeOptionalElement(out, "stress_to_target_rank", translate(stress.getCalculatedStressRating()));
			out.writeln("</stresses_target>");
		}
	}

	private void writeoutProjectSummaryElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<project_summary share_outside_organization='false'>");
	
			writeElement(out, "name", XmlUtilities.getXmlEncoded(getProjectMetadata().getProjectName()));
			
			writeOptionalElement(out, "start_date", getProjectMetadata(), ProjectMetadata.TAG_START_DATE);
			writeOptionalAreaSize(out);
			writeOptionalLocation(out);
			
			writeOptionalElement(out, "description_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_SCOPE);
			writeOptionalElement(out, "goal_comment", getProjectMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
			writeOptionalElement(out, "planning_team_comment", getProjectMetadata(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
			writeOptionalElement(out, "lessons_learned", getProjectMetadata(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
			
			writeOptionalElement(out, "stressless_threat_rank", getSimpleOverallProjectRating());
			writeOptionalElement(out, "project_threat_rank", getStressBasedOverallProjectRating());
			writeOptionalElement(out, "project_viability_rank", getComputedTncViability());
			writeTeamMembers(out);
			writeEcoregionCodes(out);
			writeCodeListElements(out, "country_code", getProjectMetadata(), ProjectMetadata.TAG_COUNTRIES);
			writeCodeListElements(out, "ou_code", getProjectMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
			
			out.writeln("<exporter_name/>");
			out.writeln("<exporter_version/>");
			out.writeln("<data_export_date>" + new MultiCalendar().toIsoDateString() + "</data_export_date>");
			
		out.writeln("</project_summary>");
	}

	private String getComputedTncViability()
	{
		String code = Target.computeTNCViability(getProject());
		return translate(code);
	}

	private String getStressBasedOverallProjectRating()
	{
		int overallProjectRating = getProject().getStressBasedThreatRatingFramework().getOverallProjectRating();
		return translate(overallProjectRating);
	}
	
	private String getSimpleOverallProjectRating()
	{
		int overallProjectRating = getProject().getSimpleThreatRatingFramework().getOverallProjectRating().getNumericValue();
		return translate(overallProjectRating);
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
	
	private void writeElement(UnicodeWriter out, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(out, elementName, object.getData(tag));
	}

	private ProjectMetadata getProjectMetadata()
	{
		return getProject().getMetadata();
	}

	private void writeoutDocumentExchangeElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<document_exchange status='success'/>");
	}
	
	private String translate(int code)
	{
		return translate(Integer.toString(code));
	}
	
	private String translate(String code)
	{
		if (code.equals("1"))
			return EAM.text("Low");
		
		if (code.equals("2"))
			return EAM.text("Medium");
		
		if (code.equals("3"))
			return EAM.text("High");
		
		if (code.equals("4"))
			return EAM.text("Very High");
		
		return "";
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