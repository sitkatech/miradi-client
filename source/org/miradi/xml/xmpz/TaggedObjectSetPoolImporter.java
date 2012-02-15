/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.TaggedObjectSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TaggedObjectSetPoolImporter extends AbstractBaseObjectPoolImporter
{
	public TaggedObjectSetPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, TAGGED_OBJECT_SET_ELEMENT_NAME, TaggedObjectSet.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		importField(node, destinationRef, TaggedObjectSet.TAG_SHORT_LABEL);
		importField(node, destinationRef, TaggedObjectSet.TAG_COMMENTS);
		importFactorRefs(node, destinationRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS);
		
	}

	private void importFactorRefs(Node node, ORef destinationRef, String tagTaggedObjectRefs) throws Exception
	{
		ORefList taggedFactorRefs = new ORefList();
		Node taggedFactorIdsNode = getImporter().getNode(node, getPoolName() + TAGGED_FACTOR_IDS);
		NodeList childNodes = getImporter().getNodes(taggedFactorIdsNode, new String[]{WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME, });
		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node factorIdNode = childNodes.item(index);
			ORef taggedFactorRef = DiagramFactorPoolImporter.getWrappedRef(factorIdNode);
			taggedFactorRefs.add(taggedFactorRef);
		}
		
		getImporter().setData(destinationRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedFactorRefs);
	}
}
