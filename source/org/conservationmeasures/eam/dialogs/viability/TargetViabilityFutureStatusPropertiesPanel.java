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
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class TargetViabilityFutureStatusPropertiesPanel extends ObjectDataInputPanelSpecial
{
	public TargetViabilityFutureStatusPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		
	
		JPanel mainPropertiesPanel = new JPanel();
		createIndicatorFutureStatusPanel(mainPropertiesPanel);
		addFieldComponent(mainPropertiesPanel);
		updateFieldsFromProject();
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
}
