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

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

abstract public class BaseObjectWithLeaderResourceFieldImporter extends BaseObjectImporter
{
	public BaseObjectWithLeaderResourceFieldImporter(Xmpz2XmlImporter importerToUse, BaseObjectSchema baseObjectSchemaToUse)
	{
		super(importerToUse, baseObjectSchemaToUse);
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(BaseObject.TAG_PLANNED_LEADER_RESOURCE))
			return true;
		
		if (tag.equals(BaseObject.TAG_ASSIGNED_LEADER_RESOURCE))
			return true;

		return super.isCustomImportField(tag);
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		getImporter().importRefField(baseObjectNode, refToUse, getXmpz2ElementName(), BaseObject.TAG_PLANNED_LEADER_RESOURCE, RESOURCE_ID);
		getImporter().importRefField(baseObjectNode, refToUse, getXmpz2ElementName(), BaseObject.TAG_ASSIGNED_LEADER_RESOURCE, RESOURCE_ID);
	}
}
