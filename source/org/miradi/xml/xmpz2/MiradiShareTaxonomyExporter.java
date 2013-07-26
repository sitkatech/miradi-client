/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.BaseObject;
import org.miradi.schemas.MiradiShareTaxonomySchema;

public class MiradiShareTaxonomyExporter extends BaseObjectExporter
{
	public MiradiShareTaxonomyExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, MiradiShareTaxonomySchema.getObjectType());
	}
	
	@Override
	protected void writeStartElement(BaseObject baseObject) throws Exception
	{
		String taxonomyCode = baseObject.getData(MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE);
		getWriter().writeObjectStartElementWithAttribute(baseObject, TAXONOMY_CODE, taxonomyCode);
	}
	
	@Override
	protected boolean shouldOmitField(String tag)
	{
		if (tag.equals(MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE))
			return true;
		
		return super.shouldOmitField(tag);
	}
}
