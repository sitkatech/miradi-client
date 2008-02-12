/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.FundingSourceId;
import org.miradi.main.EAM;
import org.miradi.objectdata.StringData;
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
	
	
	public static boolean canReferToThisType(int type)
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