/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewCustomizationRadioButton extends PlanningViewRadioButton
{
	public PlanningViewCustomizationRadioButton(Project projectToUse, RowColumnProvider rowColumnProvider)
	{
		super(projectToUse, rowColumnProvider);
	}

	public String[] getColumnList() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_COL_CONFIGURATION);
	}

	public String[] getRowList() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_ROW_CONFIGURATION);
	}
	
	//TODO planning - this gets converted back to codeList maybe should return codeList
	private String[] getList(String fieldTag) throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		ORef configurationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		PlanningViewConfiguration configuration = (PlanningViewConfiguration) getProject().findObject(configurationRef);
		
		return new CodeList(configuration.getData(fieldTag)).toArray();
	}
	
	public String getPropertyName()
	{
		return PlanningView.CUSTOMIZABLE_RADIO_CHOICE;
	}
}
