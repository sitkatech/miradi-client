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
import java.util.HashMap;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
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
		writeOptionalKeyEcologicalAttributes(out);
		writeOptionalViability(out);
		writeOptionalThreats(out);
		writeOptionalObjectives(out);
		writeOptionalStrategies(out);
		writeOptionalIndicators(out);
		
		out.writeln("</conservation_project>");
	}

	private void writeOptionalIndicators(UnicodeWriter out) throws Exception
	{
		ORefList indicatorRefs = getProject().getIndicatorPool().getRefList();
		if (indicatorRefs.size() == 0)
			return;
		
		out.writeln("<indicators>");
		for (int refIndex = 0; refIndex < indicatorRefs.size(); ++refIndex)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRefs.get(refIndex));
			out.writeln("<indicator id='" + indicator.getId().toString() + "'>");
			writeElement(out, "name", indicator, Indicator.TAG_LABEL);
//			FIXME this needs to be a concat of method names,  check the cross walk
			//writeOptionalMethods(out, indicator.getMethodRefs());
			writeOptionalRatingCodeElement(out, "priority", indicator, Indicator.TAG_PRIORITY);
			out.writeln("</indicator>");
		}
		
		out.writeln("</indicators>");
	}

//	FIXME this needs to be a concat of method names,  check the cross walk
//	private void writeOptionalMethods(UnicodeWriter out, ORefList methodRefs) throws Exception
//	{
//		for (int refIndex = 0; refIndex < methodRefs.size(); ++refIndex)
//		{
//			Task method = Task.find(getProject(), methodRefs.get(refIndex));
//			writeOptionalElement(out, "methods", method, Task.TAG_LABEL);
//		}
//	}

	private void writeOptionalStrategies(UnicodeWriter out) throws Exception
	{
		ORefList strategyRefs = getProject().getStrategyPool().getRefList();
		if (strategyRefs.size() == 0)
			return;
		
		out.writeln("<strategies>");
		for (int refIndex = 0; refIndex < strategyRefs.size(); ++refIndex)
		{
			Strategy strategy = Strategy.find(getProject(), strategyRefs.get(refIndex));
			out.writeln("<strategy id='" + strategy.getId().toString() + "'>");
			writeIds(out, "objective_id", strategy.getObjectiveRefs());
			writeElement(out, "name", strategy, Strategy.TAG_LABEL);
			writeOptionalElement(out, "taxonomy_code", strategy, Strategy.TAG_TAXONOMY_CODE);
			writeOptionalRatingCodeElement(out, "leverage", strategy, Strategy.TAG_IMPACT_RATING);
			writeOptionalRatingCodeElement(out, "feasibility", strategy, Strategy.TAG_FEASIBILITY_RATING);
			writeOptionalRatingCodeElement(out, "overall_rank", strategy.getStrategyRatingSummary());
			writeElement(out, "selected", Boolean.toString(!strategy.isStatusDraft()));
			writeOptionalElement(out, "comment", strategy, Strategy.TAG_COMMENT);
			writeOptionalActivities(out, strategy.getActivityRefs());
						
			out.writeln("</strategy>");
		}
		
		out.writeln("</strategies>");
	}

	private void writeOptionalActivities(UnicodeWriter out, ORefList activityRefs) throws Exception
	{
		if (activityRefs.size() == 0)
			return;
		
		out.writeln("<activities>");
		for (int refIndex = 0; refIndex < activityRefs.size(); ++refIndex)
		{
			Task activity = Task.find(getProject(), activityRefs.get(refIndex));
			out.writeln("<activity>");
			writeElement(out, "name", activity, Task.TAG_LABEL);
			out.writeln("</activity>");
		}
		
		out.writeln("</activities>");
	}

	private void writeOptionalObjectives(UnicodeWriter out) throws Exception
	{
		ORefList objectiveRefs = getProject().getObjectivePool().getRefList();
		if (objectiveRefs.size() == 0)
			return;
		
		out.writeln("<objectives>");
		for (int refIndex = 0; refIndex < objectiveRefs.size(); ++refIndex)
		{
			Objective objective = Objective.find(getProject(), objectiveRefs.get(refIndex));
			out.writeln("<objective id='" + objective.getId().toString() + "'>");
			writeoutIndicatorIds(out, objective);
			writeElement(out, "name", objective, Objective.TAG_LABEL);
			writeOptionalElement(out, "comment", objective, Objective.TAG_COMMENTS);
			out.writeln("</objective>");
		}
		
		out.writeln("</objectives>");
	}

	private void writeoutIndicatorIds(UnicodeWriter out, Objective objective) throws Exception
	{
		out.writeln("<indicators>");
		writeIds(out, "indicator_id", objective.getRelevantIndicatorRefList());
		out.writeln("</indicators>");
	}

	private void writeIds(UnicodeWriter out, String elementName, ORefList refs) throws Exception
	{
		for (int refIndex = 0; refIndex < refs.size(); ++refIndex)
		{
			writeElement(out, elementName, refs.get(refIndex).getObjectId().toString());
		}
	}

	private void writeOptionalThreats(UnicodeWriter out) throws Exception
	{
		Factor[] directThreats = getProject().getCausePool().getDirectThreats();
		if (directThreats.length == 0)
			return;
		
		out.writeln("<threats>");
		for (int index = 0; index < directThreats.length; ++index)
		{
			out.writeln("<threat id='" + directThreats[index].getId().toString() + "'>");
			writeElement(out, "name", directThreats[index], Cause.TAG_LABEL);
			writeOptionalElement(out, "threat_taxonomy_code", directThreats[index], Cause.TAG_TAXONOMY_CODE);
			//FIXME is this suppose to take in consideration mode
			int threatToProjectRating = getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(directThreats[index]);
			writeOptionalRatingCodeElement(out, "threat_to_project_rank", threatToProjectRating);
			out.writeln("</threat>");
		}
		
		out.writeln("</threats>");
	}

	private void writeOptionalViability(UnicodeWriter out) throws Exception
	{
		Target[] targets = getProject().getTargetPool().getTargets();
		if (!containsAtLeastOneViabilityModeTarget(targets))
			return;
		
		out.writeln("<viability>");
		try
		{
			for (int index = 0; index < targets.length; ++index)
			{
				if (targets[index].isViabilityModeTNC())
				{
					writeKeyEcologicalAttributeViability(out, targets[index]);
				}
			}
		}
		finally
		{
			out.writeln("</viability>");
		}
	}

	private boolean containsAtLeastOneViabilityModeTarget(Target[] targets)
	{
		for (int index = 0; index < targets.length; ++index)
		{
			if (targets[index].isViabilityModeTNC())
				return true;
		}
		
		return false;
	}

	private void writeKeyEcologicalAttributeViability(UnicodeWriter out, Target target) throws Exception
	{
		ORefList keaRefs = target.getKeyEcologicalAttributeRefs();
		for (int refIndex = 0; refIndex < keaRefs.size(); ++refIndex)
		{
			KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getProject(), keaRefs.get(refIndex));
			writeKeyEcologicalAttributeIndicatorViability(out, target, kea);
		}				
	}
	
	private void writeKeyEcologicalAttributeIndicatorViability(UnicodeWriter out, Target target, KeyEcologicalAttribute kea) throws Exception
	{
		ORefList indicatorRefs = kea.getIndicatorRefs();
		for (int refIndex = 0; refIndex < indicatorRefs.size(); ++refIndex)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRefs.get(refIndex));
			writeViability(out, target.getRef(), kea, indicator);
		}				
	}

	private void writeViability(UnicodeWriter out, ORef targetRef, KeyEcologicalAttribute kea, Indicator indicator) throws Exception
	{
		out.writeln("<viability_assessment>");
		writeElement(out, "target_id", targetRef.getObjectId().toString());
		writeElement(out, "indicator_id", indicator.getId().toString());
		writeElement(out, "kea_id", kea.getId().toString());
		
		writeThreshold(out, "indicator_description_poor", indicator, StatusQuestion.POOR);
		writeThreshold(out, "indicator_description_fair", indicator, StatusQuestion.FAIR);
		writeThreshold(out, "indicator_description_good", indicator, StatusQuestion.GOOD);
		writeThreshold(out, "indicator_description_very_good", indicator, StatusQuestion.VERY_GOOD);
		
		writeOptionalElement(out, "current_indicator_status_viability", indicator.getCurrentStatus());
		writeOptionalRatingCodeElement(out, "desired_viability_rating",  indicator.getFutureStatusRating());
		writeOptionalElement(out, "desired_rating_date",  indicator, Indicator.TAG_FUTURE_STATUS_DATE);
		writeOptionalElement(out, "kea_and_indicator_comment", indicator, Indicator.TAG_DETAIL);
		writeOptionalElement(out, "indicator_rating_comment", indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENT);
		writeOptionalElement(out, "desired_rating_comment", indicator, Indicator.TAG_FUTURE_STATUS_COMMENT);
		writeOptionalElement(out, "viability_record_comment", kea, KeyEcologicalAttribute.TAG_DESCRIPTION);
		writeOptionalLatestMeasurementValues(out, indicator);
			
		out.writeln("</viability_assessment>");
	}

	private void writeOptionalLatestMeasurementValues(UnicodeWriter out, Indicator indicator) throws Exception
	{
		ORef measurementRef = indicator.getLatestMeasurementRef();
		if (measurementRef.isInvalid())
			return;
		
		Measurement measurement = Measurement.find(getProject(), measurementRef);
		
		writeOptionalElement(out, "current_viability_rating",  measurement, Measurement.TAG_STATUS);
		writeOptionalElement(out, "current_rating_date",  measurement, Measurement.TAG_DATE);
		writeOptionalElement(out, "confidence_current_rating",  translateStatusConfidence(measurement.getData(Measurement.TAG_STATUS_CONFIDENCE)));
		writeOptionalElement(out, "current_rating_comment", measurement, Measurement.TAG_COMMENT);
	
	}

	private void writeThreshold(UnicodeWriter out, String elementName, Indicator indicator, String threshold) throws Exception
	{
		HashMap<String, String> stringMap = indicator.getThreshold().getStringMap().toHashMap();
		String value = stringMap.get(threshold);
		writeOptionalElement(out, elementName, value);
	}

	private void writeOptionalKeyEcologicalAttributes(UnicodeWriter out) throws Exception
	{
		KeyEcologicalAttribute keas[] = getProject().getKeyEcologicalAttributePool().getAllKeyEcologicalAttribute();
		if (keas.length == 0)
			return;
		
		out.writeln("<key_attributes>");
		for (int index = 0; index < keas.length; ++index)
		{
			out.writeln("<key_attribute id='" + keas[index].getId().toString() + "'>");
			writeElement(out, "name", keas[index], KeyEcologicalAttribute.TAG_LABEL);
			writeElement(out, "category", translateKeyEcologicalAttributeType(keas[index].getKeyEcologicalAttributeType()));
			out.writeln("</key_attribute>");
		}
		
		out.writeln("</key_attributes>");
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
			out.write("<target id='" + target.getId().toString() + "'>");
			writeElement(out, "name", target, Target.TAG_LABEL);
			writeOptionalElement(out, "description", target, Target.TAG_TEXT);
			writeOptionalElement(out, "target_viability_comment", target, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			writeOptionalRatingCodeElement(out, "target_viability_rank", target.getBasicTargetStatus());
			//FIXME cant get this work,  need a way to export each code in list, schema question
			//writeCodeListElements(out, "habitat_code", target.getCodeList(Target.TAG_HABITAT_ASSOCIATION));
			//FIXME need to resolve and export target threat_taxonomy_code
			writeOptionalStresses(out, target);
			writeThreatStressRatings(out, target);
			writeNestedTargets(out, target);
			writeSimpleTargetThreatLinkRatings(out, target);
			out.writeln("</target>");
		}
		out.writeln("</targets>");

	}
	
	private FactorLinkSet getTargetThreatFactorLinks(Target target) throws Exception
	{
		FactorLinkSet targetThreatLinks = new FactorLinkSet();
		ORefList factorLinkReferrers = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			if (factorLink.isThreatTargetLink())
			{
				targetThreatLinks.add(factorLink);
			}
		}
		
		return targetThreatLinks;
	}
	
	private FactorLinkSet getThreatLinksWithThreatStressRatings(Target target) throws Exception
	{
		FactorLinkSet linksWithThreatStressRatings = new FactorLinkSet();
		FactorLinkSet targetThreatLinks = getTargetThreatFactorLinks(target);
		for(FactorLink factorLink : targetThreatLinks)
		{
			if (factorLink.getThreatStressRatingRefs().size() > 0)
				linksWithThreatStressRatings.add(factorLink);
		}
		
		return linksWithThreatStressRatings;
	}

	private void writeSimpleTargetThreatLinkRatings(UnicodeWriter out, Target target) throws Exception
	{
		FactorLinkSet targetThreatLinks = getTargetThreatFactorLinks(target);
		if (targetThreatLinks.size() == 0)
			return;
		
		out.writeln("<threat_target_associations>");
		for(FactorLink factorLink : targetThreatLinks)
		{
			writeSimpleTargetThreatLinkRatings(out, factorLink, target.getRef());
		}
		out.writeln("</threat_target_associations>");
	}
	
	private void writeSimpleTargetThreatLinkRatings(UnicodeWriter out, FactorLink factorLink, ORef targetRef) throws Exception
	{
		ORef threatRef = factorLink.getUpstreamThreatRef();
		SimpleThreatRatingFramework simpleThreatFramework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = simpleThreatFramework.getBundle((FactorId)threatRef.getObjectId(), (FactorId)targetRef.getObjectId());
				
		int targetThreatRatingValue = simpleThreatFramework.getBundleValue(bundle).getNumericValue();
		
		out.writeln("<threat_target_association>");
		writeElement(out, "threat_id", threatRef.getObjectId().toString());
		writeOptionalRatingCodeElement(out, "threat_to_target_rank", targetThreatRatingValue);
		writeOptionalRatingCodeElement(out, "threat_severity", getSeverity(simpleThreatFramework, bundle));
		writeOptionalRatingCodeElement(out, "threat_scope", getScope(simpleThreatFramework, bundle));
		writeOptionalRatingCodeElement(out, "threat_irreversibility", getIrreversibility(simpleThreatFramework, bundle));
		writeOptionalElement(out, "threat_target_comment", factorLink, FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT);
		out.writeln("</threat_target_association>");
	}

	private int getIrreversibility(SimpleThreatRatingFramework simpleThreatFramework, ThreatRatingBundle bundle)
	{
		RatingCriterion irreversibilityCriterion = simpleThreatFramework.getIrreversibilityCriterion();
		ValueOption irreversibility = findValueOption(bundle.getValueId(irreversibilityCriterion.getId()));
		return irreversibility.getNumericValue();
	}

	private int getScope(SimpleThreatRatingFramework simpleThreatFramework, ThreatRatingBundle bundle)
	{
		RatingCriterion scopeCriterion = simpleThreatFramework.getScopeCriterion();
		ValueOption scope = findValueOption(bundle.getValueId(scopeCriterion.getId()));
		return scope.getNumericValue();
	}

	private int getSeverity(SimpleThreatRatingFramework simpleThreatFramework, ThreatRatingBundle bundle)
	{
		RatingCriterion severityCriterion = simpleThreatFramework.getSeverityCriterion();
		ValueOption severity = findValueOption(bundle.getValueId(severityCriterion.getId()));
		
		return severity.getNumericValue();
	}

	private ValueOption findValueOption(BaseId valueOptionId)
	{
		return (ValueOption)getProject().findObject(ValueOption.getObjectType(), valueOptionId);
	}
	
	private void writeNestedTargets(UnicodeWriter out, Target target) throws Exception
	{
		ORefList subTargetRefs = target.getSubTargetRefs();
		if (subTargetRefs.size() == 0)
			return;
		
		out.writeln("<nested_targets>");
		for (int refIndex = 0; refIndex < subTargetRefs.size(); ++refIndex)
		{
			SubTarget subTarget = SubTarget.find(getProject(), subTargetRefs.get(refIndex));
			out.writeln("<nested_target sequence='" + refIndex + "'>");
			writeElement(out, "name", subTarget, SubTarget.TAG_LABEL);
			writeElement(out, "comment", subTarget, SubTarget.TAG_DETAIL);
			out.writeln("</nested_target>");
		}
		out.writeln("</nested_targets>");
	}

	private void writeThreatStressRatings(UnicodeWriter out, Target target) throws Exception
	{
		FactorLinkSet targetThreatLinks = getThreatLinksWithThreatStressRatings(target);
		if (targetThreatLinks.size() == 0)
			return;
		
		out.writeln("<stresses_threats>");
		for(FactorLink factorLink : targetThreatLinks)
		{
			writeThreatStressRatings(out, factorLink);
		}
		out.writeln("</stresses_threats>");
	}

	private void writeThreatStressRatings(UnicodeWriter out, FactorLink factorLink) throws Exception
	{
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		for (int refIndex = 0; refIndex < threatStressRatingRefs.size(); ++refIndex)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingRefs.get(refIndex));
			out.write("<stresses_threat>");
			writeOptionalRatingCodeElement(out, "contrib_rank", threatStressRating.getIrreversibilityCode());
			out.write("</stresses_threat>");
		}
	}

	private void writeOptionalStresses(UnicodeWriter out, Target target) throws Exception
	{
		ORefList stressRefs = target.getStressRefs();
		if (stressRefs.size() == 0)
			return;
		
		out.writeln("<stresses_targets>");
		for (int refIndex = 0; refIndex < stressRefs.size(); ++refIndex)
		{
			out.write("<stresses_target sequence='" + refIndex + "'>");
			Stress stress = Stress.find(getProject(), stressRefs.get(refIndex));
			writeElement(out, "stress_name", stress, Stress.TAG_LABEL);
			writeOptionalRatingCodeElement(out, "stress_severity", stress.getData(Stress.TAG_SEVERITY));
			writeOptionalRatingCodeElement(out, "stress_scope", stress.getData(Stress.TAG_SCOPE));
			writeOptionalRatingCodeElement(out, "stress_to_target_rank", stress.getCalculatedStressRating());
			out.writeln("</stresses_target>");
		}
		out.writeln("</stresses_targets>");
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
			writeCodeListElements(out, "countries", "country_code", getProjectMetadata(), ProjectMetadata.TAG_COUNTRIES);
			writeCodeListElements(out, "ous", "ou_code", getProjectMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
			
			out.writeln("<exporter_name/>");
			out.writeln("<exporter_version/>");
			out.writeln("<data_export_date>" + new MultiCalendar().toIsoDateString() + "</data_export_date>");
			
		out.writeln("</project_summary>");
	}

	private String getComputedTncViability()
	{
		String code = Target.computeTNCViability(getProject());
		return ratingCodeToXmlValue(code);
	}

	private String getStressBasedOverallProjectRating()
	{
		int overallProjectRating = getProject().getStressBasedThreatRatingFramework().getOverallProjectRating();
		return ratingCodeToXmlValue(overallProjectRating);
	}
	
	private String getSimpleOverallProjectRating()
	{
		int overallProjectRating = getProject().getSimpleThreatRatingFramework().getOverallProjectRating().getNumericValue();
		return ratingCodeToXmlValue(overallProjectRating);
	}

	private void writeEcoregionCodes(UnicodeWriter out) throws Exception
	{
		CodeList allTncEcoRegionCodes = new CodeList();
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncFreshwaterEcoRegion());
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncMarineEcoRegion());
		allTncEcoRegionCodes.addAll(getProjectMetadata().getTncTerrestrialEcoRegion());
		
		if (allTncEcoRegionCodes.size() == 0)
			return;
		
		out.writeln("<ecoregions>");
		writeCodeListElements(out, "ecoregion_code", allTncEcoRegionCodes);
		out.writeln("</ecoregions>");
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
			//NOTE person is not validating.
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

	private void writeCodeListElements(UnicodeWriter out, String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		out.writeln("<" + parentElementName + ">");
		writeCodeListElements(out, elementName, object, tag);
		out.writeln("</" + parentElementName + ">");
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
	
	private void writeOptionalRatingCodeElement(UnicodeWriter out, String elementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalRatingCodeElement(out, elementName, object.getData(tag));
	}
	
	private void writeOptionalRatingCodeElement(UnicodeWriter out, String elementName, String code) throws Exception
	{
		writeOptionalElement(out, elementName, ratingCodeToXmlValue(code));
	}
	
	private void writeOptionalRatingCodeElement(UnicodeWriter out, String elementName, int code) throws Exception
	{
		writeOptionalElement(out, elementName, ratingCodeToXmlValue(code));
	}
	
	private String ratingCodeToXmlValue(int code)
	{
		return ratingCodeToXmlValue(Integer.toString(code));
	}
	
	private String translateStatusConfidence(String data)
	{
		if (data.equals(StatusConfidenceQuestion.ROUGH_GUESS_CODE))
			return "Rough Guess";
		
		if (data.equals(StatusConfidenceQuestion.EXPERT_KNOWLEDGE_CODE))
			return "Expert Knowledge";
		
		if (data.equals(StatusConfidenceQuestion.RAPID_ASSESSMENT_CODE))
			return "Rapid Assessment";
		
		if (data.equals(StatusConfidenceQuestion.INTENSIVE_ASSESSMENT_CODE))
			return "Intensive Assessment";
		
		return "";
	}
	
	private String translateKeyEcologicalAttributeType(String type)
	{
		if (type.equals("10"))
			return "Size";
		
		if (type.equals("20"))
			return "Condition";
		
		if (type.equals("30"))
			return "Landscape Context";
		
		return "";
	}
	
	private String ratingCodeToXmlValue(String code)
	{
		if (code.equals("1"))
			return "Low";
		
		if (code.equals("2"))
			return "Medium";
		
		if (code.equals("3"))
			return "High";
		
		if (code.equals("4"))
			return "Very High";
		
		return "";
	}
	
	public static void main(String[] commandLineArguments) throws Exception
	{	
		Project newProject = getOpenedProject(commandLineArguments);
		try
		{
			new ConproXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			new testSampleXml().isValid(new FileInputStream(getXmlDestination(commandLineArguments)));
			System.out.println("Export Conpro xml complete");
		}
		finally
		{
			newProject.close();
		}
	}
}