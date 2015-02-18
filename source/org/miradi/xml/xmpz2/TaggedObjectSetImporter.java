/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.xml.xmpz2.objectImporters.BaseObjectImporter;
import org.miradi.xml.xmpz2.objectImporters.DiagramFactorImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TaggedObjectSetImporter extends BaseObjectImporter
{
	public TaggedObjectSetImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new TaggedObjectSetSchema());
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
	
		importFactorRefs(baseObjectNode, refToUse, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS))
			return true;
		
		return super.isCustomImportField(tag);
	}
	
	private void importFactorRefs(Node node, ORef destinationRef, String tagTaggedObjectRefs) throws Exception
	{
		ORefList taggedFactorRefs = new ORefList();
		Node taggedFactorIdsNode = getImporter().getNamedChildNode(node, getXmpz2ElementName() + TAGGED_FACTOR_IDS);
		NodeList childNodes = getImporter().getNodes(taggedFactorIdsNode, new String[]{WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME, });
		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node factorIdNode = childNodes.item(index);
			ORef taggedFactorRef = DiagramFactorImporter.getWrappedRef(getImporter(), factorIdNode);
			taggedFactorRefs.add(taggedFactorRef);
		}
		
		getImporter().setData(destinationRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedFactorRefs);
	}
}
