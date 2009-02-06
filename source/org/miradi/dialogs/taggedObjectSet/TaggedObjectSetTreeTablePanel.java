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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.treeRelevancy.AbstractEditableTreeTablePanel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TaggedObjectSet;

public class TaggedObjectSetTreeTablePanel extends AbstractEditableTreeTablePanel
{
	public static TaggedObjectSetTreeTablePanel createTaggedItemTreeTablePanel(MainWindow mainWindowToUse, TaggedObjectSet taggedObjectSet) throws Exception
	{
		RootProjectNode rootNode = new RootProjectNode(mainWindowToUse.getProject());
		TaggedObjectSetTreeTableModel treeTableModel = new TaggedObjectSetTreeTableModel(rootNode); 
		TaggedObjectSetTreeTable treeTable = new TaggedObjectSetTreeTable(mainWindowToUse, treeTableModel);
		
		return new TaggedObjectSetTreeTablePanel(mainWindowToUse, treeTableModel, treeTable, taggedObjectSet);
	}
	
	private TaggedObjectSetTreeTablePanel(MainWindow mainWindowToUse, TaggedObjectSetTreeTableModel modelToUse, TreeTableWithStateSaving treeTable, TaggedObjectSet taggedObjectSet) throws Exception
	{
		super(mainWindowToUse, modelToUse, treeTable, taggedObjectSet);		
	}
	
	protected SingleBooleanColumnEditableModel createEditableTableModel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeTable, BaseObject baseObject)
	{
		return new TaggedObjectSetEditableTableModel(mainWindowToUse.getProject(), treeTable,  (TaggedObjectSet)baseObject);
	}
	
	protected EditableObjectTable createEditableTable(MainWindow mainWindowToUse)
	{
		return new TaggedObjectSetEditableTable(mainWindowToUse, getEditableSingleBooleanColumnTableModel());
	}
	
	protected String getDividerName()
	{
		return "TaggedObjectSetTreeTablePanel";
	}
}
