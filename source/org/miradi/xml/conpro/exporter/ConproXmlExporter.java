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
import java.util.Arrays;
import java.util.HashMap;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectToStringSorter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressReport;
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
import org.miradi.questions.ProgressReportStatusQuestion;
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
		
		createMiradiToConproCodeMaps();
	}

	@Override
	protected void exportProject(UnicodeWriter out) throws Exception
	{
		out.writeln("<?xml version='1.0'?>");
		out.writeln("<" + CONSERVATION_PROJECT + " " + XMLNS + "='" + NAME_SPACE +"'>");
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
		
		writeEndElement(out, CONSERVATION_PROJECT);
	}

	private void writeIndicators(UnicodeWriter out) throws Exception
	{
		ORefList indicatorRefs = getProject().getIndicatorPool().getRefList();
		writeStartElement(out, INDICATORS);
		for (int refIndex = 0; refIndex < indicatorRefs.size(); ++refIndex)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRefs.get(refIndex));
			out.writeln("<" + INDICATOR + " " + ID + "='" + indicator.getId().toString() + "'>");
			writeElement(out, NAME, indicator, Indicator.TAG_LABEL);
			writeOptionalMethods(out, indicator.getMethodRefs());
			writeOptionalRatingCodeElement(out, PRIORITY, indicator, Indicator.TAG_PRIORITY);
			writeOptionalProgressReportStatus(out, indicator);
			writeOptionalElement(out, WHO_MONITORS, createAppendedResourceNames(out, indicator));
			writeOptionalElement(out, ANNUAL_COST, Double.toString(indicator.getProportionalBudgetCost())); 
			writeOptionalElement(out, COMMENT, indicator, Indicator.TAG_COMMENT);
	
			writeEndElement(out, INDICATOR);
		}
		
		writeEndElement(out, INDICATORS);
	}

	private void writeOptionalProgressReportStatus(UnicodeWriter out, Indicator indicator) throws Exception
	{
		ProgressReport latestProgressReport = indicator.getLatestProgressReport();
		if (latestProgressReport == null)
			return;
		
		String progressStatusCode = latestProgressReport.getData(ProgressReport.TAG_PROGRESS_STATUS);
		writeOptionalElement(out, STATUS, statusCodeToXmlValue(progressStatusCode));
	}

	private String createAppendedResourceNames(UnicodeWriter out, Indicator indicator) throws Exception
	{
		String allResourceNames = "";
		ORefSet resourceRefs = indicator.getAllResources(indicator.getMethodRefs());
		for(ORef resourceRef : resourceRefs)
		{
			ProjectResource resource = ProjectResource.find(getProject(), resourceRef);
			allResourceNames += resource.getFullName() + "; ";
		}
		
		return allResourceNames;
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
		
		writeOptionalElement(out, METHODS, methodNames);
	}

	private void writeStrategies(UnicodeWriter out) throws Exception
	{
		ORefList strategyRefs = getProject().getStrategyPool().getRefList();
		writeStartElement(out, STRATEGIES);
		for (int refIndex = 0; refIndex < strategyRefs.size(); ++refIndex)
		{
			Strategy strategy = Strategy.find(getProject(), strategyRefs.get(refIndex));
			out.writeln("<" + STRATEGY + " " + ID + "='" + strategy.getId().toString() + "'>");
			writeIds(out, OBJECTIVES, OBJECTIVE_ID, strategy.getObjectiveRefs());
			writeElement(out, NAME, strategy, Strategy.TAG_LABEL);
			writeOptionalElement(out, TAXONOMY_CODE, strategy, Strategy.TAG_TAXONOMY_CODE);
			writeOptionalRatingCodeElement(out, LEVERAGE, strategy, Strategy.TAG_IMPACT_RATING);
			writeOptionalRatingCodeElement(out, FEASABILITY, strategy, Strategy.TAG_FEASIBILITY_RATING);
			writeOptionalRatingCodeElement(out, OVERALL_RANK, strategy.getStrategyRatingSummary());
			writeElement(out, SELECTED, Boolean.toString(!strategy.isStatusDraft()));
			writeOptionalElement(out, COMMENT, strategy, Strategy.TAG_COMMENT);
			writeActivities(out, strategy.getActivityRefs());
						
			writeEndElement(out, STRATEGY);
		}
		
		writeEndElement(out, STRATEGIES);
	}

	private void writeActivities(UnicodeWriter out, ORefList activityRefs) throws Exception
	{
		writeStartElement(out, ACTIVITIES);
		for (int refIndex = 0; refIndex < activityRefs.size(); ++refIndex)
		{
			Task activity = Task.find(getProject(), activityRefs.get(refIndex));
			writeStartElement(out, ACTIVITY);
			writeElement(out, NAME, activity, Task.TAG_LABEL);
			DateRange whenTotal = activity.getWhenTotal();
			if (whenTotal != null)
			{
				writeElement(out, ACTIVITY_START_DATE, whenTotal.getStartDate().toString());
				writeElement(out, ACTIVITY_END_DATE, whenTotal.getEndDate().toString());
			}
			writeEndElement(out, ACTIVITY);
		}
		
		writeEndElement(out, ACTIVITIES);
	}

	private void writeObjectives(UnicodeWriter out) throws Exception
	{
		ORefList objectiveRefs = getProject().getObjectivePool().getRefList();
		writeStartElement(out, OBJECTIVES);
		for (int refIndex = 0; refIndex < objectiveRefs.size(); ++refIndex)
		{
			Objective objective = Objective.find(getProject(), objectiveRefs.get(refIndex));
			out.writeln("<" + OBJECTIVE + " " + ID + "='" + objective.getId().toString() + "'>");
			writeIndicatorIds(out, objective);
			writeElement(out, NAME, objective, Objective.TAG_LABEL);
			writeOptionalElement(out, COMMENT, objective, Objective.TAG_COMMENTS);
			writeEndElement(out, OBJECTIVE);
		}
		
		writeEndElement(out, OBJECTIVES);
	}

	private void writeIndicatorIds(UnicodeWriter out, Objective objective) throws Exception
	{
		writeStartElement(out, INDICATORS);
		writeIds(out, INDICATOR_ID, objective.getRelevantIndicatorRefList());
		writeEndElement(out, INDICATORS);
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
		writeStartElement(out, THREATS);
		for (int index = 0; index < directThreats.length; ++index)
		{
			out.writeln("<" + THREAT + " " + ID + "='" + directThreats[index].getId().toString() + "'>");
			writeElement(out, NAME, directThreats[index], Cause.TAG_LABEL);
			writeOptionalElement(out, THREAT_TAXONOMY_CODE, directThreats[index], Cause.TAG_TAXONOMY_CODE);
			ChoiceItem threatRatingValue = getProject().getThreatRatingFramework().getThreatThreatRatingValue(directThreats[index].getRef());
			writeOptionalRatingCodeElement(out, THREAT_TO_PROJECT_RANK, threatRatingValue.getCode());
			writeEndElement(out, THREAT);
		}
		
		writeEndElement(out, THREATS);
	}

	private void writeViability(UnicodeWriter out) throws Exception
	{
		Target[] targets = getProject().getTargetPool().getTargets();
		writeStartElement(out, VIABILITY);
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
			writeEndElement(out, VIABILITY);
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
		writeStartElement(out, VIABILITY_ASSESSMENT);
		writeElement(out, TARGET_ID, targetRef.getObjectId().toString());
		writeElement(out, INDICATOR_ID, indicator.getId().toString());
		writeElement(out, KEA_ID, kea.getId().toString());
		
		writeThreshold(out, INDICATOR_DESCRIPTION_POOR, indicator, StatusQuestion.POOR);
		writeThreshold(out, INDICATOR_DESCRIPTION_FAIR, indicator, StatusQuestion.FAIR);
		writeThreshold(out, INDICATOR_DESCRIPTION_GOOD, indicator, StatusQuestion.GOOD);
		writeThreshold(out, INDICATOR_DESCRIPTION_VERY_GOOD, indicator, StatusQuestion.VERY_GOOD);
		
		writeOptionalElement(out, CURRENT_INDICATOR_STATUS_VIABILITY, indicator.getCurrentStatus());
		writeOptionalRatingCodeElement(out, DESIRED_VIABILITY_RATING,  indicator.getFutureStatusRating());
		writeOptionalLatestMeasurementValues(out, indicator);
		writeOptionalElement(out, DESIRED_RATING_DATE,  indicator, Indicator.TAG_FUTURE_STATUS_DATE);
		writeOptionalElement(out, KEA_AND_INDICATOR_COMMENT, indicator, Indicator.TAG_DETAIL);
		writeOptionalElement(out, INDICATOR_RATING_COMMENT, indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENT);
		writeOptionalElement(out, DESIRED_RATING_COMMENT, indicator, Indicator.TAG_FUTURE_STATUS_COMMENT);
		writeOptionalElement(out, VIABILITY_RECORD_COMMENT, kea, KeyEcologicalAttribute.TAG_DESCRIPTION);
			
		writeEndElement(out, VIABILITY_ASSESSMENT);
	}

	private void writeOptionalLatestMeasurementValues(UnicodeWriter out, Indicator indicator) throws Exception
	{
		ORef measurementRef = indicator.getLatestMeasurementRef();
		if (measurementRef.isInvalid())
			return;
		
		Measurement measurement = Measurement.find(getProject(), measurementRef);
		
		writeOptionalElement(out, CURRENT_VIABILITY_RATING,  measurement, Measurement.TAG_STATUS);
		writeOptionalElement(out, CURRENT_RATING_DATE,  measurement, Measurement.TAG_DATE);
		writeOptionalElement(out, CONFIDENE_CURRENT_RATING,  statusConfidenceToXmlValue(measurement.getData(Measurement.TAG_STATUS_CONFIDENCE)));
		writeOptionalElement(out, CURRENT_RATING_COMMENT, measurement, Measurement.TAG_COMMENT);
	
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
		writeStartElement(out, KEY_ATTRIBUTES);
		for (int index = 0; index < keas.length; ++index)
		{
			out.writeln("<" + KEY_ATTRIBUTE + " " + ID + "='" + keas[index].getId().toString() + "'>");
			writeElement(out, NAME, keas[index], KeyEcologicalAttribute.TAG_LABEL);
			writeElement(out, CATEGORY, keyEcologicalAttributeTypeToXmlValue(keas[index].getKeyEcologicalAttributeType()));
			writeEndElement(out, KEY_ATTRIBUTE);
		}
		
		writeEndElement(out, KEY_ATTRIBUTES);
	}

	private void writeTargets(UnicodeWriter out) throws Exception
	{
		Target[] targets = getProject().getTargetPool().getTargets();
		Arrays.sort(targets, new ObjectToStringSorter());
		writeStartElement(out, TARGETS);
		for (int index = 0; index < targets.length; ++index)
		{
			Target target = targets[index];
			out.write("<" + TARGET + " " + ID + "='" + target.getId().toString() + "' " + SEQUENCE + "='" + index + 1 + "'>");
			
			writeElement(out, TARGET_NAME, target, Target.TAG_LABEL);
			writeOptionalElement(out, TARGET_DESCRIPTION, target, Target.TAG_TEXT);
			writeOptionalElement(out, TARGET_DESCRIPTION_COMMENT, target, Target.TAG_COMMENT);
			writeOptionalElement(out, TARGET_VIABILITY_COMMENT, target, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			writeOptionalRankingCodeElement(out, TARGET_VIABILITY_RANK, target, Target.TAG_TARGET_STATUS);
			writeHabitatMappedCodes(out, target);
			writeStresses(out, target);
			writeThreatStressRatings(out, target);
			writeNestedTargets(out, target);
			writeSimpleTargetLinkRatings(out, target);
			writeStrategyThreatTargetAssociations(out, target);
		
			writeEndElement(out, TARGET);
		}
		writeEndElement(out, TARGETS);
	}

	
	
	private void writeStrategyThreatTargetAssociations(UnicodeWriter out, Target target) throws Exception
	{
		writeStartElement(out, STRATEGY_THREAT_TARGET_ASSOCIATIONS);
		ORefList factorLinkReferrers = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int refIndex = 0; refIndex < factorLinkReferrers.size(); ++refIndex)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(refIndex));
			if (factorLink.isThreatTargetLink())
				writeStrategyThreatTargetAssociations(out, target, factorLink.getUpstreamThreatRef());
		}
		
		writeEndElement(out, STRATEGY_THREAT_TARGET_ASSOCIATIONS);
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

	private void writeStrategyThreatTargetAssociation(UnicodeWriter out, ORef threatRef, ORef strategyRef) throws Exception
	{
		writeStartElement(out, STRATEGY_THREAT_TARGET_ASSOCIATION);
		
		writeElement(out, STRATEGY_ID, strategyRef.getObjectId().toString());
		writeElement(out, THREAT_ID, threatRef.getObjectId().toString());
		
		writeEndElement(out, STRATEGY_THREAT_TARGET_ASSOCIATION);
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
		
		writeCodeListElements(out, HABITAT_TAXONOMY_CODES, HABITAT_TAXONOMY_CODE, conProHabitatCodeList);
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
		writeStartElement(out, THREAT_TARGET_ASSOCIATIONS);
		for(FactorLink factorLink : targetLinks)
		{
			writeSimpleTargetLinkRatings(out, factorLink, target.getRef());
		}
		writeEndElement(out, THREAT_TARGET_ASSOCIATIONS);
	}
	
	private void writeSimpleTargetLinkRatings(UnicodeWriter out, FactorLink factorLink, ORef targetRef) throws Exception
	{
		ORef threatRef = factorLink.getUpstreamThreatRef();
		SimpleThreatRatingFramework simpleThreatFramework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = simpleThreatFramework.getBundle((FactorId)threatRef.getObjectId(), (FactorId)targetRef.getObjectId());
				
		int threatTargetRatingValue = simpleThreatFramework.getBundleValue(bundle).getNumericValue();
		
		writeStartElement(out, THREAT_TARGET_ASSOCIATION);
		writeElement(out, THREAT_ID, threatRef.getObjectId().toString());
		writeOptionalRatingCodeElement(out, THREAT_TO_TARGET_RANK, threatTargetRatingValue);
		writeOptionalRatingCodeElement(out, THREAT_SEVERITY, getSeverity(simpleThreatFramework, bundle));
		writeOptionalRatingCodeElement(out, THREAT_SCOPE, getScope(simpleThreatFramework, bundle));
		writeOptionalRatingCodeElement(out, THREAT_IRREVERSIBILITY, getIrreversibility(simpleThreatFramework, bundle));
		writeOptionalElement(out, THREAT_TARGET_COMMENT, factorLink, FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT);
		writeEndElement(out, THREAT_TARGET_ASSOCIATION);
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
		writeStartElement(out, NESTED_TARGETS);
		for (int refIndex = 0; refIndex < subTargetRefs.size(); ++refIndex)
		{
			SubTarget subTarget = SubTarget.find(getProject(), subTargetRefs.get(refIndex));
			out.writeln("<" + NESTED_TARGET+ " " + SEQUENCE + "='" + refIndex + "'>");
			writeElement(out, NAME, subTarget, SubTarget.TAG_LABEL);
			writeElement(out, COMMENT, subTarget, SubTarget.TAG_DETAIL);
			writeEndElement(out, NESTED_TARGET);
		}
		writeEndElement(out, NESTED_TARGETS);
	}

	private void writeThreatStressRatings(UnicodeWriter out, Target target) throws Exception
	{
		FactorLinkSet targetLinks = getThreatLinksWithThreatStressRatings(target);
		writeStartElement(out, STRESSES_THREATS);
		for(FactorLink factorLink : targetLinks)
		{
			writeThreatStressRatings(out, factorLink);
		}
		writeEndElement(out, STRESSES_THREATS);
	}

	private void writeThreatStressRatings(UnicodeWriter out, FactorLink factorLink) throws Exception
	{
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		for (int refIndex = 0; refIndex < threatStressRatingRefs.size(); ++refIndex)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingRefs.get(refIndex));
			writeStartElement(out, STRESSES_THREAT);
			writeOptionalRatingCodeElement(out, CONTRIBUTING_RANK, threatStressRating.getIrreversibilityCode());
			writeEndElement(out, STRESSES_THREAT);
		}
	}

	private void writeStresses(UnicodeWriter out, Target target) throws Exception
	{
		ORefList stressRefs = target.getStressRefs();
		writeStartElement(out, TARGET_STRESSES);
		for (int refIndex = 0; refIndex < stressRefs.size(); ++refIndex)
		{
			out.writeln("<" + TARGET_STRESS + " "+ SEQUENCE+ "='" + refIndex + "'>");
			Stress stress = Stress.find(getProject(), stressRefs.get(refIndex));
			writeElement(out, NAME, stress, Stress.TAG_LABEL);
			writeOptionalRatingCodeElement(out, STRESS_SEVERITY, stress.getData(Stress.TAG_SEVERITY));
			writeOptionalRatingCodeElement(out, STRESS_SCOPE, stress.getData(Stress.TAG_SCOPE));
			writeOptionalRatingCodeElement(out, STRESS_TO_TARGET_RANK, stress.getCalculatedStressRating());
			writeEndElement(out, TARGET_STRESS);
		}
		writeEndElement(out, TARGET_STRESSES);
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
			
			writeElement(out, PROJECT_SUMMARY_NAME, getProjectMetadata(), ProjectMetadata.TAG_PROJECT_NAME);
			
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
	
	private void writeOptionalRankingCodeElement(UnicodeWriter out, String elementName, BaseObject object, String tag) throws Exception
	{
		String code = object.getData(tag);
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
		return getXmlCode(rankingMap, code);
	}
	
	private String statusCodeToXmlValue(String code)
	{
		return getXmlCode(progressStatusMap, code);
	}
	
	private String getXmlCode(HashMap<String, String> map, String code)
	{
		String value = map.get(code);
		if (value == null)
			return "";
		
		return value.toString();
	}
	
	private void createMiradiToConproCodeMaps()
	{
		progressStatusMap = new HashMap<String, String>();
		progressStatusMap.put(ProgressReportStatusQuestion.PLANNED_CODE, CONPRO_STATUS_PLANNED_VALUE);
		progressStatusMap.put(ProgressReportStatusQuestion.ON_TRACK_CODE, CONPRO_STATUS_ON_TRACK_VALUE);
		
		rankingMap = new HashMap<String, String>();
		rankingMap.put("1", "Poor");
		rankingMap.put("2", "Fair"); 
		rankingMap.put("3", "Good");
		rankingMap.put("4", "Very Good"); 
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
	
	private HashMap<String, String> progressStatusMap;
	private HashMap<String, String> rankingMap;
	private static final String CONPRO_STATUS_PLANNED_VALUE = "Planned";
	private static final String CONPRO_STATUS_ON_TRACK_VALUE = "On Track";
}