/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.martus.swing.UiScrollPane;

public class MonitoringPanel extends JPanel
{
	public MonitoringPanel(Project projectToUse) throws Exception
	{
		super(new BorderLayout());
		model = new MonitoringModel(projectToUse);
		tree = new MonitoringTreeTable(projectToUse, model);
		restoreTreeExpansionState();
		UiScrollPane scrollPane = new FastScrollPane(tree);
		TitledBorder titileBorder = BorderFactory.createTitledBorder("Indicators linked to goals and objectives." );
		titileBorder.setTitleFont(EAM.mainWindow.getUserDataPanelFont());
		scrollPane.setBorder(titileBorder);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void dispose()
	{
		tree.dispose();
	}
	
	private void restoreTreeExpansionState() 
	{
		try
		{
			tree.restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error restoring tree state"));
		}
	}

	MonitoringTreeTable tree;
	GenericTreeTableModel model;
}
