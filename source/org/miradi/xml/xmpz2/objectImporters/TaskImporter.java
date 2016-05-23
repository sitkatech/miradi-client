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
import org.miradi.objects.Task;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;
import org.w3c.dom.Node;

public class TaskImporter extends BaseObjectWithLeaderResourceFieldImporter
{
	public TaskImporter(Xmpz2XmlImporter importerToUse)
	{
		super(importerToUse, new TaskSchema());
	}
	
	@Override
	public void importFields(Node baseObjectNode, ORef refToUse) throws Exception
	{
		super.importFields(baseObjectNode, refToUse);

		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, RESOURCE_ASSIGNMENT, ResourceAssignmentSchema.getObjectType());
		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), BaseObject.TAG_TIMEFRAME_IDS, TIMEFRAME, TimeframeSchema.getObjectType());
		getImporter().importIds(baseObjectNode, refToUse, getBaseObjectSchema(), Task.TAG_SUBTASK_IDS, SUB_TASK, TaskSchema.getObjectType());
	}
	
	@Override
	protected boolean isCustomImportField(String tag)
	{
		if (tag.equals(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;
		
		if (tag.equals(BaseObject.TAG_TIMEFRAME_IDS))
			return true;

		return super.isCustomImportField(tag);
	}
}
