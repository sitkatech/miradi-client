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
package org.miradi.views.diagram;

import java.awt.Point;
import java.util.HashSet;

import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.base.ModelessDialogWithDirections;
import org.miradi.dialogs.diagram.FactorLinkPropertiesDialog;
import org.miradi.dialogs.diagram.FactorLinkPropertiesPanel;
import org.miradi.dialogs.diagram.FactorPropertiesPanel;
import org.miradi.dialogs.diagram.GroupBoxPropertiesPanel;
import org.miradi.dialogs.diagram.ProjectScopePanel;
import org.miradi.dialogs.diagram.TextBoxPropertiesPanel;
import org.miradi.dialogs.groupboxLink.GroupBoxLinkListTablePanel;
import org.miradi.dialogs.groupboxLink.GroupBoxLinkManagementPanel;
import org.miradi.dialogs.groupboxLink.GroupBoxLinkTableModel;
import org.miradi.dialogs.stress.StressPropertiesPanel;
import org.miradi.dialogs.task.TaskPropertiesInputPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.views.umbrella.StaticPicker;

public class PropertiesDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		try
		{
			if(!getProject().isOpen())
				return false;
			
			if (! isInDiagram())
				return false;
			
			FactorCell[] selectedFactorCells = getDiagramComponent().getOnlySelectedFactorCells();
			HashSet<LinkCell> selectedLinkCells = getSelectedLinksExcludingInternalGroupBoxLinks();
			final int selectedFactorAndLinkCount = selectedLinkCells.size() + selectedFactorCells.length;
			boolean isScopeSelected = isScopeBoxSelected();
			
			if(selectedFactorAndLinkCount == 0 && isScopeSelected)
				return true;
			
			if(selectedFactorAndLinkCount != 1)
				return false;
			
			EAMGraphCell selected = getCorrectCellToShowPropertiesFor();
			if(selected == null)
				return false;
			
			if(selected.isFactor() || selected.isProjectScope())
				return true;
			
			if(selected.isFactorLink())
				return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		
		return false;
	}

	private boolean isScopeBoxSelected()
	{
		Object[] selectedCells = getDiagramComponent().getSelectionCells();
		for(Object object : selectedCells)
		{
			EAMGraphCell cell = (EAMGraphCell)object;
			if(cell.isProjectScope())
				return true;
		}
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		try
		{
			EAMGraphCell topCellAtClickPoint = getCorrectCellToShowPropertiesFor();
			if(topCellAtClickPoint.isFactor())
				doFactorProperties((FactorCell)topCellAtClickPoint, getLocation());

			else if(topCellAtClickPoint.isProjectScope())
				doProjectScopeProperties();

			else if(topCellAtClickPoint.isFactorLink())
				doFactorLinkProperties(topCellAtClickPoint.getDiagramLink());
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private EAMGraphCell getCorrectCellToShowPropertiesFor() throws Exception
	{
		EAMGraphCell[] selectedCells = getDiagramComponent().getOnlySelectedCells();
		if(selectedCells.length == 0)
			return null;
		
		EAMGraphCell selected = selectedCells[0];
		HashSet<FactorCell> children = getChildrenIfAny(selected);

		EAMGraphCell topCellAtClickPoint = (EAMGraphCell) getDiagramComponent().getFirstCellForLocation(getLocation().x, getLocation().y);
		if(topCellAtClickPoint == null)
			return selected;
		
		if (children.contains(topCellAtClickPoint))
			return topCellAtClickPoint;
		if (topCellAtClickPoint.isFactorLink())
			return topCellAtClickPoint; 
		
		return selected;
	}

	private HashSet<FactorCell> getChildrenIfAny(EAMGraphCell selected) throws Exception
	{
		if(selected.isFactorLink())
			return new HashSet();
		
		PersistentDiagramModel model = getDiagramView().getDiagramPanel().getDiagramModel();
		if (selected.isProjectScope())
			return new HashSet(model.getAllDiagramTargets());
		
		if (selected.getDiagramFactor().isGroupBoxFactor())
			return  model.getGroupBoxFactorChildren(selected);			
		
		return new HashSet();
	}
	
	class ScopePropertiesDialog extends ModelessDialogWithDirections
	{
		public ScopePropertiesDialog(MainWindow parent, ProjectScopePanel panel)
		{
			super(parent, panel.getPanelDescription());
			setScrollableMainPanel(panel);
		}
	}

	void doProjectScopeProperties() throws CommandFailedException
	{
		ProjectScopePanel projectScopePanel = new ProjectScopePanel(getProject(), getProject().getMetadata());
		ScopePropertiesDialog dlg = new ScopePropertiesDialog(getMainWindow(), projectScopePanel); 
		getView().showFloatingPropertiesDialog(dlg);
	}
	
	void doFactorLinkProperties(DiagramLink diagramLink) throws Exception
	{
		if (diagramLink.isTargetLink() && diagramLink.isGroupBoxLink())
		{
			GroupBoxLinkManagementPanel dialogPanel = createDialogPanel(diagramLink);
			showPropertiesPanel(dialogPanel);
			dialogPanel.updateSplitterLocation();
		}
		else
		{
			showPropertiesPanel(getFactorLinkPropertiesPanel(diagramLink));
		}
	}

	private void showPropertiesPanel(ModelessDialogPanel dialogPanel)
	{
		FactorLinkPropertiesDialog dlg = new FactorLinkPropertiesDialog(getMainWindow(), dialogPanel, dialogPanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(dlg);
	}

	private GroupBoxLinkManagementPanel createDialogPanel(DiagramLink diagramLink) throws Exception
	{
		ORefList children = diagramLink.getSelfOrChildren();
		ORef firstChildRef = children.get(0);
		DiagramLink diagramLinkChild = DiagramLink.find(getProject(), firstChildRef);
		
		GroupBoxLinkTableModel model = new GroupBoxLinkTableModel(getProject(), diagramLink.getRef(), DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS);
		GroupBoxLinkListTablePanel tablePanel = new GroupBoxLinkListTablePanel(getMainWindow(), model);
		FactorLinkPropertiesPanel factorLinkPropertiesPanel = FactorLinkPropertiesPanel.createGroupBoxedTargetLinkPropertiesPanel(getMainWindow(), diagramLinkChild.getWrappedRef(), tablePanel.getPicker());
		
		return new GroupBoxLinkManagementPanel(getMainWindow(), diagramLink.getRef(), DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, getMainWindow().getActions(), tablePanel, factorLinkPropertiesPanel);
	}
	
	private FactorLinkPropertiesPanel getFactorLinkPropertiesPanel(DiagramLink diagramLink) throws Exception
	{
		boolean isTargetLink = diagramLink.isTargetLink();
		boolean isStressBasedMode = getProject().getMetadata().getThreatRatingMode().equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
		if (!isTargetLink || !isStressBasedMode)
			return FactorLinkPropertiesPanel.createWithOnlyBidirectionalAndColorPropertiesPanel(getProject(), diagramLink);
		
		ORef fromRef = diagramLink.getUnderlyingLink().getFromFactorRef();
		ORef toRef = diagramLink.getUnderlyingLink().getToFactorRef();
		ORefList hierarchyRefs = new ORefList();
		hierarchyRefs.add(diagramLink.getRef());
		hierarchyRefs.add(diagramLink.getWrappedRef());
		if (Target.is(fromRef))
		{
			hierarchyRefs.add(fromRef);
			hierarchyRefs.add(toRef);
		}
		else 
		{
			hierarchyRefs.add(toRef);
			hierarchyRefs.add(fromRef);
		}
	
		StaticPicker picker = new StaticPicker(hierarchyRefs);
		
		return FactorLinkPropertiesPanel.createTargetLinkPropertiesPanel(getMainWindow(), diagramLink, picker);
	}
	
	private void doFactorProperties(FactorCell selectedFactorCell, Point at) throws Exception
	{
		int tabToStartOn = getTabToStartOn(selectedFactorCell, at);
		DiagramFactor diagramFactor = selectedFactorCell.getDiagramFactor();
		doFactorProperties(diagramFactor, tabToStartOn);
	}

	public void doFactorProperties(DiagramFactor diagramFactor, int tabToStartOn) throws Exception
	{
		int wrappedType = diagramFactor.getWrappedType();
		
		if (TextBox.is(wrappedType))
			doTextBoxProperties(diagramFactor);
		else if (GroupBox.is(wrappedType))
			doGroupBoxProperties(diagramFactor);
		else if (Stress.is(wrappedType))
			doStressProperties(diagramFactor);
		else if (Task.is(wrappedType))
			doActivityProperties(diagramFactor);
		else
			doNormalFactorProperties(diagramFactor, tabToStartOn);
	}

	private void doNormalFactorProperties(DiagramFactor diagramFactor, int tabToStartOn)
	{
		DiagramView view = (DiagramView)getView();
		view.showNodeProperties(diagramFactor, tabToStartOn);
	}
	
	private void doTextBoxProperties(DiagramFactor diagramFactor)
	{
		TextBoxPropertiesPanel panel = new TextBoxPropertiesPanel(getProject(), diagramFactor);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}

	private void doGroupBoxProperties(DiagramFactor diagramFactor)
	{
		GroupBoxPropertiesPanel panel = new GroupBoxPropertiesPanel(getProject(), diagramFactor);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}
	
	private void doStressProperties(DiagramFactor diagramFactor) throws Exception
	{
		StressPropertiesPanel panel = StressPropertiesPanel.createWithoutVisibilityPanel(getMainWindow());
		
		addDiagramWrappedRefToHierarchyAndShowPanel(diagramFactor, panel);
	}
	
	private void doActivityProperties(DiagramFactor diagramFactor) throws Exception
	{
		TaskPropertiesInputPanel panel = new TaskPropertiesInputPanel(getMainWindow(), null, BaseId.INVALID);

		addDiagramWrappedRefToHierarchyAndShowPanel(diagramFactor, panel);
	}

	private void addDiagramWrappedRefToHierarchyAndShowPanel(DiagramFactor diagramFactor, AbstractObjectDataInputPanel propertiesPanel)
	{
		ORefList selectedHierarchy = new ORefList(diagramFactor.getRef());
		selectedHierarchy.add(diagramFactor.getWrappedORef());
		propertiesPanel.setObjectRefs(selectedHierarchy);
		ModelessDialogWithClose propertiesDialog = new ModelessDialogWithClose(getMainWindow(), propertiesPanel, propertiesPanel.getPanelDescription()); 
		getView().showFloatingPropertiesDialog(propertiesDialog);
	}

	// TODO: The tab should probably be computed elsewhere?
	private int getTabToStartOn(FactorCell factorCell, Point screenPoint)
	{
		if(screenPoint == null)
			return FactorPropertiesPanel.TAB_DETAILS;
		
		Point pointRelativeToCellOrigin = getDiagramComponent().convertScreenPointToCellRelativePoint(screenPoint, factorCell);

		EAM.logVerbose(screenPoint.toString() + "->" + pointRelativeToCellOrigin.toString());
		if(factorCell.isPointInObjective(pointRelativeToCellOrigin))
		{
			EAM.logVerbose("Objective");
			return FactorPropertiesPanel.TAB_OBJECTIVES;
		}
		if (factorCell.isPointInViability(pointRelativeToCellOrigin))
		{
			EAM.logVerbose("ViabilityModeTNC");
			return FactorPropertiesPanel.TAB_VIABILITY;
		}
		if(factorCell.isPointInIndicator(pointRelativeToCellOrigin))
		{
			EAM.logVerbose("Indicator");
			return FactorPropertiesPanel.TAB_INDICATORS;
		}
		if(factorCell.isPointInGoal(pointRelativeToCellOrigin))
		{
			EAM.logVerbose("Goal");
			return FactorPropertiesPanel.TAB_GOALS;
		}
		
		return FactorPropertiesPanel.TAB_DETAILS;
	}
	
	private HashSet<LinkCell> getSelectedLinksExcludingInternalGroupBoxLinks()
	{
		HashSet<LinkCell> selectedLinkCells = new HashSet(getDiagramComponent().getOnlySelectedLinkCells());
		HashSet<LinkCell> selectedInternalGroupBoxLinks = getInternalGroupBoxLinks();
		selectedLinkCells.removeAll(selectedInternalGroupBoxLinks);
		return selectedLinkCells;
	}

	private HashSet<LinkCell> getInternalGroupBoxLinks()
	{
		try
		{
			HashSet<FactorCell> groupBoxesAndChildren = getDiagramComponent().getOnlySelectedFactorAndGroupChildCells();
			HashSet<LinkCell> linkInsideGroupBox = getDiagramComponent().getAllLinksInsideGroupBox(groupBoxesAndChildren);
			return linkInsideGroupBox;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new HashSet();
		}
	}

	private DiagramComponent getDiagramComponent()
	{
		return getDiagramView().getCurrentDiagramComponent();
	}
}
