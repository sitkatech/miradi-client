/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import javax.swing.Icon;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;

public class IndicatorFutureStatusSubPanel extends ObjectDataInputPanel
{
	public IndicatorFutureStatusSubPanel(Project project)
	{
		this(project, new ORef(Indicator.getObjectType(), BaseId.INVALID));
	}
	
	public IndicatorFutureStatusSubPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);

		PanelFieldLabel dateLabel = new PanelFieldLabel(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_DATE);
		ObjectDataInputField futureStatusDateField = createDateChooserField(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_DATE);
		PanelFieldLabel valueLabel = new PanelFieldLabel(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_SUMMARY);
		ObjectDataInputField futureStatusSummaryField = createMediumStringField(ObjectType.INDICATOR,  Indicator.TAG_FUTURE_STATUS_SUMMARY);

		futureStatusRatingLabelField = new PanelFieldLabel(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_RATING);
		futureStatusRatingField = createRatingChoiceField(ObjectType.INDICATOR, Indicator.TAG_FUTURE_STATUS_RATING, new StatusQuestion());
		
		futureStatusLabel = new PanelTitleLabel();
		Object[] components = new Object[] {
				dateLabel, futureStatusDateField, 
				valueLabel, futureStatusSummaryField, 
				futureStatusRatingLabelField, futureStatusRatingField, 
				};
		addFieldsOnOneLine(futureStatusLabel, components);

		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_DETAIL));
		addField(createMultilineField(Indicator.getObjectType(), Indicator.TAG_FUTURE_STATUS_COMMENT));
		
		updateFieldsFromProject();
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
	
		boolean isRatingFieldVisible = true;
		String futureStatusText = EAM.text("Desired Future Status");
		
		ORefList refList = new ORefList(orefsToUse);
		ORef foundKeaRef = refList.getRefForType(KeyEcologicalAttribute.getObjectType());
		if (foundKeaRef.isInvalid())
		{
			isRatingFieldVisible = false;
			futureStatusText = EAM.text("Desired Future Value");
		}
		
		futureStatusLabel.setIcon(getGoalOrObjectiveIcon(refList));
		futureStatusLabel.setText(futureStatusText);
		setVisibilityOfRatingField(isRatingFieldVisible);
	}
	
	private Icon getGoalOrObjectiveIcon(ORefList selectedHierarchy)
	{
		ORef foundTargetRef = selectedHierarchy.getRefForType(Target.getObjectType());
		if(foundTargetRef.isInvalid())
			return new ObjectiveIcon();

		return new GoalIcon();
	}

	private void setVisibilityOfRatingField(boolean isVisible)
	{
		futureStatusRatingLabelField.setVisible(isVisible);
		futureStatusRatingField.setVisible(isVisible);
	}

	public String getPanelDescription()
	{
		return EAM.text("Desired Value/Status");
	}
	
	private ObjectDataInputField futureStatusRatingField;
	private PanelTitleLabel futureStatusRatingLabelField;
	private PanelTitleLabel futureStatusLabel;
}
