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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.schemas.MiradiShareTaxonomySchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class TaxonomyImporter extends BaseObjectImporter
{
	public TaxonomyImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new MiradiShareTaxonomySchema());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE))
			return true;
		
		return false;
	}
	
	@Override
	public ORef createBaseObject(final BaseObjectImporter importer,	Node baseObjectNode) throws Exception
	{
		ORef newTaxonomyRef = getProject().createObject(MiradiShareTaxonomySchema.getObjectType());
		String taxonomyCode = getImporter().getAttributeValue(baseObjectNode, TAXONOMY_CODE);
		getImporter().setData(newTaxonomyRef, MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE, taxonomyCode);
		
		return newTaxonomyRef;
	}
}
