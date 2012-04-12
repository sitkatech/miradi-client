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

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class TaggedObjectSetExporter extends BaseObjectExporter
{
	public TaggedObjectSetExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, TaggedObjectSetSchema.getObjectType());
	}
	
	@Override
	protected void writeFields(final BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);
		
		TaggedObjectSet taggedObjectSet = (TaggedObjectSet) baseObject;
		writeFactorIds(baseObjectSchema.getObjectName(), XmpzXmlConstants.TAGGED_FACTOR_IDS, taggedObjectSet.getTaggedObjectRefs());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS))
			return true;
		
		return super.doesFieldRequireSpecialHandling(tag);
	}
	
	private void writeFactorIds(String parentElementName, String childElementName, ORefList refList) throws Exception
	{
		final String elementName = getWriter().appendChildNameToParentName(parentElementName, childElementName);
		getWriter().writeStartElement(elementName);
		for (int index = 0; index < refList.size(); ++index)
		{
			Factor factor = Factor.findFactor(getProject(), refList.get(index));
			writeWrappedFactorIdElement(factor);
		}
		
		getWriter().writeEndElement(elementName);
	}
	
	private void writeWrappedFactorIdElement(Factor wrappedFactor) throws Exception
	{
		getWriter().writeStartElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
		
		String factorTypeName = getFactorTypeName(wrappedFactor);
		getWriter().writeElement(factorTypeName, ID_ELEMENT_NAME, wrappedFactor.getFactorId().toString());
		
		getWriter().writeEndElement(WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
	}
}
