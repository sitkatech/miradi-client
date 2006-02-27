/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.interview.ThreatMatrixToolBar;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatMatrixView extends UmbrellaView implements ViewChangeListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));

		setLayout(new BorderLayout());

		Project project = getProject();
		model = new ThreatMatrixTableModel(project);
		
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
		boolean isSwitchingToThisView = viewName.equals(getViewName());
		if(!isSwitchingToThisView)
			return;
		
		grid = new JPanel();
		int rows = model.getRowCount();
		int columns = model.getColumnCount();
		grid.setLayout(new BasicGridLayout(rows, columns));
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				JComponent thisComponent = null;
				if(row == 0)
				{
					thisComponent = new JPanel();
					thisComponent.add(new UiLabel(model.getTargetName(col)));
				}
				else if(col == 0)
				{
					thisComponent = new JPanel();
					thisComponent.add(new UiLabel(model.getThreatName(row)));
				}
				else if(model.isActiveCell(row, col))
				{
					thisComponent = createBundleCell(row, col);
				}
				else
				{
					thisComponent = new JPanel();
				}
				thisComponent.setBorder(new LineBorder(Color.BLACK));
				grid.add(thisComponent);
			}
		}
		removeAll();
		add(new UiScrollPane(grid), BorderLayout.CENTER);
	}

	private JComponent createBundleCell(int row, int col)
	{
		JComponent thisComponent;
		ThreatRatingFramework framework = getProject().getThreatRatingFramework();
		int threatId = model.getThreatId(row);
		int targetId = model.getTargetId(col);
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		thisComponent = new ThreatMatrixCellPanel(framework, bundle);
		return thisComponent;
	}

	JPanel grid;
	ThreatMatrixTableModel model;
}

