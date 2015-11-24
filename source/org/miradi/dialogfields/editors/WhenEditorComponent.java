/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields.editors;

import org.martus.swing.UiComboBox;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.Project;
import org.miradi.questions.AbstractDateUnitTypeQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffortList;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import java.util.Vector;

abstract public class WhenEditorComponent extends DisposablePanel
{
	public WhenEditorComponent(Project projectToUse, ORefList planningObjectRefsToUse, AbstractDateUnitTypeQuestion dateUnitTypeQuestionToUse) throws Exception
	{
		project = projectToUse;
		planningObjectRefs = planningObjectRefsToUse;
		dateUnitTypeQuestion = dateUnitTypeQuestionToUse;

		setLayout(new BorderLayout());

		dateUnitTypeCombo = new UiComboBox(dateUnitTypeQuestion.getChoices());

		String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(planningObjectRefs);

		Vector<DateUnit> dateUnits = new Vector<DateUnit>();
		if (!singleDateUnitTypeCode.equals(AbstractDateUnitTypeQuestion.NONE_CODE) && planningObjectRefs.hasRefs())
		{
			ORef planningObjectRef = planningObjectRefs.getFirstElement();
			DateUnitEffortList dateUnitEffortList = getDateUnitEffortList(planningObjectRef);
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
		lowerPanel = new WhenEditorLowerPanel(projectToUse.getProjectCalendar(), dateUnitRange);
		lowerPanel.setBorder(createSmallCushionBorder());

		add(explanation, BorderLayout.PAGE_START);
		add(upperPanel, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.PAGE_END);

		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		dateUnitTypeCombo.setSelectedItem(dateUnitTypeQuestionToUse.findChoiceByCode(singleDateUnitTypeCode));
	}

	abstract protected DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception;
	abstract protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception;

	private Border createSmallCushionBorder()
	{
		return BorderFactory.createEmptyBorder(4,4,4,4);
	}
	
	@Override
	public void dispose()
	{
		disposePanel(lowerPanel);
		lowerPanel = null;

		super.dispose();
	}

	public Project getProject()
	{
		return project;
	}

	protected String getDefaultDateUnitTypeCode(ORefList planningObjectRefs) throws Exception
	{
		if (planningObjectRefs.isEmpty())
			return AbstractDateUnitTypeQuestion.NONE_CODE;
		
		ORef planningObjectRef = planningObjectRefs.getFirstElement();
		TimePeriodCostsMap timePeriodCostsMap = getTimePeriodCostsMap(planningObjectRef);

		Set<DateUnit> dateUnits = timePeriodCostsMap.getDateUnits();
		for(DateUnit dateUnit : dateUnits)
		{	
			if (dateUnit.isDay())
				return AbstractDateUnitTypeQuestion.DAY_CODE;
			
			if (dateUnit.isMonth())
				return AbstractDateUnitTypeQuestion.MONTH_CODE;
			
			if (dateUnit.isQuarter())
				return AbstractDateUnitTypeQuestion.QUARTER_CODE;
			
			if (dateUnit.isYear())
				return AbstractDateUnitTypeQuestion.YEAR_CODE;
			
			if (dateUnit.isProjectTotal())
				return AbstractDateUnitTypeQuestion.PROJECT_TOTAL_CODE;
		}
		
		return AbstractDateUnitTypeQuestion.NONE_CODE;
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

	private Project project;
	private ORefList planningObjectRefs;
	private AbstractDateUnitTypeQuestion dateUnitTypeQuestion;
	private WhenEditorLowerPanel lowerPanel;
	private UiComboBox dateUnitTypeCombo;
}