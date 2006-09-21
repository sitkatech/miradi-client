/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyIndirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardPanel extends WizardPanel
{
	public DiagramWizardPanel() throws Exception
	{
		steps = new WizardStep[STEP_COUNT];

		steps[OVERVIEW] = new DiagramWizardOverviewStep(this);
		steps[PROJECT_SCOPE] = new DiagramWizardProjectScopeStep(this);
		steps[VISION] = new DiagramWizardVisionStep(this);
		steps[CONSERVATION_TARGET] = new DiagramWizardDefineTargetsStep(this);
		steps[REVIEW_AND_MODIFY_TARGETS] = new DiagramWizardReviewAndModifyTargetsStep(this);
		steps[IDENTIFY_DIRECT_THREATS] = new DiagramWizardIdentifyDirectThreatStep(this);
		steps[LINK_DIRECT_THREATS_TO_TARGETS] = new DiagramWizardLinkDirectThreatsToTargetsStep(this);
		steps[IDENTIFY_INDIRECT_THREATS] = new DiagramWizardIdentifyIndirectThreatStep(this);
		steps[CONSTRUCT_CHAINS_HOWTO] = new DiagramWizardConstructChainsStep(this);
		steps[REVIEW_MODEL_AND_ADJUST] = new DiagramWizardReviewModelAndAdjustStep(this);
		
		setStep(OVERVIEW);
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
	
	public void jump(Class stepMarker) throws Exception
	{
		if(stepMarker.equals(ActionJumpDefineScope.class))
			setStep(PROJECT_SCOPE);
		else if(stepMarker.equals(ActionJumpEstablishVision.class))
			setStep(VISION);
		else if(stepMarker.equals(ActionJumpIdentifyTargets.class))
			setStep(CONSERVATION_TARGET);
		else if(stepMarker.equals(ActionJumpIdentifyDirectThreats.class))
			setStep(IDENTIFY_DIRECT_THREATS);
		else if(stepMarker.equals(ActionJumpIdentifyIndirectThreats.class))
			setStep(IDENTIFY_INDIRECT_THREATS);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	
	public void refresh() throws Exception
	{
		for(int i = 0; i < steps.length; ++i)
			steps[i].refresh();
		validate();
	}
	

	final static int OVERVIEW = 0;
	final static int PROJECT_SCOPE = 1;
	final static int VISION = 2;
	final static int CONSERVATION_TARGET = 3;
	final static int REVIEW_AND_MODIFY_TARGETS = 4;
	final static int IDENTIFY_DIRECT_THREATS = 5;
	final static int LINK_DIRECT_THREATS_TO_TARGETS = 6;
	final static int IDENTIFY_INDIRECT_THREATS = 7;
	final static int CONSTRUCT_CHAINS_HOWTO = 8;
	final static int REVIEW_MODEL_AND_ADJUST = 9;
	
	final static int STEP_COUNT = 10;
	
	WizardStep[] steps;
	int currentStep;
}
