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

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.Project;

abstract public class PlanningViewAbstractAssignmentTableModel extends EditableObjectTableModel
{
	public PlanningViewAbstractAssignmentTableModel(Project projectToUse)
	{
		super(projectToUse);
		baseObjectRefs = new ORefList();
		currencyFormatter = getProject().getCurrencyFormatterWithCommas();
	}
	
	public int getRowCount()
	{
		return baseObjectRefs.size();
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		if(hierarchyToSelectedRef.length == 0)
			return;
		
		ORef selectedRef = hierarchyToSelectedRef[0];
		
		baseObject = BaseObject.find(getProject(), selectedRef);
		baseObjectRefs = getRefsForBaseObject(baseObject);
	}
			
	public void setBaseObject(BaseObject baseObjectToUse)
	{
		if (isAlreadyCurrentBaseObject(baseObjectToUse))
			return;
			
		baseObject = baseObjectToUse;
		updateRefList();	
	}
	
	public void dataWasChanged() throws Exception
	{
		if (isAlreadyCurrentRefList())
			return;
		
		updateRefList();
	}

	private boolean isAlreadyCurrentBaseObject(BaseObject baseObjectToUse)
	{
		 if(baseObject == null || baseObjectToUse == null)
			 return false;
		 
		 return baseObject.getId().equals(baseObjectToUse.getId());
	}
	
	private boolean isAlreadyCurrentRefList()
	{
		return baseObjectRefs.equals(getRefsForBaseObject(baseObject));
	}
	
	private void updateRefList()
	{
		baseObjectRefs = getRefsForBaseObject(baseObject);
		fireTableDataChanged();
	}
		
	private ORefList getRefsForBaseObject(BaseObject baseObjectToUse)
	{
		if (baseObjectToUse == null)
			return new ORefList();
		
		try
		{
			return baseObjectToUse.getRefList(getListTag(), getListType());	
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}
	
	public ORef getRefForRow(int row)
	{
		return baseObjectRefs.get(row);
	}
	
	public Assignment getAssignment(int row)
	{
		return (Assignment) getProject().findObject(getRefForRow(row));
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return BaseObject.find(getProject(), getRefForRow(row));
	}
	
	//FIXME planning table - there should be methods that return the raw value,  then that value
	//can be used in budgetmodel to calculate the cost. (cost per unit and units need to return raw values)
	protected Object getResourceCostPerUnit(Assignment assignment)
	{
		ProjectResource resource = findProjectResource(assignment);
		if (resource == null)
			return "";
				
		double cost = resource.getCostPerUnit();
		return currencyFormatter.format(cost);
	}
	
	protected ProjectResource findProjectResource(Assignment assignment)
	{
		ORef resourceRef = assignment.getResourceRef();
		ProjectResource resource = (ProjectResource) getProject().findObject(resourceRef);
		return resource;
	}
	
	abstract protected String getListTag();
	
	abstract protected int getListType();
	
	protected ORefList baseObjectRefs;
	protected BaseObject baseObject;

	protected CurrencyFormat currencyFormatter;
}
