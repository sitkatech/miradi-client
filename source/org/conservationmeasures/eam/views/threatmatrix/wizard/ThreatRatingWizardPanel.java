/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix.wizard;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpReviewModelAndAdjust;
import org.conservationmeasures.eam.actions.jump.ActionJumpStratPlanWelcome;
import org.conservationmeasures.eam.actions.jump.ActionJumpThreatRatingWizardCheckTotals;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class ThreatRatingWizardPanel extends WizardPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse) throws Exception
	{
		super(viewToUse.getMainWindow());
		view = viewToUse;
		actions = getMainWindow().getActions();
		
		OVERVIEW = addStep(new ThreatRatingWizardOverviewStep(this));
		CHOOSE_BUNDLE = addStep(new ThreatRatingWizardChooseBundle(this));
		addStep(ThreatRatingWizardScopeStep.create(this));
		addStep(ThreatRatingWizardSeverityStep.create(this));
		addStep(ThreatRatingWizardIrreversibilityStep.create(this));
		addStep(new ThreatRatingWizardCheckBundleStep(this));
		CHECK_TOTALS = addStep(new ThreatRatingWizardCheckTotalsStep(this));

		selectBundle(null);
		setStep(OVERVIEW);
	}

	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		selectedBundle = bundle;
		refresh();
	}

	public ThreatRatingBundle getSelectedBundle() throws Exception
	{
		return selectedBundle;
	}
	
	public void bundleWasClicked(ThreatRatingBundle bundle) throws Exception
	{
		view.selectBundle(bundle);
	}
	
	public ThreatMatrixView getView()
	{
		return view;
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpRankDirectThreats.class))
			setStep(OVERVIEW);
		else if(stepMarker.equals(ThreatRatingWizardChooseBundle.class))
			setStep(CHOOSE_BUNDLE);
		else if(stepMarker.equals(ThreatRatingWizardCheckTotalsStep.class))
			setStep(CHECK_TOTALS);
		else if (stepMarker.equals(ActionJumpThreatRatingWizardCheckTotals.class))
			setStep(CHECK_TOTALS);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}

	public void next() throws Exception
	{
		if (currentStep == CHECK_TOTALS)
			actions.get(ActionJumpStratPlanWelcome.class).doAction();
		
		super.next();
	}
	
	public void previous() throws Exception
	{
		if (currentStep == OVERVIEW)
			actions.get(ActionJumpReviewModelAndAdjust.class).doAction();
			
		super.previous();
	}
	
	ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getView().getProject().getThreatRatingFramework();
		return framework;
	}
	
	private int OVERVIEW;
	private int CHOOSE_BUNDLE;
	private int CHECK_TOTALS;
	
	ThreatMatrixView view;
	ThreatRatingBundle selectedBundle;
	Actions actions;
}

