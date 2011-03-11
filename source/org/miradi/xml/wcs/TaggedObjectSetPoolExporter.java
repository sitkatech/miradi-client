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

package org.miradi.xml.wcs;

import org.martus.util.UnicodeWriter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaggedObjectSet;

public class TaggedObjectSetPoolExporter extends BaseObjectPoolExporter
{
	public TaggedObjectSetPoolExporter(XmpzXmlExporter wcsXmlExporterToUse)
	{
		super(wcsXmlExporterToUse, TAGGED_OBJECT_SET_ELEMENT_NAME, TaggedObjectSet.getObjectType());
	}
	
	@Override
	protected void exportFields(UnicodeWriter writer, BaseObject baseObject) throws Exception
	{
		super.exportFields(writer, baseObject);
		
		TaggedObjectSet taggedObjectSet = (TaggedObjectSet) baseObject;
		writeOptionalElementWithSameTag(taggedObjectSet, TaggedObjectSet.TAG_SHORT_LABEL);
		writeOptionalElementWithSameTag(taggedObjectSet, TaggedObjectSet.TAG_COMMENTS);
		writeFactorIds(XmpzXmlConstants.TAGGED_FACTOR_IDS, taggedObjectSet.getTaggedObjectRefs());
	}
}
