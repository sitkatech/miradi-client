/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ViabilityRatingsTableField;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.MeasurementStatusQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.GridLayoutPlus;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, Factor factor) throws Exception
	{
		this(projectToUse, actions, factor.getId());
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.FACTOR, idToShow));
		ObjectDataInputField keaLabel = addField(createStringField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL));
		ObjectDataInputField keaDesc = addField(createMultilineField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_DESCRIPTION));
		ObjectDataInputField keaType = addField(createChoiceField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE)));
		
		ObjectDataInputField indicatorLabel = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL));
		ObjectDataInputField indicatorShortLabel = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_SHORT_LABEL,STD_SHORT));
		ObjectDataInputField indicatorPriority = addField(createRatingChoiceField(ObjectType.INDICATOR,  new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
		ObjectDataInputField monitoringStatus = addField(createChoiceField(ObjectType.INDICATOR,  new IndicatorStatusRatingQuestion(Indicator.TAG_STATUS)));
		ObjectDataInputField measurementStatus = addField(createRatingChoiceField(ObjectType.INDICATOR, new MeasurementStatusQuestion(Indicator.TAG_MEASUREMENT_STATUS)));  
		ObjectDataInputField measurementTrend = addField(createChoiceField(ObjectType.INDICATOR, new TrendQuestion(Indicator.TAG_MEASUREMENT_TREND)));
		ObjectDataInputField measurementDate = addField(createDateChooserField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_DATE));
		ObjectDataInputField measurementSummary = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_SUMMARY,STD_SHORT));
		ObjectDataInputField measurementDetail = addField(createMultilineField(ObjectType.INDICATOR, Indicator.TAG_MEASUREMENT_DETAIL,STD_SHORT));
		ObjectDataInputField measureementStatusConfidence = addField(createChoiceField(ObjectType.INDICATOR,  new StatusConfidenceQuestion(Indicator.TAG_MEASUREMENT_STATUS_CONFIDENCE)));
		ObjectDataInputField ratingSource = addField(createRatingChoiceField(ObjectType.INDICATOR,  new RatingSourceQuestion(Indicator.TAG_RATING_SOURCE)));
		indicatorThreshold = (ViabilityRatingsTableField)
			addField(createViabilityRatingsTableField(ObjectType.INDICATOR,  new MeasurementStatusQuestion(Indicator.TAG_INDICATOR_THRESHOLDS)));
		
		ObjectDataInputField desiredStatus = addField(createRatingChoiceField(ObjectType.GOAL, new MeasurementStatusQuestion(Goal.TAG_DESIRED_STATUS)));
		ObjectDataInputField byWhen = addField(createDateChooserField(ObjectType.GOAL, Goal.TAG_BY_WHEN));
		ObjectDataInputField desiredSummary = addField(createStringField(ObjectType.GOAL, Goal.TAG_DESIRED_SUMMARY,STD_SHORT));
		ObjectDataInputField desiredDetail = addField(createMultilineField(ObjectType.GOAL, Goal.TAG_DESIRED_DETAIL,STD_SHORT));
		

		JPanel main = new JPanel(new GridLayoutPlus(0, 1));

		JPanel mainGridPanel = createGridLayoutPanel(0,2);
		
		Box box1 = Box.createHorizontalBox();
		box1.add(keaLabel.getComponent());
		box1.add(Box.createHorizontalStrut(STD_SPACE_20));
		box1.add(createLabel(keaType));
		box1.add(Box.createHorizontalStrut(STD_SPACE_20));
		box1.add(keaType.getComponent());
		mainGridPanel.add(createLabel(keaLabel));
		mainGridPanel.add(box1);
		
		Box keaDescPanel = Box.createHorizontalBox();
		keaDescPanel.add(createLabel(keaDesc));
		keaDescPanel.add(Box.createHorizontalStrut(STD_SPACE_20));
		keaDescPanel.add(keaDesc.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		mainGridPanel.add(new UiLabel(""));
		mainGridPanel.add(keaDescPanel);
		
		Box box2 = Box.createHorizontalBox();
		box2.add(indicatorLabel.getComponent());
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(createLabel(indicatorShortLabel));
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(indicatorShortLabel.getComponent());
		mainGridPanel.add(createLabel(indicatorLabel));
		mainGridPanel.add(box2);

		Box boxIndrPrty = Box.createHorizontalBox();
		boxIndrPrty.add(createLabel(indicatorPriority));
		boxIndrPrty.add(Box.createHorizontalStrut(STD_SPACE_20));
		boxIndrPrty.add(indicatorPriority.getComponent());
		boxIndrPrty.add(Box.createHorizontalStrut(STD_SPACE_20));
		boxIndrPrty.add(Box.createHorizontalStrut(STD_SPACE_20));
		boxIndrPrty.add(createLabel(monitoringStatus));
		boxIndrPrty.add(Box.createHorizontalStrut(STD_SPACE_20));
		boxIndrPrty.add(monitoringStatus.getComponent());
		mainGridPanel.add(new UiLabel(""));
		mainGridPanel.add(boxIndrPrty);


		mainGridPanel.add(createLabel(indicatorThreshold));
		JPanel box3 = createGridLayoutPanel(0,2);
		box3.add(indicatorThreshold.getComponent());
		JPanel box4 = createGridLayoutPanel(2,1);
		Box optionPanel = createOptionGroup();
		box4.add(optionPanel);
		box4.add(createColumnBox(ratingSource));
		box3.add(box4);
		mainGridPanel.add(box3);
		

		Dimension col1Model = desiredStatus.getComponent().getPreferredSize();
		Dimension col2Model = measureementStatusConfidence.getComponent().getPreferredSize();
		Dimension col3Model = desiredSummary.getComponent().getPreferredSize();
		
		
		Box box5 = Box.createHorizontalBox();
		box5.add(createColumnJPanel(measurementStatus, col1Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumnJPanel(measurementDate, col2Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumnJPanelWithIcon(measurementSummary,new IndicatorIcon(), col3Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumnBox(measurementDetail));
		mainGridPanel.add(new UiLabel(EAM.text("Current Status")));
		mainGridPanel.add(box5);

		Box box6 = Box.createHorizontalBox();
		box6.add(createColumnJPanel(measureementStatusConfidence, col2Model));
		box6.add(Box.createHorizontalStrut(STD_SPACE_20));
	
		box6.add(createColumnJPanel(measurementTrend, col1Model));
		mainGridPanel.add(new UiLabel(""));
		mainGridPanel.add(box6);
		

		Box box7 = Box.createHorizontalBox();
		box7.add(createColumnJPanel(desiredStatus, col1Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumnJPanel(byWhen, col2Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumnJPanelWithIcon(desiredSummary, new GoalIcon(), col3Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumnBox(desiredDetail));
		mainGridPanel.add(new UiLabel(EAM.text("Future Status/Goal")));
		mainGridPanel.add(box7);

		main.add(mainGridPanel);

		addFieldComponent(main);
		updateFieldsFromProject();
	}
	
	private Box createOptionGroup()
	{
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.add(new UiLabel(EAM.text("OPTIONS")));
		box.add(createCheckBox(EAM.text("Show Thresholds"), new OptionThresholdChangeListener()));
		box.add(createCheckBox(EAM.text("Show Status"), new OptionStatusChangeListener()));
		return box;
	}

	private JCheckBox createCheckBox(String text, ItemListener listener)
	{
		JCheckBox check = new JCheckBox(text,true);
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
	

	public void setObjectRefs(Vector orefsToUse)
	{
		indicatorThreshold.setIconRowObject(null);
		BaseId objectId = getObjectIdForTypeInThisList(ObjectType.INDICATOR, orefsToUse);
		if (!objectId.isInvalid())
			orefsToUse = insertIndicatorsGoalIntoCurrentSelectedObjectList(orefsToUse, objectId);
		super.setObjectRefs(orefsToUse);
		indicatorThreshold.repaint();
	}

	private Vector insertIndicatorsGoalIntoCurrentSelectedObjectList(Vector orefsToUse, BaseId objectId)
	{
		Indicator indicator = (Indicator)getProject().findObject(ObjectType.INDICATOR, objectId);
		try
		{
			IdList list = new IdList(indicator.getData(Indicator.TAG_GOAL_IDS));
			if (list.size()!=0)
				
			{
				ORef ref = new ORef(ObjectType.GOAL,list.get(0));
				orefsToUse.insertElementAt(ref,0);
				indicatorThreshold.setIconRowObject(new ORef(ObjectType.INDICATOR, objectId));
				indicatorThreshold.setIconRowObject(ref);
			}
			else
				EAM.logError("No Goals found for EKA Indicator:" + objectId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return orefsToUse;
	}
	
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		final boolean areIndicatorMeasurementFields = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_MEASUREMENT_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.INDICATOR,Indicator.TAG_MEASUREMENT_STATUS);
		
		final boolean areGoalDesireFields = 
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.GOAL,Goal.TAG_DESIRED_SUMMARY) ||
			event.isSetDataCommandWithThisTypeAndTag(ObjectType.GOAL,Goal.TAG_DESIRED_STATUS);
		
		if (areIndicatorMeasurementFields || areGoalDesireFields)
		{
			CommandSetObjectData  command = (CommandSetObjectData) event.getCommand();
			ORef ref = new ORef(command.getObjectType(), command.getObjectId());
			indicatorThreshold.setIconRowObject(ref);
		}
	}
	
	private ViabilityRatingsTableField indicatorThreshold;
	private static final int STD_SPACE_20 = 20;

}
