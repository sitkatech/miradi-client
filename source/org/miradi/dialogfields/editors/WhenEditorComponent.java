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
import org.miradi.dialogfields.TimeframeEditorField;
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
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
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

		PanelTitleLabel explanation = new PanelTitleLabel(getPanelTitle());
		explanation.setBorder(createSmallCushionBorder());

		upperPanel = new TwoColumnPanel();
		upperPanel.setBorder(BorderFactory.createEtchedBorder());
		upperPanel.add(new PanelTitleLabel(EAM.text("Enter As: ")));
		upperPanel.add(dateUnitTypeCombo);
		upperPanel.setBorder(createSmallCushionBorder());

		StartEndDateUnitProvider startEndDateUnitProvider = getStartEndDateUnitProvider(planningObjectRefs);
		lowerPanel = new WhenEditorLowerPanel(projectToUse.getProjectCalendar(), startEndDateUnitProvider);
		lowerPanel.setBorder(createSmallCushionBorder());

		add(explanation, BorderLayout.PAGE_START);
		add(upperPanel, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.PAGE_END);

		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(planningObjectRefs);
		dateUnitTypeCombo.setSelectedItem(dateUnitTypeQuestionToUse.findChoiceByCode(singleDateUnitTypeCode));
	}

	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		editorFieldChangeHandler = editorFieldChangeHandlerToUse;
		if (lowerPanel != null)
			lowerPanel.addActionListener(editorFieldChangeHandlerToUse);
	}

	abstract public DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception;

	abstract protected TimePeriodCostsMap getTimePeriodCostsMap(ORef planningObjectRef) throws Exception;

	abstract protected String getPanelTitle();

	protected StartEndDateUnitProvider getStartEndDateUnitProvider(ORefList planningObjectRefs) throws Exception
	{
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

		return new StartEndDateUnitProvider(dateUnits);
	}

	private Border createSmallCushionBorder()
	{
		return BorderFactory.createEmptyBorder(4,4,4,4);
	}
	
	@Override
	public void dispose()
	{
		disposePanel(lowerPanel);
		lowerPanel = null;

		editorFieldChangeHandler = null;

		super.dispose();
	}

	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		if (upperPanel != null)
			upperPanel.setBackground(bg);
		if (lowerPanel != null)
			lowerPanel.setBackground(bg);
	}

	@Override
	public void setEnabled(boolean isEditable)
	{
		dateUnitTypeCombo.setEnabled(isEditable);
		upperPanel.setEnabled(isEditable);
		lowerPanel.setEnabled(isEditable);
	}

	public Project getProject()
	{
		return project;
	}

	protected String getDefaultDateUnitTypeCode(ORefList planningObjectRefsToUse) throws Exception
	{
		if (planningObjectRefsToUse.isEmpty())
			return AbstractDateUnitTypeQuestion.NONE_CODE;
		
		ORef planningObjectRef = planningObjectRefsToUse.getFirstElement();
		TimePeriodCostsMap timePeriodCostsMap = getTimePeriodCostsMap(planningObjectRef);

		Set<DateUnit> dateUnits = timePeriodCostsMap.getDateUnits();
		return getDateUnitCode(dateUnits);
	}

	protected String getDateUnitCode(Set<DateUnit> dateUnits)
	{
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

	public static DateUnitEffortList createDateUnitEffortList(CodeList datesAsCodeList)
	{
		final int NO_VALUE = 0;
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < datesAsCodeList.size(); ++index)
		{
			DateUnit dateUnit = new DateUnit(datesAsCodeList.get(index));
			if (dateUnitEffortList.getDateUnitEffortForSpecificDateUnit(dateUnit) == null)
				dateUnitEffortList.add(new DateUnitEffort(dateUnit, NO_VALUE));
		}

		return dateUnitEffortList;
	}

	public static CodeList createCodeList(Object rawValue) throws Exception
	{
		return new CodeList(rawValue.toString());
	}

	public void setPlanningObjectRefs(ORefList planningObjectRefsToUse)
	{
		try
		{
			planningObjectRefs = planningObjectRefsToUse;
			String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(planningObjectRefs);
			dateUnitTypeCombo.setSelectedItem(dateUnitTypeQuestion.findChoiceByCode(singleDateUnitTypeCode));
			StartEndDateUnitProvider startEndDateUnitProvider = getStartEndDateUnitProvider(planningObjectRefs);
			lowerPanel.setStartEndDateUnitProvider(project.getProjectCalendar(), startEndDateUnitProvider);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	public CodeList getStartEndCodes() throws Exception
	{
		CodeList startEndCodes = new CodeList();

		DateUnit startDateUnit = getStartDateUnit();
		DateUnit endDateUnit = getEndDateUnit();

		// if both dates supplied, then end date must be >= start date
		if (startDateUnit != null && endDateUnit != null && startDateUnit.compareTo(endDateUnit) > 0)
			endDateUnit = startDateUnit;

		if (startDateUnit != null)
			startEndCodes.add(startDateUnit.getDateUnitCode());

		if (endDateUnit != null)
			startEndCodes.add(endDateUnit.getDateUnitCode());
		
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

			if (e.getStateChange() == ItemEvent.SELECTED && editorFieldChangeHandler != null)
				editorFieldChangeHandler.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "WhenEditorComponent"));
		}
	}

	private TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandler;
	private Project project;
	private ORefList planningObjectRefs;
	private AbstractDateUnitTypeQuestion dateUnitTypeQuestion;
	private TwoColumnPanel upperPanel;
	private WhenEditorLowerPanel lowerPanel;
	private UiComboBox dateUnitTypeCombo;
}