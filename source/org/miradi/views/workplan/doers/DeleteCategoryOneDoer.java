/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.views.workplan.doers;

import org.miradi.main.EAM;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.views.umbrella.doers.DeletePoolObjectDoer;

public class DeleteCategoryOneDoer extends DeletePoolObjectDoer
{
	@Override
	protected String getCustomText()
	{
		return EAM.text("Category One");
	}
	
	@Override
	protected void doWork(BaseObject objectToDelete) throws Exception
	{
		removeAssignmentReferenceToObject(objectToDelete, Assignment.TAG_CATEGORY_ONE_REF);
	}

	@Override
	protected boolean canDelete(BaseObject singleSelectedObject)
	{
		return BudgetCategoryOne.is(singleSelectedObject);
	}
}
