package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class FactorDetailManagementPanel extends ObjectManagementPanel
{

	public FactorDetailManagementPanel(Project projectToUse,SplitterPositionSaverAndGetter splitPositionSaverToUse,DiagramFactor diagramFactor) throws Exception
	{
		super(splitPositionSaverToUse, createCollectionPanel(projectToUse), new FactorDetailsPanel(projectToUse, diagramFactor));
		realPanel = (FactorDetailsPanel) propertiesPanel;
	}

	private static ObjectCollectionPanel createCollectionPanel(Project projectToUse)
	{
		return new ObjectCollectionPanel(projectToUse, null) {

			public void commandExecuted(CommandExecutedEvent event)
			{
			}

			public BaseObject getSelectedObject()
			{
				return null;
			}};
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

	FactorDetailsPanel realPanel;
}
