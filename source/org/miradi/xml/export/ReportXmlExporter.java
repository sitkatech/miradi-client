/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.export;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.Target;
import org.miradi.objects.ValueOption;
import org.miradi.project.PlanningTreeXmlExporter;
import org.miradi.project.Project;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.Translation;


public class ReportXmlExporter
{
	public ReportXmlExporter(Project projectToUse) throws Exception
	{
		project = projectToUse;
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
	
	private void exportProject(UnicodeWriter out) throws IOException, Exception
	{
		out.writeln("<MiradiProject>");
		out.writeln("<FileName>" + XmlUtilities.getXmlEncoded(getProject().getFilename()) + "</FileName>");
		out.writeln("<ExportDate>" + new MultiCalendar().toIsoDateString() + "</ExportDate>");
		
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
			fieldTagsToIncludeInXml.remove(Objective.TAG_RELEVANT_STRATEGY_SET);
			fieldTagsToIncludeInXml.add(Objective.PSEUDO_RELEVANT_INDICATOR_REFS);
			fieldTagsToIncludeInXml.add(Objective.PSEUDO_RELEVANT_STRATEGY_REFS);
		}
		
		return fieldTagsToIncludeInXml;
	}
	
	public Set<String> getFieldTagsToIncludeInXml(BaseObject foundObject)
	{
		HashSet<String> tagsToInclude = new HashSet();
		String[] tags = foundObject.getFieldTags();
		for (int tagIndex = 0; tagIndex < tags.length; ++tagIndex)
		{
			if(!foundObject.isPseudoField(tags[tagIndex]))
				tagsToInclude.add(tags[tagIndex]);	
		}
	
		return tagsToInclude;
	}

	public void writeNonFieldXml(BaseObject foundObject, UnicodeWriter out) throws Exception
	{
		writeFactorLinkNonFieldXml(foundObject, out);
		writeIndicatorNonFieldXml(foundObject, out);
		writeKeyEcologicalAttributeNonFieldXml(foundObject, out);
		writeMeasurementNonFieldXml(foundObject, out);
	}
	
	public void writeMeasurementNonFieldXml(BaseObject object, UnicodeWriter out) throws Exception
	{
		if (!Measurement.is(object.getType()))
			return;
		
		Measurement measurement = (Measurement) object;
		String statusRatingCode = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(measurement.getStatus()).getCode();
		writeRatingCodes(out, statusRatingCode, measurement.getSummary(), "StatusRatingValues");
	}
	
	public void writeKeyEcologicalAttributeNonFieldXml(BaseObject object, UnicodeWriter out) throws Exception
	{
		if (!KeyEcologicalAttribute.is(object.getType()))
			return;
		
		KeyEcologicalAttribute keyEcologicalAttribute = (KeyEcologicalAttribute) object;
			
		ChoiceItem statusChoice = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(keyEcologicalAttribute.computeTNCViability());
		if (statusChoice == null)
			return;
		
		out.write("<" + KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS + ">");
		statusChoice.toXml(out);
		out.writeln("</" + KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS + ">");
	}
	
	public void writeIndicatorNonFieldXml(BaseObject object, UnicodeWriter out) throws Exception
	{
		if (!Indicator.is(object.getType()))
			return;
		
		Indicator indicator = (Indicator) object;
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
			out.write(statusRatingValue);

		out.write("</Value>");
	}
	
	public void writeFactorLinkNonFieldXml(BaseObject object, UnicodeWriter out) throws Exception
	{
		if (!FactorLink.is(object.getType()))
			return;
		
		FactorLink factorLink = (FactorLink) object;
		if(!factorLink.isThreatTargetLink())
			return;
		
		ORef targetRef = factorLink.getDownstreamTargetRef();
		ORef threatRef = factorLink.getUpstreamThreatRef();
		SimpleThreatRatingFramework simpleThreatFramework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = simpleThreatFramework.getBundle((FactorId)threatRef.getObjectId(), (FactorId)targetRef.getObjectId());

		RatingCriterion scopeCriterion = simpleThreatFramework.getScopeCriterion();
		RatingCriterion severityCriterion = simpleThreatFramework.getSeverityCriterion();
		RatingCriterion irreversibilityCriterion = simpleThreatFramework.getIrreversibilityCriterion();
		BaseId scopeId = bundle.getValueId(scopeCriterion.getId());
		BaseId severityId = bundle.getValueId(severityCriterion.getId());
		BaseId irreversibilityId = bundle.getValueId(irreversibilityCriterion.getId());
		ValueOption scope = (ValueOption)getProject().findObject(ValueOption.getObjectType(), scopeId);
		ValueOption severity = (ValueOption)getProject().findObject(ValueOption.getObjectType(), severityId);
		ValueOption irreversibility = (ValueOption)getProject().findObject(ValueOption.getObjectType(), irreversibilityId);

		out.writeln("<ThreatRatingSimple>");
		writeCriterionAndValue(out, scopeCriterion, scope);
		writeCriterionAndValue(out, severityCriterion, severity);
		writeCriterionAndValue(out, irreversibilityCriterion, irreversibility);
		out.writeln("</ThreatRatingSimple>");
		
		writeOutTargetThreatRatingXML(factorLink, out, simpleThreatFramework, bundle);
		
		Target target = Target.find(getProject(), targetRef);
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
	
	private void writeOutTargetThreatRatingXML(FactorLink factorLink, UnicodeWriter out, SimpleThreatRatingFramework simpleThreatFramework, ThreatRatingBundle bundle) throws IOException, Exception
	{
		
		int targetThreatRatingValue = 0;
		if (getProject().isStressBaseMode())
			targetThreatRatingValue = factorLink.calculateThreatRatingBundleValue();
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
		if (incorrectArgumentCount(commandLineArguments))
			throw new RuntimeException("Incorrect number of arguments " + commandLineArguments.length);

		Project newProject = new Project();
		File projectDirectory = getProjectDirectory(commandLineArguments);
		if(!ProjectServer.isExistingProject(projectDirectory))
			throw new RuntimeException("Project does not exist:" + projectDirectory);

		newProject.createOrOpen(projectDirectory);
		try
		{
			Translation.loadFieldLabels();
			new ReportXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			System.out.println("Export complete");
		}
		finally
		{
			newProject.close();
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
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project; 
}
