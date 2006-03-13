/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
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
		getProject().addViewChangeListener(this);
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return "ThreatMatrix";
	}
	
	public void switchToView(String viewName) throws Exception
	{
		boolean isSwitchingToThisView = viewName.equals(getViewName());
		if(!isSwitchingToThisView)
			return;
		
		model = new ThreatMatrixTableModel(getProject());
		summaryCells = new HashSet();

		int rows = model.getThreatCount() + 4;
		int columns = model.getTargetCount() + 4;
		Box[][] cells = new Box[rows][columns];
		grid = new JPanel();
		grid.setLayout(new BasicGridLayout(rows, columns));
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				Box thisComponent = Box.createHorizontalBox();
				thisComponent.setBorder(new LineBorder(Color.BLACK));
				cells[row][col] = thisComponent;
				grid.add(cells[row][col]);
			}
		}
			
		populateThreatSummaries(cells);
		populateTargetSummaries(cells);
		populateBundleCells(cells);

		removeAll();
		add(new UiScrollPane(grid), BorderLayout.CENTER);
	}

	private void populateBundleCells(Box[][] cells) throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				Box cell = cells[2 + threatIndex][2 + targetIndex];
				if(model.isActiveCell(threatIndex, targetIndex))
					cell.add(createBundleCell(threatIndex, targetIndex));
			}
		}
	}

	private void populateTargetSummaries(Box[][] cells)
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			Box header = cells[0][2 + targetIndex];
			header.add(createLabel(model.getTargetName(targetIndex)));

			Box footer = cells[3 + model.getThreatCount()][2 + targetIndex];
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createTargetSummary(model, targetIndex);
			footer.add(summaryCell);
			summaryCells.add(summaryCell);
		}
	}

	private void populateThreatSummaries(Box[][] cells)
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			Box header = cells[2 + threatIndex][0];
			header.add(createLabel(model.getThreatName(threatIndex)));
			
			Box footer = cells[2 + threatIndex][3 + model.getTargetCount()];
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createThreatSummary(model, threatIndex);
			footer.add(summaryCell);
			summaryCells.add(summaryCell);
		}
	}

	public void cellHasChanged() throws Exception
	{
		Iterator iter = summaryCells.iterator();
		while(iter.hasNext())
		{
			ThreatRatingSummaryCell cell = (ThreatRatingSummaryCell)iter.next();
			cell.dataHasChanged();
		}
	}

	private JComponent createLabel(String text)
	{
		JPanel panel = new JPanel();
		panel.add(new UiLabel(text));
		return panel;
	}

	private JComponent createBundleCell(int row, int col) throws Exception
	{
		JComponent thisComponent;
		ThreatRatingFramework framework = getFramework();
		ThreatRatingBundle bundle = getBundle(row, col);
		thisComponent = new ThreatMatrixCellPanel(getMainWindow(), this, framework, bundle);
		return thisComponent;
	}

	private ThreatRatingBundle getBundle(int threatIndex, int targetIndex) throws Exception
	{
		int threatId = model.getThreatId(threatIndex);
		int targetId = model.getTargetId(targetIndex);
		ThreatRatingBundle bundle = getFramework().getBundle(threatId, targetId);
		return bundle;
	}

	private ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getProject().getThreatRatingFramework();
		return framework;
	}

	JPanel grid;
	ThreatMatrixTableModel model;
	HashSet summaryCells;
}

