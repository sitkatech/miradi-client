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

package org.miradi.xml.xmpz1;

import org.miradi.objects.WwfProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.Xmpz1XmlConstants;
import org.w3c.dom.Node;

public class WwfProjectDataImporter extends AbstractXmpzObjectImporter
{
	public WwfProjectDataImporter(Xmpz1XmlImporter importerToUse)
	{
		super(importerToUse, Xmpz1XmlConstants.WWF_PROJECT_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node wwfProjectDataNode = getImporter().getNamedChildNode(getImporter().getRootNode(), getPoolName());
		
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_MANAGING_OFFICES);
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_REGIONS);
		importCodeListField(wwfProjectDataNode, getWwfProjectDataRef(), WwfProjectData.TAG_ECOREGIONS);
	}
}
