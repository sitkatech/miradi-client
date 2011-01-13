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

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class ThreatReductionResultsPoolImporter extends FactorPoolImporter
{
	public ThreatReductionResultsPoolImporter(XmpzXmlImporter importerToUse)
	{
		super(importerToUse, XmpzXmlConstants.THREAT_REDUCTION_RESULTS, ThreatReductionResult.getObjectType());
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		super.importFields(node, destinationRef);

		importObjectiveIds(node, destinationRef);
		importIndicatorIds(node, destinationRef);
		importThreatId(node, destinationRef);
	}

	private void importThreatId(Node node, ORef destinationRef) throws Exception
	{
		Node relatedThreatIdNode = getImporter().getNode(node, getPoolName() + XmpzXmlConstants.RELATED_THREAT_ID);
		if (relatedThreatIdNode == null)
			return;
		
		Node threatIdNode = getImporter().getNode(relatedThreatIdNode, getPoolName() + XmpzXmlConstants.THREAT_ID);
		Node idNode = getImporter().getNode(threatIdNode, XmpzXmlConstants.THREAT_ID);
		BaseId relatedThreatId = new BaseId(idNode.getTextContent());
		ORef relatedThreatRef = new ORef(Cause.getObjectType(), relatedThreatId);
		getImporter().setData(destinationRef, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, relatedThreatRef.toString());
	}
}
