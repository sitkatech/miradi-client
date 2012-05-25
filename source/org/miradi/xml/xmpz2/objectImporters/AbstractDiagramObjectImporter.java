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

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramObject;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

abstract public class AbstractDiagramObjectImporter extends BaseObjectImporter
{
	public AbstractDiagramObjectImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef destinationRef) throws Exception
	{
		super.importFields(baseObjectNode, destinationRef);
		
		getImporter().importIds(baseObjectNode, destinationRef, getBaseObjectSchema(), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, XmpzXmlConstants.DIAGRAM_FACTOR, DiagramFactorSchema.getObjectType());
		getImporter().importIds(baseObjectNode, destinationRef, getBaseObjectSchema(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, XmpzXmlConstants.DIAGRAM_LINK, DiagramLinkSchema.getObjectType());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(DiagramObject.TAG_DIAGRAM_FACTOR_IDS))
			return true;
		
		if (tag.equals(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS))
			return true;
		
		return super.isCustomImportField(tag);
	}
}
