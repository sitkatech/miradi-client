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

import org.miradi.objects.WcsProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

public class WcsProjectDataImporter extends AbstractXmpzObjectImporter
{
	public WcsProjectDataImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, WcsXmlConstants.WCS_PROJECT_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node wcsProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_SWOT_COMPLETED);
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_SWOT_URL);
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_STEP_COMPLETED);
		importField(wcsProjectDataNode, getWcsProjectDataRef(), WcsProjectData.TAG_STEP_URL);
	}
}
