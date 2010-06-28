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

package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class RollupReportsNode extends AbstractPlanningTreeNode
{
	public RollupReportsNode(Project project, CodeList visibleRows,	BaseObject nodeObjectToUse, CodeList levelObjectTypesToUse, int levelToUse) throws Exception
	{
		super(project, visibleRows);
		
		nodeObject = nodeObjectToUse;
		levelObjectTypes = levelObjectTypesToUse;
		currentLevel = levelToUse;
		rebuild();
	}

	@Override
	public BaseObject getObject()
	{
		return nodeObject;
	}
	
	@Override
	public void rebuild() throws Exception
	{
		children = new Vector();
		String levelObjectTypeAsString = levelObjectTypes.get(currentLevel);
		int levelObjectType = Integer.parseInt(levelObjectTypeAsString);
		ORefList refs = getProject().getPool(levelObjectType).getRefList();
		++currentLevel;
		for (int index = 0; index < refs.size(); ++index)
		{	
			BaseObject baseObject = BaseObject.find(getProject(), refs.get(index));
			children.add(new RollupReportsNode(getProject(), getVisibleRows(), baseObject, levelObjectTypes, currentLevel));
			
		}		
	}
	
	private BaseObject nodeObject;
	private CodeList levelObjectTypes;
	private int currentLevel;
}
