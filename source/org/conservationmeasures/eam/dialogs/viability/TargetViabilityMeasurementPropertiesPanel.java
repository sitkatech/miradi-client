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
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanelSpecial;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

public class TargetViabilityMeasurementPropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityMeasurementPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
	
		JPanel mainPropertiesPanel = new JPanel();
		createMeasurementPropertiesPanel(mainPropertiesPanel);	
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
}
