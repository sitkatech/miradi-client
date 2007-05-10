package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopDraftStrategiesStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramOverviewStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardDefineTargetsStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyDirectThreatStep;
import org.conservationmeasures.eam.actions.jump.ActionJumpDiagramWizardIdentifyIndirectThreatStep;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;

public class FactorSummaryTabPanel extends ModelessDialogPanel
{

	public FactorSummaryTabPanel(Project projectToUse, DiagramFactor diagramFactor) throws Exception
	{
		realPanel = new FactorSummaryPanel(projectToUse, diagramFactor);
		add(realPanel);
	}

	public void dispose()
	{
		realPanel.dispose();
		super.dispose();
	}

	public BaseObject getObject()
	{
		return realPanel.getObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Summary");
	}

	public Icon getIcon()
	{
		return realPanel.getIcon();
	}
	
	public Class getJumpActionClass()
	{
		Factor factor = realPanel.getFactor();
		if (factor.isContributingFactor())
			return ActionJumpDiagramWizardIdentifyIndirectThreatStep.class;
		else if (factor.isDirectThreat())
			return ActionJumpDiagramWizardIdentifyDirectThreatStep.class;
		else if (factor.isStrategy())
			return ActionJumpDevelopDraftStrategiesStep.class;
		else if (factor.isTarget())
			return ActionJumpDiagramWizardDefineTargetsStep.class;
		return ActionJumpDiagramOverviewStep.class;
	}
	

	FactorSummaryPanel realPanel;
}
