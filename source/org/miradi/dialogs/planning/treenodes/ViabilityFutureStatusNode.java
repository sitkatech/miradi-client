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

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;

public class ViabilityFutureStatusNode extends UnspecifiedBaseObjectNode
{
	public ViabilityFutureStatusNode(Project projectToUse, TreeTableNode parentNodeToUse)
	{
		super(projectToUse, parentNodeToUse, Goal.getObjectType(), Goal.OBJECT_NAME);
	}
	
	@Override
	public String toRawString()
	{
		try
		{
			return getParentNode().getObject().getData(Indicator.TAG_FUTURE_STATUS_DATE);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("label|Error");
		}
	}
}
