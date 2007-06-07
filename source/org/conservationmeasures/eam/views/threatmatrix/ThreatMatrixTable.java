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

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMenuItem;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;
import org.conservationmeasures.eam.views.diagram.DeleteSelectedItemDoer;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class ThreatMatrixTable extends TableWithHelperMethods
{
	public ThreatMatrixTable(ThreatMatrixTableModel model, ThreatGridPanel panel)
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
	

	
	public Project getProject()
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel)getModel();
		return model.getProject();
	}
	
	class TableMouseAdapter extends MouseAdapter
	{
	    public void mousePressed(MouseEvent event)
		{
			if (event.isPopupTrigger())
				showPopUp(event);
		}


	    public void mouseReleased(MouseEvent event)
	    {
			if (event.isPopupTrigger())
				showPopUp(event);
	    }

		private void showPopUp(MouseEvent event)
		{
			ThreatMatrixTable table = (ThreatMatrixTable) event.getSource();
	    	ThreatMatrixTableModel model = (ThreatMatrixTableModel)table.getModel();
			int row = table.rowAtPoint(event.getPoint());
			int col = table.columnAtPoint(event.getPoint());
			if(model.isSummaryData(row, col))
				return;
			JPopupMenu menu = getRightClickMenu(table, row, col);
			menu.show(table, event.getX(), event.getY());
		}
	    
		private JPopupMenu getRightClickMenu(ThreatMatrixTable table, int row, int col)
		{
			JPopupMenu menu = new JPopupMenu();
			boolean areLinked = table.areLinked(row, col);
			
			EAMenuItem creamMenuItem = new EAMenuItem(new ActionCreateModelLinkage(row, col));
			creamMenuItem.setEnabled(!areLinked);
			creamMenuItem.setText(EAM.text("Create Link"));
			
			EAMenuItem deleteMenuItem = new EAMenuItem(new ActionDeleteModelLinkage(row, col));
			deleteMenuItem.setEnabled(areLinked);
			deleteMenuItem.setText(EAM.text("Delete Link"));
			
			menu.add(creamMenuItem);
			menu.add(deleteMenuItem);
			return menu;
		}
	}
	
	
	public void setSelectedThreat(FactorId factorId)
	{
		selectedThreatFactorId = factorId;
	}
	
	public FactorId getSelectedThreat()
	{
		return selectedThreatFactorId;
	}
	
	public void setSelectedTarget(FactorId factorId)
	{
		selectedTargetFactorId = factorId;
	}
	
	public FactorId getSelectedTarget()
	{
		return selectedTargetFactorId;
	}
	

	
	
	class ActionDeleteModelLinkage extends AbstractAction
	{
		public ActionDeleteModelLinkage(int rowToUse, int colToUse)
		{
			row = rowToUse;
			col = colToUse;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			ThreatMatrixTableModel model = (ThreatMatrixTableModel)getModel();
			Project project = model.getProject();
			FactorId threatId = model.getThreatId(row); 
			FactorId targetId = model.getTargetId(col);
			FactorLinkId modelLinkageId = project.getFactorLinkPool().getLinkedId(threatId, targetId);

			String[] body = new String[] {
					EAM.text("Are sure you want to delete the link between this Threat and Target?"),
					(model).getThreatName(row),
					(model).getTargetName(col),
					};
			String[] buttons = new String[] {EAM.text("Delete Link"), EAM.text("Cancel")};
			if(!EAM.confirmDialog(EAM.text("Delete a link?"), body, buttons))
				return;
			
			try
			{
				doDelete(project, modelLinkageId);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}

		private void doDelete(Project project, FactorLinkId modelLinkageId) throws Exception
		{
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				DiagramObject diagramObject = project.getConceptualModelDiagram();
				DiagramFactorLink diagramFactorLink = diagramObject.getDiagramFactorLink(modelLinkageId);
				DeleteSelectedItemDoer.deleteFactorLink(diagramObject, diagramFactorLink);
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}
		
		int row;
		int col;
	}
	
	class ActionCreateModelLinkage extends AbstractAction
	{
		public ActionCreateModelLinkage(int rowToUse, int colToUse)
		{
			row = rowToUse;
			col = colToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				createLink((ThreatMatrixTableModel)getModel());
			}
			catch (Exception ex)
			{
				EAM.logException(ex);
			}
		}

		private void createLink(ThreatMatrixTableModel model) throws CommandFailedException
		{
			DiagramObject diagramObject = model.getProject().getConceptualModelDiagram();
			model.getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				FactorId fromThreatId = model.getThreatId(row);
				FactorId toTargetId = model.getTargetId(col);
				InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(diagramObject, fromThreatId, toTargetId);
			}
			catch (Exception ex)
			{
				throw new CommandFailedException(ex);
			}
			finally
			{
				model.getProject().executeCommand(new CommandEndTransaction());
			}
		}
		
		int row;
		int col;
	}
	
	private FactorId selectedThreatFactorId;
	private FactorId selectedTargetFactorId;
	private BundleColumnSortHandler bundleColumnSortHandler;
	private final static Graphics staticGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
}
