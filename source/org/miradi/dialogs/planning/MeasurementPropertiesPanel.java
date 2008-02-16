/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.MeasurementIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;

public class MeasurementPropertiesPanel extends ObjectDataInputPanel
{
	public MeasurementPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, Measurement.getObjectType(), BaseId.INVALID);

		ObjectDataInputField dateField = createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE);
		ObjectDataInputField summaryField = createMediumStringField(Measurement.TAG_SUMMARY);
		addFieldsOnOneLine(EAM.text("Measurement"), new MeasurementIcon(), new ObjectDataInputField[]{dateField, summaryField,});

		addField(createMultilineField(Measurement.TAG_DETAIL));		
		
		statusField = createRatingChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_STATUS, new StatusQuestion());
		statusLabelField = new PanelFieldLabel(statusField.getObjectType(), statusField.getTag());
		ObjectDataInputField trendField = createIconChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_TREND, new TrendQuestion());
		PanelTitleLabel trendLabelField = new PanelFieldLabel(trendField.getObjectType(), trendField.getTag());
		addFieldsOnOneLine(EAM.text("Current Status"), new Object[]{statusField, statusLabelField, trendField, trendLabelField});

		addField(createChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_STATUS_CONFIDENCE, new StatusConfidenceQuestion()));
		addField(createMultilineField(ObjectType.MEASUREMENT, Measurement.TAG_COMMENT));

		updateFieldsFromProject();
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
	
		boolean isVisible = true;
		ORef foundRef = new ORefList(orefsToUse).getRefForType(KeyEcologicalAttribute.getObjectType());
		if (foundRef.isInvalid())
			isVisible = false;
			
		setVisibilityOfRatingField(isVisible);	
	}

	private void setVisibilityOfRatingField(boolean isVisible)
	{
		statusLabelField.setVisible(isVisible);
		statusField.setVisible(isVisible);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Measurement Properties");
	}
	
	private ObjectDataInputField statusField;
	private PanelFieldLabel statusLabelField;
}
