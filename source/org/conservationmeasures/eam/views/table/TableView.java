/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

public class TableView extends UmbrellaView 
{

	public TableView(MainWindow mainWindowToUse) 
	{
		super(mainWindowToUse);
		setToolBar(new TableToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		model = new TableViewModel(mainWindowToUse);
		table = new UiTable(model);
		table.setShowGrid(true);
		UiScrollPane pane = new UiScrollPane(table);
		add(pane, BorderLayout.CENTER);
		setBorder(new LineBorder(Color.BLACK));
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Table";
	}
	
	class TableViewModel extends DefaultTableModel
	{
		public TableViewModel(MainWindow mainWindow)
		{
			super();
			setRowCount(0);
			setColumnCount(3);
			Vector columnNames = new Vector();
			columnNames.add(EAM.text("Table|Name"));
			columnNames.add(EAM.text("Table|X"));
			columnNames.add(EAM.text("Table|Y"));
			setColumnIdentifiers(columnNames);
			
			
		}
	}
	
	UiTable table;
	TableModel model;

}
