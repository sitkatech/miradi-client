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

import org.miradi.ids.SlideShowId;
import org.miradi.objectdata.ORefListData;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class SlideShow extends BaseObject
{
	public SlideShow(ObjectManager objectManager, SlideShowId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public SlideShow(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new SlideShowId(idAsInt), jsonObject);
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
		return ObjectType.SLIDESHOW;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.SLIDE:
				return true;
			default:
				return false;
		}
	}
	
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.SLIDE: 
				list.addAll(slideRefs.getORefList());
				break;
		}
		return list;
	}
	

	void clear()
	{
		super.clear();
		slideRefs = new ORefListData(TAG_SLIDE_REFS);
		addField(TAG_SLIDE_REFS, slideRefs);
	}
	
	
	public static String TAG_SLIDE_REFS = "SlideRefs";


	public static final String OBJECT_NAME = "SlideShow";

	
	private ORefListData slideRefs;
}