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
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.questions.ThreatRatingEvidenceConfidenceQuestion;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.xml.xmpz2.objectImporters.AbstractXmpz2ObjectImporter;
import org.w3c.dom.Node;

abstract public class AbstractThreatRatingImporter extends AbstractXmpz2ObjectImporter
{
	public AbstractThreatRatingImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse);
	}

	protected void endUsingCommandsToSetData()
	{
		getProject().endCommandSideEffectMode();
	}

	protected void beginUsingCommandsToSetData()
	{
		getProject().beginCommandSideEffectMode();
	}

	protected abstract AbstractThreatRatingData findOrCreateThreatRatingData(ORef threatRef, ORef targetRef) throws Exception;

	protected void importThreatRatingsData(Node threatRatingNode) throws Exception
	{
		ORef targetRef = getTargetRef(threatRatingNode);
		ORef threatRef = getThreatRef(threatRatingNode);

		AbstractThreatRatingData threatRatingData = findOrCreateThreatRatingData(threatRef, targetRef);

		Node commentsNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + COMMENTS);
		if (commentsNode != null)
		{
			String comments = getImporter().getFormattedNodeContent(commentsNode);
			threatRatingData.setData(AbstractThreatRatingData.TAG_COMMENTS, comments);
		}

		Node evidenceConfidenceNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + EVIDENCE_CONFIDENCE);
		if (evidenceConfidenceNode != null)
		{
			getImporter().importCodeField(threatRatingNode, getParentElementName(), threatRatingData.getRef(), AbstractThreatRatingData.TAG_EVIDENCE_CONFIDENCE, new ThreatRatingEvidenceConfidenceQuestion());
		}

		Node evidenceNotesNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + EVIDENCE_NOTES);
		if (evidenceNotesNode != null)
		{
			String evidenceNotes = getImporter().getFormattedNodeContent(evidenceNotesNode);
			threatRatingData.setData(AbstractThreatRatingData.TAG_EVIDENCE_NOTES, evidenceNotes);
		}
	}
	
	protected ORef getThreatRef(Node threatRatingNode) throws Exception
	{
		Node threatIdNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + THREAT + ID);
		ORef threatRef = getImporter().getNodeAsRef(threatIdNode, THREAT + ID, CauseSchema.getObjectType());
		return threatRef;
	}

	protected ORef getTargetRef(Node threatRatingNode) throws Exception
	{
		Node targetIdNode = getImporter().getNamedChildNode(threatRatingNode, getParentElementName() + TARGET + ID);
		ORef targetRef = getImporter().getNodeAsRef(targetIdNode,  BIODIVERSITY_TARGET + ID, TargetSchema.getObjectType());
		return targetRef;
	}
	
	protected int extractNodeTextContentAsInt(Node node) throws Exception
	{
		return Integer.parseInt(node.getTextContent());
	}

	abstract protected String getParentElementName();
}
