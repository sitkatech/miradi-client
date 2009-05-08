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
package org.miradi.objecthelpers;

import java.util.HashMap;
import java.util.Set;

import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		expense = new OptionalDouble();
		resourceUnitsMap = new HashMap();
	}
	
	public void setExpense(OptionalDouble expenseToUse)
	{
		expense = expenseToUse;
	}
	
	public void addResourceCost(ORef resourceRef, OptionalDouble unitsToUse)
	{
		resourceUnitsMap.put(resourceRef, unitsToUse);
	}
	
	public OptionalDouble calculateTotal(Project projectToUse)
	{
		return calculateProjectResources(projectToUse).add(getExpense());
	}
	
	public OptionalDouble calculateProjectResources(Project projectToUse)
	{
		OptionalDouble projectResourceTotalUnits = new OptionalDouble();
		Set<ORef> projectResourcRefs = resourceUnitsMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			ProjectResource projectResource = ProjectResource.find(projectToUse, projectResourceRef);
			Double costPerUnit = projectResource.getCostPerUnit();
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			OptionalDouble multiplyValue = units.multiplyValue(costPerUnit);
			projectResourceTotalUnits = projectResourceTotalUnits.add(multiplyValue);
		}
		
		return projectResourceTotalUnits;
	}
	
	public OptionalDouble getUnits(ORef resourceRef)
	{
		return resourceUnitsMap.get(resourceRef);
	}
	
	public OptionalDouble getExpense()
	{
		return expense;
	}
	
	private OptionalDouble expense;
	private HashMap<ORef, OptionalDouble> resourceUnitsMap;
}
