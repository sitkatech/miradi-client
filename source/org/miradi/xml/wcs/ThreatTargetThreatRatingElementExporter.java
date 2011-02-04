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

package org.miradi.xml.wcs;

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
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.project.threatrating.ThreatRatingFramework;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StressRatingChoiceQuestion;
import org.miradi.questions.ThreatStressRatingChoiceQuestion;
import org.miradi.utils.ThreatStressRatingDetailsTableExporter;

public class ThreatTargetThreatRatingElementExporter extends AbstractXmlExporter
{
	public ThreatTargetThreatRatingElementExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse);
	}

	@Override
	public void exportXml() throws Exception
	{
		getWcsXmlExporter().writeStartPoolElement(THREAT_RATING);
		exportStressBasedThreatRating();
		exportSimpleThreatRating();
		getWcsXmlExporter().writeEndPoolElement(THREAT_RATING);
	}

	private void exportSimpleThreatRating() throws Exception
	{
		Vector<Target> targets = getSortedTargetsInConceptualModelDiagrams();
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			ORefList sortedUpStreamThreats = new ORefList(upstreamThreats);
			sortedUpStreamThreats.sort();
			for (int index = 0; index < sortedUpStreamThreats.size(); ++index)
			{
				ORef threatRef = sortedUpStreamThreats.get(index);
				getWcsXmlExporter().writeStartElement(THREAT_RATING);
				
				ORef targetRef = target.getRef();
				exportTargetId(targetRef);
				exportThreatId(threatRef);
				exportSimpleRatingComment(threatRef, targetRef);				
				exportSimpleBaseThreatRatingDetails(threatRef, targetRef);
				
				getWcsXmlExporter().writeEndElement(THREAT_RATING);
			}		
		}
	}

	private Vector<Target> getSortedTargetsInConceptualModelDiagrams()
	{
		Vector<Target> targets = new Vector<Target>(TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject()));
		Collections.sort(targets, new BaseObjectByRefSorter());

		return targets;
	}

	private void exportSimpleBaseThreatRatingDetails(ORef threatRef, ORef targetRef) throws Exception
	{
		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
		getWcsXmlExporter().writeStartElement(SIMPLE_BASED_THREAT_RATING);
		
		ThreatRatingBundle bundle = getSimpleThreatRatingFramework().getBundle(threatRef, targetRef);
		
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SCOPE, getSimpleThreatRatingFramework().getScopeChoiceItem(bundle).getCode());
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + SEVERITY, getSimpleThreatRatingFramework().getSeverityChoiceItem(bundle).getCode());
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY, getSimpleThreatRatingFramework().getIrreversibilityChoiceItem(bundle).getCode());
		
		getWcsXmlExporter().writeEndElement(SIMPLE_BASED_THREAT_RATING);
		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
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
		ORefList stressRefs = target.getStressRefs();
		for (int index = 0; index < stressRefs.size(); ++index)
		{
			Stress stress = Stress.find(getProject(), stressRefs.get(index));
			ThreatTargetChainWalker chainWalker = new ThreatTargetChainWalker(getProject());
			ORefSet upstreamThreatsFromTarget = chainWalker.getUpstreamThreatRefsFromTarget(target);
			ORefList sortedThreatRefs = new ORefList(upstreamThreatsFromTarget);
			sortedThreatRefs.sort();
			for(int threatIndex = 0; threatIndex < sortedThreatRefs.size(); ++threatIndex)
			{
				Cause threat = Cause.find(getProject(), sortedThreatRefs.get(threatIndex));
				getWcsXmlExporter().writeStartElement(THREAT_RATING);
				ORef targetRef = target.getRef();
				ORef threatRef = threat.getRef();
				exportTargetId(targetRef);
				exportThreatId(threatRef);
				exportStressBasedRatingComment(threatRef, targetRef);
				exportStressBasedThreatRatingDetails(target, stress, threat);
				getWcsXmlExporter().writeEndElement(THREAT_RATING);
			}
		}
	}

	private void exportStressBasedThreatRatingDetails(Target target, Stress stress, Cause threat) throws Exception
	{
		getWcsXmlExporter().writeStartElement(THREAT_RATING + RATINGS);
		getWcsXmlExporter().writeStartElement(STRESS_BASED_THREAT_RATING);
		
		exportId(STRESS_BASED_THREAT_RATING + STRESS, STRESS, stress.getRef());
		ChoiceItem irreversibility = ThreatStressRatingDetailsTableExporter.getIrreversibility(getProject(), target.getRef(), threat.getRef(), stress);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + IRREVERSIBILITY, irreversibility.getCode());
		
		ChoiceItem contribution = ThreatStressRatingDetailsTableExporter.getContribution(getProject(), target.getRef(), threat.getRef(), stress);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), STRESS_BASED_THREAT_RATING + CONTRIBUTION, contribution.getCode());
		
		ThreatStressRating threatStressRating = ThreatStressRatingDetailsTableExporter.findThreatStressRating(getProject(), target.getRef(), threat.getRef(), stress);
		if (threatStressRating != null)
			getWcsXmlExporter().writeOptionalElement(STRESS_BASED_THREAT_RATING, IS_ACTIVE, threatStressRating, ThreatStressRating.TAG_IS_ACTIVE);
		
		exportStressBasedStressRating(stress.getCalculatedStressRating());
		exportStressBasedThreatStressRating(target.getRef(), threat.getRef());
		
		getWcsXmlExporter().writeEndElement(STRESS_BASED_THREAT_RATING);
		getWcsXmlExporter().writeEndElement(THREAT_RATING + RATINGS);
	}
	
	private void exportStressBasedThreatStressRating(ORef targetRef, ORef threatRef) throws Exception
	{
		ThreatTargetVirtualLinkHelper virtualLink = new ThreatTargetVirtualLinkHelper(getProject());
		int rawThreatStressRating = virtualLink.calculateStressBasedThreatRating(threatRef, targetRef);
		String safeThreatRatingCode = ThreatRatingFramework.getSafeThreatRatingCode(rawThreatStressRating);
		ChoiceQuestion question = getProject().getQuestion(ThreatStressRatingChoiceQuestion.class);
		exportStressBasedThreatRatingCode(THREAT_STRESS_RATING, question.findChoiceByCode(safeThreatRatingCode));
	}

	private void exportStressBasedStressRating(String stressRating) throws Exception
	{
		ChoiceQuestion question = getProject().getQuestion(StressRatingChoiceQuestion.class);
		ChoiceItem stressRatingChoiceItem = question.findChoiceByCode(stressRating);
		exportStressBasedThreatRatingCode(STRESS_RATING, stressRatingChoiceItem);
	}
	
	private void exportStressBasedThreatRatingCode(String elementName, ChoiceItem rating) throws Exception
	{
		getWcsXmlExporter().writeStartElement(STRESS_BASED_THREAT_RATING + elementName);
		getWcsXmlExporter().writeXmlEncodedData(getWcsXmlExporter().getWriter(), rating.getCode());
		getWcsXmlExporter().writeEndElement(STRESS_BASED_THREAT_RATING + elementName);
	}

	private void exportSimpleRatingComment(ORef threatRef, ORef targetRef) throws Exception
	{
		exportThreatRatingComment(threatRef, targetRef, ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
	}

	private void exportStressBasedRatingComment(ORef threatRef, ORef targetRef) throws Exception
	{
		exportThreatRatingComment(threatRef, targetRef, ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP);
	}
	
	private void exportThreatRatingComment(ORef threatRef, ORef targetRef, String threatRatingCommentsMapTag) throws Exception
	{
		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
		String threatTargetRefsAsKey = threatRatingCommentsData.createKey(threatRef, targetRef);
		String threatRatingComment = threatRatingCommentsData.getThreatRatingCommentsMap(threatRatingCommentsMapTag).get(threatTargetRefsAsKey);
		getWcsXmlExporter().writeOptionalElement(getWcsXmlExporter().getWriter(), THREAT_RATING + COMMENTS, threatRatingComment);
	}
	
	private void exportThreatId(ORef threatRef) throws Exception
	{
		exportId(THREAT_RATING + THREAT, THREAT, threatRef);
	}

	private void exportTargetId(ORef targetRef) throws Exception
	{
		exportId(THREAT_RATING + TARGET, BIODIVERSITY_TARGET, targetRef);
	}

	private void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
	{
		getWcsXmlExporter().writeStartElement(parentElementName + ID);
		
		getWcsXmlExporter().writeStartElement(idElementName + ID);
		getWcsXmlExporter().writeXmlEncodedData(getWcsXmlExporter().getWriter(), ref.getObjectId().toString());
		getWcsXmlExporter().writeEndElement(idElementName + ID);
		
		getWcsXmlExporter().writeEndElement(parentElementName + ID);
	}
	
	private SimpleThreatRatingFramework getSimpleThreatRatingFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
}
