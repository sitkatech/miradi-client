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

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objects.BaseObject;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.project.ObjectManager;

public class HumanWelfareTargetPool extends EAMNormalObjectPool
{
	public HumanWelfareTargetPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, HumanWelfareTarget.getObjectType());
	}
	
	public void put(HumanWelfareTarget humanWelfareTarget) throws Exception
	{
		put(humanWelfareTarget.getId(), humanWelfareTarget);
	}
	
	public HumanWelfareTarget find(BaseId id)
	{
		return (HumanWelfareTarget)getRawObject(id);
	}
	
	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new HumanWelfareTarget(objectManager ,new FactorId(actualId.asInt()));
	}
	
	public HumanWelfareTarget[] getSortedHumanWelfareTargets()
	{
		HumanWelfareTarget[] humanWelfareTargets = getValues().toArray(new HumanWelfareTarget[0]);
		Arrays.sort(humanWelfareTargets, new BaseObjectByFullNameSorter());
		return humanWelfareTargets;
	}
}
