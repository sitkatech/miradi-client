/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.summary;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.ObjectMultilineDisplayField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.propertiesPanel.FillerPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.Assignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Timeframe;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.MiradiTextPane;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SummaryPlanningWorkPlanSubPanel extends ObjectDataInputPanel
{
	public SummaryPlanningWorkPlanSubPanel(Project projectToUse, ORef oRefToUse) throws Exception
	{
		super(projectToUse, oRefToUse);
		
		addHiddenWorkPlanDataWarningLabel();
		
		ObjectDataInputField startDate = createDateChooserField(ProjectMetadata.TAG_START_DATE);
		startDate.getComponent().addFocusListener(new ProjectPlanningDateFocusListener());
		ObjectDataInputField endDate = createDateChooserField(ProjectMetadata.TAG_EXPECTED_END_DATE);
		endDate.getComponent().addFocusListener(new ProjectPlanningDateFocusListener());
		ObjectDataInputField[] projectDateFields = new ObjectDataInputField[] {startDate, endDate, };
		addFieldsOnOneLine(EAM.text("Label|Project Dates"), projectDateFields);

		ObjectDataInputField workPlanStartDate = createDateChooserField(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		workPlanStartDate.getComponent().addFocusListener(new ProjectPlanningDateFocusListener());
		ObjectDataInputField workPlanEndDate = createDateChooserField(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		workPlanEndDate.getComponent().addFocusListener(new ProjectPlanningDateFocusListener());
		ObjectDataInputField[] workPlanDateFields = new ObjectDataInputField[] {workPlanStartDate, workPlanEndDate, };
		addFieldsOnOneLine(EAM.text("Label|Work Plan Dates"), workPlanDateFields);

		addDataDateRangeWarningField();

		addDataDateRangeInfoField();

		addField(createChoiceField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_FISCAL_YEAR_START, new FiscalYearStartQuestion()));

		addFieldWithPopUpInformation(createNumericField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR), "FteFieldDescription.html");

		ChoiceQuestion quarterColumnsVisibilityQuestion = StaticQuestionManager.getQuestion(QuarterColumnsVisibilityQuestion.class);
		quarterColumnVisibilityComponent = addRadioButtonFieldWithCustomLabel(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, quarterColumnsVisibilityQuestion, "");
		addQuarterColumnVisibilityExplanationLabel();

		ChoiceQuestion dayColumnsVisibilityQuestion = StaticQuestionManager.getQuestion(DayColumnsVisibilityQuestion.class);
		dayColumnVisibilityComponent = addRadioButtonFieldWithCustomLabel(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY, dayColumnsVisibilityQuestion, "");
		addDayColumnVisibilityExplanationLabel();

		addField(createMultilineField(ProjectMetadata.TAG_PLANNING_COMMENTS));

		updateQuarterColumnVisibilityEnableStatus();
		
		updateDayColumnVisibilityEnableStatus();

		setObjectRefs(new ORef[] {oRefToUse, projectToUse.getSingletonObjectRef(ProjectMetadataSchema.getObjectType()), });
		updateFieldsFromProject();
	}

	class ProjectPlanningDateFocusListener implements FocusListener
	{

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			updateWorkPlanDataOutOfRangeWarningField();
			updateDateRangeWarningField();
		}
	}

	private void addDataDateRangeInfoField() throws Exception
	{
		add(new FillerPanel());
		dateRangeInfoMessage = new DateRangeInfoMessageLabel();
		add(dateRangeInfoMessage);
	}

	class DateRangeInfoMessageLabel extends PanelTitleLabel
	{
		public DateRangeInfoMessageLabel()
		{
			this.setBackground(AppPreferences.getDataPanelBackgroundColor());
		}

		public void rebuild()
		{
			String startDate = getFirstDateWithData();
			String endDate = getLastDateWithData();

			String text = EAM.text("Work plan data currently exists for %startDate - %endDate");

			if (!startDate.isEmpty() && !endDate.isEmpty())
			{
				text = EAM.substitute(text, "%startDate", startDate);
				text = EAM.substitute(text, "%endDate", endDate);
			}
			else
			{
				text = "";
			}

			this.setText(text);
		}

		private String getLastDateWithData()
		{
			DateRange dateRange = safeGetProjectAssignedDataDateRange();

			if (dateRange == null)
				return "";

			return dateRange.getEndDate().toIsoDateString();
		}

		private String getFirstDateWithData()
		{
			DateRange dateRange = safeGetProjectAssignedDataDateRange();

			if (dateRange == null)
				return "";

			return dateRange.getStartDate().toIsoDateString();
		}

		private DateRange safeGetProjectAssignedDataDateRange()
		{
			DateRange dateRange = null;

			try
			{
				dateRange = getProjectAssignedDataDateRange(getProject());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}

			return dateRange;
		}
	}

	private void addDataDateRangeWarningField() throws Exception
	{
		FillerPanel dateRangeWarningLabelFillerPanel = createAndAddFillerPanel();
		dateRangeWarningMessage = new DateRangeWarningMessageLabel(dateRangeWarningLabelFillerPanel);
		add(dateRangeWarningMessage);
	}

	class DateRangeWarningMessageLabel extends Box
	{
		public DateRangeWarningMessageLabel(FillerPanel dateRangeWarningLabelFillerPanel)
		{
			super(BoxLayout.X_AXIS);

			this.fillerPanel = dateRangeWarningLabelFillerPanel;

			label = new MiradiTextPane(getMainWindow(), ObjectMultilineDisplayField.DEFAULT_WIDE_FIELD_CHARACTERS, 1);
			label.setText("");
			label.setEditable(false);
			label.setAlignmentY(TOP_ALIGNMENT);

			JLabel icon = new JLabel(IconManager.getWarningIcon());
			icon.setAlignmentY(TOP_ALIGNMENT);

			this.setBackground(AppPreferences.getDataPanelBackgroundColor());
			label.setBackground(AppPreferences.getDataPanelBackgroundColor());
			this.add(icon);
			this.add(label);
			this.setVisible(false);
		}

		public void rebuild()
		{
			String text = "";
			boolean showWarning = false;

			boolean showProjectPlanningDateRangeWarning = getProject().getProjectCalendar().isStartDateAfterEndDate();
			if (showProjectPlanningDateRangeWarning)
				text = projectPlanningDateRangeWarningMessage;

			boolean showWorkPlanDateRangeWarning = getProject().getProjectCalendar().isWorkPlanStartDateBeforeProjectStartDate();
			if (showWorkPlanDateRangeWarning)
				text = workPlanDateRangeWarningMessage;

			showWarning = showProjectPlanningDateRangeWarning || showWorkPlanDateRangeWarning;
			this.setVisible(showWarning);
			this.fillerPanel.setVisible(!showWarning);

			label.setText(text);
		}

		MiradiTextPane label;
		FillerPanel fillerPanel;

		String projectPlanningDateRangeWarningMessage = EAM.text("Effective end date for project planning date range is before start date.");
		String workPlanDateRangeWarningMessage = EAM.text("Work plan start date is before project start date.");
	}

	private void addHiddenWorkPlanDataWarningLabel() throws Exception
	{
		workPlanDataWarningLabelFillerReplacement = createAndAddFillerPanel();
		String warningMessage = EAM.text("Some work plan data is currently hidden and not included in " +
										"calculated totals. If the data is outside the planning date range, " +
										"it will be visible again if you set the planning start and end date " +
										"to include the entire date range of the existing data. " +
										"Otherwise, it could be that annual data was entered for a " +
										"different fiscal year setting, in which case it will be visible " +
										"if you set the fiscal year start back to its previous setting.");
		workPlanDataWarningPanel = createAndAddWarningPanel(warningMessage);
		updateWorkPlanDataOutOfRangeWarningField();
	}

	private void addQuarterColumnVisibilityExplanationLabel() throws Exception
	{
		quarterVisibilityExplanationFillerReplacement = createAndAddFillerPanel();
		String message = EAM.text("NOTE: Quarter columns cannot be hidden because this project already has data for some quarters.");
		quarterColumnExplanationPanel = createAndAddInformationalNotePanel(message);
		updateQuarterColumnVisibilityEnableStatus();
	}

	private void addDayColumnVisibilityExplanationLabel() throws Exception
	{
		dayVisibilityExplanationFillerReplacement = createAndAddFillerPanel();
		String message = EAM.text("NOTE: Day columns cannot be hidden because this project already has data for some days.");
		dayColumnExplanationPanel = createAndAddInformationalNotePanel(message);
		updateDayColumnVisibilityEnableStatus();
	}

	private FillerPanel createAndAddFillerPanel()
	{
		add(new FillerPanel());
		FillerPanel fillerPanel = new FillerPanel();
		add(fillerPanel);

		return fillerPanel;
	}

	private JComponent createAndAddInformationalNotePanel(String message) throws Exception
	{
		MiradiTextPane label = new MiradiTextPane(getMainWindow(), ObjectMultilineDisplayField.DEFAULT_WIDE_FIELD_CHARACTERS, 1);
		label.setText(message);
		label.setEditable(false);
		label.setAlignmentY(TOP_ALIGNMENT);
		label.setForeground(getForeground());
		label.setBackground(getBackground());

		add(label);
		
		return label;
	}
	
	private JComponent createAndAddWarningPanel(String message) throws Exception
	{
		MiradiTextPane label = new MiradiTextPane(getMainWindow(), ObjectMultilineDisplayField.DEFAULT_WIDE_FIELD_CHARACTERS, 1);
		label.setText(message);
		label.setEditable(false);
		label.setAlignmentY(TOP_ALIGNMENT);
		
		JLabel icon = new JLabel(IconManager.getWarningIcon());
		icon.setAlignmentY(TOP_ALIGNMENT);

		Box panel = Box.createHorizontalBox();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		label.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.add(icon);
		panel.add(label);

		add(panel);
		
		return panel;
	}
	
	@Override
	public void setObjectRefs(ORef[] oRefsToUse)
	{
		super.setObjectRefs(oRefsToUse);
		updateWorkPlanDataOutOfRangeWarningField();
		updateDateRangeWarningField();
	}
	
	private void updateWorkPlanDataOutOfRangeWarningField()
	{
		if (workPlanDataWarningPanel == null)
			return;

		boolean showWarning = hasAssignedDataOutsideOfProjectDateRange(getProject());
		workPlanDataWarningPanel.setVisible(showWarning);
		workPlanDataWarningLabelFillerReplacement.setVisible(!showWarning);
	}

	private void updateDateRangeWarningField()
	{
		if (dateRangeWarningMessage == null)
			return;

		dateRangeWarningMessage.rebuild();
	}

	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (!event.isSetDataCommand())
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		if (isOneOfOurFields(setCommand.getFieldTag()))
		{
			updateQuarterColumnVisibilityEnableStatus();
			updateDayColumnVisibilityEnableStatus();
			updateWorkPlanDataOutOfRangeWarningField();
			updateDateRangeWarningField();
			getMainWindow().updatePlanningDateRelatedStatus();
		}
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		dateRangeInfoMessage.rebuild();
		dateRangeWarningMessage.rebuild();
		updateQuarterColumnVisibilityEnableStatus();
		updateDayColumnVisibilityEnableStatus();
	}

	private void updateQuarterColumnVisibilityEnableStatus()
	{
		final boolean enableQuarterVisibilityOption = hasQuarterData() && getProject().getMetadata().areQuarterColumnsVisible();

		quarterColumnVisibilityComponent.setEditable(!enableQuarterVisibilityOption);
		quarterColumnExplanationPanel.setVisible(enableQuarterVisibilityOption);
		quarterVisibilityExplanationFillerReplacement.setVisible(!enableQuarterVisibilityOption);
	}

	private void updateDayColumnVisibilityEnableStatus()
	{
		final boolean enableDayVisibilityOption = hasDayData() && getProject().getMetadata().areDayColumnsVisible();

		dayColumnVisibilityComponent.setEditable(!enableDayVisibilityOption);
		dayColumnExplanationPanel.setVisible(enableDayVisibilityOption);
		dayVisibilityExplanationFillerReplacement.setVisible(!enableDayVisibilityOption);
	}

	public static boolean hasAssignedDataOutsideOfProjectDateRange(Project projectToUse)
	{
		try
		{
			DateRange allDataDateRange = getProjectAssignedDataDateRange(projectToUse);
			return hasDataOutsideOfProjectDateRange(projectToUse, allDataDateRange);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	private static boolean hasDataOutsideOfProjectDateRange(Project projectToUse, DateRange allDataDateRange) throws Exception
	{
		if (allDataDateRange == null)
			return false;

		DateRange projectPlanningDateRange = projectToUse.getProjectCalendar().getProjectPlanningDateRange();
		return !projectPlanningDateRange.contains(allDataDateRange);
	}

	private static DateRange getProjectAssignedDataDateRange(Project projectToUse) throws Exception
	{
		DateRange projectDateRange = projectToUse.getProjectCalendar().getProjectPlanningDateRange();
		TimePeriodCostsMap tpcm = getTimePeriodCostsMapForAllAssignments(projectToUse);

		return tpcm.getRolledUpDateRange(projectDateRange);
	}

	private static TimePeriodCostsMap getTimePeriodCostsMapForAllAssignments(Project projectToUse) throws Exception
	{
		ORefList assignmentRefs = new ORefList();
		assignmentRefs.addAll(projectToUse.getAssignmentPool().getORefList());
		assignmentRefs.addAll(projectToUse.getPool(ExpenseAssignmentSchema.getObjectType()).getRefList());
		
		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();
		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(projectToUse, assignmentRefs.get(index));
			tpcm.mergeAll(assignment.convertAllDateUnitEffortList());
		}
		
		return tpcm;
	}
	
	private static TimePeriodCostsMap getTimePeriodCostsMapForAllTimeframes(Project projectToUse) throws Exception
	{
		ORefList timeframeRefs = new ORefList();
		timeframeRefs.addAll(projectToUse.getTimeframePool().getORefList());
		timeframeRefs.addAll(projectToUse.getPool(TimeframeSchema.getObjectType()).getRefList());

		TimePeriodCostsMap tpcm = new TimePeriodCostsMap();
		for (int index = 0; index < timeframeRefs.size(); ++index)
		{
			Timeframe timeframe = Timeframe.find(projectToUse, timeframeRefs.get(index));
			tpcm.mergeAll(timeframe.convertAllDateUnitEffortList());
		}

		return tpcm;
	}

	private boolean hasQuarterData()
	{
		try
		{
			TimePeriodCostsMap tpcmAssignments = getTimePeriodCostsMapForAllAssignments(getProject());
			TimePeriodCostsMap tpcmTimeframes = getTimePeriodCostsMapForAllTimeframes(getProject());
			return tpcmAssignments.containsQuarterDateUnit() || tpcmTimeframes.containsQuarterDateUnit();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			return false;
		}
	}

	private boolean hasDayData()
	{
		try
		{
			TimePeriodCostsMap tpcmAssignments = getTimePeriodCostsMapForAllAssignments(getProject());
			TimePeriodCostsMap tpcmTimeframes = getTimePeriodCostsMapForAllTimeframes(getProject());
			return tpcmAssignments.containsDayDateUnit() || tpcmTimeframes.containsDayDateUnit();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			return false;
		}
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Work Plan Settings");
	}

	private DateRangeInfoMessageLabel dateRangeInfoMessage;
	private DateRangeWarningMessageLabel dateRangeWarningMessage;

	private FillerPanel workPlanDataWarningLabelFillerReplacement;
	private FillerPanel quarterVisibilityExplanationFillerReplacement;
	private FillerPanel dayVisibilityExplanationFillerReplacement;
	private JComponent workPlanDataWarningPanel;
	private JComponent quarterColumnExplanationPanel;
	private JComponent dayColumnExplanationPanel;
	private ObjectDataInputField quarterColumnVisibilityComponent;
	private ObjectDataInputField dayColumnVisibilityComponent;
}
