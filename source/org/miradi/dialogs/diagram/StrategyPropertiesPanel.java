/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;


import org.miradi.actions.ActionEditStrategyProgressReports;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.StrategyIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.utils.ObjectsActionButton;

public class StrategyPropertiesPanel extends ObjectDataInputPanel
{
	public StrategyPropertiesPanel(MainWindow mainWindow)
	{
		super(mainWindow.getProject(), Strategy.getObjectType(), BaseId.INVALID);
		
		ObjectDataInputField shortLabelField = createStringField(Strategy.getObjectType(), Strategy.TAG_SHORT_LABEL,10);
		ObjectDataInputField labelField = createStringField(Strategy.getObjectType(), Strategy.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Strategy"), new StrategyIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createChoiceField(Strategy.getObjectType(), Strategy.TAG_TAXONOMY_CODE, new StrategyTaxonomyQuestion()));
		
		ObjectDataInputField impactField = createRatingChoiceField(Strategy.TAG_IMPACT_RATING, new StrategyImpactQuestion());
		ObjectDataInputField feasibilityField = createRatingChoiceField(Strategy.TAG_FEASIBILITY_RATING, new StrategyFeasibilityQuestion());
		ObjectDataInputField prioritySummaryField = createReadOnlyChoiceField(Strategy.PSEUDO_TAG_RATING_SUMMARY, new StrategyRatingSummaryQuestion());

		addFieldsOnOneLine(EAM.text("Priority"), new ObjectDataInputField[] {impactField, feasibilityField, prioritySummaryField});
		
		ObjectsActionButton editProgressReportButton = createObjectsActionButton(mainWindow.getActions().getObjectsAction(ActionEditStrategyProgressReports.class), getPicker());
		ObjectDataInputField readOnlyProgressReportsList = createReadOnlyObjectList(Strategy.getObjectType(), Strategy.TAG_PROGRESS_REPORT_REFS);
		addFieldWithEditButton(EAM.text("Progress"), readOnlyProgressReportsList, editProgressReportButton);

		BudgetOverrideSubPanel budgetSubPanel = new BudgetOverrideSubPanel(getProject(), new ORef(Strategy.getObjectType(), BaseId.INVALID));
		addSubPanel(budgetSubPanel);

		addLabel(EAM.text("Budget"));
		addFieldComponent(budgetSubPanel);
		
		addField(createMultilineField(Strategy.TAG_COMMENT));
		
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_TARGETS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_DIRECT_THREATS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_GOALS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_OBJECTIVES));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Strategy Properties");
	}

}
