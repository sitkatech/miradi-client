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

import java.util.Vector;

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.AbstractTableExporter;

public class PlanningViewMainModelExporter extends AbstractTableExporter
{
	public PlanningViewMainModelExporter(Project projectToUse, ChoiceItemTableModel choiceItemTableModelToUse, RowColumnBaseObjectProvider objectProviderToUse, String uniqueModelIdentifier)
	{
		super(projectToUse, uniqueModelIdentifier);
		
		choiceItemTableModel = choiceItemTableModelToUse;
		objectProvider = objectProviderToUse;
	}
	
	public int getRowCount()
	{
		return getModel().getRowCount();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return objectProvider.getBaseObjectForRowColumn(row, column);
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		return getBaseObjectForRowColumn(row, 0);
	}
	
	public int getDepth(int row, int tableColumn)
	{
		return 0;
	}
	
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	public String getColumnName(int tableColumn)
	{
		return getModel().getColumnName(tableColumn);
	}
	
	@Override
	public int getColumnCount()
	{
		return getModel().getColumnCount();
	}

	@Override
	public ChoiceItem getChoiceItemAt(int row, int tableColumn)
	{
		return getModel().getChoiceItemAt(row, tableColumn);
	}

	@Override
	public int getRowType(int row)
	{
		return 0;
	}

	@Override
	public String getTextAt(int row, int tableColumn)
	{
		Object value = getModel().getValueAt(row, tableColumn);
		return getSafeValue(value);
	}

	private ChoiceItemTableModel getModel()
	{
		return choiceItemTableModel;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		EAM.logError("getAllRefs is not implemented");
		return new ORefList();
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		EAM.logError("getAllTypes is not implemented");
		return new Vector<Integer>();
	}

	private ChoiceItemTableModel choiceItemTableModel;
	private RowColumnBaseObjectProvider objectProvider;
}
