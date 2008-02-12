/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.project.ObjectManager;

public class CausePool extends EAMNormalObjectPool
{
	public CausePool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.CAUSE);
	}
	
	public void put(Cause cause)
	{
		put(cause.getId(), cause);
	}
	
	public Cause find(BaseId id)
	{
		return (Cause)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Cause(objectManager ,new FactorId(actualId.asInt()));
	}
	
	public FactorId[] getModelNodeIds()
	{
		return (FactorId[])new HashSet(getRawIds()).toArray(new FactorId[0]);
	}
	
	public Factor[] getDirectThreats()
	{
		Vector cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			Factor cmNode = (Factor)getRawObject(ids[i]);
			if(cmNode.isDirectThreat())
				cmNodes.add(cmNode);
		}
		return (Factor[])cmNodes.toArray(new Factor[0]);
	}
}
