/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.miradi.icons.ConceptualModelIcon;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.diagram.ConceptualModelDiagramSplitPane;
import org.miradi.views.diagram.DiagramSplitPane;

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
