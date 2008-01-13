/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.icons.MeasurementIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;

public class MeasurementPropertiesPanel extends ObjectDataInputPanel
{
	public MeasurementPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, Measurement.getObjectType(), BaseId.INVALID);

		ObjectDataInputField dateField = createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE);
		ObjectDataInputField summaryField = createStringField(Measurement.TAG_SUMMARY);
		addFieldsOnOneLine(EAM.text("Measurement"), new MeasurementIcon(), new ObjectDataInputField[]{dateField, summaryField,});

		addField(createMultilineField(Measurement.TAG_DETAIL));		
		
		ObjectDataInputField statusField = createRatingChoiceField(ObjectType.MEASUREMENT, new StatusQuestion(Measurement.TAG_STATUS));
		ObjectDataInputField trendField = createIconChoiceField(ObjectType.MEASUREMENT, new TrendQuestion(Measurement.TAG_TREND));
		addFieldsOnOneLine(EAM.text("Current Status"), new ObjectDataInputField[]{statusField, trendField,});

		addField(createChoiceField(ObjectType.MEASUREMENT,  new StatusConfidenceQuestion(Measurement.TAG_STATUS_CONFIDENCE)));
		addField(createMultilineField(ObjectType.MEASUREMENT, Measurement.TAG_COMMENT));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Measurement Properties");
	}
}
