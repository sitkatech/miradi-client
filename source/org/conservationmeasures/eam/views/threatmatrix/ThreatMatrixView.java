/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;


import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.interview.ThreatMatrixToolBar;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

public class ThreatMatrixView extends UmbrellaView implements ViewChangeListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));

		setLayout(new BorderLayout());

		Project project = getMainWindow().getProject();
		model = new ThreatMatrixTableModel(project);
		grid = new UiTable();
		grid.setModel(model);
		add(grid, BorderLayout.CENTER);
		
		ratingPanel = new ThreatRatingPanel();
		add(new UiScrollPane(ratingPanel), BorderLayout.AFTER_LAST_LINE);
		
		project.addViewChangeListener(this);
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return "ThreatMatrix";
	}
	
	public void switchToView(String viewName)
	{
		if(viewName.equals(getViewName()))
		{
			model.fireTableStructureChanged();
		}
	}

	UiTable grid;
	ThreatRatingPanel ratingPanel;
	ThreatMatrixTableModel model;
}

