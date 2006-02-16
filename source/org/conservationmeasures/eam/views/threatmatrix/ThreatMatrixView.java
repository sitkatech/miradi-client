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
import org.conservationmeasures.eam.objects.RatingValueOption;
import org.conservationmeasures.eam.project.Project;
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
		
		RatingValueOption[] options = getProject().getThreatRatingOptions();
		grid = new JPanel();
		int rows = model.getRowCount();
		int columns = model.getColumnCount();
		grid.setLayout(new BasicGridLayout(rows, columns));
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				JComponent thisComponent = null;
				if(row >= ThreatMatrixTableModel.reservedRows && 
						col >= ThreatMatrixTableModel.reservedColumns &&
						model.isActiveCell(row, col))
				{
					thisComponent = new ThreatSummaryPanel(options);
				}
				else
				{
					String text = (String)model.getValueAt(row, col);
					UiLabel label = new UiLabel(text);
					thisComponent = new JPanel();
					thisComponent.add(label);
				}
				thisComponent.setBorder(new LineBorder(Color.BLACK));
				grid.add(thisComponent);
			}
		}
		removeAll();
		add(new UiScrollPane(grid), BorderLayout.CENTER);
	}

	JPanel grid;
	ThreatMatrixTableModel model;
}

