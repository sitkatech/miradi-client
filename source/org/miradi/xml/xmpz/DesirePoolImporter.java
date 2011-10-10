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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Desire;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

abstract public class DesirePoolImporter extends AbstractBaseObjectPoolImporter
{
	public DesirePoolImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int objectTypeToImportToUse)
	{
		super(importerToUse, poolNameToUse, objectTypeToImportToUse);
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);
	
		importField(node, destinationRef, Desire.TAG_SHORT_LABEL);
		importField(node, destinationRef, Desire.TAG_FULL_TEXT);
		importField(node, destinationRef, Desire.TAG_COMMENTS);
		importProgressPercentRefs(node, destinationRef);

		importRelevantIndicatorIds(node, destinationRef);
		importRelevantStrategyAndActivityIds(node, destinationRef);
	}

	private void importRelevantIndicatorIds(Node node, ORef destinationDesireRef) throws Exception
	{
		ORefList importedRelevantRefs = extractRefs(node, XmpzXmlConstants.RELEVANT_INDICATOR_IDS, Indicator.getObjectType(), XmpzXmlConstants.INDICATOR + XmpzXmlConstants.ID);
		Desire desire = Desire.findDesire(getProject(), destinationDesireRef);
		RelevancyOverrideSet set = desire.getCalculatedRelevantIndicatorOverrides(importedRelevantRefs);		
		getImporter().setData(destinationDesireRef, Desire.TAG_RELEVANT_INDICATOR_SET, set.toString());
	}

	private void importRelevantStrategyAndActivityIds(Node node, ORef destinationDesireRef) throws Exception
	{
		ORefList importedStrategyAndActivityRefs = new ORefList();
		importedStrategyAndActivityRefs.addAll(extractRefs(node, XmpzXmlConstants.RELEVANT_STRATEGY_IDS, Strategy.getObjectType(), XmpzXmlConstants.STRATEGY + XmpzXmlConstants.ID));
		importedStrategyAndActivityRefs.addAll(extractRefs(node, XmpzXmlConstants.RELEVANT_ACTIVITY_IDS, Task.getObjectType(), XmpzXmlConstants.ACTIVITY + XmpzXmlConstants.ID));
		
		Desire desire = Desire.findDesire(getProject(), destinationDesireRef);
		RelevancyOverrideSet set = desire.getCalculatedRelevantStrategyActivityOverrides(importedStrategyAndActivityRefs);
		getImporter().setData(destinationDesireRef, Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, set.toString());
	}	
}
