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

import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IndicatorImporter extends BaseObjectImporter
{
	public IndicatorImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}

	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importThresholds(baseObjectNode, refToUse);
		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), Indicator.TAG_METHOD_IDS, TaskSchema.getObjectType(), METHOD);
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
		
		return false;
	}
	
	private void importThresholds(Node indicatorNode, ORef destinationRef) throws Exception
	{
		NodeList thresholdNodes = getImporter().getNodes(indicatorNode, new String[]{getPoolName() + THRESHOLDS, THRESHOLD});
		CodeToUserStringMap thresholdValues = new CodeToUserStringMap();
		CodeToUserStringMap thresholdDetails = new CodeToUserStringMap();
		for (int index = 0; index < thresholdNodes.getLength(); ++index)
		{
			Node thrsholdNode = thresholdNodes.item(index);
			Node statusCodeNode = getImporter().getNode(thrsholdNode, STATUS_CODE);
			if (statusCodeNode != null)
			{
				String statusCode = statusCodeNode.getTextContent();
				Node thresholdValueNode = getImporter().getNode(thrsholdNode, THRESHOLD_VALUE);
				thresholdValues.putUserString(statusCode, getImporter().getSafeNodeContent(thresholdValueNode));
				
				Node thresholdDetailsNode = getImporter().getNode(thrsholdNode, THRESHOLD_DETAILS);
				thresholdDetails.putUserString(statusCode, getImporter().getSafeNodeContent(thresholdDetailsNode));
			}			
		}
		
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLDS_MAP, thresholdValues.toJsonString());
		getImporter().setData(destinationRef, Indicator.TAG_THRESHOLD_DETAILS_MAP, thresholdDetails.toJsonString());
	}
}
