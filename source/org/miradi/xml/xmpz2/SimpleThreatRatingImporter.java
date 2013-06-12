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

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ThreatRatingCommentsData;
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
		String poolName = Xmpz2XmlWriter.createPoolElementName(getParentElementName());
		Node threatRatingPoolNode = getImporter().getNode(getImporter().getRootNode(), poolName);
		NodeList threatRatingNodes = getImporter().getNodes(threatRatingPoolNode, new String[]{getParentElementName(), });
		for (int index = 0; index < threatRatingNodes.getLength(); ++index)
		{
			Node threatRatingNode = threatRatingNodes.item(index);
			importSimpleThreatRating(threatRatingNode);
		}		
	}

	private void importSimpleThreatRating(Node threatRatingNode) throws Exception
	{
		importThreatRatingsComment(threatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);
		ORef targetRef = getTargetRef(threatRatingNode);
		Node scopeNode = getImporter().getNode(threatRatingNode, getParentElementName() + SCOPE);
		if (scopeNode != null)
			getProject().getSimpleThreatRatingFramework().setScope(threatRef, targetRef, extractNodeTextContentAsInt(scopeNode));
		
		Node severityNode = getImporter().getNode(threatRatingNode, getParentElementName() + SEVERITY);
		if (severityNode != null)
			getProject().getSimpleThreatRatingFramework().setSeverity(threatRef, targetRef, extractNodeTextContentAsInt(severityNode));
		
		Node irreversibilityNode = getImporter().getNode(threatRatingNode, getParentElementName() + IRREVERSIBILITY);
		if (irreversibilityNode != null)
			getProject().getSimpleThreatRatingFramework().setIrreversibility(threatRef, targetRef, extractNodeTextContentAsInt(irreversibilityNode));
	}

	@Override
	protected CodeToUserStringMap getThreatRatingCommentsMap(Node threatRatingNode, ThreatRatingCommentsData threatRatingCommentsData) throws Exception
	{
		return threatRatingCommentsData.getSimpleThreatRatingCommentsMap();
	}

	@Override
	protected String getCommentsMapTag(Node threatRatingNode) throws Exception
	{
		return ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP;
	}
	
	@Override
	protected String getParentElementName()
	{
		return SIMPLE_BASED_THREAT_RATING;
	}
}
