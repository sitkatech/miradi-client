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

package org.miradi.xml.xmpz2.objectImporters;

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Indicator;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IndicatorImporter extends BaseObjectWithLeaderResourceFieldImporter
{
	public IndicatorImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new IndicatorSchema());
	}

	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importThresholds(baseObjectNode, refToUse);
		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), Indicator.TAG_METHOD_IDS, METHOD, TaskSchema.getObjectType());
		importRelevantStrategyAndActivityIds(baseObjectNode, refToUse);
	}

	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(Indicator.TAG_THRESHOLDS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_THRESHOLD_DETAILS_MAP))
			return true;
		
		if (tag.equals(Indicator.TAG_METHOD_IDS))
			return true;

		if (tag.equals(Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		return false;
	}
	
	private void importThresholds(Node indicatorNode, ORef destinationRef) throws Exception
	{
		NodeList thresholdNodes = getImporter().getNodes(indicatorNode, new String[]{getXmpz2ElementName() + THRESHOLDS, THRESHOLD});
		CodeToUserStringMap thresholdValuesMap = new CodeToUserStringMap();
		CodeToUserStringMap thresholdDetailsMap = new CodeToUserStringMap();
		for (int index = 0; index < thresholdNodes.getLength(); ++index)
		{
			Node thrsholdNode = thresholdNodes.item(index);
			Node statusCodeNode = getImporter().getNamedChildNode(thrsholdNode, STATUS_CODE);
			if (statusCodeNode != null)
			{
				String statusCode = statusCodeNode.getTextContent();
				Node thresholdValueNode = getImporter().getNamedChildNode(thrsholdNode, THRESHOLD_VALUE);
				final String thresholdValue = getImporter().getSafeNodeContent(thresholdValueNode);
				thresholdValuesMap.putUserString(statusCode, getImporter().escapeDueToParserUnescaping(thresholdValue));
				
				Node thresholdDetailsNode = getImporter().getNamedChildNode(thrsholdNode, THRESHOLD_DETAILS);
				final String thresholdDetails = getImporter().getSafeNodeContent(thresholdDetailsNode);
				thresholdDetailsMap.putUserString(statusCode, getImporter().escapeDueToParserUnescaping(thresholdDetails));
			}			
		}
		
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLDS_MAP, thresholdValuesMap.toJsonString());
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLD_DETAILS_MAP, thresholdDetailsMap.toJsonString());
	}

	private void importRelevantStrategyAndActivityIds(Node node, ORef destinationRef) throws Exception
	{
		ORefList importedStrategyAndActivityRefs = new ORefList();
		importedStrategyAndActivityRefs.addAll(getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_STRATEGY_IDS, StrategySchema.OBJECT_NAME));
		importedStrategyAndActivityRefs.addAll(getImporter().extractRefs(node, getXmpz2ElementName(), RELEVANT_ACTIVITY_IDS, ACTIVITY));

		Indicator indicator = Indicator.find(getProject(), destinationRef);
		RelevancyOverrideSet set = indicator.getCalculatedRelevantStrategyActivityOverrides(importedStrategyAndActivityRefs);

		getImporter().setData(destinationRef, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, set.toString());
	}
}
