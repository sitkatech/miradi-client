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

import org.miradi.objects.RareProjectData;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class RareProjetDataImporter extends AbstractXmpzObjectImporter
{
	public RareProjetDataImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.RARE_PROJECT_DATA);
	}

	@Override
	public void importElement() throws Exception
	{
		Node rareProjectDataNode = getImporter().getNode(getImporter().getRootNode(), getPoolName());
		
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_COHORT);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_CAMPAIGN_SLOGAN);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		importField(rareProjectDataNode, getRareProjectDataRef(), RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
	}
}
