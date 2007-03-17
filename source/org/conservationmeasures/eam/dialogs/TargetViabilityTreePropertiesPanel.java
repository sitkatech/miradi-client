/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ViabilityRatingsTableField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
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
		
		ObjectDataInputField indicatorLabel = addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL,STD_SPACE_20));
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
		
		
		ObjectDataInputField desiredStatus = addField(createRatingChoiceField(ObjectType.GOAL, new MeasurementStatusQuestion(Goal.TAG_DESIRED_STATUS)));
		ObjectDataInputField byWhen = addField(createDateChooserField(ObjectType.GOAL, Goal.TAG_BY_WHEN));
		ObjectDataInputField desiredSummary = addField(createStringField(ObjectType.GOAL, Goal.TAG_DESIRED_SUMMARY,STD_SHORT));
		ObjectDataInputField desiredDetail = addField(createMultilineField(ObjectType.GOAL, Goal.TAG_DESIRED_DETAIL,STD_SHORT));
		
		ObjectDataInputField indicatorThreshold = 
			addField(createViabilityRatingsTableField(
					ObjectType.INDICATOR,  
					new MeasurementStatusQuestion(Indicator.TAG_INDICATOR_THRESHOLDS), 
					measurementSummary, 
					desiredSummary
					));
		
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
		
		JPanel keaDescPanel = new JPanel(new BorderLayout());
		mainGridPanel.add(createLabel(keaDesc));
		keaDescPanel.add(keaDesc.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		mainGridPanel.add(keaDescPanel);
		
		Box box2 = Box.createHorizontalBox();
		box2.add(createLabel(indicatorShortLabel));
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(indicatorShortLabel.getComponent());
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(createLabel(indicatorLabel));
		box2.add(Box.createHorizontalStrut(STD_SPACE_20));
		box2.add(indicatorLabel.getComponent());
		mainGridPanel.add(new UiLabel("Indicator"));
		mainGridPanel.add(box2);

		Box boxIndrPrty = Box.createHorizontalBox();
		normalizeSize(indicatorPriority,indicatorPriority);
		boxIndrPrty.add(indicatorPriority.getComponent());
		boxIndrPrty.add(Box.createHorizontalGlue());
		mainGridPanel.add(createLabel(indicatorPriority));
		mainGridPanel.add(boxIndrPrty);


		Box boxMonStatus = Box.createHorizontalBox();
		normalizeSize(monitoringStatus,monitoringStatus);
		boxMonStatus.add(monitoringStatus.getComponent());
		mainGridPanel.add(createLabel(monitoringStatus));
		mainGridPanel.add(boxMonStatus);
		
		
		mainGridPanel.add(createLabel(indicatorThreshold));
		JPanel box3 = createGridLayoutPanel(0,2);
		box3.add(indicatorThreshold.getComponent());
		JPanel box4 = createGridLayoutPanel(2,1);
		Box optionPanel = createOptionGroup();
		box4.add(optionPanel);
		box4.add(createColumn(ratingSource));
		box3.add(box4);
		mainGridPanel.add(box3);
		

		Dimension col1Model = desiredStatus.getComponent().getPreferredSize();
		Dimension col2Model = byWhen.getComponent().getPreferredSize();
		Dimension col3Model = desiredSummary.getComponent().getPreferredSize();
		
		
		Box box5 = Box.createHorizontalBox();
		box5.add(createColumn(measurementStatus, col1Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumn(measurementDate, col2Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumn(measurementSummary, col3Model));
		box5.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box5.add(createColumn(measurementDetail));
		mainGridPanel.add(new UiLabel("Current Status"));
		mainGridPanel.add(box5);

		Box box6 = Box.createHorizontalBox();
		box6.add(createColumn(measurementTrend, col1Model));
		box6.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box6.add(createColumn(measureementStatusConfidence, col2Model));
		mainGridPanel.add(new UiLabel(""));
		mainGridPanel.add(box6);
		

		Box box7 = Box.createHorizontalBox();
		box7.add(createColumn(desiredStatus, col1Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumn(byWhen, col2Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumn(desiredSummary, col3Model));
		box7.add(Box.createHorizontalStrut(STD_SPACE_20));
		
		box7.add(createColumn(desiredDetail));
		mainGridPanel.add(new UiLabel(""));
		mainGridPanel.add(box7);

		main.add(mainGridPanel);

		addFieldComponent(main);
		updateFieldsFromProject();
	}
	
	private Box createOptionGroup()
	{
		ButtonGroup bg = new ButtonGroup();
		JRadioButton statusButton = new JRadioButton("Show Status");
		JRadioButton thresholdsButton = new JRadioButton("Show Thresholds");
		bg.add(thresholdsButton);
		bg.add(statusButton);
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		box.add(new UiLabel("OPTIONS"));
		box.add(statusButton);
		box.add(thresholdsButton);
		return box;
	}

	public ObjectDataInputField createViabilityRatingsTableField(int objectType, ChoiceQuestion question, ObjectDataInputField measurementSummary, ObjectDataInputField desiredSummary)
	{
		return new ViabilityRatingsTableField(getProject(), objectType, getObjectIdForType(objectType), question, measurementSummary, desiredSummary);
	}
	
	private void normalizeSize(ObjectDataInputField target, ObjectDataInputField source)
	{
		target.getComponent().setMaximumSize(source.getComponent().getPreferredSize());
	}

	public void setObjectRefs(Vector orefsToUse)
	{
		BaseId objectId = getObjectIdForTypeInThisList(ObjectType.INDICATOR, orefsToUse);
		if (!objectId.isInvalid())
			orefsToUse = insertIndicatorsGoal(orefsToUse, objectId);
		super.setObjectRefs(orefsToUse);
	}

	private Vector insertIndicatorsGoal(Vector orefsToUse, BaseId objectId)
	{
		Indicator indicator = (Indicator)getProject().findObject(ObjectType.INDICATOR, objectId);
		try
		{
			IdList list = new IdList(indicator.getData(Indicator.TAG_GOAL_IDS));
			if (list.size()!=0)
				orefsToUse.insertElementAt(new ORef(ObjectType.GOAL,list.get(0)),0);
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
	
	private static final int STD_SPACE_20 = 20;



}
