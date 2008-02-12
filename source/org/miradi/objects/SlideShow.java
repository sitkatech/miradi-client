/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import java.util.Set;

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
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_SLIDE_REFS);
		return set;
	}
	

	public static boolean canReferToThisType(int type)
	{
		return false;
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