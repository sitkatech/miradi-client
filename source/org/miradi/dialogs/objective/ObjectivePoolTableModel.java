/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.objective;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Objective;
import org.miradi.project.Project;


public class ObjectivePoolTableModel extends ObjectPoolTableModel
{
	public ObjectivePoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.OBJECTIVE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		Objective.TAG_SHORT_LABEL,
		Objective.TAG_LABEL,
		Objective.PSEUDO_TAG_FACTOR,
	};

}
