/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.diagram;

import javax.swing.Icon;

import org.miradi.actions.jump.*;
import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.utils.MiradiScrollPane;

public class FactorSummaryScrollablePanel extends ModelessDialogPanel
{
	public FactorSummaryScrollablePanel(MainWindow mainWindowToUse, DiagramFactor diagramFactorToUse) throws Exception
	{
		summaryPanel = new FactorSummaryPanel(mainWindowToUse, diagramFactorToUse);
		MiradiScrollPane summaryScrollPane = new MiradiScrollPane(summaryPanel);
		add(summaryScrollPane);
	}
	
	public void setFocusOnFirstField()
	{
		summaryPanel.setFocusOnFirstField();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		disposePanel(summaryPanel);
		summaryPanel = null;
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		summaryPanel.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		summaryPanel.becomeInactive();
		super.becomeInactive();
	}
	
	@Override
	public String getPanelDescription()
	{
		return summaryPanel.getPanelDescription();
	}
	
	public Icon getIcon()
	{
		return summaryPanel.getIcon();
	}

	@Override
	public BaseObject getObject()
	{
		return null;
	}
	
	@Override
	public Class getJumpActionClass()
	{
		// TODO: This looks like duplicate code from FactorSummaryPanel
		// Can we delegate to that?
		ORef currentFactorRef = summaryPanel.getCurrentDiagramFactor().getWrappedORef();
		if(Target.is(currentFactorRef))
			return ActionJumpDiagramWizardDefineTargetsStep.class;
		if(HumanWelfareTarget.is(currentFactorRef))
			return ActionJumpDiagramWizardHumanWelfareTargetsStep.class;
		
		if(Cause.is(currentFactorRef))
		{
			Cause cause = Cause.find(summaryPanel.getProject(), currentFactorRef);
			if(cause.isDirectThreat())
				return ActionJumpDiagramWizardIdentifyDirectThreatStep.class;
			
			return ActionJumpDiagramWizardIdentifyIndirectThreatStep.class;
		}
		
		if(Strategy.is(currentFactorRef))
			return ActionJumpSelectChainStep.class;
		
		if(IntermediateResult.is(currentFactorRef) || ThreatReductionResult.is(currentFactorRef) || BiophysicalResult.is(currentFactorRef))
			return ActionJumpDiagramWizardResultsChainSelectStrategyStep.class;

		if (BiophysicalFactor.is(currentFactorRef))
			return ActionJumpTargetStressesStep.class;

		return super.getJumpActionClass();
	}

	private FactorSummaryPanel summaryPanel;
}
