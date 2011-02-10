/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.treenodes;

import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;

public class PlanningRootNode extends NewAbstractPlanningTreeNode
{
	public PlanningRootNode(Project projectToUse)
	{
		super(projectToUse, null);
	}

	@Override
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	@Override
	public BaseObject getObject()
	{
		return project.getMetadata();
	}

	@Override
	public Object getValueAt(int column)
	{
		if (column == 0)
			return getObject().toString();
		
		return new ChoiceItem("", "", new MiradiApplicationIcon());
	}

	@Override
	public String toRawString()
	{
		return getProject().getFilename();
	}

}
