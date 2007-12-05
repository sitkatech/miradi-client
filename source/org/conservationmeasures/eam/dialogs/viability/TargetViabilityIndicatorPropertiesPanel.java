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
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelCheckBox;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
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
import org.conservationmeasures.eam.questions.StatusQuestion;

public class TargetViabilityIndicatorPropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityIndicatorPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
	
		indicatorThreshold = (ViabilityRatingsTableField) addField(createViabilityRatingsTableField(ObjectType.INDICATOR,  new StatusQuestion(Indicator.TAG_INDICATOR_THRESHOLD)));
		createIndicatorPropertiesPanel();
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

	public ObjectDataInputField createViabilityRatingsTableField(int objectType, ChoiceQuestion question)
	{
		return new ViabilityRatingsTableField(getProject(), objectType, getObjectIdForType(objectType), question);
	}	

	private void createIndicatorPropertiesPanel() throws Exception
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
		
		ObjectDataInputField futureStatusRating = addField(createRatingChoiceField(ObjectType.INDICATOR, new StatusQuestion(Indicator.TAG_FUTURE_STATUS_RATING)));
		ObjectDataInputField futureStatusDate = addField(createDateChooserField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_DATE));
		ObjectDataInputField futureStatusSummary = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_SUMMARY,STD_SHORT));
		ObjectDataInputField futureStatusDetail = addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_DETAIL,NARROW_DETAILS));

		JPanel box8 = createGridLayoutPanel(1,5);
		addBoldedTextBorder(box8, EAM.text("Future Status"));
		box8.add(createColumnJPanel(futureStatusDate));
		box8.add(createColumnJPanel(futureStatusRating));
		box8.add(Box.createHorizontalStrut(STD_SPACE_20));
		box8.add(createColumnJPanelWithIcon(futureStatusSummary, new GoalIcon()));
		box8.add(createColumnJPanel(futureStatusDetail));

		JPanel mainIndicatorPanel = createGridLayoutPanel(3, 1);
		addBoldedTextBorder(mainIndicatorPanel, "Indicator");
		mainIndicatorPanel.add(box2);
		mainIndicatorPanel.add(boxIndrPrty);
		mainIndicatorPanel.add(box3);
		mainIndicatorPanel.add(box8);
		
		setLayout(new OneColumnGridLayout());
		add(mainIndicatorPanel);
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
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		final boolean areIndicatorMeasurementFields = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_STATUS) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.MEASUREMENT,Measurement.TAG_TREND) ||
			
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_FUTURE_STATUS_RATING);
		
		if (areIndicatorMeasurementFields)
		{
			CommandSetObjectData  command = (CommandSetObjectData) event.getCommand();
			indicatorThreshold.setIconRowObject(command.getObjectORef());
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
	
	class OptionStatusChangeListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			JCheckBox check = (JCheckBox)event.getSource();
			indicatorThreshold.showStatus(check.isSelected());
		}
	}
	
	private ViabilityRatingsTableField indicatorThreshold;
}
