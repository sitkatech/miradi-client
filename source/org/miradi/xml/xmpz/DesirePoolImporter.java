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
import org.miradi.objects.Desire;
import org.w3c.dom.Node;

//FIXME Class under construction 
abstract public class DesirePoolImporter extends AbstractBaseObjectImporter
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
		importRelevantStrategyIds(node, destinationRef);
		importRelevantActivityIds(node, destinationRef);
	}

	private void importRelevantStrategyIds(Node node, ORef destinationRef)
	{
	}

	private void importRelevantIndicatorIds(Node node, ORef destinationRef) throws Exception
	{
	}
	
	private void importRelevantActivityIds(Node node, ORef destinationRef)
	{
	}
}
