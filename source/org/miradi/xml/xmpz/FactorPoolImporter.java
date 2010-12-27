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
import org.miradi.objects.Factor;
import org.miradi.objects.Objective;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;

abstract public class FactorPoolImporter extends AbstractBaseObjectPoolImporter
{
	public FactorPoolImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int objectTypeToImportToUse)
	{
		super(importerToUse, poolNameToUse, objectTypeToImportToUse);
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);
		
		importField(node, destinationRef, Factor.TAG_SHORT_LABEL);
		importField(node, destinationRef, getDetailsTag());
		importField(node, destinationRef, Factor.TAG_COMMENTS);
	}
	
	protected String getDetailsTag()
	{
		return Factor.TAG_TEXT;
	}
	
	protected void importObjectiveIds(Node node, ORef destinationRef)	throws Exception
	{
		importIds(node, destinationRef, Factor.TAG_OBJECTIVE_IDS, Objective.getObjectType(), WcsXmlConstants.OBJECTIVE);
	}
}
