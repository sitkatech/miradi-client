/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.indicator;

import org.conservationmeasures.eam.actions.ActionEditMethods;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;
import org.conservationmeasures.eam.utils.ObjectsActionButton;

public class IndicatorPropertiesPanel extends ObjectDataInputPanel
{
	public IndicatorPropertiesPanel(Project projectToUse) throws Exception
	{
		this(projectToUse, new IndicatorId(BaseId.INVALID.asInt()));
	}
	
	public IndicatorPropertiesPanel(Project projectToUse, IndicatorId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.INDICATOR, idToShow);

		ObjectDataInputField shortLabelField = createStringField(Indicator.getObjectType(), Indicator.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createStringField(Indicator.getObjectType(), Indicator.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Indicator"), new IndicatorIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createRatingChoiceField(new PriorityRatingQuestion(Indicator.TAG_PRIORITY)));
		addField(createRatingChoiceField(new IndicatorStatusRatingQuestion(Indicator.TAG_STATUS)));
		
		ObjectsAction editMethods = EAM.getMainWindow().getActions().getObjectsAction(ActionEditMethods.class);
		PanelButton editMethodsButton = new ObjectsActionButton(editMethods, getPicker());
		addFieldWithEditButton(EAM.text("Label|Methods"), createReadonlyTextField(Indicator.PSEUDO_TAG_METHODS), editMethodsButton);
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_FACTOR));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_STRATEGIES));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Indicator.PSEUDO_TAG_TARGETS));
		
		addField(createDateChooserField(Measurement.getObjectType(), Measurement.TAG_DATE));
		addField(createStringField(Measurement.getObjectType(), Measurement.TAG_SUMMARY));
		addField(createMultilineField(Measurement.getObjectType(), Measurement.TAG_DETAIL));
		addField(createIconChoiceField(Measurement.getObjectType(), new TrendQuestion(Measurement.TAG_TREND)));
				
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Indicator Properties");
	}

}
