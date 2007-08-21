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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMenuItem;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;
import org.conservationmeasures.eam.views.diagram.LinkCreator;
import org.conservationmeasures.eam.views.diagram.LinkDeletor;

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
	
	private boolean canBeLinked(ThreatMatrixTable table, int row, int col)
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) getModel();
		FactorId fromFactorId = model.getThreatId(row);
		FactorId toFactorId = model.getTargetId(col);
		
		if (! areBothFactorsContainedInAnyConceptualModel(fromFactorId, toFactorId))
			return false;
		
		return ! table.areLinked(row, col);
	}

	private boolean areBothFactorsContainedInAnyConceptualModel(FactorId fromFactorId, FactorId toFactorId)
	{
		ORef foundConceptualModel = findConceptualModelThatContainsBothFactors(fromFactorId, toFactorId);
		if (! foundConceptualModel.isInvalid())
			return true;
		
		return false;
	}

	private ORef findConceptualModelThatContainsBothFactors(FactorId fromFactorId, FactorId toFactorId)
	{
		ConceptualModelDiagramPool diagramPool = getProject().getConceptualModelDiagramPool();
		ORefList diagramORefs = diagramPool.getORefList();
		for (int i = 0; i < diagramORefs.size(); ++i)
		{
			ORef thisDiagramRef = diagramORefs.get(i);
			ConceptualModelDiagram diagram =  (ConceptualModelDiagram) getProject().findObject(thisDiagramRef);
			if (containsWrappedFactor(diagram.getAllDiagramFactorIds(), fromFactorId) && containsWrappedFactor(diagram.getAllDiagramFactorIds(), toFactorId))
				return thisDiagramRef; 		
		}
		
		return ORef.INVALID;
	}
	
	private boolean containsWrappedFactor(IdList diagramFactorIds, FactorId fromFactorId)
	{
		for (int i = 0; i < diagramFactorIds.size(); ++i)
		{
			ORef diagramFactorRef = new ORef(DiagramFactor.getObjectType(), diagramFactorIds.get(i));
			DiagramFactor diagramFactor = (DiagramFactor) getProject().findObject(diagramFactorRef);
			if (diagramFactor.getWrappedId().equals(fromFactorId))
				return true;
		}
		
		return false;
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
			boolean canBeLinked = canBeLinked(table, row, col);
			
			EAMenuItem creamMenuItem = new EAMenuItem(new ActionCreateFactorLink(row, col));
			creamMenuItem.setEnabled(canBeLinked);
			creamMenuItem.setText(EAM.text("Create Link"));
			
			boolean areLinked = table.areLinked(row, col);
			EAMenuItem deleteMenuItem = new EAMenuItem(new ActionDeleteFactorLink(row, col));
			deleteMenuItem.setEnabled(areLinked);
			deleteMenuItem.setText(EAM.text("Delete Link"));
			
			menu.add(creamMenuItem);
			menu.add(deleteMenuItem);
			return menu;
		}
	}
	
	class ActionDeleteFactorLink extends AbstractAction
	{
		public ActionDeleteFactorLink(int rowToUse, int colToUse)
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
			FactorLinkId factorLinkId = project.getFactorLinkPool().getLinkedId(threatId, targetId);

			if (!userConfirms())
				return;
			
			try
			{
				doDelete(project, factorLinkId);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}

		private void doDelete(Project project, FactorLinkId factorLinkId) throws Exception
		{
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				new LinkDeletor(project).deleteFactorLinkAndAllRefferers(factorLinkId);
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}
		
		private boolean userConfirms()
		{
			String[] body = new String[] {EAM.text("The link(s) will be deleted from all Conceptual Model pages" +
	  												" and Results Chains, not just this one. ")};	
			
			String[] buttons = new String[] {EAM.text("Delete Link"), EAM.text("Cancel")};
			return EAM.confirmDialog(EAM.text("Delete a link?"), body, buttons);
		}
		
		int row;
		int col;
	}
	
	class ActionCreateFactorLink extends AbstractAction
	{
		public ActionCreateFactorLink(int rowToUse, int colToUse)
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
			model.getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				FactorId fromThreatId = model.getThreatId(row);
				FactorId toTargetId = model.getTargetId(col);
				
				ORef conceptualModelDiagramRef = findConceptualModelThatContainsBothFactors(fromThreatId, toTargetId);
				ConceptualModelDiagram conceptualModelDiagram = (ConceptualModelDiagram) getProject().findObject(conceptualModelDiagramRef);
			
				LinkCreator linkCreator = new LinkCreator(getProject());
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(conceptualModelDiagram, fromThreatId, toTargetId);
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
	
	private BundleColumnSortHandler bundleColumnSortHandler;
	private final static Graphics staticGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
}
