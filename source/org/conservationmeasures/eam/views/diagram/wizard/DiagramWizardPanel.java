/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyIndirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class DiagramWizardPanel extends WizardPanel
{
	public DiagramWizardPanel() throws Exception
	{
		OVERVIEW = addStep(new DiagramWizardOverviewStep(this));
		PROJECT_SCOPE = addStep(new DiagramWizardProjectScopeStep(this));
		VISION = addStep(new DiagramWizardVisionStep(this));
		CONSERVATION_TARGET = addStep(new DiagramWizardDefineTargetsStep(this));
		REVIEW_AND_MODIFY_TARGETS = addStep(new DiagramWizardReviewAndModifyTargetsStep(this));
		IDENTIFY_DIRECT_THREATS = addStep(new DiagramWizardIdentifyDirectThreatStep(this));
		LINK_DIRECT_THREATS_TO_TARGETS = addStep(new DiagramWizardLinkDirectThreatsToTargetsStep(this));
		IDENTIFY_INDIRECT_THREATS = addStep(new DiagramWizardIdentifyIndirectThreatStep(this));
		CONSTRUCT_CHAINS_HOWTO = addStep(new DiagramWizardConstructChainsStep(this));
		REVIEW_MODEL_AND_ADJUST = addStep(new DiagramWizardReviewModelAndAdjustStep(this));
		
		setStep(OVERVIEW);
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
		else if(stepMarker.equals(ActionJumpCreateModel.class))
			setStep(OVERVIEW);
		else
			throw new RuntimeException("Step not in this view: " + stepMarker);
	}
	

	int OVERVIEW;
	int PROJECT_SCOPE;
	int VISION;
	int CONSERVATION_TARGET;
	int REVIEW_AND_MODIFY_TARGETS;
	int IDENTIFY_DIRECT_THREATS;
	int LINK_DIRECT_THREATS_TO_TARGETS;
	int IDENTIFY_INDIRECT_THREATS;
	int CONSTRUCT_CHAINS_HOWTO;
	int REVIEW_MODEL_AND_ADJUST;
}
