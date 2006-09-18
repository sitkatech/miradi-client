/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatGridPanel extends JPanel
{
	public ThreatGridPanel(ThreatMatrixView viewToUse, ThreatMatrixTableModel modelToUse) throws Exception
	{
		view = viewToUse;
		model = modelToUse;

		threatSummaryCells = new HashSet();
		targetSummaryCells = new HashSet();
		bundleCells = new HashMap();

		int rows = model.getThreatCount() + headerRowCount + 2;
		int columns = model.getTargetCount() + headerColumnCount + 2;
		setLayout(new BasicGridLayout(rows, columns));
		createGridCells(rows, columns);	
		createBundleCells();
		createThreatSummaries();
		createTargetSummaries();
		createGrandTotal();

		populateThreatNamesColumnHeading();
		populateTargetSummaryHeading();
		populateThreatSummaryHeading();
		populateGrandTotal();

		populateTargetHeaders();
		populateTargetSummaries();

		sortArbitrarily();
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

	private void createBundleCells() throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
				ThreatMatrixCellPanel thisComponent = new ThreatMatrixCellPanel(this, bundle);
				bundleCells.put(bundle, thisComponent);
			}
		}
	}
	
	private void createThreatSummaries()
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createThreatSummary(model, threatIndex);
			threatSummaryCells.add(summaryCell);
		}
	}

	private void createTargetSummaries()
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			ThreatRatingSummaryCell summaryCell = ThreatRatingSummaryCell.createTargetSummary(model, targetIndex);
			targetSummaryCells.add(summaryCell);
		}
	}

	private void createGrandTotal()
	{
		grandTotal = ThreatRatingSummaryCell.createGrandTotal(model);
	}
	
	private void populateThreatNamesColumnHeading()
	{
		String threatLabelText = "<html><h2>THREATS</h2></html>";
		UiLabel threatLabel = new UiLabel(threatLabelText);
		threatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		UiButton sort = new UiButton(EAM.text("Button|Sort"));
		sort.addActionListener(new SortByNameListener());

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(threatLabel, BorderLayout.CENTER);
		panel.add(sort, BorderLayout.AFTER_LAST_LINE);
		setCellContents(0, 0, panel);
	}
	
	private void populateThreatSummaryHeading()
	{
		UiLabel label = createBoldLabel("Label|Summary Threat Rating");
		
		UiButton sort = new UiButton(EAM.text("Button|Sort"));
		sort.addActionListener(new SortBySummaryListener());
		
		JPanel contents = new JPanel(new BorderLayout());
		contents.add(label, BorderLayout.CENTER);
		contents.add(sort, BorderLayout.AFTER_LAST_LINE);

		setCellContents(0, getTargetSummaryColumn(), contents);
	}

	private void populateTargetSummaryHeading()
	{
		UiLabel contents = createBoldLabel("Label|Summary Target Rating");
		setCellContents(getThreatSummaryRow(), 0, contents);
	}

	private void populateGrandTotal()
	{
		int rows = cells.length;
		int columns = cells[0].length;
		setCellContents(rows-1, columns-1, grandTotal);
	}
	
	

	private void populateDynamicCells() throws Exception
	{
		populateThreatHeaders();
		populateBundleCells();
		populateThreatSummaries();
		validate();
		repaint();
	}

	private void populateThreatHeaders()
	{
		int column = 0;
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			int row = getRowFromThreatIndex(threatIndex);
			JComponent contents = createLabel(model.getThreatName(threatIndex));
			setCellContents(row, column, contents);
		}
	}
	
	class SortByNameListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			try
			{
				sortByThreatName();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				EAM.errorDialog(EAM.text("An error prevented that operation from succeeding"));
			}
		}
		
	}

	class SortByTargetValueListener implements ActionListener
	{
		public SortByTargetValueListener(int targetIndexToUse)
		{
			targetIndex = targetIndexToUse;
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			try
			{
				sortByTargetValue(targetIndex);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				EAM.errorDialog(EAM.text("An error prevented that operation from succeeding"));
			}
		}
		
		int targetIndex;
	}

	class SortBySummaryListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			try
			{
				sortByThreatSummaryValue();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				EAM.errorDialog(EAM.text("An error prevented that operation from succeeding"));
			}
		}
	}

	private void populateTargetHeaders()
	{
		for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
		{
			int row = 0;
			int column = getColumnFromTargetIndex(targetIndex);
			JComponent label = createLabel(model.getTargetName(targetIndex));
			
			UiButton sort = new UiButton(EAM.text("Button|Sort"));
			sort.addActionListener(new SortByTargetValueListener(targetIndex));
			
			JPanel contents = new JPanel(new BorderLayout());
			contents.add(label, BorderLayout.CENTER);
			contents.add(sort, BorderLayout.AFTER_LAST_LINE);
			setCellContents(row, column, contents);
		}
	}

	private void populateBundleCells() throws Exception
	{
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
		{
			int row = getRowFromThreatIndex(threatIndex);
			for(int targetIndex = 0; targetIndex < model.getTargetCount(); ++targetIndex)
			{
				int column = getColumnFromTargetIndex(targetIndex);
				if(model.isActiveCell(threatIndex, targetIndex))
				{
					ThreatRatingBundle bundle = getBundle(threatIndex, targetIndex);
					JComponent contents = getCellForBundle(bundle);
					setCellContents(row, column, contents);
				}
			}
		}
	}

	private void populateThreatSummaries()
	{
		int column = getTargetSummaryColumn();
		Iterator iter = threatSummaryCells.iterator();
		while(iter.hasNext())
		{
			ThreatRatingSummaryCell summaryCell = (ThreatRatingSummaryCell)iter.next();
			int threatIndex = summaryCell.threatIndex;
			int row = getRowFromThreatIndex(threatIndex);
			setCellContents(row, column, summaryCell);
		}
	}

	private void populateTargetSummaries()
	{
		int row = getThreatSummaryRow();
		Iterator iter = targetSummaryCells.iterator();
		while(iter.hasNext())
		{
			ThreatRatingSummaryCell summaryCell = (ThreatRatingSummaryCell)iter.next();
			int targetIndex = summaryCell.targetIndex;
			int column = getColumnFromTargetIndex(targetIndex);
			setCellContents(row, column, summaryCell);
		}
	}


	
	private int getRowFromThreatIndex(int threatIndex)
	{
		int sortedRow = sortedIndexes[threatIndex];
		return headerRowCount + sortedRow;
	}

	private int getColumnFromTargetIndex(int targetIndex)
	{
		return headerColumnCount + targetIndex;
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

	private ThreatMatrixCellPanel getCellForBundle(ThreatRatingBundle bundle)
	{
		return (ThreatMatrixCellPanel)bundleCells.get(bundle);
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
		updateSummaries(threatSummaryCells);
		updateSummaries(targetSummaryCells);
		grandTotal.dataHasChanged();
	}
	
	private void updateSummaries(Collection summaries) throws Exception
	{
		Iterator iter = summaries.iterator();
		while(iter.hasNext())
		{
			ThreatRatingSummaryCell cell = (ThreatRatingSummaryCell)iter.next();
			cell.dataHasChanged();
		}
	}
	
	private void sortArbitrarily() throws Exception
	{
		sortedIndexes = new int[model.getThreatCount()];
		for(int i = 0; i < sortedIndexes.length; ++i)
			sortedIndexes[i] = i;
		
		populateDynamicCells();
	}

	private void sortByThreatName() throws Exception
	{
		String[] sortedNames = model.getThreatNames();

		Vector unsortedNames = new Vector(Arrays.asList(sortedNames));
		Arrays.sort(sortedNames);
		for(int i = 0; i < sortedNames.length; ++i)
		{
			sortedIndexes[unsortedNames.indexOf(sortedNames[i])] =  i;
		}
		populateDynamicCells();
	}

	private void sortByTargetValue(int targetIndex) throws Exception
	{
		class BundleSorter implements Comparator
		{
			public BundleSorter(ModelNodeId targetIdToSortOn, ThreatRatingFramework frameworkTouse)
			{
				targetId = targetIdToSortOn;
				framework = frameworkTouse;
			}
			
			public int compare(Object raw0, Object raw1)
			{
				ModelNodeId threatId0 = (ModelNodeId)raw0;
				ModelNodeId threatId1 = (ModelNodeId)raw1;
				
				ThreatRatingBundle bundle0 = getBundle(threatId0);
				ThreatRatingBundle bundle1 = getBundle(threatId1);
					
				return getBundleValue(bundle0).compareTo(getBundleValue(bundle1));
			}

			private ThreatRatingBundle getBundle(ModelNodeId threatId)
			{
				try
				{
					return framework.getBundle(threatId, targetId);
				}
				catch (Exception e)
				{
					return null;
				}
			}
			
			Integer getBundleValue(ThreatRatingBundle bundle)
			{
				if(bundle == null)
					return new Integer(-1);
				
				ThreatRatingValueOption value = framework.getBundleValue(bundle);
				return new Integer(value.getNumericValue());
			}

			ModelNodeId targetId;
			ThreatRatingFramework framework;
		}
		
		ThreatRatingFramework framework = getProject().getThreatRatingFramework();
		
		Vector sortedThreatIndexes = new Vector();
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
			sortedThreatIndexes.add(model.getThreatId(threatIndex));

		ModelNodeId targetId = model.getTargetId(targetIndex);
		Collections.sort(sortedThreatIndexes, new BundleSorter(targetId, framework));
		for(int i = 0; i < sortedThreatIndexes.size(); ++i)
		{
			ModelNodeId thisSortedIndex = (ModelNodeId)sortedThreatIndexes.get(i);
			int threatIndex = model.findThreatIndexById(thisSortedIndex);
			sortedIndexes[threatIndex] =  i;
		}
		populateDynamicCells();
	}

	private void sortByThreatSummaryValue() throws Exception
	{
		class ThreatSummarySorter implements Comparator
		{
			public ThreatSummarySorter(ThreatRatingFramework frameworkTouse)
			{
				framework = frameworkTouse;
			}
			
			public int compare(Object raw0, Object raw1)
			{
				ModelNodeId threatId0 = (ModelNodeId)raw0;
				ModelNodeId threatId1 = (ModelNodeId)raw1;
				
				ThreatRatingValueOption result0 = framework.getThreatThreatRatingValue(threatId0);
				Integer value0 = new Integer(result0.getNumericValue());
				ThreatRatingValueOption result1 = framework.getThreatThreatRatingValue(threatId1);
				Integer value1 = new Integer(result1.getNumericValue());
				
				return value0.compareTo(value1);
			}

			ModelNodeId targetId;
			ThreatRatingFramework framework;
		}
		
		ThreatRatingFramework framework = getProject().getThreatRatingFramework();
		
		Vector sortedThreatIndexes = new Vector();
		for(int threatIndex = 0; threatIndex < model.getThreatCount(); ++threatIndex)
			sortedThreatIndexes.add(model.getThreatId(threatIndex));

		Collections.sort(sortedThreatIndexes, new ThreatSummarySorter(framework));
		for(int i = 0; i < sortedThreatIndexes.size(); ++i)
		{
			ModelNodeId thisSortedIndex = (ModelNodeId)sortedThreatIndexes.get(i);
			int threatIndex = model.findThreatIndexById(thisSortedIndex);
			sortedIndexes[threatIndex] =  i;
		}
		populateDynamicCells();
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
	HashSet threatSummaryCells;
	HashSet targetSummaryCells;
	HashMap bundleCells;
	ThreatRatingBundle highlightedBundle;
	ThreatRatingSummaryCell grandTotal;

	int[] sortedIndexes;
	JPanel[][] cells;
}
