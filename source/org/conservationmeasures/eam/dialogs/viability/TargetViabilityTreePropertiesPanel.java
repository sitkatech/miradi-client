/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
				
		JPanel mainPropertiesPanel = createGridLayoutPanel(4, 1);
		targetViabilityKeaPropertiesPanel = new TargetViabilityKeaPropertiesPanel(projectToUse, actions);
		targetViabilityIndicatorPropertiesPanel = new TargetViabilityIndicatorPropertiesPanel(projectToUse, actions);
		mainPropertiesPanel.add(targetViabilityKeaPropertiesPanel);
		mainPropertiesPanel.add(targetViabilityIndicatorPropertiesPanel);
		
		createMeasurementPropertiesPanel(mainPropertiesPanel);
		createIndicatorFutureStatusPanel(mainPropertiesPanel);
		
		addFieldComponent(mainPropertiesPanel);
		updateFieldsFromProject();
	}
	
	public void dispose()
	{
		super.dispose();
		targetViabilityKeaPropertiesPanel.dispose();
		targetViabilityIndicatorPropertiesPanel.dispose();
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

	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		targetViabilityKeaPropertiesPanel.setObjectRefs(orefsToUse);
		targetViabilityIndicatorPropertiesPanel.setObjectRefs(orefsToUse);
	}
	
	private static final int NARROW_DETAILS = 30;
	private TargetViabilityKeaPropertiesPanel targetViabilityKeaPropertiesPanel;
	private TargetViabilityIndicatorPropertiesPanel targetViabilityIndicatorPropertiesPanel;
}
