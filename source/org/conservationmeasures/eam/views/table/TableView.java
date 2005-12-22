/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.DiagramModelEvent;
import org.conservationmeasures.eam.diagram.DiagramModelListener;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiSortableTable;
import org.martus.swing.UiTabbedPane;
import org.martus.util.SortableTableModel;

public class TableView extends UmbrellaView
{
	public TableView(MainWindow mainWindowToUse) 
	{
		super(mainWindowToUse);
		setToolBar(new TableToolBar(mainWindowToUse.getActions()));
		addDiagramViewDoersToMap();

		setLayout(new BorderLayout());
		DiagramModel diagramModel = mainWindowToUse.getProject().getDiagramModel();
		TableNodesModel nodesModel = new TableNodesModel(diagramModel);
		nodesModel.addListener();
		nodesTable = new UiSortableTable(nodesModel);
		
		TableViewLinkagesModel linkagesModel = new TableViewLinkagesModel(diagramModel);
		linkagesModel.addListener();
		linkagesTable = new UiSortableTable(linkagesModel);

		tabbedPane = new UiTabbedPane();
		tabbedPane.add(EAM.text("Tab|Nodes"),new UiScrollPane(nodesTable));
		tabbedPane.add(EAM.text("Tab|Linkages"),new UiScrollPane(linkagesTable));
		tabbedPane.addChangeListener(new TabbedChangeListener());
		add(tabbedPane, BorderLayout.CENTER);
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
	
	public JComponent getPrintableComponent()
	{
		JTable sourceTable = getCurrentTable();
		JTable printTable = new JTable(sourceTable.getModel());
		JScrollPane printPane = new JScrollPane(printTable);
		printPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		printPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		AdjustableDimension tableSize = getTableSize(sourceTable);
		tableSize.addInsets(getBorderInsets(printPane, printPane.getBorder()));
		tableSize.addInsets(getBorderInsets(printPane.getViewport(), printPane.getViewportBorder()));
		printPane.setPreferredSize(tableSize);
		return printPane;
	}
	
	private Insets getBorderInsets(JComponent borderedComponent, Border border)
	{
		if(border == null)
			return new Insets(0, 0, 0, 0);
		return border.getBorderInsets(borderedComponent);
	}
	
	static class AdjustableDimension extends Dimension
	{
		public AdjustableDimension(int initialWidth, int initialHeight)
		{
			super(initialWidth, initialHeight);
		}
		
		public void addInsets(Insets insets)
		{
			width += (insets.left + insets.right);
			height += (insets.top + insets.bottom);
		}
	}

	private JTable getCurrentTable()
	{
		if (tabbedPane.getSelectedIndex() == 0)
			return nodesTable;
		return linkagesTable;
	}
	
	private AdjustableDimension getTableSize(JTable table) 
	{
		int tableHeight = table.getHeight() + table.getTableHeader().getHeight();
		return new AdjustableDimension(table.getWidth(), tableHeight);
	}

	private void addDiagramViewDoersToMap()
	{
		addDoerToMap(ActionPrint.class, new Print());
	}
	
	public boolean anythingToPrint()
	{
		return getCurrentTable().getRowCount() > 0;
	}
	
	class TabbedChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) 
		{
			getActions().updateActionStates();
		}
	}
	

	class TableNodesModel extends SortableTableModel implements DiagramModelListener
	{
		public TableNodesModel(DiagramModel diagramModelToUse)
		{
			super();
			diagramModel = diagramModelToUse;
			columnNames = new Vector();
			columnNames.add(EAM.text("Table|Name"));
			columnNames.add(EAM.text("Table|Type"));
			columnNames.add(EAM.text("Table|Indicator"));
			columnNames.add(EAM.text("Table|Priority"));
			columnNames.add(EAM.text("Table|X"));
			columnNames.add(EAM.text("Table|Y"));
			columnNames.add(EAM.text("Table|Width"));
			columnNames.add(EAM.text("Table|Height"));
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

		public Object getValueAtDirect(int rowIndex, int columnIndex) 
		{
			Vector nodes = diagramModel.getAllNodes();
			try 
			{
				DiagramNode node = (DiagramNode)nodes.get(rowIndex);
				switch (columnIndex)
				{
				case TABLE_COLUMN_NAME:
					return node.getText();
				case TABLE_COLUMN_TYPE:
					return getNodeType(node);
				case TABLE_COLUMN_INDICATOR:
					return getNodeIndicator(node);
				case TABLE_COLUMN_PRIORITY:
					return getNodePriority(node);
				case TABLE_COLUMN_X:
					return new Integer(node.getLocation().x);
				case TABLE_COLUMN_Y:
					return new Integer(node.getLocation().y);
				case TABLE_COLUMN_WIDTH:
					return new Integer(node.getSize().width);
				case TABLE_COLUMN_HEIGHT:
					return new Integer(node.getSize().height);
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

		public String getNodeType(EAMGraphCell cell)
		{
			if(cell.isLinkage())
				return EAM.text("Type|Linkage");
			DiagramNode node = (DiagramNode)cell;
			if(node.isTarget())
				return EAM.text("Type|Target");
			if(node.isIndirectFactor())
				return EAM.text("Type|Indirect Factor");
			if(node.isDirectThreat())
				return EAM.text("Type|Direct Threat");
			if(node.isStress())
				return EAM.text("Type|Stress");
			if(node.isIntervention())
				return EAM.text("Type|Intervention");
			return EAM.text("Type|Unknown Type");
		}
		
		public String getNodePriority(DiagramNode node)
		{
			if(!node.canHavePriority())
				return "";
			return node.getThreatPriority().getStringValue();
		}
		
		public String getNodeIndicator(DiagramNode node)
		{
			return node.getIndicator().toString();
		}

		public String getColumnName(int column) 
		{
			return (String)columnNames.get(column);
		}
		
		public void nodeAdded(DiagramModelEvent event) 
		{
			fireTableDataChanged();
			clearSortedOrder();
			EAM.logVerbose("DiagramModelListener: NodeAdded");
		}

		public void nodeDeleted(DiagramModelEvent event) 
		{
			fireTableDataChanged();
			clearSortedOrder();
			EAM.logVerbose("DiagramModelListener: NodeDeleted");
		}

		public void nodeChanged(DiagramModelEvent event) 
		{
			fireTableDataChanged();
			clearSortedOrder();
			EAM.logVerbose("DiagramModelListener: NodeChanged");
		}

		public void linkageAdded(DiagramModelEvent event) 
		{
		}

		public void linkageDeleted(DiagramModelEvent event) 
		{
		}
		
		public void nodeMoved(DiagramModelEvent event)
		{
		}
		
		final static int TABLE_COLUMN_NAME = 0;
		final static int TABLE_COLUMN_TYPE = 1;
		final static int TABLE_COLUMN_INDICATOR = 2;
		final static int TABLE_COLUMN_PRIORITY = 3;
		final static int TABLE_COLUMN_X = 4;
		final static int TABLE_COLUMN_Y = 5;
		final static int TABLE_COLUMN_WIDTH = 6;
		final static int TABLE_COLUMN_HEIGHT = 7;
		
		private Vector columnNames;
		private DiagramModel diagramModel;
	}
	
	class TableViewLinkagesModel extends SortableTableModel implements DiagramModelListener
	{
		public TableViewLinkagesModel(DiagramModel diagramModelToUse)
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

		public Object getValueAtDirect(int rowIndex, int columnIndex) 
		{
			Vector linkages = diagramModel.getAllLinkages();
			try 
			{
				DiagramLinkage linkage = (DiagramLinkage)linkages.get(rowIndex);
				
				switch (columnIndex)
				{
				case TABLE_COLUMN_FROM:
					return linkage.getFromNode().getText();
				case TABLE_COLUMN_TO:
					return linkage.getToNode().getText();
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
		}

		public void nodeDeleted(DiagramModelEvent event) 
		{
		}

		public void nodeChanged(DiagramModelEvent event) 
		{
		}
		
		public void nodeMoved(DiagramModelEvent event)
		{
		}
		
		public void linkageAdded(DiagramModelEvent event) 
		{
			fireTableDataChanged();
			clearSortedOrder();
			EAM.logVerbose("DiagramModelLinkListener: linkAdded");
		}

		public void linkageDeleted(DiagramModelEvent event) 
		{
			fireTableDataChanged();
			clearSortedOrder();
			EAM.logVerbose("DiagramModelLinkListener: linkDeleted");
		}

		final static int TABLE_COLUMN_FROM = 0;
		final static int TABLE_COLUMN_TO = 1;
		
		private Vector columnNames;
		private DiagramModel diagramModel;
	}
	
	UiTabbedPane tabbedPane;
	UiSortableTable nodesTable;
	UiSortableTable linkagesTable;
}
