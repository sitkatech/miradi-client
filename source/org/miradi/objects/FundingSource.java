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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.FundingSourceId;
import org.miradi.main.EAM;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class FundingSource extends BaseObject
{
	public FundingSource(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public FundingSource(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new FundingSourceId(idAsInt), json);
	}

	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.FUNDING_SOURCE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public String toString()
	{
		String result = getLabel();
		if(result.length() > 0)
			return result;
		return EAM.text("Label|(Undefined Accounting Code)");
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
			
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
	public static final String OBJECT_NAME = "FundingSource";
	

	StringData code;
	StringData comments;
}