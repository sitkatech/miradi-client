/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ViabilityRatingsTableField;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelCheckBox;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
		
		indicatorThreshold = (ViabilityRatingsTableField) addField(createViabilityRatingsTableField(ObjectType.INDICATOR,  new StatusQuestion(Indicator.TAG_INDICATOR_THRESHOLD)));		
		JPanel mainPropertiesPanel = createGridLayoutPanel(4, 1);
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(projectToUse, actions);
		mainPropertiesPanel.add(targetViabilityKeaPropertiesPanel);
		createIndicatorPropertiesPanel(mainPropertiesPanel);
		createMeasurementPropertiesPanel(mainPropertiesPanel);
		createIndicatorFutureStatusPanel(mainPropertiesPanel);
		
		addFieldComponent(mainPropertiesPanel);
		updateFieldsFromProject();
	}

	private void createMeasurementPropertiesPanel(JPanel mainPropertiesPanel)
	{
		ObjectDataInputField measurementStatus = addField(createRatingChoiceField(ObjectType.MEASUREMENT, new StatusQuestion(Measurement.TAG_STATUS)));  
		ObjectDataInputField measurementTrend = addField(createIconChoiceField(ObjectType.MEASUREMENT, new TrendQuestion(Measurement.TAG_TREND)));
		ObjectDataInputField measurementDate = addField(createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE));
		ObjectDataInputField measurementSummary = addField(createStringField(ObjectType.MEASUREMENT, Measurement.TAG_SUMMARY,STD_SHORT));
		ObjectDataInputField measurementDetail = addField(createMultilineField(ObjectType.MEASUREMENT, Measurement.TAG_DETAIL,NARROW_DETAILS));
		ObjectDataInputField measureementStatusConfidence = addField(createChoiceField(ObjectType.MEASUREMENT,  new StatusConfidenceQuestion(Measurement.TAG_STATUS_CONFIDENCE)));
	
		JPanel box5 = createGridLayoutPanel(1,5);
		box5.add(createColumnJPanel(measurementStatus));
		box5.add(createColumnJPanel(measurementDate));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		box5.add(createColumnJPanelWithIcon(measurementSummary,new IndicatorIcon()));
		box5.add(createColumnJPanel(measurementDetail));

		JPanel box6 = createGridLayoutPanel(1,5);
		box6.add(createColumnJPanel(measurementTrend));
		box6.add(Box.createHorizontalStrut(STD_SPACE_20));
		box6.add(add(createColumnJPanel(measureementStatusConfidence)));
		
		JPanel mainPanel = createGridLayoutPanel(2, 1);
		addBoldedTextBorder(mainPanel, "CS");
		mainPanel.add(box5);
		mainPanel.add(box6);
		mainPropertiesPanel.add(mainPanel);		
	}

	private void createIndicatorPropertiesPanel(JPanel mainPropertiesPanel)
	{
		ObjectDataInputField indicatorLabel = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL));
		ObjectDataInputField indicatorShortLabel = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_SHORT_LABEL,STD_SHORT));
		ObjectDataInputField indicatorPriority = addField(createRatingChoiceField(ObjectType.INDICATOR,  new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
		ObjectDataInputField monitoringStatus = addField(createChoiceField(ObjectType.INDICATOR,  new IndicatorStatusRatingQuestion(Indicator.TAG_STATUS)));
		ObjectDataInputField ratingSource = addField(createRatingChoiceField(ObjectType.INDICATOR,  new RatingSourceQuestion(Indicator.TAG_RATING_SOURCE)));
		
		JPanel box2 = createGridLayoutPanel(1,2);
		box2.add(createColumnJPanel(indicatorLabel));
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(createColumnJPanel(indicatorShortLabel));
		
		JPanel boxIndrPrty = createGridLayoutPanel(1,5);
		boxIndrPrty.add(createColumnJPanel(indicatorPriority));
		boxIndrPrty.add(Box.createHorizontalStrut(STD_SPACE_20));
		boxIndrPrty.add(createColumnJPanel(monitoringStatus));
		boxIndrPrty.add(Box.createVerticalStrut((3*STD_SPACE_20)));
		
		JPanel box3 = createGridLayoutPanel(0,2);
		box3.add(indicatorThreshold.getComponent());
		JPanel box4 = createGridLayoutPanel(2,1);
		Box optionPanel = createOptionGroup();
		box4.add(optionPanel);
		box4.add(createColumnJPanel(ratingSource, ratingSource.getComponent().getPreferredSize()));
		box3.add(box4);		
		
		JPanel mainIndicatorPanel = createGridLayoutPanel(3, 1);
		addBoldedTextBorder(mainIndicatorPanel, "Indicator");
		mainIndicatorPanel.add(box2);
		mainIndicatorPanel.add(boxIndrPrty);
		mainIndicatorPanel.add(box3);
		mainPropertiesPanel.add(mainIndicatorPanel);
	}	

	private void createIndicatorFutureStatusPanel(JPanel mainPropertiesPanel)
	{
		ObjectDataInputField futureStatusRating = addField(createRatingChoiceField(ObjectType.INDICATOR, new StatusQuestion(Indicator.TAG_FUTURE_STATUS_RATING)));
		ObjectDataInputField futureStatusDate = addField(createDateChooserField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_DATE));
		ObjectDataInputField futureStatusSummary = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_SUMMARY,STD_SHORT));
		ObjectDataInputField futureStatusDetail = addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_DETAIL,NARROW_DETAILS));

		JPanel box8 = createGridLayoutPanel(1,5);
		addBoldedTextBorder(box8, "Future Status");
		box8.add(createColumnJPanel(futureStatusRating));
		box8.add(createColumnJPanel(futureStatusDate));
		box8.add(Box.createHorizontalStrut(STD_SPACE_20));
		box8.add(createColumnJPanelWithIcon(futureStatusSummary, new GoalIcon()));
		box8.add(createColumnJPanel(futureStatusDetail));
		mainPropertiesPanel.add(box8);		
	}

	private Box createOptionGroup()
	{
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.add(new PanelTitleLabel(EAM.text("OPTIONS")));
		box.add(createCheckBox(EAM.text("Show Thresholds"), new OptionThresholdChangeListener()));
		box.add(createCheckBox(EAM.text("Show Status"), new OptionStatusChangeListener()));
		return box;
	}

	private JCheckBox createCheckBox(String text, ItemListener listener)
	{
		JCheckBox check = new PanelCheckBox(text,true);
		check.addItemListener(listener);
		return check;
	}

	class OptionStatusChangeListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			JCheckBox check = (JCheckBox)event.getSource();
			indicatorThreshold.showStatus(check.isSelected());
		}
	}
	
	class OptionThresholdChangeListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			JCheckBox check = (JCheckBox)event.getSource();
			indicatorThreshold.showThreshold(check.isSelected());
		}
	}
	
	public ObjectDataInputField createViabilityRatingsTableField(int objectType, ChoiceQuestion question)
	{
		return new ViabilityRatingsTableField(getProject(), objectType, getObjectIdForType(objectType), question);
	}	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		final boolean areIndicatorMeasurementFields = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_MEASUREMENT_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_MEASUREMENT_STATUS) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_MEASUREMENT_TREND) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_RATING);
		
		if (areIndicatorMeasurementFields)
		{
			CommandSetObjectData  command = (CommandSetObjectData) event.getCommand();
			ORef ref = new ORef(command.getObjectType(), command.getObjectId());
			indicatorThreshold.setIconRowObject(ref);
		}
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		targetViabilityKeaPropertiesPanel.setObjectRefs(orefsToUse);	
	}

	private ViabilityRatingsTableField indicatorThreshold;
	private static final int STD_SPACE_20 = 20;
	private static final int NARROW_DETAILS = 30;
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
}
