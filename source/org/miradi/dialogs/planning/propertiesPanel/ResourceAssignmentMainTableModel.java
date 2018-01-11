/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItemBaseObjectWrapper;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Translation;

public class ResourceAssignmentMainTableModel extends AbstractAssignmentSummaryTableModel
{
	public ResourceAssignmentMainTableModel(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public String getColumnName(int column)
	{
		if (isResourceColumn(column))
			return EAM.text("People (Who)");

		if (isTotalsColumn(column))
			return EAM.text("Total");

		return super.getColumnName(column);
	}

	@Override
	public String getColumnTag(int modelColumn)
	{
		if (isResourceColumn(modelColumn))
			return RESOURCE_COLUMN_TAG;

		if (isTotalsColumn(modelColumn))
			return TOTALS_COLUMN_TAG;

		return super.getColumnTag(modelColumn);
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_COUNT;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		return getCellValue(row, column);
	}
	
	private Object getCellValue(int row, int column)
	{
		ORef resourceAssignmentRef = getRefForRow(row);
		ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(), resourceAssignmentRef);
		if (isResourceColumn(column))
			return new ChoiceItemBaseObjectWrapper(findProjectResource(resourceAssignment));
		if (isTotalsColumn(column))
			return getTotals(resourceAssignment);

		return null;
	}

	private Object getTotals(ResourceAssignment assignment)
	{
		try
		{
			TimePeriodCosts timePeriodCosts = calculateTotalTimePeriodAssignedCosts(assignment);
			OptionalDouble totalWorkUnits = timePeriodCosts.getTotalWorkUnits();
			if (totalWorkUnits.hasValue())
				return currencyFormatter.format(totalWorkUnits.getValue());
			else
				return null;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return Translation.getCellTextWhenException();
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		if (row < 0 || row >= getRowCount())
		{
			EAM.errorDialog(EAM.text("An error has occurred while writing assignment data."));
			EAM.logWarning("Row out of bounds in ResourceAssignmentMainTableModel.setValueAt value = "+ value + " row = " + row + " column = " + column);
			return;
		}
		
		ORef resourceAssignmentRefForRow = getRefForRow(row);
		setResourceCell(value, resourceAssignmentRefForRow, column);
		
		super.setValueAt(value, row, column);
	}
	
	private void setResourceCell(Object value, ORef resourceAssignmentRefForRow, int column)
	{
		if (! isResourceColumn(column))
			return;

		ProjectResource projectResource = (ProjectResource) ((ChoiceItemBaseObjectWrapper)value).getBaseObject();
		BaseId resourceId = projectResource.getId();
		String idAsString = resourceId.toString();
		if (resourceId.isInvalid())
			idAsString = "";
		
		setValueUsingCommand(resourceAssignmentRefForRow, ResourceAssignment.TAG_RESOURCE_ID, idAsString);
	}
	
	private ProjectResource findProjectResource(ResourceAssignment resourceAssignment)
	{
		ORef resourceRef = resourceAssignment.getResourceRef();
		if (resourceRef.isInvalid())
			return createInvalidResource(getObjectManager());
		
		return ProjectResource.find(getProject(), resourceRef);
	}
		
	@Override
	public boolean isResourceColumn(int column)
	{
		return getResourceColumn() == column;
	}

	private int getResourceColumn()
	{
		return RESOURCE_COLUMN;
	}
	
	@Override
	public boolean isTotalsColumn(int column)
	{
		return getTotalsColumn() == column;
	}

	private int getTotalsColumn()
	{
		return TOTALS_COLUMN;
	}

	@Override
	protected String getListTag()
	{
		return BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;
	}

	@Override
	protected int getListType()
	{
		return ResourceAssignmentSchema.getObjectType();
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
				
	public static ProjectResource createInvalidResource(ObjectManager objectManager)
	{
		try
		{
			ProjectResource invalidResource = new ProjectResource(objectManager, BaseId.INVALID);
			invalidResource.setData(ProjectResource.TAG_GIVEN_NAME, "(" + EAM.text("Not Specified") + ")");
	
			return invalidResource;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}

	private static final String UNIQUE_MODEL_IDENTIFIER = "ResourceAssignmentMainTableModel";
	
	private static final int COLUMN_COUNT = 2;
	
	private static final int RESOURCE_COLUMN = 0;
	private static final int TOTALS_COLUMN = 1;

	private static final String RESOURCE_COLUMN_TAG = "Fake Tag: Resource";
	private static final String TOTALS_COLUMN_TAG = "Fake Tag: Totals";
}
