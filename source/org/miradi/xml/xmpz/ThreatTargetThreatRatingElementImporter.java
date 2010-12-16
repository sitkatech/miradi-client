/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ThreatTargetThreatRatingElementImporter extends AbstractXmpzObjectImporter
{
	public ThreatTargetThreatRatingElementImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.THREAT_RATING);
	}

	@Override
	public void importElement() throws Exception
	{
		Node threatRatingPoolNode = getImporter().getNode(getImporter().getRootNode(), getPoolName() + WcsXmlConstants.POOL_ELEMENT_TAG);
		NodeList threatRatingNodes = getImporter().getNodes(threatRatingPoolNode, new String[]{THREAT_RATING, });
		for (int index = 0; index < threatRatingNodes.getLength(); ++index)
		{
			Node threatRatingNode = threatRatingNodes.item(index);
			importThreatRatings(threatRatingNode);
		}		
	}

	private void importThreatRatings(Node threatRatingNode) throws Exception
	{
		importThreatRatingsComment(threatRatingNode);
		Node threatRatingRatings = getImporter().getNode(threatRatingNode, THREAT_RATING + RATINGS);
		if (isSimpleThreatRatingNode(threatRatingNode))
			importSimpleThreatRating(threatRatingNode, threatRatingRatings);
		else
			importStressBasedThreatRating(threatRatingNode, threatRatingRatings);	
	}
	
	private void importThreatRatingsComment(Node threatRatingNode) throws Exception
	{
		ORef targetRef = getTargetRef(threatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);
		
		Node commentsNode = getImporter().getNode(threatRatingNode, THREAT_RATING + COMMENTS);
		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
		if (commentsNode != null)
		{
			String comment = commentsNode.getTextContent();
			StringStringMap commentsMap = getThreatRatingCommentsMap(threatRatingNode, threatRatingCommentsData);
			String threatTargetKey = ThreatRatingCommentsData.createKey(threatRef, targetRef);
			
			commentsMap.put(threatTargetKey, comment);
			String commentsMapTag = getCommentsMapTag(threatRatingNode);
			getImporter().setData(threatRatingCommentsData, commentsMapTag, commentsMap.toString());
		}
	}

	private ORef getThreatRef(Node threatRatingNode) throws Exception
	{
		Node threatIdNode = getImporter().getNode(threatRatingNode, getPoolName() + THREAT + ID);
		ORef threatRef = getImporter().getNodeAsRef(threatIdNode, THREAT + ID, Cause.getObjectType());
		return threatRef;
	}

	private ORef getTargetRef(Node threatRatingNode) throws Exception
	{
		Node targetIdNode = getImporter().getNode(threatRatingNode, getPoolName() + TARGET + ID);
		ORef targetRef = getImporter().getNodeAsRef(targetIdNode,  BIODIVERSITY_TARGET + ID, Target.getObjectType());
		return targetRef;
	}

	private StringStringMap getThreatRatingCommentsMap(Node threatRatingNode, ThreatRatingCommentsData threatRatingCommentsData) throws Exception
	{
		if (isSimpleThreatRatingNode(threatRatingNode))
			return threatRatingCommentsData.getSimpleThreatRatingCommentsMap();
		
		return threatRatingCommentsData.getStressBasedThreatRatingCommentsMap();
	}

	private String getCommentsMapTag(Node threatRatingNode) throws Exception
	{
		if (isSimpleThreatRatingNode(threatRatingNode))
			return ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP;
		
		return ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP;
	}
	
	private boolean isSimpleThreatRatingNode(Node threatRatingNode) throws Exception
	{
		Node threatRatingRatings = getImporter().getNode(threatRatingNode, THREAT_RATING + RATINGS);
		Node simpleThreatRatingNode = getImporter().getNode(threatRatingRatings, SIMPLE_BASED_THREAT_RATING);
		if (simpleThreatRatingNode != null)
			return true;

		return false;
	}

	private void importSimpleThreatRating(Node threatRatingNode, Node threatRatingRatings) throws Exception
	{
		ORef threatRef = getThreatRef(threatRatingNode);
		ORef targetRef = getTargetRef(threatRatingNode);
		Node simpleThreatRatingNode = getImporter().getNode(threatRatingRatings, SIMPLE_BASED_THREAT_RATING);
		Node scopeNode = getImporter().getNode(simpleThreatRatingNode, SIMPLE_BASED_THREAT_RATING + SCOPE);
		if (scopeNode != null)
			getProject().getSimpleThreatRatingFramework().setScope(threatRef, targetRef, extractNodeTextContentAsInt(scopeNode));
		
		Node severityNode = getImporter().getNode(simpleThreatRatingNode, SIMPLE_BASED_THREAT_RATING + SEVERITY);
		if (severityNode != null)
			getProject().getSimpleThreatRatingFramework().setSeverity(threatRef, targetRef, extractNodeTextContentAsInt(severityNode));
		
		Node irreversibilityNode = getImporter().getNode(simpleThreatRatingNode, SIMPLE_BASED_THREAT_RATING + IRREVERSIBILITY);
		if (irreversibilityNode != null)
			getProject().getSimpleThreatRatingFramework().setIrreversibility(threatRef, targetRef, extractNodeTextContentAsInt(irreversibilityNode));
	}

	private void importStressBasedThreatRating(Node threatRatingNode, Node threatRatingRatings) throws Exception
	{
		Node stressBasedThreatRatingNode = getImporter().getNode(threatRatingRatings, STRESS_BASED_THREAT_RATING);
		ORef stressRef = getStressRef(stressBasedThreatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		ORef threatStressRatingRef = helper.findThreatStressRatingReferringToStress(threatRef, getTargetRef(threatRatingNode), stressRef);
		importCodeField(stressBasedThreatRatingNode, STRESS_BASED_THREAT_RATING, threatStressRatingRef, ThreatStressRating.TAG_CONTRIBUTION, new StressContributionQuestion());
		importCodeField(stressBasedThreatRatingNode, STRESS_BASED_THREAT_RATING, threatStressRatingRef, ThreatStressRating.TAG_IRREVERSIBILITY, new StressIrreversibilityQuestion());
		importIsActive(stressBasedThreatRatingNode, threatStressRatingRef);
	}
	
	private void importIsActive(Node stressBasedThreatRatingNode, ORef threatStressRatingRef)	throws Exception
	{
		Node isActiveNode = getImporter().getNode(stressBasedThreatRatingNode, STRESS_BASED_THREAT_RATING + IS_ACTIVE);
		String isActive = BooleanData.BOOLEAN_FALSE;
		if (isActiveNode != null && getImporter().isTrue(isActiveNode.getTextContent()))
			isActive = BooleanData.BOOLEAN_TRUE;;
		
		getImporter().setData(threatStressRatingRef, ThreatStressRating.TAG_IS_ACTIVE, isActive);
	}

	private ORef getStressRef(Node stressBasedThreatRatingNode)	throws Exception
	{
		Node stressIdNode = getImporter().getNode(stressBasedThreatRatingNode, STRESS_BASED_THREAT_RATING + STRESS + ID);
		ORef stressRef = getImporter().getNodeAsRef(stressIdNode,  STRESS+ ID, Stress.getObjectType());
		return stressRef;
	}
}
