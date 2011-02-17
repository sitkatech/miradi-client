/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import java.util.HashMap;

import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class AbstractMultiRowColumnProvider extends AbstractPlanningTreeRowColumnProvider
{
	public AbstractMultiRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
		
		codeToProviderMap = createCodeToProviderMap();
	}
	
	public CodeList getColumnCodesToShow() throws Exception
	{
		return getSubViewProvider().getColumnCodesToShow();
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return getSubViewProvider().getRowCodesToShow();
	}
	
	public boolean shouldIncludeResultsChain() throws Exception
	{
		return getSubViewProvider().shouldIncludeResultsChain();
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return getSubViewProvider().shouldIncludeConceptualModelPage();
	}
	
	@Override
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return getSubViewProvider().doObjectivesContainStrategies();
	}
	
	protected AbstractPlanningTreeRowColumnProvider getSubViewProvider() throws Exception
	{
		String currentCode = getProject().getCurrentViewData().getData(getConfigurationTag());
		
		return codeToProviderMap.get(currentCode);
	}
	
	abstract protected String getConfigurationTag();
	
	abstract protected HashMap<String, AbstractPlanningTreeRowColumnProvider> createCodeToProviderMap();
	
	protected HashMap<String, AbstractPlanningTreeRowColumnProvider> codeToProviderMap;
}
