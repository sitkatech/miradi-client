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
package org.miradi.objectpools;

import java.util.Arrays;
import java.util.Comparator;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Target;
import org.miradi.project.ObjectManager;

public class TargetPool extends EAMNormalObjectPool
{

	public TargetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TARGET);
	}
	
	public void put(Target target) throws Exception
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
