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

import org.miradi.objects.BaseObject;
import org.miradi.objects.IucnRedlistSpecies;

public class IucnRedlistSpeciesExporter extends BaseObjectExporter
{
	public IucnRedlistSpeciesExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		super(writerToUse);
	}
	
	@Override
	public void writeBaseObjectDataSchemaElement(BaseObject baseObject)	throws Exception
	{
		getWriter().writeStartElementWithAttribute(IUCN_REDLIST_SPECIES, ID, baseObject.getId().toString());
		getWriter().writeElement(IUCN_REDLIST_SPECIES, IucnRedlistSpecies.TAG_LABEL, baseObject.getLabel());
		getWriter().writeEndElement(IUCN_REDLIST_SPECIES);
	}

	@Override
	public String getExporterContainerName(int objectType)
	{
		return IUCN_REDLIST_SPECIES;
	}
}
