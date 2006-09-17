/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatGridPanel extends JPanel
{
	public ThreatGridPanel(ThreatMatrixView viewToUse, ThreatMatrixTableModel modelToUse) throws Exception
	{
		view = viewToUse;
		model = modelToUse;

		summaryCells = new HashSet();
		activeCells = new HashMap();

		int rows = model.getThreatCount() + headerRowCount + 2;
		int columns = model.getTargetCount() + headerColumnCount + 2;
		setLayout(new BasicGridLayout(rows, columns));

		Box[][] cells = createGridCells(rows, columns);	
		populateThreatNamesColumnHeading(cells);
		populateThreatHeaders(cells);
		populateThreatSummaries(cells);
		populateTargetHeaders(cells);
		populateTargetSummaries(cells);
		populateBundleCells(cells);
		populateGrandTotal(rows, columns, cells);
	}

	private void populateGrandTotal(int rows, int columns, Box[][] cells)
	{
		grandTotal = ThreatRatingSummaryCell.createGrandTotal(model);
		cells[rows-1][columns-1].add(grandTotal);
		summaryCells.add(grandTotal);
	}

	private Box[][] createGridCells(int rows, int columns)
	{
		Box[][] cells = new Box[rows][columns];
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				Box thisComponent = Box.createHorizontalBox();
				thisComponent.setBorder(new LineBorder(Color.BLACK));
				cells[row][col] = thisComponent;
				add(cells[row][col]);
			}
		}
		return cells;
	}

	private void populateThreatNamesColumnHeading(Box[][] cells)
	{
		String threatLabelText = "<html><h2>THREATS</h2></html>";
		UiLabel threatLabel = new UiLabel(threatLabelText);
		threatLabel.setHorizontalAlignment(SwingConstants.CENTER);

		cells[0][0].add(threatLabel);
	}
	
	public void bundleWasClicked(ThreatRatingBundle bundle) throws Exception
	{
		view.selectBundle(bundle);
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		ThreatMatrixCellPanel wasSelected = (ThreatMatrixCellPanel)activeCells.get(highlightedBundle);
		if(wasSelected != null)
			wasSelected.setHighlighted(false);
		ThreatMatrixCellPanel nowSelected = (ThreatMatrixCellPanel)activeCells.get(bundle);
		if(nowSelected != null)
			nowSelected.setHighlighted(true);
		
		highlightedBundle = bundle;
		refreshCell(bundle);
	}
	
	public ThreatRatingBundle getSelectedBundle()
	{
		return highlightedBundle;
	}

	public void refreshCell(ThreatRatingBundle bundle) throws Exception
	{
		ThreatMatrixCellPanel cellPanel = (ThreatMatrixCellPanel)activeCells.get(bundle);
		if(cellPanel != null)
			cellPanel.refreshCell();
		updateSummaries();
	}

	private void populateThreatSummaries(Box[][] cells)
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			Box footer = cells[headerRowCount + threatIndex][1 + headerColumnCount + model.getTargetCount()];
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createThreatSummary(model, threatIndex);
			footer.add(summaryCell);
			summaryCells.add(summaryCell);
		}
		
		Box rollUpLabel = cells[1 + headerRowCount + model.getThreatCount()][0];
		JPanel panel = new JPanel();
		panel.add(createBoldLabel("Label|Summary Target Rating"));
		rollUpLabel.add(panel);
		
	}

	private void populateThreatHeaders(Box[][] cells)
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			Box header = cells[headerRowCount + threatIndex][0];
			header.add(createLabel(model.getThreatName(threatIndex)));
		}
	}

	private UiLabel createBoldLabel(String text)
	{
		UiLabel label = new UiLabel("<html><h3>" + EAM.text(text) + "</h3></html>");
		return label;
	}

	private void populateTargetSummaries(Box[][] cells)
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			Box footer = cells[1 + headerRowCount + model.getThreatCount()][headerColumnCount + targetIndex];
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createTargetSummary(model, targetIndex);
			footer.add(summaryCell);
			summaryCells.add(summaryCell);
		}
		
		Box rollUpLabel = cells[0][1 + headerColumnCount + model.getTargetCount()];
		JPanel panel = new JPanel();
		panel.add(createBoldLabel("Label|Summary Threat Rating"));
		rollUpLabel.add(panel);
	}

	private void populateTargetHeaders(Box[][] cells)
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			Box header = cells[0][headerColumnCount + targetIndex];
			header.add(createLabel(model.getTargetName(targetIndex)));
		}
	}

	private void populateBundleCells(Box[][] cells) throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				Box cell = cells[headerRowCount + threatIndex][headerColumnCount + targetIndex];
				if(model.isActiveCell(threatIndex, targetIndex))
					cell.add(createBundleCell(threatIndex, targetIndex));
			}
		}
	}

	private JComponent createBundleCell(int row, int col) throws Exception
	{
		ThreatRatingBundle bundle = getBundle(row, col);
		ThreatMatrixCellPanel thisComponent = new ThreatMatrixCellPanel(this, bundle);
		activeCells.put(bundle, thisComponent);
		return thisComponent;
	}

	private ThreatRatingBundle getBundle(int threatIndex, int targetIndex) throws Exception
	{
		ModelNodeId threatId = model.getThreatId(threatIndex);
		ModelNodeId targetId = model.getTargetId(targetIndex);
		ThreatRatingBundle bundle = getFramework().getBundle(threatId, targetId);
		return bundle;
	}

	private JComponent createLabel(String text)
	{
		JPanel panel = new JPanel();
		panel.add(new UiLabel(text));
		return panel;
	}

	public void updateSummaries() throws Exception
	{
		Iterator iter = summaryCells.iterator();
		while(iter.hasNext())
		{
			ThreatRatingSummaryCell cell = (ThreatRatingSummaryCell)iter.next();
			cell.dataHasChanged();
		}
	}

	private ThreatRatingFramework getFramework()
	{
		ThreatRatingFramework framework = getProject().getThreatRatingFramework();
		return framework;
	}
	
	public MainWindow getMainWindow()
	{
		return view.getMainWindow();
	}
	
	public Project getProject()
	{
		return view.getProject();
	}
	
	final int headerRowCount = 2;
	final int headerColumnCount = 2;

	ThreatMatrixView view;
	ThreatMatrixTableModel model;
	HashSet summaryCells;
	HashMap activeCells;
	ThreatRatingBundle highlightedBundle;
	ThreatRatingSummaryCell grandTotal;
}
