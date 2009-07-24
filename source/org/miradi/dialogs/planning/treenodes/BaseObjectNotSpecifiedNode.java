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

package org.miradi.dialogs.planning.treenodes;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class BaseObjectNotSpecifiedNode extends AbstractPlanningTreeNode
{
	public BaseObjectNotSpecifiedNode(Project projectToUse, int objectType, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		
		unspecifiedResource = new UnspecifiedProjectResource(getProject().getObjectManager(), objectType);
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return unspecifiedResource;
	}

	@Override
	public void rebuild() throws Exception
	{
	}
	
	public class UnspecifiedProjectResource extends BaseObject
	{
		public UnspecifiedProjectResource(ObjectManager objectManagerToUse, int objectTypeToUse)
		{
			super(objectManagerToUse, BaseId.INVALID);
			
			objectType = objectTypeToUse;
		}

		@Override
		public int getType()
		{
			return objectType;
		}

		@Override
		public String getTypeName()
		{
			return "";
		}
		
		@Override
		public String getLabel()
		{
			return EAM.text("Not Specified");
		}
		
		private int objectType;
	}
	
	private UnspecifiedProjectResource unspecifiedResource;
}
