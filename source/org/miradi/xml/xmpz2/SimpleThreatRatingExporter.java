/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.objecthelpers.BaseObjectByRefSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.ThreatRatingQuestion;

public class SimpleThreatRatingExporter implements Xmpz2XmlConstants
{
	public SimpleThreatRatingExporter(Xmpz2XmlWriter writerToUse)
	{
		writer = writerToUse;
	}

	public void writeThreatRatings() throws Exception
	{
		getWriter().writeStartPoolElement(getParentElementName());
		exportSimpleThreatRating();
		getWriter().writeEndPoolElement(getParentElementName());
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
				getWriter().writeStartElement(getParentElementName());
				
				ORef targetRef = target.getRef();
				exportTargetId(targetRef);
				exportThreatId(threatRef);
				exportThreatRating(targetRef, threatRef);
				exportSimpleRatingComment(threatRef, targetRef);				
				exportSimpleBaseThreatRatingDetails(threatRef, targetRef);
				
				getWriter().writeEndElement(getParentElementName());
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
		ThreatRatingBundle bundle = getSimpleThreatRatingFramework().getBundle(threatRef, targetRef);
		final String scopeCode = getSimpleThreatRatingFramework().getScopeChoiceItem(bundle).getCode();
		getWriter().writeElement(getParentElementName() + SCOPE, scopeCode);
		
		final String severityCode = getSimpleThreatRatingFramework().getSeverityChoiceItem(bundle).getCode();
		getWriter().writeElement(getParentElementName() + SEVERITY, severityCode);
		
		final String irreversibilityCode = getSimpleThreatRatingFramework().getIrreversibilityChoiceItem(bundle).getCode();
		getWriter().writeElement(getParentElementName() + IRREVERSIBILITY, irreversibilityCode);
	}

	private void exportThreatRating(ORef targetRef, ORef threatRef) throws Exception
	{
		ThreatTargetVirtualLinkHelper threatTargetVirtualLink = new ThreatTargetVirtualLinkHelper(getProject());
		int calculatedValue = threatTargetVirtualLink.calculateSimpleThreatRating(threatRef, targetRef);
		String threatRatingCode = AbstractThreatTargetTableModel.convertIntToString(calculatedValue);
		final ChoiceQuestion threatRatingQuestion = StaticQuestionManager.getQuestion(ThreatRatingQuestion.class);
		
		getWriter().writeNonOptionalCodeElement(getParentElementName(), SIMPLE_THREAT_TARGET_CALCULATED_RATING, threatRatingQuestion, threatRatingCode);
	}

	private void exportSimpleRatingComment(ORef threatRef, ORef targetRef) throws Exception
	{
		exportThreatRatingComment(threatRef, targetRef, ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP);
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
		exportId(getParentElementName() + THREAT, THREAT, threatRef);
	}

	private void exportTargetId(ORef targetRef) throws Exception
	{
		exportId(getParentElementName() + TARGET, BIODIVERSITY_TARGET, targetRef);
	}

	private void exportId(String parentElementName, String idElementName, ORef ref) throws Exception
	{
		getWriter().writeStartElement(parentElementName + ID);
		getWriter().writeStartElement(idElementName + ID);
		getWriter().writeXmlText(ref.getObjectId().toString());
		getWriter().writeEndElement(idElementName + ID);
		getWriter().writeEndElement(parentElementName + ID);
	}
	
	private SimpleThreatRatingFramework getSimpleThreatRatingFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	private String getParentElementName()
	{
		return SIMPLE_BASED_THREAT_RATING;
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
