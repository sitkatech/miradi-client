/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMenuItem;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.Delete;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class ThreatMatrixTable extends JTable
{
	public ThreatMatrixTable(TableModel model, ThreatGridPanel panel)
	{
		super(model);
		setIntercellSpacing(new Dimension(0, 0));
		setRowHeight(ThreatGridPanel.ROW_HEIGHT);
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setColumnWidths();
		
		ListSelectionModel listSelectionModel = getSelectionModel();

		CellSelectionListener selectionListener = new CellSelectionListener(this, panel);
		listSelectionModel.addListSelectionListener(selectionListener);
		
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer(panel);
		setDefaultRenderer(Object.class, customTableCellRenderer);
		
		JTableHeader columnHeader = getTableHeader();
		bundleColumnSortHandler = new BundleColumnSortHandler(panel);
		columnHeader.addMouseListener(bundleColumnSortHandler);
		columnHeader.addMouseMotionListener(bundleColumnSortHandler);
		
		addMouseListener(new TableMouseAdapter());
	}


	public void columnMoved(TableColumnModelEvent event)
	{
		if(event.getToIndex() == getSummaryColumn())
			moveColumn(event.getToIndex(), event.getFromIndex());
		else
			super.columnMoved(event);
	}
	
	public int getSummaryColumn()
	{
		return getColumnCount() - 1;
	}

	private void setColumnWidths()
	{
		Enumeration columns = getColumnModel().getColumns();
		while(columns.hasMoreElements())
		{
			TableColumn columnToAdjust = (TableColumn)columns.nextElement();
			int textWidth = avoidSplittingFirstWord(columnToAdjust);
			int width = Math.max(ThreatGridPanel.DEFAULT_COLUMN_WIDTH, textWidth + 20);
			columnToAdjust.setHeaderRenderer(new TableHeaderRenderer());
			columnToAdjust.setPreferredWidth(width);
			columnToAdjust.setWidth(width);
			columnToAdjust.setResizable(true);
		}
	}

	private int avoidSplittingFirstWord(TableColumn columnToAdjust)
	{
		String headerText = (String)columnToAdjust.getHeaderValue();
		
		int firstSpace = headerText.indexOf(' ');
		if(firstSpace >= 0)
			headerText = headerText.substring(0, firstSpace);
		
		if (headerText.length() == 0)
			headerText = "W";
		
		Graphics2D g2 = (Graphics2D)staticGraphics;
		TextLayout textLayout = new TextLayout(headerText, g2.getFont(), g2.getFontRenderContext());
		return textLayout.getBounds().getBounds().width;
	}
	
	public void sort(boolean sortOrder, int sortColumn)
	{
		bundleColumnSortHandler.setToggle(sortOrder);
		bundleColumnSortHandler.sort(sortColumn);
	}

	public boolean areLinked(int row, int column)
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) getModel();
		return model.getProject().isLinked(model.getThreatId(row), model.getTargetId(column));
	}
	
	
	class TableMouseAdapter extends MouseAdapter
	{
	    public void mousePressed(MouseEvent event)
		{
	    	ThreatMatrixTable table = (ThreatMatrixTable) event.getSource();
			int row = table.rowAtPoint(event.getPoint());
			int col = table.columnAtPoint(event.getPoint());
			final boolean isSummaryCell = (row == table.getRowCount() - 1) || (col == table.getColumnCount() - 1);
			if(isSummaryCell)
				return;
			boolean areLinked = table.areLinked(row, col);
			JPopupMenu menu = getRightClickMenu(areLinked, row, col);
			menu.show(table, event.getX(), event.getY());
		}

		private JPopupMenu getRightClickMenu(boolean areLinked, int row, int col)
		{
			JPopupMenu menu = new JPopupMenu();
			EAMenuItem creamMenuItem = new EAMenuItem(new ActionCreateModelLinkage(!areLinked, row, col));
			creamMenuItem.setText(EAM.text("Create Link"));
			EAMenuItem deleteMenuItem = new EAMenuItem(new ActionDeleteModelLinkage(areLinked,  row, col));
			deleteMenuItem.setText(EAM.text("Delete Link"));
			menu.add(creamMenuItem);
			menu.add(deleteMenuItem);
			return menu;
		}
	}
	
	class ActionDeleteModelLinkage extends AbstractAction
	{
		public ActionDeleteModelLinkage(boolean enabled, int rowToUse, int colToUse)
		{
			setEnabled(enabled);
			row = rowToUse;
			col = colToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			ThreatMatrixTableModel model = (ThreatMatrixTableModel)getModel();
			Project project = model.getProject();
			DiagramModel diagramModel = project.getDiagramModel();
			FactorId threatId = model.getThreatId(row); 
			FactorId targetId = model.getTargetId(col);
			FactorLinkId modelLinkageId = project.getFactorLinkPool().getLinkedId(threatId, targetId);
			try
			{
				DiagramFactorLink linkageToDelete = diagramModel.getDiagramFactorLinkbyWrappedId(modelLinkageId);
				project.executeCommand(new CommandBeginTransaction());
				Delete.deleteFactorLink(project, linkageToDelete);
				project.executeCommand(new CommandEndTransaction());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		int row;
		int col;
	}
	
	class ActionCreateModelLinkage extends AbstractAction
	{
		public ActionCreateModelLinkage(boolean enabled, int rowToUse, int colToUse)
		{
			setEnabled(enabled);
			row = rowToUse;
			col = colToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			ThreatMatrixTableModel model = (ThreatMatrixTableModel)getModel();
			Project project = model.getProject();
			try
			{
				project.executeCommand(new CommandBeginTransaction());
				InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(
					project, 
					model.getThreatId(row), 
					model.getTargetId(col));
				project.executeCommand(new CommandEndTransaction());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		int row;
		int col;
	}
	
	private BundleColumnSortHandler bundleColumnSortHandler;
	private final static Graphics staticGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
}
