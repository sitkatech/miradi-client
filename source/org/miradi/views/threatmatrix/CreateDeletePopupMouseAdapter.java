/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAM;
import org.miradi.main.EAMenuItem;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.project.Project;
import org.miradi.views.diagram.LinkCreator;
import org.miradi.views.diagram.LinkDeletor;

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
		ORef fromFactorRef = getThreatMatrixTableModel().getThreatRef(row);
		ORef toFactorRef = getThreatMatrixTableModel().getTargetRef(modelColumn);
		
		if (! areBothFactorsContainedInAnyConceptualModel(fromFactorRef, toFactorRef))
			return false;
		
		return true;
	}
	
	private boolean areBothFactorsContainedInAnyConceptualModel(ORef fromFactorRef, ORef toFactorRef)
	{
		ORefList foundConceptualModels = getProject().findConceptualModelThatContainsBothFactors(fromFactorRef, toFactorRef);
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
			ORef threatRef = getThreatMatrixTableModel().getThreatRef(row); 
			ORef targetRef = getThreatMatrixTableModel().getTargetRef(modelColumn);
			FactorLinkId factorLinkId = project.getFactorLinkPool().getLinkedId(threatRef, targetRef);

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
				ORef fromThreatId = getThreatMatrixTableModel().getThreatRef(row);
				ORef toTargetId = getThreatMatrixTableModel().getTargetRef(modelColumn);
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
		
		private void createLinksInConceptualModels(ORef fromThreatRef, ORef toTargetRef) throws Exception
		{

			ORefList conceptualModelDiagramRefs = getProject().findConceptualModelThatContainsBothFactors(fromThreatRef, toTargetRef);
			for (int i = 0; i < conceptualModelDiagramRefs.size(); ++i)
			{
				ORef conceptualModelDiagramRef = conceptualModelDiagramRefs.get(i);
				ConceptualModelDiagram conceptualModelDiagram = (ConceptualModelDiagram) getProject().findObject(conceptualModelDiagramRef);

				LinkCreator linkCreator = new LinkCreator(getProject());
				linkCreator.createFactorLinkAndAddToDiagramUsingCommands(conceptualModelDiagram, fromThreatRef, toTargetRef);
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