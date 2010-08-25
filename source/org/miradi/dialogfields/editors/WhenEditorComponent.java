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
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.martus.swing.UiComboBox;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DateUnitTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffortList;

public class WhenEditorComponent extends DisposablePanel
{
	public WhenEditorComponent(ProjectCalendar projectCalendar, BaseObject baseObjectToUse) throws Exception
	{
		setLayout(new BorderLayout());

		ORefList resourceAssignmentRefs = baseObjectToUse.getResourceAssignmentRefs();
		DateUnitTypeQuestion dateUnitTypeQuestion = new DateUnitTypeQuestion(baseObjectToUse.getProject(), resourceAssignmentRefs);
		dateUnitTypeCombo = new UiComboBox(dateUnitTypeQuestion.getChoices());
		
		String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(baseObjectToUse.getProject(), resourceAssignmentRefs);
		
		Vector<DateUnit> dateUnits = new Vector<DateUnit>();
		if (!singleDateUnitTypeCode.equals(DateUnitTypeQuestion.NONE_CODE) && resourceAssignmentRefs.hasRefs())
		{
			ORef resourceAssignmentRef = resourceAssignmentRefs.getFirstElement();
			ResourceAssignment resourceAssignment = ResourceAssignment.find(baseObjectToUse.getProject(), resourceAssignmentRef);
			DateUnitEffortList dateUnitEffortList = resourceAssignment.getDateUnitEffortList();
			if (dateUnitEffortList.size() == 1)
			{
				dateUnits.add(dateUnitEffortList.getDateUnitEffort(0).getDateUnit());
				dateUnits.add(dateUnitEffortList.getDateUnitEffort(0).getDateUnit());
			}
			if (dateUnitEffortList.size() == 2)
			{
				dateUnits.add(dateUnitEffortList.getDateUnitEffort(0).getDateUnit());
				dateUnits.add(dateUnitEffortList.getDateUnitEffort(1).getDateUnit());
			}
		}

		PanelTitleLabel explanation = new PanelTitleLabel(
				EAM.text("<html>" +
						"Specifying when this work item will take place using this dialog <br>" +
						"will enter zeros in the appropriate time period column(s)."));
		explanation.setBorder(createSmallCushionBorder());
		
		TwoColumnPanel upperPanel = new TwoColumnPanel();
		upperPanel.setBorder(BorderFactory.createEtchedBorder());
		upperPanel.add(new PanelTitleLabel(EAM.text("Enter As: ")));
		upperPanel.add(dateUnitTypeCombo);
		upperPanel.setBorder(createSmallCushionBorder());
		
		StartEndDateUnitProvider dateUnitRange = new StartEndDateUnitProvider(dateUnits);
		lowerPanel = new WhenEditorLowerPanel(projectCalendar, dateUnitRange);
		lowerPanel.setBorder(createSmallCushionBorder());

		add(explanation, BorderLayout.PAGE_START);
		add(upperPanel, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.PAGE_END);
		
		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		dateUnitTypeCombo.setSelectedItem(dateUnitTypeQuestion.findChoiceByCode(singleDateUnitTypeCode));
	}

	private Border createSmallCushionBorder()
	{
		return BorderFactory.createEmptyBorder(4,4,4,4);
	}
	
	@Override
	public void dispose()
	{
		disposePanel(lowerPanel);

		super.dispose();
	}

	private String getDefaultDateUnitTypeCode(Project projectToUse, ORefList resourceAssignmentRefs) throws Exception
	{
		if (resourceAssignmentRefs.isEmpty())
			return DateUnitTypeQuestion.NONE_CODE;
		
		ORef resourceAssignmentRef = resourceAssignmentRefs.getFirstElement();
		ResourceAssignment resourceAssignment = ResourceAssignment.find(projectToUse, resourceAssignmentRef);
		TimePeriodCostsMap timePeriodCostsMap = resourceAssignment.getResourceAssignmentsTimePeriodCostsMap();		
		Set<DateUnit> dateUnits = timePeriodCostsMap.getDateUnits();
		for(DateUnit dateUnit : dateUnits)
		{	
			if (dateUnit.isDay())
				return DateUnitTypeQuestion.DAY_CODE;
			
			if (dateUnit.isMonth())
				return DateUnitTypeQuestion.MONTH_CODE;
			
			if (dateUnit.isQuarter())
				return DateUnitTypeQuestion.QUARTER_CODE;
			
			if (dateUnit.isYear())
				return DateUnitTypeQuestion.YEAR_CODE;
			
			if (dateUnit.isProjectTotal())
				return DateUnitTypeQuestion.PROJECT_TOTAL_CODE;
		}
		
		return DateUnitTypeQuestion.NONE_CODE;
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
			ChoiceItem selectedChoiceItem = (ChoiceItem) e.getItem();
			lowerPanel.showCard(selectedChoiceItem.getCode());
		}
	}
	
	private WhenEditorLowerPanel lowerPanel;
	private UiComboBox dateUnitTypeCombo;
}