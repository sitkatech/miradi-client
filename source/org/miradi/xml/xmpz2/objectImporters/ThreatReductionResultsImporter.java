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

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.ThreatReductionResultSchema;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class ThreatReductionResultsImporter extends BaseObjectImporter
{
	public ThreatReductionResultsImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new ThreatReductionResultSchema());
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		importThreatId(baseObjectNode, refToUse);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF))
			return true;
		
		return super.isCustomImportField(tag);
	}
	
	private void importThreatId(Node node, ORef destinationRef) throws Exception
	{
		Node relatedThreatIdNode = getImporter().getNode(node, getPoolName() + XmpzXmlConstants.RELATED_THREAT_ID);
		if (relatedThreatIdNode == null)
			return;
		
		Node threatIdNode = getImporter().getNode(relatedThreatIdNode, getPoolName() + XmpzXmlConstants.THREAT_ID);
		Node idNode = getImporter().getNode(threatIdNode, XmpzXmlConstants.THREAT_ID);
		BaseId relatedThreatId = new BaseId(idNode.getTextContent());
		ORef relatedThreatRef = new ORef(CauseSchema.getObjectType(), relatedThreatId);
		getImporter().setData(destinationRef, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, relatedThreatRef.toString());
	}
}
