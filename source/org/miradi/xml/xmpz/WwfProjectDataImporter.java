/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.WwfProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class WwfProjectDataImporter extends AbstractXmpzObjectImporter
{
	public WwfProjectDataImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.WWF_PROJECT_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node wwfProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_MANAGING_OFFICES);
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_REGIONS);
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_ECOREGIONS);
	}
}
