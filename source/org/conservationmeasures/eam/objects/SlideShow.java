/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.ids.SlideShowId;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
	
	public SlideShow(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new SlideShowId(idAsInt), jsonObject);
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
		slideRefs = new ORefListData();
		addField(TAG_SLIDE_REFS, slideRefs);
	}
	
	
	public static String TAG_SLIDE_REFS = "SlideRefs";


	public static final String OBJECT_NAME = "SlideShow";

	
	private ORefListData slideRefs;
}