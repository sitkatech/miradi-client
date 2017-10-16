/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.TimeframeEditorField;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.fieldComponents.UiComboBox;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Timeframe;
import org.miradi.project.Project;
import org.miradi.project.ProjectCalendar;
import org.miradi.questions.AbstractDateUnitTypeQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;
import java.util.Vector;

abstract public class AbstractTimeframeEditorComponent extends DisposablePanel
{
	public AbstractTimeframeEditorComponent(Project projectToUse, ORefList planningObjectRefsToUse, AbstractDateUnitTypeQuestion dateUnitTypeQuestionToUse) throws Exception
	{
		project = projectToUse;
		planningObjectRefs = planningObjectRefsToUse;
		dateUnitTypeQuestion = dateUnitTypeQuestionToUse;

		setLayout(new BorderLayout());

		dateUnitTypeCombo = new UiComboBox<>(dateUnitTypeQuestion.getChoices());

		PanelTitleLabel explanation = new PanelTitleLabel(getPanelTitle());
		explanation.setBorder(createSmallCushionBorder());

		upperPanel = new TwoColumnPanel();
		upperPanel.setBorder(BorderFactory.createEtchedBorder());
		upperPanel.add(new PanelTitleLabel(EAM.text("Enter As: ")));
		upperPanel.add(dateUnitTypeCombo);
		upperPanel.setBorder(createSmallCushionBorder());

		StartEndDateUnitProvider startEndDateUnitProvider = getStartEndDateUnitProvider(planningObjectRefs);
		lowerPanel = createTimeframeEditorLowerPanel(projectToUse.getProjectCalendar(), startEndDateUnitProvider);
		lowerPanel.setBorder(createSmallCushionBorder());

		add(explanation, BorderLayout.PAGE_START);
		add(upperPanel, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.PAGE_END);

		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(planningObjectRefs);
		dateUnitTypeCombo.setSelectedItem(dateUnitTypeQuestionToUse.findChoiceByCode(singleDateUnitTypeCode));
	}

	abstract protected AbstractTimeframeEditorLowerPanel createTimeframeEditorLowerPanel(ProjectCalendar projectCalendarToUse, StartEndDateUnitProvider startEndDateUnitProviderToUse);

	public void addActionListener(TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandlerToUse)
	{
		editorFieldChangeHandler = editorFieldChangeHandlerToUse;
		if (lowerPanel != null)
			lowerPanel.addActionListener(editorFieldChangeHandlerToUse);
	}

	private StartEndDateUnitProvider getStartEndDateUnitProvider(ORefList planningObjectRefs) throws Exception
	{
		String singleDateUnitTypeCode = getDefaultDateUnitTypeCode(planningObjectRefs);

		Vector<DateUnit> dateUnits = new Vector<>();
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

	private String getPanelTitle()
	{
		return "";
	}

	public DateUnitEffortList getDateUnitEffortList(ORef planningObjectRef) throws Exception
	{
		Timeframe timeframe = Timeframe.find(getProject(), planningObjectRef);
		return timeframe.getDateUnitEffortList();
	}

	private String getDefaultDateUnitTypeCode(ORefList planningObjectRefsToUse) throws Exception
	{
		if (planningObjectRefsToUse.isEmpty())
			return AbstractDateUnitTypeQuestion.NONE_CODE;

		ORef planningObjectRef = planningObjectRefsToUse.getFirstElement();
		Timeframe timeframe = Timeframe.find(getProject(), planningObjectRef);
		TimePeriodCostsMap timePeriodCostsMap = timeframe.convertAllDateUnitEffortList();

		Set<DateUnit> dateUnits = timePeriodCostsMap.getDateUnits();
		return getDateUnitCode(dateUnits);
	}

	public static void setTimeframeValue(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		project.executeBeginTransaction();
		try
		{
			clearTimeframeDateUnitEfforts(project, baseObjectForRow);

			ORefList timeframeRefs = baseObjectForRow.getTimeframeRefs();

			if (datesAsCodeList.hasData() && timeframeRefs.isEmpty())
				createTimeframe(project, baseObjectForRow, datesAsCodeList);

			if (datesAsCodeList.hasData() && timeframeRefs.hasRefs())
				updateTimeframe(project, timeframeRefs, datesAsCodeList);

			if (datesAsCodeList.isEmpty() && timeframeRefs.size() == 1)
				deleteEmptyTimeframe(project, timeframeRefs.getFirstElement());
		}
		finally
		{
			project.executeEndTransaction();
		}
	}

	private static void updateTimeframe(Project project, ORefList timeframeRefs, CodeList datesAsCodeList) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		for (int index = 0; index < timeframeRefs.size(); ++index)
		{
			setTimeframeDateUnitEffortList(project, timeframeRefs.get(index), dateUnitEffortList);
		}
	}

	private static void clearTimeframeDateUnitEfforts(Project project, BaseObject baseObjectForRow) throws Exception
	{
		ORefList timeframeRefs = baseObjectForRow.getTimeframeRefs();
		for (int index = 0; index < timeframeRefs.size(); ++index)
		{
			Timeframe timeframe = Timeframe.find(project, timeframeRefs.get(index));
			Command clearDateUnitEffortList = new CommandSetObjectData(timeframe, ResourceAssignment.TAG_DATEUNIT_DETAILS, new DateUnitEffortList().toString());
			project.executeCommand(clearDateUnitEffortList);
		}
	}

	private static void deleteEmptyTimeframe(Project project, ORef timeframeRef) throws Exception
	{
		Timeframe timeframe = Timeframe.find(project, timeframeRef);
		if (timeframe.isEmpty())
		{
			CommandVector removeTimeframeCommands = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(project, timeframe, BaseObject.TAG_TIMEFRAME_IDS);
			project.executeCommands(removeTimeframeCommands);
		}
	}

	private static void createTimeframe(Project project, BaseObject baseObjectForRow, CodeList datesAsCodeList) throws Exception
	{
		CommandCreateObject createTimeframe = new CommandCreateObject(TimeframeSchema.getObjectType());
		project.executeCommand(createTimeframe);

		ORef timeframeRef = createTimeframe.getObjectRef();
		DateUnitEffortList dateUnitEffortList = createDateUnitEffortList(datesAsCodeList);
		setTimeframeDateUnitEffortList(project, timeframeRef, dateUnitEffortList);

		CommandSetObjectData appendTimeframe = CommandSetObjectData.createAppendIdCommand(baseObjectForRow, BaseObject.TAG_TIMEFRAME_IDS, timeframeRef);
		project.executeCommand(appendTimeframe);
	}

	private static void setTimeframeDateUnitEffortList(Project project, ORef timeframeRef, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		CommandSetObjectData addEffortList = new CommandSetObjectData(timeframeRef, Timeframe.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		project.executeCommand(addEffortList);
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

	private String getDateUnitCode(Set<DateUnit> dateUnits)
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
				editorFieldChangeHandler.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "AbstractTimeframeEditorComponent"));
		}
	}

	private TimeframeEditorField.TimeframeEditorChangeHandler editorFieldChangeHandler;
	private Project project;
	private ORefList planningObjectRefs;
	private AbstractDateUnitTypeQuestion dateUnitTypeQuestion;
	private TwoColumnPanel upperPanel;
	private AbstractTimeframeEditorLowerPanel lowerPanel;
	private UiComboBox<ChoiceItem> dateUnitTypeCombo;
}