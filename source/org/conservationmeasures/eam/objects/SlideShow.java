/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

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