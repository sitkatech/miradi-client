/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import javax.swing.JPanel;

import org.martus.swing.UiLabel;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.MeasurementIcon;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.PrecisionTypeQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MeasurementSchema;

public class MeasurementPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public MeasurementPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, MeasurementSchema.getObjectType());
		
		createSingleSection(EAM.text("Measurement"));
		PanelTitleLabel measurementLabel = new PanelTitleLabel(EAM.text("Measurement"), new MeasurementIcon());
		PanelTitleLabel dateLabel = new PanelTitleLabel(EAM.text("Date"));
		ObjectDataInputField dateField = createDateChooserField(ObjectType.MEASUREMENT, Measurement.TAG_DATE);
		PanelTitleLabel valueLabel = new PanelTitleLabel(EAM.text("Value"));
		ObjectDataInputField summaryField = createExpandableField(Measurement.TAG_SUMMARY);
		PanelTitleLabel unitLabel = new PanelTitleLabel(EAM.text("Unit"));
		ObjectDataInputField readonlyUnitField = createReadonlyShortTextField(IndicatorSchema.getObjectType(), Indicator.TAG_UNIT);
		addFieldsOnOneLine(measurementLabel, new Object[]{dateLabel, dateField, valueLabel, summaryField, unitLabel, readonlyUnitField,});

		addField(createMultilineField(Measurement.TAG_DETAIL));		
		
		PanelTitleLabel statusLabel = new PanelTitleLabel(EAM.text("Current Status"));
		statusField = createRatingChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_STATUS, new StatusQuestion());
		statusLabelField = new PanelFieldLabel(statusField.getObjectType(), statusField.getTag());
		ObjectDataInputField trendField = createDropdownWithIconField(ObjectType.MEASUREMENT, Measurement.TAG_TREND, new TrendQuestion());
		PanelTitleLabel trendLabelField = new PanelFieldLabel(trendField.getObjectType(), trendField.getTag());
		addFieldsOnOneLine(statusLabel, new Object[]{statusLabelField, statusField, trendLabelField, trendField});

		addField(createChoiceField(ObjectType.MEASUREMENT, Measurement.TAG_STATUS_CONFIDENCE, new StatusConfidenceQuestion()));
		ObjectDataInputField sampleSizeField = createNumericField(MeasurementSchema.getObjectType(), Measurement.TAG_SAMPLE_SIZE);
		ObjectDataInputField samplePrecisionField = createNumericField(MeasurementSchema.getObjectType(), Measurement.TAG_SAMPLE_PRECISION);
		ObjectDataInputField samplePrecisionTypeField = createChoiceField(MeasurementSchema.getObjectType(), Measurement.TAG_SAMPLE_PRECISION_TYPE, getQuestion(PrecisionTypeQuestion.class));
		samplingBasedPanel = createFieldPanel(new ObjectDataInputField[]{sampleSizeField, samplePrecisionField, samplePrecisionTypeField, });
		samplingBasedLabel = addHtmlWrappedLabel(EAM.text("Sample"));
		add(samplingBasedPanel);
		
		addField(createMultilineField(ObjectType.MEASUREMENT, Measurement.TAG_COMMENTS));

		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
	
		boolean isVisible = true;
		ORef foundRef = new ORefList(orefsToUse).getRefForType(KeyEcologicalAttributeSchema.getObjectType());
		if (foundRef.isInvalid())
			isVisible = false;
			
		setVisibilityOfRatingField(isVisible);
		setVisibilityOfSampleBasedFields();
	}

	private void setVisibilityOfRatingField(boolean isVisible)
	{
		statusLabelField.setVisible(isVisible);
		statusField.setVisible(isVisible);
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisTypeAndTag(MeasurementSchema.getObjectType(), Measurement.TAG_STATUS_CONFIDENCE))
			setVisibilityOfSampleBasedFields();
	}
	
	private void setVisibilityOfSampleBasedFields()
	{
		final boolean shouldShowSampleBasedFields = areSampleBasedFieldsVisible();
		samplingBasedPanel.setVisible(shouldShowSampleBasedFields);
		samplingBasedLabel.setVisible(shouldShowSampleBasedFields);
	}

	public boolean areSampleBasedFieldsVisible()
	{
		ORef measurementRef = getRefForType(MeasurementSchema.getObjectType());
		if (measurementRef.isInvalid())
			return false;
		
		Measurement measurement = Measurement.find(getProject(), measurementRef);
		return measurement.isSampleBased();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Measurement Properties");
	}
	
	private ObjectDataInputField statusField;
	private PanelFieldLabel statusLabelField;
	
	private JPanel samplingBasedPanel;
	private UiLabel samplingBasedLabel;
}
