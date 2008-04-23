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
package org.miradi.xml.conpro.exporter;

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
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.ConproMiradiHabitatCodeMap;
import org.miradi.utils.DateRange;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.conpro.ConProMiradiXml;

public class ConproXmlExporter extends XmlExporter implements ConProMiradiXml
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
		//FIXME output schema url
		
		writeoutDocumentExchangeElement(out);
		writeoutProjectSummaryElement(out);
		writeTargets(out);
		writeKeyEcologicalAttributes(out);
		writeViability(out);
		writeThreats(out);
		writeObjectives(out);
		writeStrategies(out);
		writeIndicators(out);
		
		out.writeln("</conservation_project>");
	}

	private void writeIndicators(UnicodeWriter out) throws Exception
	{
		ORefList indicatorRefs = getProject().getIndicatorPool().getRefList();
		out.writeln("<indicators>");
		for (int refIndex = 0; refIndex < indicatorRefs.size(); ++refIndex)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRefs.get(refIndex));
			out.writeln("<indicator id='" + indicator.getId().toString() + "'>");
			writeElement(out, "name", indicator, Indicator.TAG_LABEL);
			writeOptionalMethods(out, indicator.getMethodRefs());
			writeOptionalRatingCodeElement(out, "priority", indicator, Indicator.TAG_PRIORITY);
			out.writeln("</indicator>");
		}
		
		out.writeln("</indicators>");
	}

	private void writeOptionalMethods(UnicodeWriter out, ORefList methodRefs) throws Exception
	{
		String methodNames = "";
		for (int refIndex = 0; refIndex < methodRefs.size(); ++refIndex)
		{
			Task method = Task.find(getProject(), methodRefs.get(refIndex));
			if (refIndex != 0)
				methodNames += ";";
			
			methodNames += method.getData(Task.TAG_LABEL);
		}
		
		writeOptionalElement(out, "methods", methodNames);
	}

	private void writeStrategies(UnicodeWriter out) throws Exception
	{
		ORefList strategyRefs = getProject().getStrategyPool().getRefList();
		out.writeln("<strategies>");
		for (int refIndex = 0; refIndex < strategyRefs.size(); ++refIndex)
		{
			Strategy strategy = Strategy.find(getProject(), strategyRefs.get(refIndex));
			out.writeln("<strategy id='" + strategy.getId().toString() + "'>");
			writeIds(out, "objectives", "objective_id", strategy.getObjectiveRefs());
			writeElement(out, "name", strategy, Strategy.TAG_LABEL);
			writeOptionalElement(out, "taxonomy_code", strategy, Strategy.TAG_TAXONOMY_CODE);
			writeOptionalRatingCodeElement(out, "leverage", strategy, Strategy.TAG_IMPACT_RATING);
			writeOptionalRatingCodeElement(out, "feasibility", strategy, Strategy.TAG_FEASIBILITY_RATING);
			writeOptionalRatingCodeElement(out, "overall_rank", strategy.getStrategyRatingSummary());
			writeElement(out, "selected", Boolean.toString(!strategy.isStatusDraft()));
			writeOptionalElement(out, "comment", strategy, Strategy.TAG_COMMENT);
			writeActivities(out, strategy.getActivityRefs());
						
			out.writeln("</strategy>");
		}
		
		out.writeln("</strategies>");
	}

	private void writeActivities(UnicodeWriter out, ORefList activityRefs) throws Exception
	{
		out.writeln("<activities>");
		for (int refIndex = 0; refIndex < activityRefs.size(); ++refIndex)
		{
			Task activity = Task.find(getProject(), activityRefs.get(refIndex));
			out.writeln("<activity>");
			writeElement(out, "name", activity, Task.TAG_LABEL);
			DateRange whenTotal = activity.getWhenTotal();
			if (whenTotal != null)
			{
				writeElement(out, "start_date", whenTotal.getStartDate().toString());
				writeElement(out, "end_date", whenTotal.getEndDate().toString());
			}
			out.writeln("</activity>");
		}
		
		out.writeln("</activities>");
	}

	private void writeObjectives(UnicodeWriter out) throws Exception
	{
		ORefList objectiveRefs = getProject().getObjectivePool().getRefList();
		out.writeln("<objectives>");
		for (int refIndex = 0; refIndex < objectiveRefs.size(); ++refIndex)
		{
			Objective objective = Objective.find(getProject(), objectiveRefs.get(refIndex));
			out.writeln("<objective id='" + objective.getId().toString() + "'>");
			writeIndicatorIds(out, objective);
			writeElement(out, "name", objective, Objective.TAG_LABEL);
			writeOptionalElement(out, "comment", objective, Objective.TAG_COMMENTS);
			out.writeln("</objective>");
		}
		
		out.writeln("</objectives>");
	}

	private void writeIndicatorIds(UnicodeWriter out, Objective objective) throws Exception
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

	private void writeIds(UnicodeWriter out, String parentElementName, String elementName, ORefList refs) throws Exception
	{
		out.writeln("<" + parentElementName + ">");
		writeIds(out, elementName, refs);
		out.writeln("</" + parentElementName + ">");
	}
	
	private void writeThreats(UnicodeWriter out) throws Exception
	{
		Factor[] directThreats = getProject().getCausePool().getDirectThreats();
		out.writeln("<threats>");
		for (int index = 0; index < directThreats.length; ++index)
		{
			out.writeln("<threat id='" + directThreats[index].getId().toString() + "'>");
			writeElement(out, "name", directThreats[index], Cause.TAG_LABEL);
			writeOptionalElement(out, "threat_taxonomy_code", directThreats[index], Cause.TAG_TAXONOMY_CODE);
			ChoiceItem threatRatingValue = getProject().getThreatRatingFramework().getThreatThreatRatingValue(directThreats[index].getRef());
			writeOptionalRatingCodeElement(out, "threat_to_project_rank", threatRatingValue.getCode());
			out.writeln("</threat>");
		}
		
		out.writeln("</threats>");
	}

	private void writeViability(UnicodeWriter out) throws Exception
	{
		Target[] targets = getProject().getTargetPool().getTargets();
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
		writeOptionalLatestMeasurementValues(out, indicator);
		writeOptionalElement(out, "desired_rating_date",  indicator, Indicator.TAG_FUTURE_STATUS_DATE);
		writeOptionalElement(out, "kea_and_indicator_comment", indicator, Indicator.TAG_DETAIL);
		writeOptionalElement(out, "indicator_rating_comment", indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENT);
		writeOptionalElement(out, "desired_rating_comment", indicator, Indicator.TAG_FUTURE_STATUS_COMMENT);
		writeOptionalElement(out, "viability_record_comment", kea, KeyEcologicalAttribute.TAG_DESCRIPTION);
			
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
		writeOptionalElement(out, "confidence_current_rating",  statusConfidenceToXmlValue(measurement.getData(Measurement.TAG_STATUS_CONFIDENCE)));
		writeOptionalElement(out, "current_rating_comment", measurement, Measurement.TAG_COMMENT);
	
	}

	private void writeThreshold(UnicodeWriter out, String elementName, Indicator indicator, String threshold) throws Exception
	{
		HashMap<String, String> stringMap = indicator.getThreshold().getStringMap().toHashMap();
		String value = stringMap.get(threshold);
		writeOptionalElement(out, elementName, value);
	}

	private void writeKeyEcologicalAttributes(UnicodeWriter out) throws Exception
	{
		KeyEcologicalAttribute keas[] = getProject().getKeyEcologicalAttributePool().getAllKeyEcologicalAttribute();
		out.writeln("<key_attributes>");
		for (int index = 0; index < keas.length; ++index)
		{
			out.writeln("<key_attribute id='" + keas[index].getId().toString() + "'>");
			writeElement(out, "name", keas[index], KeyEcologicalAttribute.TAG_LABEL);
			writeElement(out, "category", keyEcologicalAttributeTypeToXmlValue(keas[index].getKeyEcologicalAttributeType()));
			out.writeln("</key_attribute>");
		}
		
		out.writeln("</key_attributes>");
	}

	private void writeTargets(UnicodeWriter out) throws Exception
	{
		ORefList targetRefs = getProject().getTargetPool().getRefList();
		out.writeln("<targets>");
		for (int refIndex = 0; refIndex < targetRefs.size(); ++refIndex)
		{
			Target target = Target.find(getProject(), targetRefs.get(refIndex));
			out.write("<target id='" + target.getId().toString() + "' sequence='" + refIndex +1 + "'>");
			writeElement(out, "name", target, Target.TAG_LABEL);
			writeOptionalElement(out, "description", target, Target.TAG_TEXT);
			writeOptionalElement(out, "description_comment", target, Target.TAG_COMMENT);
			writeOptionalElement(out, "target_viability_comment", target, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			writeOptionalRankingCodeElement(out, "target_viability_rank", target.getBasicTargetStatus());
			writeHabitatMappedCodes(out, target);
			writeOptionalStresses(out, target);
			writeThreatStressRatings(out, target);
			writeNestedTargets(out, target);
			writeSimpleTargetLinkRatings(out, target);
			writeStrategyThreatTargetAssociations(out, target);
			out.writeln("</target>");
		}
		out.writeln("</targets>");
	}

	private void writeStrategyThreatTargetAssociations(UnicodeWriter out, Target target) throws Exception
	{
		out.writeln("<strategy_threat_target_associations>");
		ORefList factorLinkReferrers = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			if (factorLink.isThreatTargetLink())
				writeStrategyThreatTargetAssociations(out, target, factorLink.getUpstreamThreatRef());
		}
		
		out.writeln("</strategy_threat_target_associations>");
	}

	private void writeStrategyThreatTargetAssociations(UnicodeWriter out, Target target, ORef threatRef) throws Exception
	{
		Cause threat = Cause.find(getProject(), threatRef);
		ORefList factorLinkReferrers = threat.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink thisFactorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			ORef strategyRef = getStrategyRef(thisFactorLink);
			if (!strategyRef.isInvalid())
				writeStrategyThreatTargetAssociation(out, threatRef, strategyRef);
		}	
	}

	private ORef getStrategyRef(FactorLink thisFactorLink)
	{
		if (Strategy.is(thisFactorLink.getFromFactorRef()))
			return thisFactorLink.getFromFactorRef();
		
		if (Strategy.is(thisFactorLink.getToFactorRef()))
			return thisFactorLink.getToFactorRef();
		
		return ORef.INVALID;
	}

	private void writeStrategyThreatTargetAssociation(UnicodeWriter out, ORef threatRef, ORef strategyRef) throws IOException
	{
		out.writeln("<strategy_threat_target_association>");
		
		out.write("<strategy_id>");
		out.write(strategyRef.getObjectId().toString());
		out.writeln("</strategy_id>");
		
		out.write("<threat_id>");
		out.write(threatRef.getObjectId().toString());
		out.writeln("</threat_id>");
		
		out.writeln("</strategy_threat_target_association>");
	}

	private void writeHabitatMappedCodes(UnicodeWriter out, Target target) throws Exception
	{
		CodeList conProHabitatCodeList = new CodeList();
		HashMap<String, String> habitatCodeMap = new ConproMiradiHabitatCodeMap().loadMap();
		CodeList miradiHabitatCodeList = target.getCodeList(Target.TAG_HABITAT_ASSOCIATION);
		for (int codeIndex = 0; codeIndex < miradiHabitatCodeList.size(); ++codeIndex)
		{
			String miradiHabitatCode = miradiHabitatCodeList.get(codeIndex);
			String conProHabitatCode = habitatCodeMap.get(miradiHabitatCode);
			conProHabitatCodeList.add(conProHabitatCode);
		}
		
		writeCodeListElements(out, "habitat_taxonomy_codes", "habitat_taxonomy_code", conProHabitatCodeList);
	}
	
	private FactorLinkSet getThreatTargetFactorLinks(Target target) throws Exception
	{
		FactorLinkSet targetLinks = new FactorLinkSet();
		ORefList factorLinkReferrers = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			if (factorLink.isThreatTargetLink())
			{
				targetLinks.add(factorLink);
			}
		}
		
		return targetLinks;
	}
	
	private FactorLinkSet getThreatLinksWithThreatStressRatings(Target target) throws Exception
	{
		FactorLinkSet linksWithThreatStressRatings = new FactorLinkSet();
		FactorLinkSet targetLinks = getThreatTargetFactorLinks(target);
		for(FactorLink factorLink : targetLinks)
		{
			if (factorLink.getThreatStressRatingRefs().size() > 0)
				linksWithThreatStressRatings.add(factorLink);
		}
		
		return linksWithThreatStressRatings;
	}

	private void writeSimpleTargetLinkRatings(UnicodeWriter out, Target target) throws Exception
	{
		FactorLinkSet targetLinks = getThreatTargetFactorLinks(target);
		out.writeln("<threat_target_associations>");
		for(FactorLink factorLink : targetLinks)
		{
			writeSimpleTargetLinkRatings(out, factorLink, target.getRef());
		}
		out.writeln("</threat_target_associations>");
	}
	
	private void writeSimpleTargetLinkRatings(UnicodeWriter out, FactorLink factorLink, ORef targetRef) throws Exception
	{
		ORef threatRef = factorLink.getUpstreamThreatRef();
		SimpleThreatRatingFramework simpleThreatFramework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = simpleThreatFramework.getBundle((FactorId)threatRef.getObjectId(), (FactorId)targetRef.getObjectId());
				
		int threatTargetRatingValue = simpleThreatFramework.getBundleValue(bundle).getNumericValue();
		
		out.writeln("<threat_target_association>");
		writeElement(out, "threat_id", threatRef.getObjectId().toString());
		writeOptionalRatingCodeElement(out, "threat_to_target_rank", threatTargetRatingValue);
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
		FactorLinkSet targetLinks = getThreatLinksWithThreatStressRatings(target);
		out.writeln("<stresses_threats>");
		for(FactorLink factorLink : targetLinks)
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
			out.writeln("<stresses_threat>");
			writeOptionalRatingCodeElement(out, "contrib_rank", threatStressRating.getIrreversibilityCode());
			out.writeln("</stresses_threat>");
		}
	}

	private void writeOptionalStresses(UnicodeWriter out, Target target) throws Exception
	{
		ORefList stressRefs = target.getStressRefs();
		out.writeln("<target_stresses>");
		for (int refIndex = 0; refIndex < stressRefs.size(); ++refIndex)
		{
			out.writeln("<target_stress sequence='" + refIndex + "'>");
			Stress stress = Stress.find(getProject(), stressRefs.get(refIndex));
			writeElement(out, "name", stress, Stress.TAG_LABEL);
			writeOptionalRatingCodeElement(out, "stress_severity", stress.getData(Stress.TAG_SEVERITY));
			writeOptionalRatingCodeElement(out, "stress_scope", stress.getData(Stress.TAG_SCOPE));
			writeOptionalRatingCodeElement(out, "stress_to_target_rank", stress.getCalculatedStressRating());
			out.writeln("</target_stress>");
		}
		out.writeln("</target_stresses>");
	}

	private void writeoutProjectSummaryElement(UnicodeWriter out) throws Exception
	{
		out.writeln("<" + PROJECT_SUMMARY + " " + SHARE_OUTSIDE_ORGANIZATION + "='false'>");
	
			//TODO,  need to write out read project ids
//			out.writeln("<project_id context='ConPro'>");
//			out.writeln("noId");
//			out.writeln("</project_id>");
//			
//			out.writeln("<parent_project_id context='ConPro'>");
//			out.writeln("noId");
//			out.writeln("</parent_project_id>");
			
			writeElement(out, PROJECT_SUMMARY_NAME, XmlUtilities.getXmlEncoded(getProjectMetadata().getProjectName()));
			
			writeOptionalElement(out, START_DATE, getProjectMetadata(), ProjectMetadata.TAG_START_DATE);
			writeOptionalAreaSize(out);
			writeOptionalLocation(out);
			
			writeOptionalElement(out, DESCRIPTION_COMMENT, getProjectMetadata(), ProjectMetadata.TAG_PROJECT_SCOPE);
			writeOptionalElement(out, GOAL_COMMENT, getProjectMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
			writeOptionalElement(out, PLANNING_TEAM_COMMENT, getProjectMetadata(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
			writeOptionalElement(out, LESSONS_LEARNED, getProjectMetadata(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
			
			writeOptionalElement(out, STRESSLESS_THREAT_RANK, getSimpleOverallProjectRating());
			writeOptionalElement(out, PROJECT_THREAT_RANK, getStressBasedOverallProjectRating());
			writeOptionalElement(out, PROJECT_VIABILITY_RANK, getComputedTncViability());
			writeTeamMembers(out);
			writeEcoregionCodes(out);
			writeCodeListElements(out, COUNTRIES, COUNTRY_CODE, getProjectMetadata(), ProjectMetadata.TAG_COUNTRIES);
			writeCodeListElements(out, OUS, OU_CODE, getProjectMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
			
			out.writeln("<" + EXPORTER_NAME + ">" + MIRADI + "</" + EXPORTER_NAME + ">");
			out.writeln("<" + EXPORTER_VERSION + ">" + EXPORT_VERSION + "</" + EXPORTER_VERSION + ">");
			out.writeln("<" + EXPORT_DATE + ">" + new MultiCalendar().toIsoDateString() + "</" + EXPORT_DATE+ ">");
			
		writeEndElement(out, PROJECT_SUMMARY);
	}

	private String getComputedTncViability()
	{
		String code = Target.computeTNCViability(getProject());
		return rankingCodeToXmlValue(code);
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
				
		writeCodeListElements(out, ECOREGIONS, ECOREGION_CODE, allTncEcoRegionCodes);
	}
	
	private void writeOptionalAreaSize(UnicodeWriter out) throws IOException
	{
		double sizeInHectaresAsInt = getProjectMetadata().getSizeInHectaresAsDouble();
		if (sizeInHectaresAsInt == 0)
			return;
		
		out.write("<" + AREA_SIZE + " " + AREA_SIZE_UNIT + "='hectares'>");
		out.write(Integer.toString((int)sizeInHectaresAsInt));
		writeEndElement(out, AREA_SIZE);
	}

	private void writeOptionalLocation(UnicodeWriter out) throws IOException, Exception
	{
		float latitudeAsFloat = getProjectMetadata().getLatitudeAsFloat();
		float longitudeAsFloat = getProjectMetadata().getLongitudeAsFloat();
		if (latitudeAsFloat == 0 && longitudeAsFloat == 0)
			return;
		
		out.writeln("<" + GEOSPATIAL_LOCATION + " " + GEOSPATIAL_LOCATION_TYPE + "='point'>");
		writeOptionalFloatElement(out, LATITUDE, latitudeAsFloat);
		writeOptionalFloatElement(out, LONGITUDE, longitudeAsFloat);
		writeEndElement(out, GEOSPATIAL_LOCATION);
	}
	
	private void writeTeamMembers(UnicodeWriter out) throws Exception
	{
		ORefList teamMemberRefs = getProject().getResourcePool().getTeamMemberRefs();
		writeStartElement(out, TEAM);
		for (int memberIndex = 0; memberIndex < teamMemberRefs.size(); ++memberIndex)
		{
			writeStartElement(out, PERSON);
			ProjectResource member = ProjectResource.find(getProject(), teamMemberRefs.get(memberIndex));
			writeMemberRoles(out, member);
			
			writeOptionalElement(out, GIVEN_NAME, member, ProjectResource.TAG_GIVEN_NAME);
			writeOptionalElement(out, SUR_NAME, member, ProjectResource.TAG_SUR_NAME);
			writeOptionalElement(out, EMAIL, member, ProjectResource.TAG_EMAIL);
			writeOptionalElement(out, PHONE, member, ProjectResource.TAG_PHONE_NUMBER);
			writeOptionalElement(out, ORGANIZATION, member, ProjectResource.TAG_ORGANIZATION);
			writeEndElement(out, PERSON);	
		}
		writeEndElement(out, TEAM);
	}

	private void writeMemberRoles(UnicodeWriter out, ProjectResource member) throws Exception
	{
		ChoiceQuestion question = getProject().getQuestion(ResourceRoleQuestion.class);
		writeElement(out, ROLE, question.findChoiceByCode(ResourceRoleQuestion.TeamMemberRoleCode).getLabel());
		if (member.isTeamLead())
			writeElement(out, ROLE, TEAM_LEADER_VALUE);
	}
	
	private void writeOptionalFloatElement(UnicodeWriter out, String elementName, float value) throws Exception
	{
		if (value == 0)
			return;
		
		writeOptionalElement(out, elementName, Float.toString(value));
	}

	protected void writeCodeListElements(UnicodeWriter out, String parentElementName, String elementName, CodeList codeList) throws Exception
	{
		out.writeln("<" + parentElementName + ">");
		writeCodeListElements(out, elementName, codeList);
		out.writeln("</" + parentElementName + ">");
	}
	
	private void writeCodeListElements(UnicodeWriter out, String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		CodeList codeList = object.getCodeList(tag);
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
		out.writeln("<" + DOCUMENT_EXCHANGE + " " + DOCIMENT_EXCHANGE_STATUS + "='success'/>");
		//NOTE: we never write the optional error message
	}
	
	private void writeOptionalRankingCodeElement(UnicodeWriter out, String elementName, String code) throws Exception
	{
		writeOptionalElement(out, elementName, rankingCodeToXmlValue(code));
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
	
	private void writeEndElement(UnicodeWriter out, String endElementName) throws IOException
	{
		out.writeln("</" + endElementName + ">");
	}
	
	private void writeStartElement(UnicodeWriter out, String startElementName) throws IOException
	{
		out.writeln("<" + startElementName + ">");
	}
	
	private String ratingCodeToXmlValue(int code)
	{
		return ratingCodeToXmlValue(Integer.toString(code));
	}
	
	private String statusConfidenceToXmlValue(String data)
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
	
	private String keyEcologicalAttributeTypeToXmlValue(String type)
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
	
	private String rankingCodeToXmlValue(String code)
	{
		if (code.equals("1"))
			return "Poor";
		
		if (code.equals("2"))
			return "Fair";
		
		if (code.equals("3"))
			return "Good";
		
		if (code.equals("4"))
			return "Very Good";
		
		return "";
	}
	
	public static void main(String[] commandLineArguments) throws Exception
	{	
		Project newProject = getOpenedProject(commandLineArguments);
		try
		{
			new ConproXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			new ConProMiradiXmlValidator().isValid(new FileInputStream(getXmlDestination(commandLineArguments)));
			System.out.println("Export Conpro xml complete");
		}
		finally
		{
			newProject.close();
		}
	}
}