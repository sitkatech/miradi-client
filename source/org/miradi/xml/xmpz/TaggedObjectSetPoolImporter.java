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
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TaggedObjectSet;
import org.w3c.dom.Node;

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
		importRefs(node, TAGGED_FACTOR_IDS, destinationRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, DiagramFactor.getObjectType(), DIAGRAM_FACTOR);
	}
}
