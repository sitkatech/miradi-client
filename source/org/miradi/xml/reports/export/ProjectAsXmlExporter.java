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
package org.miradi.xml.reports.export;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.diagram.ThreatTargetChainObject;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ThreatTargetVirtualLink;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.Target;
import org.miradi.objects.ValueOption;
import org.miradi.project.PlanningTreeXmlExporter;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.MiradiMultiCalendar;
import org.miradi.xml.XmlExporter;


public class ProjectAsXmlExporter extends XmlExporter
{
	public ProjectAsXmlExporter(Project projectToUse) throws Exception
	{
		super(projectToUse);
	}

	@Override
	protected void exportProject(UnicodeWriter out) throws Exception
	{
		out.writeln("<MiradiProject>");
		out.writeln("<FileName>" + XmlUtilities.getXmlEncoded(getProject().getFilename()) + "</FileName>");
		out.writeln("<ExportDate>" + new MiradiMultiCalendar().toIsoDateString() + "</ExportDate>");
		
		writeRating(getProject(), out, getProject().getProjectSummaryThreatRating(), "OverallProjectThreatRating");
		exportPools(out);
		new PlanningTreeXmlExporter(getProject()).toXmlPlanningTreeTables(out);
		out.writeln("</MiradiProject>");
	}

	private void exportPools(UnicodeWriter out) throws IOException, Exception
	{
		out.writeln("<ObjectPools>");
		HashMap allPools = getProject().getObjectManager().getAllPools();
		Iterator iter = allPools.keySet().iterator();
		while(iter.hasNext())
		{
			EAMObjectPool pool = (EAMObjectPool)allPools.get(iter.next());
			exportPoolObjects(out, pool);
		}
		out.writeln("</ObjectPools>");
	}

	private void exportPoolObjects(UnicodeWriter out, EAMObjectPool pool) throws IOException, Exception
	{
		out.writeln("<Pool objectType='" + pool.getObjectType() + "'>");
		ORefList ids = pool.getSortedRefList();
		for(int i = 0; i < ids.size(); ++i)
		{
			BaseObject foundObject = pool.findObject(ids.get(i));
			writeBaseObjectAsXml(out, foundObject);
		}
		out.writeln("</Pool>");
	}

