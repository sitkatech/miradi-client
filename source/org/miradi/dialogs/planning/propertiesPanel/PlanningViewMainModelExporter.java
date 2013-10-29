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
import org.miradi.utils.AbstractSingleTableExporter;

public class PlanningViewMainModelExporter extends AbstractSingleTableExporter
{
	public PlanningViewMainModelExporter(Project projectToUse, ChoiceItemTableModel choiceItemTableModelToUse, RowColumnBaseObjectProvider objectProviderToUse, String uniqueModelIdentifier)
	{
		super(projectToUse, uniqueModelIdentifier);
		
		choiceItemTableModel = choiceItemTableModelToUse;
		objectProvider = objectProviderToUse;
	}
	
	@Override
	public int getRowCount()
	{
		return getModel().getRowCount();
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return objectProvider.getBaseObjectForRowColumn(row, column);
	}
	
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return getBaseObjectForRowColumn(row, 0);
	}
	
	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		return 0;
	}
	
	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	@Override
	public String getModelColumnName(int modelColumn)
	{
		return getModel().getColumnName(modelColumn);
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		return getModel().getColumnGroupCode(modelColumn);
	}
	
	@Override
	public int getColumnCount()
	{
		return getModel().getColumnCount();
	}

	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		return getModel().getChoiceItemAt(row, modelColumn);
	}

	@Override
	public int getRowType(int row)
	{
		return 0;
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		Object value = getModel().getValueAt(row, modelColumn);
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
