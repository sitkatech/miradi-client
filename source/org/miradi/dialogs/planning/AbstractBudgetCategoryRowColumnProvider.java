/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning;

import org.miradi.objects.AbstractBudgetCategoryObject;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class AbstractBudgetCategoryRowColumnProvider extends AbstractPlanningViewRowColumnProvider implements WorkPlanCategoryTreeRowColumnProvider
{
	public AbstractBudgetCategoryRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	public CodeList getColumnCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				AbstractBudgetCategoryObject.TAG_CODE,
				BaseObject.TAG_LABEL,
		});
	}
	
	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				getObjectTypeName(),
		});
	}
	
	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeEmptyRows()
	{
		return true;
	}
	
	public CodeList getLevelTypeCodes()
	{
		return new CodeList(new String[] {
				Integer.toString(getObjectType()),
		});
	}
	
	abstract protected int getObjectType();
	
	abstract protected String getObjectTypeName();
}
