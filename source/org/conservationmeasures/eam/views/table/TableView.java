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
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;
import org.martus.swing.UiVBox;

public class TableView extends UmbrellaView
{
	public TableView(MainWindow mainWindowToUse) 
	{
		super(mainWindowToUse);
		setToolBar(new TableToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		DiagramModel diagramModel = mainWindowToUse.getProject().getDiagramModel();
		TableViewNamesLocationsModel nameLocationModel = new TableViewNamesLocationsModel(diagramModel);
		nameLocationModel.addListener();
		UiTable tableNamesLocations = new UiTable(nameLocationModel);
		
		TableViewConnectionsModel connectionModel = new TableViewConnectionsModel(diagramModel);
		connectionModel.addListener();
		UiTable tableLinks = new UiTable(connectionModel);

		UiVBox vBox = new UiVBox();
		vBox.add(new UiScrollPane(tableNamesLocations));
		vBox.addSpace();
		vBox.add(new UiScrollPane(tableLinks));
		add(vBox, BorderLayout.CENTER);
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
	
	class TableViewNamesLocationsModel extends AbstractTableModel implements DiagramModelListener
	{
		public TableViewNamesLocationsModel(DiagramModel diagramModelToUse)
		{
			super();
			diagramModel = diagramModelToUse;
			columnNames = new Vector();
			columnNames.add(EAM.text("Table|Name"));
			columnNames.add(EAM.text("Table|X"));
			columnNames.add(EAM.text("Table|Y"));
		}
		
		protected void addListener()
		{
			diagramModel.addDiagramModelListener(this);
		}

		public int getColumnCount() 
		{
			return columnNames.size();
		}

		public int getRowCount() 
		{
			return diagramModel.getNodeCount();
		}

		public Object getValueAt(int rowIndex, int columnIndex) 
		{
			try 
			{
				EAMGraphCell node = diagramModel.getNodeByIndex(rowIndex);
				switch (columnIndex)
				{
				case TABLE_COLUMN_NAME:
					return node.getText();
				case TABLE_COLUMN_X:
					return new Integer(node.getLocation().x);
				case TABLE_COLUMN_Y:
					return new Integer(node.getLocation().y);
				default:
					return null;
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}

		public String getColumnName(int column) 
		{
			return (String)columnNames.get(column);
		}
		
		public void nodeAdded(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsInserted(index,index);
			EAM.logDebug("DiagramModelListener: NodeAdded");
		}

		public void nodeDeleted(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsDeleted(index,index);
			EAM.logDebug("DiagramModelListener: NodeDeleted");
		}

		public void nodeChanged(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsUpdated(index,index);
			EAM.logDebug("DiagramModelListener: NodeChanged");
		}
		
		final static int TABLE_COLUMN_NAME = 0;
		final static int TABLE_COLUMN_X = 1;
		final static int TABLE_COLUMN_Y = 2;
		
		private Vector columnNames;
		private DiagramModel diagramModel;
	}
	
	class TableViewConnectionsModel extends AbstractTableModel implements DiagramModelListener
	{
		public TableViewConnectionsModel(DiagramModel diagramModelToUse)
		{
			super();
			diagramModel = diagramModelToUse;
			columnNames = new Vector();
			columnNames.add(EAM.text("Table|From Node"));
			columnNames.add(EAM.text("Table|To Node"));
		}
		
		protected void addListener()
		{
			diagramModel.addDiagramModelListener(this);
		}

		public int getColumnCount() 
		{
			return columnNames.size();
		}

		public int getRowCount() 
		{
			return diagramModel.getLinkageCount();
		}

		public Object getValueAt(int rowIndex, int columnIndex) 
		{
			try 
			{
				EAMGraphCell node = diagramModel.getNodeByIndex(rowIndex);
				
				switch (columnIndex)
				{
				case TABLE_COLUMN_FROM:
					return node.getText();
				case TABLE_COLUMN_TO:
					return node.getText();
				default:
					return null;
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}

		public String getColumnName(int column) 
		{
			return (String)columnNames.get(column);
		}
		
		public void nodeAdded(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsInserted(index,index);
			EAM.logDebug("DiagramModelLinkListener: NodeAdded");
		}

		public void nodeDeleted(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsDeleted(index,index);
			EAM.logDebug("DiagramModelLinkListener: NodeDeleted");
		}

		public void nodeChanged(DiagramModelEvent event) 
		{
			int index = event.getIndex();
			fireTableRowsUpdated(index,index);
			EAM.logDebug("DiagramModelLinkListener: NodeChanged");
		}
		
		final static int TABLE_COLUMN_FROM = 0;
		final static int TABLE_COLUMN_TO = 1;
		
		private Vector columnNames;
		private DiagramModel diagramModel;
	}
}
