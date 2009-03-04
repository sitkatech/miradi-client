/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.planning;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.MeasurementIcon;
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

public class MeasurementPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public MeasurementPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, Measurement.getObjectType());
		createSingleSection(EAM.text("Measurement"));

		PanelTitleLabel measurementLabel = new PanelTitleLabel(EAM.text("Measurement"), new MeasurementIcon());
		PanelTitleLabel dateLabel = new PanelTitleLabel(EAM.text("Date"));
		ObjectDataInputField dateField = createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE);
		PanelTitleLabel valueLabel = new PanelTitleLabel(EAM.text("Value"));
		ObjectDataInputField summaryField = createMediumStringField(Measurement.TAG_SUMMARY);
		addFieldsOnOneLine(measurementLabel, new Object[]{dateLabel, dateField, valueLabel, summaryField,});

		addField(createMultilineField(Measurement.TAG_DETAIL));		
		
		PanelTitleLabel statusLabel = new PanelTitleLabel(EAM.text("Current Status"));
		statusField = createRatingChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_STATUS, new StatusQuestion());
		statusLabelField = new PanelFieldLabel(statusField.getObjectType(), statusField.getTag());
		ObjectDataInputField trendField = createIconChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_TREND, new TrendQuestion());
		PanelTitleLabel trendLabelField = new PanelFieldLabel(trendField.getObjectType(), trendField.getTag());
		addFieldsOnOneLine(statusLabel, new Object[]{statusLabelField, statusField, trendLabelField, trendField});

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
