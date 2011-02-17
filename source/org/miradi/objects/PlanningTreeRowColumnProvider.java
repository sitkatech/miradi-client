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

package org.miradi.objects;

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.utils.CodeList;

public interface PlanningTreeRowColumnProvider extends RowColumnProvider
{
	public abstract CodeList getRowCodesToShow() throws Exception;
	public abstract CodeList getColumnCodesToShow() throws Exception;
	public abstract boolean shouldIncludeResultsChain() throws Exception;
	public abstract boolean shouldIncludeConceptualModelPage() throws Exception;
	public abstract boolean doObjectivesContainStrategies() throws Exception;
	public abstract boolean shouldPutTargetsAtTopLevelOfTree() throws Exception;
	public String getWorkPlanBudgetMode() throws Exception;
}