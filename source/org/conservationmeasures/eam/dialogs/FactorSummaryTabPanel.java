package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class FactorSummaryTabPanel extends ObjectManagementPanel
{

	public FactorSummaryTabPanel(Project projectToUse,SplitterPositionSaverAndGetter splitPositionSaverToUse,DiagramFactor diagramFactor) throws Exception
	{
		super(splitPositionSaverToUse, new FakeCollectionPanel(projectToUse), new FactorSummaryPanel(projectToUse, diagramFactor));
		realPanel = (FactorSummaryPanel) propertiesPanel;
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

	FactorSummaryPanel realPanel;
}