	private void writeBaseObjectAsXml(UnicodeWriter out, BaseObject foundObject) throws IOException, Exception
	{
		out.write("<" + foundObject.getTypeName() + " ref='");
		foundObject.getRef().toXml(out);
		out.writeln("'>");
		Set fieldTags = getBaseObjectFieldTags(foundObject);
		Iterator iter = fieldTags.iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			ObjectData data = foundObject.getField(tag);
			data.toXml(out);
		}
		writeNonFieldXml(foundObject, out);
		out.writeln("</" + foundObject.getTypeName() + ">");
	}

	private Set<String> getBaseObjectFieldTags(BaseObject foundObject)
	{
		Set<String> fieldTagsToIncludeInXml = getFieldTagsToIncludeInXml(foundObject);
		if (Objective.is(foundObject.getRef()))
		{
			fieldTagsToIncludeInXml.remove(Objective.TAG_RELEVANT_INDICATOR_SET);
			fieldTagsToIncludeInXml.remove(Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
			fieldTagsToIncludeInXml.add(Objective.PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
			fieldTagsToIncludeInXml.add(Objective.PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS);
		}
		
		return fieldTagsToIncludeInXml;
	}
	
	public Set<String> getFieldTagsToIncludeInXml(BaseObject foundObject)
	{
		HashSet<String> tagsToInclude = new HashSet();
		tagsToInclude.addAll(foundObject.getStoredFieldTags());
		
		return tagsToInclude;
	}

	public void writeNonFieldXml(BaseObject foundObject, UnicodeWriter out) throws Exception
	{
		if (Target.is(foundObject.getType()))
			writeSimpleThreatRatingsForTarget((Target) foundObject, out);
	
		if (Indicator.is(foundObject.getType()))
			writeIndicatorNonFieldXml((Indicator) foundObject, out);
		
		if (KeyEcologicalAttribute.is(foundObject.getType()))
			writeKeyEcologicalAttributeNonFieldXml((KeyEcologicalAttribute) foundObject, out);
	
		if (Measurement.is(foundObject.getType()))
			writeMeasurementNonFieldXml((Measurement) foundObject, out);
	}
	
	public void writeMeasurementNonFieldXml(Measurement measurement, UnicodeWriter out) throws Exception
	{
		String statusRatingCode = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(measurement.getStatus()).getCode();
		writeRatingCodes(out, statusRatingCode, measurement.getSummary(), "StatusRatingValues");
	}
	
	public void writeKeyEcologicalAttributeNonFieldXml(KeyEcologicalAttribute keyEcologicalAttribute, UnicodeWriter out) throws Exception
	{
		ChoiceItem statusChoice = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(keyEcologicalAttribute.computeTNCViability());
		if (statusChoice == null)
			return;
		
		out.write("<" + KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS + ">");
		statusChoice.toXml(out);
		out.writeln("</" + KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS + ">");
	}
	
	public void writeIndicatorNonFieldXml(Indicator indicator, UnicodeWriter out) throws Exception
	{
		out.writeln("<CurrentStatus>");
		ChoiceItem choice = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(indicator.getCurrentStatus());
		choice.toXml(out);
		out.writeln("</CurrentStatus>");
		
		out.writeln("<LatestProgressReport>");
		ChoiceItem latestProgressChoice = getProject().getQuestion(ProgressReportStatusQuestion.class).findChoiceByCode(indicator.getLatestProgressReportDate());
		latestProgressChoice.toXml(out);
		out.writeln("</LatestProgressReport>");
		
		//TODO: Don't export these values in a hard-coded way
		String futureStatusRatingCode = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(indicator.getFutureStatusRating()).getCode();
		writeRatingCodes(out, futureStatusRatingCode, indicator.getFutureStatusSummary(), "FutureStatusRatingValues");
	}
	
	public static void writeRatingCodes(UnicodeWriter out, String statusRatingCode, String statusRatingValue, String xmlTag) throws IOException
	{
		out.writeln("<" + xmlTag + ">");
		writeOutValue(out, statusRatingCode, statusRatingValue, StatusQuestion.POOR);
		writeOutValue(out, statusRatingCode, statusRatingValue, StatusQuestion.FAIR);
		writeOutValue(out, statusRatingCode, statusRatingValue, StatusQuestion.GOOD);
		writeOutValue(out, statusRatingCode, statusRatingValue, StatusQuestion.VERY_GOOD);
		out.write("</" + xmlTag + ">");
	}

	private static void writeOutValue(UnicodeWriter out, String statusRatingCode, String statusRatingValue, String ratingCode) throws IOException
	{
		out.write("<Value code='" + ratingCode+ "'>");
		if (statusRatingCode.equals(ratingCode))
			out.write(XmlUtilities.getXmlEncoded(statusRatingValue));

		out.write("</Value>");
	}
	
	private void writeSimpleThreatRatingsForTarget(Target target, UnicodeWriter out) throws Exception
	{
		out.writeln("<ThreatRatings>");
		
		ORef targetRef = target.getRef();
		ThreatTargetChainObject threatTargetChainObject = new ThreatTargetChainObject(getProject());
		ORefSet upstreamThreatRefs = threatTargetChainObject.getUpstreamThreatRefsFromTarget(target);
		for(ORef threatRef : upstreamThreatRefs)
		{
			SimpleThreatRatingFramework simpleThreatFramework = getProject().getSimpleThreatRatingFramework();
			ThreatRatingBundle bundle = simpleThreatFramework.getBundle((FactorId)threatRef.getObjectId(), (FactorId)targetRef.getObjectId());

			RatingCriterion scopeCriterion = simpleThreatFramework.getScopeCriterion();
			BaseId scopeId = bundle.getValueId(scopeCriterion.getId());
			ValueOption scope = (ValueOption)getProject().findObject(ValueOption.getObjectType(), scopeId);

			RatingCriterion severityCriterion = simpleThreatFramework.getSeverityCriterion();
			BaseId severityId = bundle.getValueId(severityCriterion.getId());
			ValueOption severity = (ValueOption)getProject().findObject(ValueOption.getObjectType(), severityId);

			RatingCriterion irreversibilityCriterion = simpleThreatFramework.getIrreversibilityCriterion();
			BaseId irreversibilityId = bundle.getValueId(irreversibilityCriterion.getId());
			ValueOption irreversibility = (ValueOption)getProject().findObject(ValueOption.getObjectType(), irreversibilityId);

			out.writeln("<ThreatRatingSimple ThreatId = " + threatRef.getObjectId() + ">");
			writeCriterionAndValue(out, scopeCriterion, scope);
			writeCriterionAndValue(out, severityCriterion, severity);
			writeCriterionAndValue(out, irreversibilityCriterion, irreversibility);
			out.writeln("</ThreatRatingSimple>");

			writeOutTargetThreatRatingXML(threatRef, targetRef, out, simpleThreatFramework, bundle);

			Cause cause = Cause.find(getProject(), threatRef);

			//NOTE, this test exist for corrupted projects
			if (target == null || cause == null)
				return;

			writeRating(getProject(), out, getThreatRating(out, simpleThreatFramework, cause), "ThreatRating");
			writeRating(getProject(), out, getTargetRating(out, simpleThreatFramework, target), "TargetRating");

			out.write("<TargetName>");
			out.write(XmlUtilities.getXmlEncoded(target.toString()));
			out.writeln("</TargetName>");

			out.write("<ThreatName>");
			out.write(XmlUtilities.getXmlEncoded(cause.toString()));
			out.writeln("</ThreatName>");
		}
		
		out.writeln("</ThreatRatings>");
	}
	
	public static void writeRating(Project project, UnicodeWriter out, int threatRatingValue, String xmlTagName) throws IOException
	{
		ChoiceItem targetRatingChoice = project.getQuestion(ThreatRatingQuestion.class).findChoiceByCode(Integer.toString(threatRatingValue));
		if (targetRatingChoice == null)
			return;
		
		out.write("<" + xmlTagName + ">");
		targetRatingChoice.toXml(out);
		out.writeln("</" + xmlTagName + ">");
	}
	
	private int getThreatRating(UnicodeWriter out, SimpleThreatRatingFramework simpleThreatFramework, Cause cause) throws Exception
	{
		if (getProject().isStressBaseMode())
			return getStressBasedRating(cause);
		
		return simpleThreatFramework.getThreatThreatRatingValue(cause.getId()).getNumericValue();
	}

	private int getTargetRating(UnicodeWriter out, SimpleThreatRatingFramework simpleThreatFramework, Target target) throws Exception
	{
		if (getProject().isStressBaseMode())
			return getStressBasedRating(target);
		
		return simpleThreatFramework.getTargetThreatRatingValue(target.getId()).getNumericValue();
	}
	
	private int getStressBasedRating(Factor factor) throws Exception
	{
		return getProject().getStressBasedThreatRatingFramework().get2PrimeSummaryRatingValue(factor);
	}
	
	private void writeOutTargetThreatRatingXML(ORef threatRef, ORef targetRef, UnicodeWriter out, SimpleThreatRatingFramework simpleThreatFramework, ThreatRatingBundle bundle) throws IOException, Exception
	{
		ThreatTargetVirtualLink threatTargetVirtualLink = new ThreatTargetVirtualLink(getProject());
		int targetThreatRatingValue = 0;
		if (getProject().isStressBaseMode())
			targetThreatRatingValue = threatTargetVirtualLink.calculateThreatRatingBundleValue(threatRef, targetRef);
		else
			targetThreatRatingValue = simpleThreatFramework.getBundleValue(bundle).getNumericValue();

		ChoiceItem targetThreatRatingChoice = getProject().getQuestion(ThreatRatingQuestion.class).findChoiceByCode(Integer.toString(targetThreatRatingValue));
		if (targetThreatRatingChoice == null)
			return;
		
		out.write("<TargetThreatRating>");
		targetThreatRatingChoice.toXml(out);
		out.writeln("</TargetThreatRating>");
	}
	
	private void writeCriterionAndValue(UnicodeWriter out, RatingCriterion criterion, ValueOption value) throws Exception
	{
		out.write("<" + criterion.getLabel() + ">");
		out.write(Integer.toString(value.getNumericValue()));
		out.write("</" + criterion.getLabel() + ">");
		out.writeln();
	}

	public static void main(String[] commandLineArguments) throws Exception
	{	
		Project newProject = getOpenedProject(commandLineArguments);
		try
		{
			new ProjectAsXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			System.out.println("Export report xml complete");
		}
		finally
		{
			newProject.close();
		}
	}
}