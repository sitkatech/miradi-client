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

import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ThreatStressRatingEnsurer;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.schemas.StressSchema;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StressBasedThreatTargetThreatRatingImporter extends AbstractThreatRatingImporter
{
	public StressBasedThreatTargetThreatRatingImporter(Xmpz2XmlImporter xmpz2XmlImporter)
	{
		super(xmpz2XmlImporter);
	}
	
	public void importFields() throws Exception
	{
		beginUsingCommandsToSetData();
		try
		{
			ThreatStressRatingEnsurer ensurer = new ThreatStressRatingEnsurer(getProject());
			ensurer.createOrDeleteThreatStressRatingsAsNeeded();
		}
		finally
		{
			endUsingCommandsToSetData();
		}

		String poolName = Xmpz2XmlWriter.createPoolElementName(getParentElementName());
		Node threatRatingPoolNode = getImporter().getNode(getImporter().getRootNode(), poolName);
		if (threatRatingPoolNode == null)
			return;
		
		NodeList threatRatingNodes = getImporter().getNodes(threatRatingPoolNode, new String[]{getParentElementName(), });
		for (int index = 0; index < threatRatingNodes.getLength(); ++index)
		{
			Node threatRatingNode = threatRatingNodes.item(index);
			importThreatRatings(threatRatingNode);
		}		
	}

	private void endUsingCommandsToSetData()
	{
		getProject().endCommandSideEffectMode();
	}

	private void beginUsingCommandsToSetData()
	{
		getProject().beginCommandSideEffectMode();
	}

	private void importThreatRatings(Node threatRatingNode) throws Exception
	{
		importThreatRatingsComment(threatRatingNode);
		importStressBasedThreatRating(threatRatingNode);	
	}
	
	@Override
	protected CodeToUserStringMap getThreatRatingCommentsMap(Node threatRatingNode, ThreatRatingCommentsData threatRatingCommentsData) throws Exception
	{
		return threatRatingCommentsData.getStressBasedThreatRatingCommentsMap();
	}

	@Override
	protected String getCommentsMapTag(Node threatRatingNode) throws Exception
	{
		return ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP;
	}
	
	private void importStressBasedThreatRating(Node threatRatingNode) throws Exception
	{
		ORef stressRef = getStressRef(threatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		ORef threatStressRatingRef = helper.findThreatStressRatingReferringToStress(threatRef, getTargetRef(threatRatingNode), stressRef);
		getImporter().importCodeField(threatRatingNode, getParentElementName(), threatStressRatingRef, ThreatStressRating.TAG_CONTRIBUTION, new StressContributionQuestion());
		getImporter().importCodeField(threatRatingNode, getParentElementName(), threatStressRatingRef, ThreatStressRating.TAG_IRREVERSIBILITY, new StressIrreversibilityQuestion());
		importIsActive(threatRatingNode, threatStressRatingRef);
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
		Node stressIdNode = getImporter().getNode(stressBasedThreatRatingNode, getParentElementName() + STRESS + ID);
		ORef stressRef = getImporter().getNodeAsRef(stressIdNode,  STRESS + ID, StressSchema.getObjectType());
		return stressRef;
	}
	
	@Override
	protected String getParentElementName()
	{
		return STRESS_BASED_THREAT_RATING;
	}
}
