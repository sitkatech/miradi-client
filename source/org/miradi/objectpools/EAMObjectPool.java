/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import org.martus.util.UnicodeWriter;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;

public class EAMObjectPool extends ObjectPool
{
	public EAMObjectPool(int objectTypeToStore)
	{
		super(objectTypeToStore);
	}
	
	public BaseObject findObject(BaseId id)
	{
		return (BaseObject)getRawObject(id);
	}
	
	public ORefList getORefList()
	{
		return new ORefList(getObjectType(), new IdList(getObjectType(), getIds()));
	}

	
	public void toXml(UnicodeWriter out) throws Exception
	{
		out.writeln("<Pool objectType='" + getObjectType() + "'>");
		IdList ids = getIdList();
		for(int i = 0; i < ids.size(); ++i)
		{
			findObject(ids.get(i)).toXml(out);
		}
		out.writeln("</Pool>");
	}
}
