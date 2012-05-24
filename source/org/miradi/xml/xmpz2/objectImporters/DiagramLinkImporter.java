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
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.FactorLinkSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class DiagramLinkImporter extends BaseObjectImporter
{
	public DiagramLinkImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchema)
	{
		super(importerToUse, baseObjectSchema);
	}
	
	@Override
	public void postCreateFix(ORef ref, Node node) throws Exception
	{
		Node fromDiagramFactorIdNode = getImporter().getNode(node, getPoolName() + FROM_DIAGRAM_FACTOR_ID);
		Node toDiagramFactorIdNode = getImporter().getNode(node, getPoolName() + TO_DIAGRAM_FACTOR_ID);
		
		DiagramFactorId fromId = new DiagramFactorId(fromDiagramFactorIdNode.getTextContent().trim());
		DiagramFactorId toId = new DiagramFactorId(toDiagramFactorIdNode.getTextContent().trim());
		
		ORef fromDiagramFactorRef = new ORef(DiagramFactorSchema.getObjectType(), fromId);
		ORef toDiagramFactorRef = new ORef(DiagramFactorSchema.getObjectType(), toId);
		DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), fromDiagramFactorRef);
		DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), toDiagramFactorRef);
		ORef factorLinkRef = new ORef(ObjectType.FAKE, BaseId.INVALID);
		if (!fromDiagramFactor.isGroupBoxFactor() && !toDiagramFactor.isGroupBoxFactor())
		{
			factorLinkRef = getProject().createObject(FactorLinkSchema.getObjectType());
			getProject().setObjectData(factorLinkRef, FactorLink.TAG_FROM_REF, fromDiagramFactor.getWrappedORef().toString());
			getProject().setObjectData(factorLinkRef, FactorLink.TAG_TO_REF, toDiagramFactor.getWrappedORef().toString());
		}
		
		getProject().setObjectData(ref, DiagramLink.TAG_WRAPPED_ID, factorLinkRef.getObjectId().toString());
		getProject().setObjectData(ref, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, fromId.toString());
		getProject().setObjectData(ref, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, toId.toString());
	}
}
