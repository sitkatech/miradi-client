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

package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.StringData;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

abstract public class AbstractBudgetCategoryObject extends BaseObject
{
	public AbstractBudgetCategoryObject(ObjectManager objectManagerToUse, BaseId idToUse)
	{
		super(objectManagerToUse, idToUse);
	}

	public AbstractBudgetCategoryObject(ObjectManager objectManager, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}
	
	@Override
	public String getFullName()
	{
		return toFullNameWithCode(code.get());
	}
	
	@Override
	public void clear()
	{
		super.clear();
		
		code = new StringData(TAG_CODE);
		comments = new StringData(TAG_COMMENTS);
		
		addField(TAG_CODE, code);
		addField(TAG_COMMENTS, comments);
	}
	
	public static final String TAG_CODE = "Code";
	public static final String TAG_COMMENTS = "Comments";

	private ObjectData code;
	private ObjectData comments;
}
