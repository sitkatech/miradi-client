/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.ConceptualModelIcon;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.diagram.ConceptualModelDiagramSplitPane;
import org.conservationmeasures.eam.views.diagram.DiagramSplitPane;

public class ConceptualModelDiagramPanel extends DiagramPanel implements CommandExecutedListener
{
	public ConceptualModelDiagramPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		brainstormModePanel = new BrainstormModePanel(mainWindowToUse.getActions());
		add(brainstormModePanel, BorderLayout.AFTER_LAST_LINE);
		mainWindowToUse.getProject().addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		mainWindow.getProject().removeCommandExecutedListener(this);
		super.dispose();
	}

	protected DiagramSplitPane createDiagramSplitter() throws Exception
	{
		return new ConceptualModelDiagramSplitPane(mainWindow);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		brainstormModePanel.updateVisibility();
	}

	public Icon getIcon()
	{
		return new ConceptualModelIcon();
	}

	public String getTabName()
	{
		return EAM.text("Conceptual Model");
	}

	BrainstormModePanel brainstormModePanel;
}
