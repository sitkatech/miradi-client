/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import java.util.Arrays;
import java.util.Comparator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.ObjectManager;

public class TargetPool extends EAMNormalObjectPool
{

	public TargetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TARGET);
	}
	
	public void put(Target target)
	{
		put(target.getId(), target);
	}
	
	public Target find(BaseId id)
	{
		return (Target)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Target(objectManager ,new FactorId(actualId.asInt()));
	}
	
	public Target[] getTargets()
	{
		class TargetSorter implements Comparator
		{
			public int compare(Object arg0, Object arg1)
			{
				return arg0.toString().trim().compareTo(arg1.toString().trim());
			}
		}
		
		Target[] targets = (Target[]) getValues().toArray(new Target[0]);
		Arrays.sort(targets, new TargetSorter());
		return targets;
	}

}
