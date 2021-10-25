/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.SingleBooleanColumnEditableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.EAM;
import org.miradi.objects.*;
import org.miradi.project.DiagramFactorTaggedObjectSetHelper;
import org.miradi.project.Project;

import java.util.Vector;

public class TaggedObjectSetEditableTableModel extends SingleBooleanColumnEditableModel
{
	public TaggedObjectSetEditableTableModel(Project projectToUse, TreeTableWithStateSaving treeTableToUse, TaggedObjectSet taggedObjectSetToUse)
	{
		super(projectToUse, treeTableToUse);

		treeTable = treeTableToUse;
		taggedObjectSet = taggedObjectSetToUse;
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return isValidFactorRow(row, column);
	}

	private boolean isValidFactorRow(int row, int column)
	{
		BaseObject objectForRow = getBaseObjectForRowColumn(row, column);
		return Factor.isFactor(objectForRow.getRef());
	}
	
	@Override
	public String getColumnName(int column)
	{
		return SINGLE_COLUMN_NAME;
	}	
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;

		try
		{
			DiagramFactor diagramFactor = getDiagramFactorForRow(row);
			if (diagramFactor != null)
			{
				boolean isSelected = (boolean) value;

				DiagramFactorTaggedObjectSetHelper helper = new DiagramFactorTaggedObjectSetHelper(getProject());
				Vector<CommandSetObjectData> commandsToTagUntagDiagramFactor =
						isSelected ? helper.createCommandsToTagDiagramFactor(diagramFactor, taggedObjectSet) :
								helper.createCommandsToUntagDiagramFactor(diagramFactor, taggedObjectSet);

				getProject().executeCommands(commandsToTagUntagDiagramFactor.toArray(new Command[0]));
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	@Override
	protected boolean isRowSelected(int row, int column) throws Exception
	{
		boolean isSelected = false;

		DiagramFactor diagramFactor = getDiagramFactorForRow(row);
		if (diagramFactor != null)
			isSelected = diagramFactor.getTaggedObjectSetRefs().contains(taggedObjectSet.getRef());

		return isSelected;
	}

	private DiagramFactor getDiagramFactorForRow(int row) throws Exception
	{
		TreeTableNode node = treeTable.getNodeForRow(row);
		if (node instanceof FactorTreeTableNode)
		{
			Factor factor = (Factor) node.getObject();
			DiagramObject diagramObject = (DiagramObject) node.getParentNode().getObject();
			return diagramObject.getDiagramFactor(factor.getRef());
		}

		return null;
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}

	private static final String UNIQUE_MODEL_IDENTIFIER = "TaggedObjectSetEditableTableModel";
	
	private TaggedObjectSet taggedObjectSet;
	private TreeTableWithStateSaving treeTable;

	public static final String SINGLE_COLUMN_NAME = EAM.text("Is Tagged");
}
