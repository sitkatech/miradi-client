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
import org.miradi.objects.Strategy;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class StrategyImporter extends BaseObjectImporter
{
	public StrategyImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new StrategySchema());
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);
		
		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), Strategy.TAG_ACTIVITY_IDS, ACTIVITY, TaskSchema.getObjectType());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		
		return super.isCustomImportField(tag);
	}
}
