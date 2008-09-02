/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.treeRelevancy;

import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Objective;

public class StrategyActivityRelevancyTreeTablePanel extends AbstractEditableTreeTablePanel
{
	public static StrategyActivityRelevancyTreeTablePanel createStrategyActivityRelevancyTreeTablePanel(MainWindow mainWindowToUse, Objective objective) throws Exception
	{
		RootRelevancyTreeTableNode rootNode = new RootRelevancyTreeTableNode(mainWindowToUse.getProject(), objective);
		StrategyActivityRelevancyTreeTableModel treeTableModel = new StrategyActivityRelevancyTreeTableModel(rootNode); 
		StrategyActivityRelevancyTreeTable treeTable = new StrategyActivityRelevancyTreeTable(mainWindowToUse, treeTableModel);
		
		return new StrategyActivityRelevancyTreeTablePanel(mainWindowToUse, treeTableModel, treeTable, objective);
	}
	
	private StrategyActivityRelevancyTreeTablePanel(MainWindow mainWindowToUse, StrategyActivityRelevancyTreeTableModel modelToUse, TreeTableWithStateSaving treeTable, Objective objective) throws Exception
	{
		super(mainWindowToUse, modelToUse, treeTable, objective);		
	}
	
	protected SingleBooleanColumnEditableModel createEditableTableModel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeTable, BaseObject baseObject)
	{
		return new StrategyActivityRelevancyTableModel(mainWindowToUse.getProject(), treeTable,  (Objective)baseObject);
	}
	
	protected EditableObjectTable createEditableTable(MainWindow mainWindowToUse)
	{
		return new StrategyActivityRelevancyTable(mainWindowToUse, getEditableSingleBooleanColumnTableModel());
	}
	
	protected String getDividerName()
	{
		return "StrategyActivityRelevancyTreeTablePanel";
	}	
}