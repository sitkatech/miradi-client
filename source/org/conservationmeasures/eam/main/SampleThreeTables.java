/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import org.conservationmeasures.eam.utils.MultiTableVerticalScrollController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;


//FIXME planning - this is a sample code to test scrolling and selection controllers.  remove it after properites panel is done
public class SampleThreeTables extends JFrame
{
	public SampleThreeTables()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createThreeTablePanel();
		pack();
		setVisible(true);	
	}
	
	private void createThreeTablePanel()
	{
		JTable firstTable = new JTable(rows, columns);
		JTable secondTable = new JTable(rows, columns);
		JTable thirdTable = new JTable(rows, columns);
		
		MultiTableVerticalScrollController scrollController = new MultiTableVerticalScrollController();
		MultipleTableSelectionController selectionController = new MultipleTableSelectionController();
		selectionController.addTable(firstTable);
		selectionController.addTable(secondTable);
		selectionController.addTable(thirdTable);
		
		JScrollPane firstScroll = new JScrollPane(firstTable);
		scrollController.addTable(firstScroll);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.add(firstScroll);
		
		JScrollPane secondScroll = new JScrollPane(secondTable);
		secondScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		secondScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		horizontalBox.add(secondScroll);
		scrollController.addTable(secondScroll);
		
		JScrollPane thirdScroll = new JScrollPane(thirdTable);
		thirdScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		horizontalBox.add(thirdScroll);
		scrollController.addTable(thirdScroll);
		
		
		horizontalBox.setMinimumSize(new Dimension(100, 100));
		horizontalBox.setPreferredSize(new Dimension(100, 100));
		getContentPane().add(horizontalBox);
	}

	public static void main(String[] args)
	{
		new SampleThreeTables();
	}
	
	Object rows[][] = { { "AMZN", "Amazon", "67 9/16" },
						{ "CDNW", "CDnow", "4 7/16" },
						{ "EBAY", "eBay", "180 7/8" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "MKTW", "MarketWatch", "29" },
						{ "YHOO", "Yahoo!", "151 1/8" } };
	
	    Object columns[] = { "Symbol", "Name", "Price" };	
}
