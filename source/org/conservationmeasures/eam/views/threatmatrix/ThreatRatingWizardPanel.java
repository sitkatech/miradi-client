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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatRatingWizardPanel extends JPanel
{
	public ThreatRatingWizardPanel(ThreatMatrixView viewToUse) throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		
		ThreatRatingWizardChooseBundle chooseBundleStep = new ThreatRatingWizardChooseBundle(this);
		
		steps = new ThreatRatingWizardStep[3];
		steps[CHOOSE_BUNDLE] = chooseBundleStep;
		steps[SET_SCOPE] = createCriterionStep("Scope");
		steps[SET_SEVERITY] = createCriterionStep("Severity");

		selectBundle(chooseBundleStep.getSelectedBundle());
		setStep(CHOOSE_BUNDLE);
	}

	private ThreatRatingWizardStep createCriterionStep(String criterionLabel) throws Exception
	{
		int criterionId = getFramework().findCriterionByLabel(criterionLabel).getId();
		ThreatRatingWizardStep step = new ThreatRatingWizardSetValue(this, criterionId);
		return step;
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
		setStep(1);
	}
	
	public void setStep(int newStep) throws Exception
	{
		currentStep = newStep;
		setContents(steps[currentStep]);
		steps[currentStep].refresh();
	}
	
	public void refresh() throws Exception
	{
		for(int i = 0; i < steps.length; ++i)
			steps[i].refresh();
		validate();
	}
	
	public void setContents(JPanel contents)
	{
		removeAll();
		add(contents);
		validate();
	}
	
	ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getView().getProject().getThreatRatingFramework();
		return framework;
	}
	
	private static final int CHOOSE_BUNDLE = 0;
	private static final int SET_SCOPE = 1;
	private static final int SET_SEVERITY = 2;
	
	ThreatMatrixView view;
	ThreatRatingWizardStep[] steps;
	int currentStep;
	ThreatRatingBundle selectedBundle;
}