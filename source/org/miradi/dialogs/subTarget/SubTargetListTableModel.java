/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.subTarget;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.schemas.SubTargetSchema;

public class SubTargetListTableModel extends ObjectListTableModel
{
	public SubTargetListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_SUB_TARGET_REFS, SubTargetSchema.getObjectType(), COLUMN_TAGS);
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_MODEL_IDENTIFIER = "SubTargetListTableModel";

	public static final String[] COLUMN_TAGS = new String[] {
		SubTarget.TAG_SHORT_LABEL,
		SubTarget.TAG_LABEL,
	};
}
