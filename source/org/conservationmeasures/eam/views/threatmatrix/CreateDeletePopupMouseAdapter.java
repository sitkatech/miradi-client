/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMenuItem;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.LinkCreator;
import org.conservationmeasures.eam.views.diagram.LinkDeletor;

public class CreateDeletePopupMouseAdapter extends MouseAdapter
{
	public CreateDeletePopupMouseAdapter(Project projectToUse, AbstractThreatTargetTableModel modelToUse)
	{
		project = projectToUse;
		model = modelToUse;
	}
	
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
		JTable table = (JTable) event.getSource();
		int row = table.rowAtPoint(event.getPoint());
		int tableColumn = table.columnAtPoint(event.getPoint());
		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		if (!getThreatMatrixTableModel().isPopupSupportableCell(row, modelColumn))
			return;
		
		JPopupMenu menu = getRightClickMenu(table, row, tableColumn);
		menu.show(table, event.getX(), event.getY());
	}

	private JPopupMenu getRightClickMenu(JTable table, int row, int tableColumn)
	{
		JPopupMenu menu = new JPopupMenu();
		boolean canBeLinked = canBeLinked(table, row, tableColumn);
		int modelColumn = table.convertColumnIndexToModel(tableColumn);

		EAMenuItem creamMenuItem = new EAMenuItem(new ActionCreateFactorLink(row, modelColumn));
		creamMenuItem.setEnabled(canBeLinked);
		creamMenuItem.setText(EAM.text("Create Link"));
		
		boolean areLinked = areLinked(table, row, tableColumn);
		EAMenuItem deleteMenuItem = new EAMenuItem(new ActionDeleteFactorLink(row, modelColumn));
		deleteMenuItem.setEnabled(areLinked);
		deleteMenuItem.setText(EAM.text("Delete Link"));
		
		menu.add(creamMenuItem);
		menu.add(deleteMenuItem);
		return menu;
	}
	
	public boolean areLinked(JTable table, int row, int tableColumn)
	{
		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		return getThreatMatrixTableModel().isActiveCell(row, modelColumn);
	}
	
	private boolean canBeLinked(JTable table, int row, int tableColumn)
	{
		if (areLinked(table, row, tableColumn))
			return false;
		
		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		FactorId fromFactorId = getThreatMatrixTableModel().getThreatId(row);
		FactorId toFactorId = getThreatMatrixTableModel().getTargetId(modelColumn);
		
		if (! areBothFactorsContainedInAnyConceptualModel(fromFactorId, toFactorId))
			return false;
		
		return true;
	}
	
	private boolean areBothFactorsContainedInAnyConceptualModel(FactorId fromFactorId, FactorId toFactorId)
	{
		ORefList foundConceptualModels = getProject().findConceptualModelThatContainsBothFactors(fromFactorId, toFactorId);
		if (foundConceptualModels.size() > 0)
			return true;
		
		return false;
	}
	
	class ActionDeleteFactorLink extends AbstractAction
	{
		public ActionDeleteFactorLink(int rowToUse, int modelColumnToUse)
		{
			row = rowToUse;
			modelColumn = modelColumnToUse;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			FactorId threatId = getThreatMatrixTableModel().getThreatId(row); 
			FactorId targetId = getThreatMatrixTableModel().getTargetId(modelColumn);
			FactorLinkId factorLinkId = project.getFactorLinkPool().getLinkedId(threatId, targetId);

			if (!userConfirmsLinkDeletion(getThreatMatrixTableModel().getThreatName(row), getThreatMatrixTableModel().getTargetName(modelColumn)))
				return;
			
			try
			{
				doDelete(factorLinkId);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}

		private void doDelete(FactorLinkId factorLinkId) throws Exception
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
		
		private boolean userConfirmsLinkDeletion(String threatName, String targetName)
		{
			String text = EAM.text("This link will be deleted from any Conceptual Model pages" +
				  					" and Results Chains that it appears on: ");
			String threatAndTarget = "      " + threatName + " -> " + targetName;
			String[] body = new String[] {text, threatAndTarget,};	
			
			String[] buttons = new String[] {EAM.text("Delete Link"), EAM.text("Cancel")};
			return EAM.confirmDialog(EAM.text("Delete a link?"), body, buttons);
		}
		
		int row;
		int modelColumn;
	}
	
	class ActionCreateFactorLink extends AbstractAction
	{
		public ActionCreateFactorLink(int rowToUse, int modelColumnToUse)
		{
			row = rowToUse;
			modelColumn = modelColumnToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				createLink();
			}
			catch (Exception ex)
			{
				EAM.logException(ex);
			}
		}

		private void createLink() throws CommandFailedException
		{
			getProject().executeCommand(new CommandBeginTransaction());
			try
			{
				FactorId fromThreatId = getThreatMatrixTableModel().getThreatId(row);
				FactorId toTargetId = getThreatMatrixTableModel().getTargetId(modelColumn);
				createLinksInConceptualModels(fromThreatId, toTargetId);
			}
			catch (Exception ex)
			{
				throw new CommandFailedException(ex);
			}
			finally
			{
				getProject().executeCommand(new CommandEndTransaction());
			}
		}
		
		private void createLinksInConceptualModels(FactorId fromThreatId, FactorId toTargetId) throws Exception
		{

			ORefList conceptualModelDiagramRefs = getProject().findConceptualModelThatContainsBothFactors(fromThreatId, toTargetId);
			for (int i = 0; i < conceptualModelDiagramRefs.size(); ++i)
			{
				ORef conceptualModelDiagramRef = conceptualModelDiagramRefs.get(i);
				ConceptualModelDiagram conceptualModelDiagram = (ConceptualModelDiagram) getProject().findObject(conceptualModelDiagramRef);

				LinkCreator linkCreator = new LinkCreator(getProject());
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(conceptualModelDiagram, fromThreatId, toTargetId);
			}
		}

		int row;
		int modelColumn;
	}
	
	private AbstractThreatTargetTableModel getThreatMatrixTableModel()
	{
		return model;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private AbstractThreatTargetTableModel model;
}