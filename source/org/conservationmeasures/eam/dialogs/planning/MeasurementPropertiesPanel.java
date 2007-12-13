/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
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
		
		addField(createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE));
		addField(createRatingChoiceField(ObjectType.MEASUREMENT, new StatusQuestion(Measurement.TAG_STATUS)));  
		addField(createIconChoiceField(ObjectType.MEASUREMENT, new TrendQuestion(Measurement.TAG_TREND)));
		addField(createStringField(ObjectType.MEASUREMENT, Measurement.TAG_SUMMARY,STD_SHORT));
		addField(createMultilineField(ObjectType.MEASUREMENT, Measurement.TAG_DETAIL));
		addField(createChoiceField(ObjectType.MEASUREMENT,  new StatusConfidenceQuestion(Measurement.TAG_STATUS_CONFIDENCE)));

		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Measurement Properties");
	}
}
