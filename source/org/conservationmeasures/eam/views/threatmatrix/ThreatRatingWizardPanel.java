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
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class ThreatRatingWizardPanel extends WizardPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse) throws Exception
	{
		view = viewToUse;
		
		ThreatRatingWizardChooseBundle chooseBundleStep = new ThreatRatingWizardChooseBundle(this);
		
		steps = new ThreatRatingWizardStep[STEP_COUNT];
		steps[OVERVIEW] = new ThreatRatingWizardOverviewStep(this);
		steps[CHOOSE_BUNDLE] = chooseBundleStep;
		steps[SET_SCOPE] = ThreatRatingWizardScopeStep.create(this);
		steps[SET_SEVERITY] = ThreatRatingWizardSeverityStep.create(this);
		steps[SET_IRREVERSIBILITY] = ThreatRatingWizardIrreversibilityStep.create(this);
		steps[CHECK_BUNDLE] = new ThreatRatingWizardCheckBundleStep(this);
		steps[CHECK_TOTALS] = new ThreatRatingWizardCheckTotalsStep(this);

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
	
	public void next() throws Exception
	{
		int nextStep = currentStep + 1;
		if(nextStep >= steps.length)
			nextStep = 0;
		
		setStep(nextStep);
	}
	
	public void previous() throws Exception
	{
		int nextStep = currentStep - 1;
		if(nextStep < 0)
			return;
		
		setStep(nextStep);
	}
	
	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		steps[currentStep].refresh();
		setContents(steps[currentStep]);
	}
	
	public void refresh() throws Exception
	{
		for(int i = 0; i < steps.length; ++i)
			steps[i].refresh();
		validate();
	}
	
	ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getView().getProject().getThreatRatingFramework();
		return framework;
	}
	
	private static final int OVERVIEW = 0;
	static final int CHOOSE_BUNDLE = 1;
	private static final int SET_SCOPE = 2;
	private static final int SET_SEVERITY = 3;
	private static final int SET_IRREVERSIBILITY = 4;
	private static final int CHECK_BUNDLE = 5;
	static final int CHECK_TOTALS = 6;
	
	private static final int STEP_COUNT = 7;
	
	ThreatMatrixView view;
	ThreatRatingWizardStep[] steps;
	int currentStep;
	ThreatRatingBundle selectedBundle;
}

