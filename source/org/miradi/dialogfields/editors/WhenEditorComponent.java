/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields.editors;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;

import org.martus.swing.UiComboBox;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

public class WhenEditorComponent extends DisposablePanel
{
	public WhenEditorComponent(ProjectCalendar projectCalendar, BaseObject baseObjectToUse) throws Exception
	{
		setLayout(new BorderLayout());

		String[] choices = new String[]{NONE_ITEM, PROJECT_TOTAL_ITEM, YEAR_ITEM, QUARTER_ITEM, MONTH_ITEM, DAY_ITEM,};
		dateUnitTypeCombo = new UiComboBox(choices);
		
		ORefList resourceAssignmentRefs = baseObjectToUse.getResourceAssignmentRefs();
		String defaultDateUnitType = getDefaultDateUnitType(baseObjectToUse.getProject(), resourceAssignmentRefs);
		
		DateUnit dateUnit1 = null;
		DateUnit dateUnit2 = null;
		if (!defaultDateUnitType.equals(NONE_ITEM) && resourceAssignmentRefs.hasRefs())
		{
			ORef resourceAssignmentRef = resourceAssignmentRefs.getFirstElement();
			ResourceAssignment resourceAssignment = ResourceAssignment.find(baseObjectToUse.getProject(), resourceAssignmentRef);
			DateUnitEffortList dateUnitEffortList = resourceAssignment.getDateUnitEffortList();
			if (dateUnitEffortList.size() == 1)
			{
				dateUnit1 = dateUnitEffortList.getDateUnitEffort(0).getDateUnit();
				dateUnit2 = dateUnitEffortList.getDateUnitEffort(0).getDateUnit();
			}
			if (dateUnitEffortList.size() == 2)
			{
				dateUnit1 = dateUnitEffortList.getDateUnitEffort(0).getDateUnit();
				dateUnit2 = dateUnitEffortList.getDateUnitEffort(1).getDateUnit();
			}
		}
		
		DateUnitRange dateUnitRange = new DateUnitRange(projectCalendar, dateUnit1, dateUnit2);
		lowerPanel = new WhenEditorLowerPanel(projectCalendar, dateUnitRange);
		TwoColumnPanel upperPanel = new TwoColumnPanel();
		upperPanel.setBorder(BorderFactory.createEtchedBorder());
		upperPanel.add(new PanelTitleLabel(EAM.text("Enter As: ")));
		upperPanel.add(dateUnitTypeCombo);
		
		add(upperPanel, BorderLayout.PAGE_START);
		add(lowerPanel, BorderLayout.PAGE_END);
		
		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		dateUnitTypeCombo.setSelectedItem(defaultDateUnitType);
	}
	
	private String getDefaultDateUnitType(Project projectToUse, ORefList resourceAssignmentRefs) throws Exception
	{
		if (resourceAssignmentRefs.isEmpty())
			return NONE_ITEM;
		
		ORef resourceAssignmentRef = resourceAssignmentRefs.getFirstElement();
		ResourceAssignment resourceAssignment = ResourceAssignment.find(projectToUse, resourceAssignmentRef);
		DateUnitEffortList dateUnitEffortList = resourceAssignment.getDateUnitEffortList();
		if (dateUnitEffortList.size() == 0)
			return NONE_ITEM;
		
		for (int index = 0; index < dateUnitEffortList.size(); ++index)
		{
			DateUnitEffort dateUnitEffort = dateUnitEffortList.getDateUnitEffort(index);
			DateUnit dateUnit = dateUnitEffort.getDateUnit(); 
			if (dateUnit.isDay())
				return DAY_ITEM;
			
			if (dateUnit.isMonth())
				return MONTH_ITEM;
			
			if (dateUnit.isQuarter())
				return QUARTER_ITEM;
			
			if (dateUnit.isYear())
				return YEAR_ITEM;
			
			if (dateUnit.isProjectTotal())
				return PROJECT_TOTAL_ITEM;
		}
		
		return null;
	}

	public CodeList getStartEndCodes() throws Exception
	{
		CodeList startEndCodes = new CodeList();
		if (getStartDateUnit() != null)
			startEndCodes.add(getStartDateUnit().getDateUnitCode());
		
		if (getEndDateUnit() != null)
			startEndCodes.add(getEndDateUnit().getDateUnitCode());
		
		return startEndCodes;
	}
	
	private DateUnit getStartDateUnit() throws Exception
	{
		return lowerPanel.getStartDateUnit();
	}
	
	private DateUnit getEndDateUnit() throws Exception
	{
		return lowerPanel.getEndDateUnit();
	}
	
	private class ChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			lowerPanel.showCard(e.getItem().toString());
		}
	}
	
	private WhenEditorLowerPanel lowerPanel;
	private UiComboBox dateUnitTypeCombo;
	
	public static final String NONE_ITEM = "None";
	public static final String PROJECT_TOTAL_ITEM = "Project Total";
	public static final String YEAR_ITEM = "Year";
	public static final String QUARTER_ITEM = "Quarter";
	public static final String MONTH_ITEM = "Month";
	public static final String DAY_ITEM = "Day";
}