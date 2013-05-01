/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2;

import java.util.Collections;
import java.util.Vector;

import org.miradi.diagram.ThreatTargetChainWalker;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.objecthelpers.BaseObjectByRefSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.project.threatrating.ThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ThreatStressRatingChoiceQuestion;
import org.miradi.utils.ThreatStressRatingDetailsTableExporter;

public class StressBasedThreatRatingExporter implements Xmpz2XmlConstants
{
	public StressBasedThreatRatingExporter(Xmpz2XmlWriter writerToUse)
	{
		writer = writerToUse;
	}

	public void writeThreatRatings() throws Exception
	{
		getWriter().writeStartPoolElement(getParentElementName());
		exportStressBasedThreatRating();
		getWriter().writeEndPoolElement(getParentElementName());
	}

	private Vector<Target> getSortedTargetsInConceptualModelDiagrams()
	{
		Vector<Target> targets = new Vector<Target>(TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject()));
		Collections.sort(targets, new BaseObjectByRefSorter());

		return targets;
	}

	private void exportStressBasedThreatRating() throws Exception
	{
		Vector<Target> targets = getSortedTargetsInConceptualModelDiagrams();
		for(Target target : targets)
		{
			if (target.getStressRefs().hasRefs())
				exportStressBasedThreatRatingDetailsRow(target);
		}
	}
	
	private void exportStressBasedThreatRatingDetailsRow(Target target) throws Exception
	{
		ThreatTargetChainWalker chainWalker = new ThreatTargetChainWalker(getProject());
		ORefSet upstreamThreatsFromTarget = chainWalker.getUpstreamThreatRefsFromTarget(target);
		ORefList sortedThreatRefs = new ORefList(upstreamThreatsFromTarget);
		sortedThreatRefs.sort();
		for(int threatIndex = 0; threatIndex < sortedThreatRefs.size(); ++threatIndex)
		{
			Cause threat = Cause.find(getProject(), sortedThreatRefs.get(threatIndex));
			getWriter().writeStartElement(getParentElementName());
			ORef targetRef = target.getRef();
			ORef threatRef = threat.getRef();
			exportThreatId(threatRef);
			exportTargetId(targetRef);
			exportStressBasedRatingComment(threatRef, targetRef);
			exportStressBasedCalculatedThreatTargetRating(target.getRef(), threat.getRef());
			writeThreatStressRatings(target, threat);
			getWriter().writeEndElement(getParentElementName());
		}
	}

	private void writeThreatStressRatings(Target target, Cause threat) throws Exception
	{
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		getWriter().writeStartElement(getParentElementName() + THREAT_STRESS_RATING);
		ORefList stressRefs = target.getStressRefs();
		for(ORef stressRef : stressRefs)
		{
			getWriter().writeStartElement(THREAT_STRESS_RATING);
			Stress stress = Stress.find(getProject(), stressRef);
			ORef threatStressRatingRef = helper.findThreatStressRatingReferringToStress(threat.getRef(), target.getRef(), stress.getRef());
			exportThreatStressRating(target, stress, threat, ThreatStressRating.find(getProject(), threatStressRatingRef));
			getWriter().writeEndElement(THREAT_STRESS_RATING);
		}
		
		getWriter().writeEndElement(getParentElementName() + THREAT_STRESS_RATING);
	}
	
	private void exportThreatStressRating(Target target, Stress stress, Cause threat, ThreatStressRating threatStressRating) throws Exception
	{
		exportThreatId(THREAT_STRESS_RATING, threat.getRef());
		exportStressId(stress.getRef());

		ChoiceItem irreversibility = ThreatStressRatingDetailsTableExporter.getIrreversibility(getProject(), target.getRef(), threat.getRef(), stress);
		getWriter().writeElement(THREAT_STRESS_RATING + IRREVERSIBILITY, irreversibility.getCode());

		ChoiceItem contribution = ThreatStressRatingDetailsTableExporter.getContribution(getProject(), target.getRef(), threat.getRef(), stress);
		getWriter().writeElement(THREAT_STRESS_RATING + CONTRIBUTION, contribution.getCode());
		
		getWriter().writeElement(THREAT_STRESS_RATING, threatStressRating, ThreatStressRating.TAG_IS_ACTIVE);

		int calculatedThreatRating = threatStressRating.calculateThreatRating();
		String safeThreatRatingCode = ThreatRatingFramework.getSafeThreatRatingCode(calculatedThreatRating);
		getWriter().writeElement(THREAT_STRESS_RATING + CALCULATED_THREAT_STRESS_RATING, safeThreatRatingCode);
	}

	private void exportStressBasedCalculatedThreatTargetRating(ORef targetRef, ORef threatRef) throws Exception
	{
		ThreatTargetVirtualLinkHelper virtualLink = new ThreatTargetVirtualLinkHelper(getProject());
		int rawThreatStressRating = virtualLink.calculateStressBasedThreatRating(threatRef, targetRef);
		String safeThreatRatingCode = ThreatRatingFramework.getSafeThreatRatingCode(rawThreatStressRating);
		ChoiceQuestion question = getProject().getQuestion(ThreatStressRatingChoiceQuestion.class);
		exportStressBasedThreatRatingCode(CALCULATED_THREAT_TARGET_RATING, question.findChoiceByCode(safeThreatRatingCode));
	}

	private void exportStressBasedThreatRatingCode(String elementName, ChoiceItem rating) throws Exception
	{
		getWriter().writeCodeElement(getParentElementName(), elementName, rating);
	}

	private void exportStressBasedRatingComment(ORef threatRef, ORef targetRef) throws Exception
	{
		exportThreatRatingComment(threatRef, targetRef, ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
	}
	
	private void exportThreatRatingComment(ORef threatRef, ORef targetRef, String threatRatingCommentsMapTag) throws Exception
	{
		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
		String threatTargetRefsAsKey = threatRatingCommentsData.createKey(threatRef, targetRef);
		String threatRatingComments = threatRatingCommentsData.getThreatRatingCommentsMap(threatRatingCommentsMapTag).getUserString(threatTargetRefsAsKey);
		getWriter().writeElement(getParentElementName() + COMMENTS, threatRatingComments);
	}
	
	private void exportThreatId(ORef threatRef) throws Exception
	{
		exportThreatId(getParentElementName(), threatRef);
	}

	private void exportThreatId(final String parentElementName, ORef threatRef)	throws Exception
	{
		exportId(parentElementName + THREAT, THREAT, threatRef);
	}

	private void exportTargetId(ORef targetRef) throws Exception
	{
		exportId(getParentElementName() + TARGET, BIODIVERSITY_TARGET, targetRef);
	}
	
	private void exportStressId(ORef stressRef) throws Exception
	{
		exportId(THREAT_STRESS_RATING + STRESS, STRESS, stressRef);
	}

	private void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
	{
		getWriter().writeStartElement(parentElementName + ID);
		
		getWriter().writeStartElement(idElementName + ID);
		getWriter().writeXmlText(ref.getObjectId().toString());
		getWriter().writeEndElement(idElementName + ID);
		
		getWriter().writeEndElement(parentElementName + ID);
	}
	
	private String getParentElementName()
	{
		return STRESS_BASED_THREAT_RATING;
	}
	
	private Xmpz2XmlWriter getWriter()
	{
		return writer;
	}
	
	private Project getProject()
	{
		return getWriter().getProject();
	}

	private Xmpz2XmlWriter writer;
}
