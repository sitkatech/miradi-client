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
package org.miradi.views.planning.doers;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;


abstract public class AbstractTreeNodeCreateTaskDoer extends AbstractTreeNodeDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		try
		{
			BaseObject selectedObject = getSingleSelectedObject();
			if(selectedObject == null)
				return false;
			ORefList[] selectedHierarchies = getSelectedHierarchies();
			if (selectedHierarchies.length != 1)
				return false;
			ORefList selectionHierarchy = selectedHierarchies[0];
			if(!containsParentOfTask(selectionHierarchy))
				return false;
			if(!childWouldBeVisible(Task.getChildTaskTypeCode(selectedObject.getType())))
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
	
	protected boolean canBeParentOfTask(BaseObject selectedObject) throws Exception
	{
		if(selectedObject.getType() == Task.getObjectType())
			return true;
		
		return false;
	}
	
	protected Vector<String> getParentObjectName()
	{
		Vector<String> parentTypeNames = new Vector<String>();
		parentTypeNames.add(Task.OBJECT_NAME);
		parentTypeNames.add(Task.METHOD_NAME);
		parentTypeNames.add(Task.ACTIVITY_NAME);
		
		return parentTypeNames;
	}
	
	protected String getChildObjectName()
	{
		return Task.OBJECT_NAME;
	}
	
	protected boolean containsParentOfTask(ORefList selectionHierarchy) throws Exception
	{
		return findParentOfTask(selectionHierarchy) != null;
	}
	
	protected BaseObject findParentOfTask(ORefList selectionHierarchy) throws Exception
	{
		for(int index = 0; index < selectionHierarchy.size(); ++index)
		{
			ORef objectRef = selectionHierarchy.get(index);
			if (objectRef.isInvalid())
				continue;
			
			BaseObject baseObject = BaseObject.find(getProject(), objectRef);
			String typeName = baseObject.getTypeName();
			if (getParentObjectName().contains(typeName))
				return baseObject;
		}
		
		return null;
	}
}
