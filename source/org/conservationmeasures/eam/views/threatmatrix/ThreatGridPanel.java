/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
		createGridCells(rows, columns);	
		createBundleCells();

		populateThreatNamesColumnHeading();
		populateTargetSummaryHeading();
		populateThreatSummaryHeading();

		populateThreatHeaders();
		populateTargetHeaders();
		populateThreatSummaries();
		populateTargetSummaries();
		populateBundleCells();
		populateGrandTotal();
	}

	private void populateGrandTotal()
	{
		int rows = cells.length;
		int columns = cells[0].length;
		grandTotal = ThreatRatingSummaryCell.createGrandTotal(model);
		setCellContents(rows-1, columns-1, grandTotal);

		summaryCells.add(grandTotal);
	}

	private void createGridCells(int rows, int columns)
	{
		cells = new JPanel[rows][columns];
		for(int row = 0; row < rows; ++row)
		{
			for(int col = 0; col < columns; ++col)
			{
				JPanel thisComponent = new JPanel(new BorderLayout());
				thisComponent.setBorder(new LineBorder(Color.BLACK));
				cells[row][col] = thisComponent;
				add(cells[row][col]);
			}
		}
	}

	private void populateThreatNamesColumnHeading()
	{
		String threatLabelText = "<html><h2>THREATS</h2></html>";
		UiLabel threatLabel = new UiLabel(threatLabelText);
		threatLabel.setHorizontalAlignment(SwingConstants.CENTER);

		setCellContents(0, 0, threatLabel);
	}
	
	public void bundleWasClicked(ThreatRatingBundle bundle) throws Exception
	{
		view.selectBundle(bundle);
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		ThreatMatrixCellPanel wasSelected = getCellForBundle(highlightedBundle);
		if(wasSelected != null)
			wasSelected.setHighlighted(false);
		ThreatMatrixCellPanel nowSelected = getCellForBundle(bundle);
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
		ThreatMatrixCellPanel cellPanel = getCellForBundle(bundle);
		if(cellPanel != null)
			cellPanel.refreshCell();
		updateSummaries();
	}

	private void populateThreatSummaries()
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			int row = headerRowCount + threatIndex;
			int column = getTargetSummaryColumn();
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createThreatSummary(model, threatIndex);
			setCellContents(row, column, summaryCell);

			summaryCells.add(summaryCell);
		}
		
	}

	private void populateTargetSummaryHeading()
	{
		UiLabel contents = createBoldLabel("Label|Summary Target Rating");
		setCellContents(getThreatSummaryRow(), 0, contents);
	}

	private void populateThreatHeaders()
	{
		int column = 0;
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			int row = headerRowCount + threatIndex;
			JComponent contents = createLabel(model.getThreatName(threatIndex));
			setCellContents(row, column, contents);
		}
	}

	private void setCellContents(int row, int column, JComponent contents)
	{
		cells[row][column].removeAll();
		cells[row][column].add(contents);
	}

	private UiLabel createBoldLabel(String text)
	{
		UiLabel label = new UiLabel("<html><h3>" + EAM.text(text) + "</h3></html>");
		return label;
	}

	private void populateTargetSummaries()
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			int column = headerColumnCount + targetIndex;
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createTargetSummary(model, targetIndex);
			setCellContents(getThreatSummaryRow(), column, summaryCell);
			
			summaryCells.add(summaryCell);
		}
		
	}

	private void populateThreatSummaryHeading()
	{
		UiLabel contents = createBoldLabel("Label|Summary Threat Rating");
		setCellContents(0, getTargetSummaryColumn(), contents);
	}

	private void populateTargetHeaders()
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			int row = 0;
			int column = headerColumnCount + targetIndex;
			JComponent contents = createLabel(model.getTargetName(targetIndex));
			setCellContents(row, column, contents);
		}
	}

	private void populateBundleCells() throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			int row = headerRowCount + threatIndex;
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				int column = headerColumnCount + targetIndex;
				if(model.isActiveCell(threatIndex, targetIndex))
				{
					ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
					JComponent contents = getCellForBundle(bundle);
					setCellContents(row, column, contents);
				}
			}
		}
	}

	private void createBundleCells() throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
				ThreatMatrixCellPanel thisComponent = new ThreatMatrixCellPanel(this, bundle);
				activeCells.put(bundle, thisComponent);
			}
		}
	}
	
	private ThreatMatrixCellPanel getCellForBundle(ThreatRatingBundle bundle)
	{
		return (ThreatMatrixCellPanel)activeCells.get(bundle);
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
		return getProject().getThreatRatingFramework();
	}
	
	public MainWindow getMainWindow()
	{
		return view.getMainWindow();
	}
	
	public Project getProject()
	{
		return view.getProject();
	}
	
	private int getTargetSummaryColumn()
	{
		return 1 + headerColumnCount + model.getTargetCount();
	}

	private int getThreatSummaryRow()
	{
		return 1 + headerRowCount + model.getThreatCount();
	}

	final int headerRowCount = 2;
	final int headerColumnCount = 2;

	ThreatMatrixView view;
	ThreatMatrixTableModel model;
	HashSet summaryCells;
	HashMap activeCells;
	ThreatRatingBundle highlightedBundle;
	ThreatRatingSummaryCell grandTotal;

	JPanel[][] cells;
}
