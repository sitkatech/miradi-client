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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractThreatRatingData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimpleThreatRatingImporter extends	AbstractThreatRatingImporter
{
	public SimpleThreatRatingImporter(Xmpz2XmlImporter xmpz2XmlImporter)
	{
		super(xmpz2XmlImporter);
	}
	
	public void importFields() throws Exception
	{
		beginUsingCommandsToSetData();
		try
		{
			String poolName = Xmpz2XmlWriter.createPoolElementName(getParentElementName());
			Node threatRatingPoolNode = getImporter().getNamedChildNode(getImporter().getRootNode(), poolName);
			NodeList threatRatingNodes = getImporter().getNodes(threatRatingPoolNode, new String[]{getParentElementName(), });
			for (int index = 0; index < threatRatingNodes.getLength(); ++index)
			{
				Node threatRatingNode = threatRatingNodes.item(index);
				importSimpleThreatRating(threatRatingNode);
			}
		}
		finally
		{
			endUsingCommandsToSetData();
		}
	}

	private void importSimpleThreatRating(Node threatRatingNode) throws Exception
	{
		importThreatRatingsData(threatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);
		ORef targetRef = getTargetRef(threatRatingNode);
		Node scopeNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + SCOPE);
		if (scopeNode != null)
			getProject().getSimpleThreatRatingFramework().setScope(threatRef, targetRef, extractNodeTextContentAsInt(scopeNode));
		
		Node severityNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + SEVERITY);
		if (severityNode != null)
			getProject().getSimpleThreatRatingFramework().setSeverity(threatRef, targetRef, extractNodeTextContentAsInt(severityNode));
		
		Node irreversibilityNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + IRREVERSIBILITY);
		if (irreversibilityNode != null)
			getProject().getSimpleThreatRatingFramework().setIrreversibility(threatRef, targetRef, extractNodeTextContentAsInt(irreversibilityNode));
	}

	@Override
	protected AbstractThreatRatingData findOrCreateThreatRatingData(ORef threatRef, ORef targetRef) throws Exception
	{
		return AbstractThreatRatingData.findOrCreateThreatRatingData(getProject(), threatRef, targetRef, ObjectType.THREAT_SIMPLE_RATING_DATA);
	}

	@Override
	protected String getParentElementName()
	{
		return SIMPLE_BASED_THREAT_RATING;
	}
}
