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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Desire;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

abstract public class DesireImporter extends BaseObjectImporter
{
	public DesireImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef destinationRef) throws Exception
	{
		super.importFields(baseObjectNode, destinationRef);
		
		importRelevantIndicatorIds(baseObjectNode, destinationRef);
		importRelevantStrategyAndActivityIds(baseObjectNode, destinationRef);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(Desire.TAG_RELEVANT_INDICATOR_SET))
			return true;
		
		if (tag.equals(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		
		return super.isCustomImportField(tag);
	}
	
	private void importRelevantIndicatorIds(Node node, ORef destinationDesireRef) throws Exception
	{
		ORefList importedRelevantRefs = getImporter().extractRefs(node, getPoolName(), XmpzXmlConstants.RELEVANT_INDICATOR_IDS, IndicatorSchema.getObjectType(), XmpzXmlConstants.INDICATOR);
		Desire desire = Desire.findDesire(getProject(), destinationDesireRef);
		RelevancyOverrideSet set = desire.getCalculatedRelevantIndicatorOverrides(importedRelevantRefs);		
		getImporter().setData(destinationDesireRef, Desire.TAG_RELEVANT_INDICATOR_SET, set.toString());
	}

	private void importRelevantStrategyAndActivityIds(Node node, ORef destinationDesireRef) throws Exception
	{
		ORefList importedStrategyAndActivityRefs = new ORefList();
		importedStrategyAndActivityRefs.addAll(getImporter().extractRefs(node, getPoolName(), XmpzXmlConstants.RELEVANT_STRATEGY_IDS, StrategySchema.getObjectType(), XmpzXmlConstants.STRATEGY));
		importedStrategyAndActivityRefs.addAll(getImporter().extractRefs(node, getPoolName(), XmpzXmlConstants.RELEVANT_ACTIVITY_IDS, TaskSchema.getObjectType(), XmpzXmlConstants.ACTIVITY));
		
		Desire desire = Desire.findDesire(getProject(), destinationDesireRef);
		RelevancyOverrideSet set = desire.getCalculatedRelevantStrategyActivityOverrides(importedStrategyAndActivityRefs);
		getImporter().setData(destinationDesireRef, Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, set.toString());
	}	
}
