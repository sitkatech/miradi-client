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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2GroupedConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DiagramLinkImporter extends BaseObjectImporter
{
	public DiagramLinkImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new DiagramLinkSchema());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(DiagramLink.TAG_WRAPPED_ID))
			return true;
		
		return super.isCustomImportField(tag);
	}
	
	@Override
	public void postCreateFix(ORef ref, Node node) throws Exception
	{
		DiagramFactor fromDiagramFactor = getDiagramFactorForLinkEnd(node, FROM_DIAGRAM_FACTOR_ID);
		DiagramFactor toDiagramFactor = getDiagramFactorForLinkEnd(node, TO_DIAGRAM_FACTOR_ID);
		
		ORef factorLinkRef = new ORef(ObjectType.FAKE, BaseId.INVALID);
		if (!fromDiagramFactor.isGroupBoxFactor() && !toDiagramFactor.isGroupBoxFactor())
		{
			factorLinkRef = getProject().createObject(FactorLinkSchema.getObjectType());
			getProject().setObjectData(factorLinkRef, FactorLink.TAG_FROM_REF, fromDiagramFactor.getWrappedORef().toString());
			getProject().setObjectData(factorLinkRef, FactorLink.TAG_TO_REF, toDiagramFactor.getWrappedORef().toString());
		}
		
		getProject().setObjectData(ref, DiagramLink.TAG_WRAPPED_ID, factorLinkRef.getObjectId().toString());
		getProject().setObjectData(ref, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, fromDiagramFactor.getId().toString());
		getProject().setObjectData(ref, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, toDiagramFactor.getId().toString());
	}

	private DiagramFactor getDiagramFactorForLinkEnd(Node node,	final String diagramFactorIdElementName) throws Exception
	{
		Node diagramFactorIdNode = getImporter().getNode(node, getXmpz2ElementName() + diagramFactorIdElementName);
		Node linkableNode = getImporter().getNode(diagramFactorIdNode, LINKABLE_FACTOR_ID);
		Node factorNodeId = getFactorNodeId(linkableNode.getChildNodes());
		DiagramFactorId diagramFactorId = new DiagramFactorId(factorNodeId.getTextContent().trim());
		ORef diagramFactorRef = new ORef(DiagramFactorSchema.getObjectType(), diagramFactorId);
		
		return DiagramFactor.find(getProject(), diagramFactorRef);
	}

	private Node getFactorNodeId(NodeList childNodes) throws Exception
	{
		//FIXME medium - Instead of looping nodes, use expath to find our node, Example getXpath.evaluate(*ID).
		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node node = childNodes.item(index);
			if (!node.getNodeName().endsWith(ID))
				continue;
			
			if (isFactorNode(node))
				return node;
		}
		
		throw new Exception("Factor node Id not found");
	}

	private boolean isFactorNode(Node node)
	{
		String[] linkableFactorNames = Xmpz2GroupedConstants.getLinkableFactorNames();
		for(String linkableFactorName : linkableFactorNames)
		{
			if (node.getNodeName().endsWith(linkableFactorName + ID))
				return true;
		}
		
		return false;
	}
}
