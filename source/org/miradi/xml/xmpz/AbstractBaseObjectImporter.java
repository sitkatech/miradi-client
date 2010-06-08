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
import org.miradi.objects.BaseObject;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.WcsXmlConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

abstract public class AbstractBaseObjectImporter extends AbstractXmpzObjectImporter
{
	public AbstractBaseObjectImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int objectTypeToImportToUse)
	{
		super(importerToUse, poolNameToUse);
		
		objectTypeToImport = objectTypeToImportToUse;
	}
	
	@Override
	public void importElement() throws Exception
	{
		importObjects();
	}
	
	protected void importObjects() throws Exception
	{
		NodeList nodes = getImporter().getNodes(getImporter().getRootNode(), new String[]{getPoolName() + WcsXmlConstants.POOL_ELEMENT_TAG, getPoolName(), });
		for (int index = 0; index < nodes.getLength(); ++index)
		{
			Node node = nodes.item(index);
			String intIdAsString = getImporter().getAttributeValue(node, WcsXmlConstants.ID);
			ORef ref = getProject().createObject(getObjectTypeToImport(), new BaseId(intIdAsString));
			
			importFields(node, ref);
		}
	}
	
	private int getObjectTypeToImport()
	{
		return objectTypeToImport;
	}
	
	protected void importFields(Node node, ORef destinationRef) throws Exception
	{
		importField(node, destinationRef, BaseObject.TAG_LABEL);	
	}
	
	private int objectTypeToImport;
}