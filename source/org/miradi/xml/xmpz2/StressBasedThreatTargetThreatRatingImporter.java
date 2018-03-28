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

import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatStressRatingEnsurer;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.AbstractThreatRatingData;
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
		Node threatRatingPoolNode = getImporter().getNamedChildNode(getImporter().getRootNode(), poolName);
		if (threatRatingPoolNode == null)
			return;
		
		NodeList threatRatingNodes = getImporter().getNodes(threatRatingPoolNode, new String[]{getParentElementName(), });
		for (int index = 0; index < threatRatingNodes.getLength(); ++index)
		{
			Node threatRatingNode = threatRatingNodes.item(index);
			importStressBasedThreatRatings(threatRatingNode);
		}		
	}

	private void importStressBasedThreatRatings(Node threatRatingNode) throws Exception
	{
		importStressBasedThreatRating(threatRatingNode);
		importThreatRatingsComment(threatRatingNode);
	}
	
	private void importStressBasedThreatRating(Node stressBasedThreatRatingNode) throws Exception
	{
		ORef threatRef = getThreatRef(stressBasedThreatRatingNode);
		final ORef targetRef = getTargetRef(stressBasedThreatRatingNode);
		Node stressBasedThreatRatingThreatRatingNode = getImporter().getNamedChildNode(stressBasedThreatRatingNode, STRESS_BASED_THREAT_RATING + THREAT_STRESS_RATING);
		NodeList threatStressRatingNodes = getImporter().getNodes(stressBasedThreatRatingThreatRatingNode, new String[]{THREAT_STRESS_RATING, });
		for(int index = 0; index < threatStressRatingNodes.getLength(); ++index)
		{
			Node threatStressRatingNode = threatStressRatingNodes.item(index);
			importThreatStressRating(threatStressRatingNode, threatRef, targetRef);
		}
	}

	private void importThreatStressRating(Node threatStressRatingNode, ORef threatRef, ORef targetRef)	throws Exception
	{
		ORef stressRef = getStressRef(threatStressRatingNode);
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		ORef threatStressRatingRef = helper.findThreatStressRatingReferringToStress(threatRef, targetRef, stressRef);
		getImporter().importCodeFieldWithoutElementNameSubstitute(threatStressRatingNode, THREAT_STRESS_RATING + CONTRIBUTION, threatStressRatingRef, ThreatStressRating.TAG_CONTRIBUTION, new StressContributionQuestion());
		getImporter().importCodeFieldWithoutElementNameSubstitute(threatStressRatingNode, THREAT_STRESS_RATING + IRREVERSIBILITY, threatStressRatingRef, ThreatStressRating.TAG_IRREVERSIBILITY, new StressIrreversibilityQuestion());
		importIsActive(threatStressRatingNode, threatStressRatingRef);
	}
	
	private void importIsActive(Node threatStressRatingNode, ORef threatStressRatingRef)	throws Exception
	{
		Node isActiveNode = getImporter().getNamedChildNode(threatStressRatingNode, THREAT_STRESS_RATING + IS_ACTIVE);
		String isActive = BooleanData.BOOLEAN_FALSE;
		if (isActiveNode != null && getImporter().isTrue(isActiveNode.getTextContent()))
			isActive = BooleanData.BOOLEAN_TRUE;;
		
		getImporter().setData(threatStressRatingRef, ThreatStressRating.TAG_IS_ACTIVE, isActive);
	}

	private ORef getStressRef(Node threatStressRatingNode)	throws Exception
	{
		Node stressIdNode = getImporter().getNamedChildNode(threatStressRatingNode, THREAT_STRESS_RATING + STRESS_ID);
		ORef stressRef = getImporter().getNodeAsRef(stressIdNode,  STRESS_ID, StressSchema.getObjectType());
		return stressRef;
	}

	@Override
	protected AbstractThreatRatingData findOrCreateThreatRatingData(ORef threatRef, ORef targetRef) throws Exception
	{
		return AbstractThreatRatingData.findOrCreateThreatRatingData(getProject(), threatRef, targetRef, ObjectType.THREAT_STRESS_RATING_DATA);
	}

	@Override
	protected String getParentElementName()
	{
		return STRESS_BASED_THREAT_RATING;
	}
}
