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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectpools.ResourcePool;
import org.miradi.objects.ProjectResource;

public class AssignmentSummaryTable extends AbstractSummaryTable
{
	public AssignmentSummaryTable(MainWindow mainWindowToUse, AssignmentSummaryTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse, UNIQUE_IDENTIFIER);
	}
	
	public void rebuildColumnEditorsAndRenderers() throws Exception
	{
		for (int tableColumn = 0; tableColumn < getColumnCount(); ++tableColumn)
		{
			createResourceCombo(tableColumn);
		}
		
		super.rebuildColumnEditorsAndRenderers();
	}
	
	private void createResourceCombo(int tableColumn) throws Exception
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		if (! getPlanningViewResourceTableModel().isResourceColumn(modelColumn))
			return;
		
		ProjectResource[] resources = getAllProjectResources();
		ProjectResource invalidResource = new ProjectResource(getObjectManager(), BaseId.INVALID);
		invalidResource.setData(ProjectResource.TAG_GIVEN_NAME, EAM.text("(not specified)"));
		createComboColumn(resources, tableColumn, invalidResource);
	}
	
	private ProjectResource[] getAllProjectResources()
	{
		return  getResourcePool().getAllProjectResources();
	}
	
	private ResourcePool getResourcePool()
	{
		return getObjectManager().getResourcePool();
	}
	
    public static final String UNIQUE_IDENTIFIER = "AssignmentSummaryTable";
}
