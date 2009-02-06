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
package org.miradi.diagram;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.martus.swing.UiMenu;
import org.martus.swing.UiPopupMenu;
import org.martus.swing.Utilities;
import org.miradi.actions.ActionCopy;
import org.miradi.actions.ActionCreateBendPoint;
import org.miradi.actions.ActionCreateIncomingJunction;
import org.miradi.actions.ActionCreateOutgoingJunction;
import org.miradi.actions.ActionCreateResultsChain;
import org.miradi.actions.ActionCut;
import org.miradi.actions.ActionDelete;
import org.miradi.actions.ActionDeleteBendPoint;
import org.miradi.actions.ActionDeleteGroupBox;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionGroupBoxAddFactor;
import org.miradi.actions.ActionGroupBoxRemoveFactor;
import org.miradi.actions.ActionInsertContributingFactor;
import org.miradi.actions.ActionInsertDirectThreat;
import org.miradi.actions.ActionInsertDraftStrategy;
import org.miradi.actions.ActionInsertFactorLink;
import org.miradi.actions.ActionInsertGroupBox;
import org.miradi.actions.ActionInsertIntermediateResult;
import org.miradi.actions.ActionInsertStrategy;
import org.miradi.actions.ActionInsertTarget;
import org.miradi.actions.ActionInsertTextBox;
import org.miradi.actions.ActionInsertThreatReductionResult;
import org.miradi.actions.ActionManageFactorTagsFromMenu;
import org.miradi.actions.ActionPaste;
import org.miradi.actions.ActionPasteFactorContent;
import org.miradi.actions.ActionPasteWithoutLinks;
import org.miradi.actions.ActionProperties;
import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionSelectAll;
import org.miradi.actions.ActionSelectChain;
import org.miradi.actions.ActionShowConceptualModel;
import org.miradi.actions.ActionShowFullModelMode;
import org.miradi.actions.ActionShowResultsChain;
import org.miradi.actions.ActionShowSelectedChainMode;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.actions.LocationAction;
import org.miradi.actions.MainWindowAction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.icons.TaggedObjectSetIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objectpools.TaggedObjectSetPool;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;
import org.miradi.utils.LocationHolder;
import org.miradi.utils.MenuItemWithoutLocation;
import org.miradi.views.diagram.DiagramView;

public class DiagramContextMenuHandler
{
	public DiagramContextMenuHandler(MainWindow  mainWindowToUse, DiagramComponent diagramComponentToUse, Actions actionsToUse)
	{
		mainWindow = mainWindowToUse;
		diagramComponent = diagramComponentToUse;
		actions = actionsToUse;
	}
	
	public UiPopupMenu getPopupMenu(Point menuInvokedAt)
	{
		UiPopupMenu menu = new UiPopupMenu();

		JMenuItem propMenuItem = createMenuItem(ActionProperties.class, menuInvokedAt);
		propMenuItem.setFont(propMenuItem.getFont().deriveFont(Font.BOLD));
		menu.add(propMenuItem);
		
		menu.addSeparator();
		menu.add(getInsertMenu(menuInvokedAt));
		menu.add(getGroupBoxMenu(menuInvokedAt));
		menu.add(createMenuItem(ActionCreateBendPoint.class, menuInvokedAt));
		
		MainWindowAction objectsAction = actions.getMainWindowAction(ActionDeleteBendPoint.class);
		if (objectsAction.isEnabled())
			menu.add(createMenuItem(ActionDeleteBendPoint.class, menuInvokedAt));
		
		int selectedFactorCount = diagramComponent.getOnlySelectedFactors().length;
		menu.add(createMenuItem(ActionCreateIncomingJunction.class, menuInvokedAt));
		menu.add(createMenuItem(ActionCreateOutgoingJunction.class, menuInvokedAt));
		if(selectedFactorCount == 1)
			menu.add(createMenuItem(ActionManageFactorTagsFromMenu.class, menuInvokedAt));
		if(selectedFactorCount > 0)
		{
			menu.add(createTagFactorsMenu());
			menu.add(createUntagFactorsMenu());
		}
		
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionUndo.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionRedo.class)));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionCut.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionCopy.class)));
		menu.add(createMenuItem(ActionPaste.class, menuInvokedAt));
		menu.add(createMenuItem(ActionPasteFactorContent.class, menuInvokedAt));
		menu.add(createMenuItem(ActionPasteWithoutLinks.class, menuInvokedAt));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionDelete.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionSelectAll.class)));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionSelectChain.class)));
		menu.add(getModeSwitchMenuItem());
		DiagramView diagramView = (DiagramView) mainWindow.getCurrentView();
		menu.add(getDiagamModeSwitchItem(diagramView, actions));
		menu.add(new MenuItemWithoutLocation(actions.get(ActionCreateResultsChain.class)));
		menu.addSeparator();
		menu.add(new MenuItemWithoutLocation(actions.get(ActionDiagramProperties.class)));
		return menu;
	}

	abstract class AbstractActionTagUntagFactor extends AbstractAction
	{
		public AbstractActionTagUntagFactor(ORefSet factorRefsToTag, TaggedObjectSet tagSetToApply)
		{
			super(tagSetToApply.getLabel(), new TaggedObjectSetIcon());
			factorRefs = factorRefsToTag;
			tagSetRef = tagSetToApply.getRef();
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				String refsAsString = getProject().getObjectData(tagSetRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS);
				ORefSet refs = new ORefSet(new ORefList(refsAsString));
				updateRefsWithFactorRefs(refs);
				refsAsString = new ORefList(refs).toString();
				CommandSetObjectData command = new CommandSetObjectData(tagSetRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, refsAsString);
				getProject().executeCommand(command);
			}
			catch(Exception e)
			{
				EAM.errorDialog(EAM.text("Unexpected error prevented the tag operation from succeeding"));
			}
		}
		
		protected ORefSet getFactorRefs()
		{
			return factorRefs;
		}

		abstract protected void updateRefsWithFactorRefs(ORefSet refsToUpdate);
		
		private ORefSet factorRefs;
		private ORef tagSetRef;
	}
	
	class ActionTagFactors extends AbstractActionTagUntagFactor
	{
		public ActionTagFactors(ORefSet factorRefsToTag, TaggedObjectSet tagSetToApply)
		{
			super(factorRefsToTag, tagSetToApply);
		}

		@Override
		protected void updateRefsWithFactorRefs(ORefSet refsToUpdate)
		{
			refsToUpdate.addAll(getFactorRefs());
		}
		
	}

	class ActionUntagFactors extends AbstractActionTagUntagFactor
	{
		public ActionUntagFactors(ORefSet factorRefsToTag, TaggedObjectSet tagSetToApply)
		{
			super(factorRefsToTag, tagSetToApply);
		}

		@Override
		protected void updateRefsWithFactorRefs(ORefSet refsToUpdate)
		{
			refsToUpdate.removeAll(getFactorRefs());
		}
		
	}
	
	interface ActionCreator
	{
		public Action createAction(ORefSet factorRefs, TaggedObjectSet set);
	}
	
	class TagActionCreator implements ActionCreator
	{
		public Action createAction(ORefSet factorRefs, TaggedObjectSet set)
		{
			return new ActionTagFactors(factorRefs, set);	
		}
	}

	class UntagActionCreator implements ActionCreator
	{
		public Action createAction(ORefSet factorRefs, TaggedObjectSet set)
		{
			return new ActionUntagFactors(factorRefs, set);		
		}
	}

	private UiMenu createTagFactorsMenu()
	{
		String template = EAM.text("Menu|Add Tag to %n Selected Item(s)");
		return createTagUntagFactorsMenu(template, new TagActionCreator());
	}

	private UiMenu createUntagFactorsMenu()
	{
		String template = EAM.text("Menu|Remove Tag from %n Selected Item(s)");
		return createTagUntagFactorsMenu(template, new UntagActionCreator());
	}

	private UiMenu createTagUntagFactorsMenu(String template, ActionCreator actionCreator)
	{
		ORefSet factorRefs = new ORefSet(new ORefList(diagramComponent.getOnlySelectedFactors()));

		String label = EAM.substitute(template, "%n", Integer.toString(factorRefs.size()));
		UiMenu menu = new UiMenu(label);

		TaggedObjectSetPool pool = getProject().getTaggedObjectSetPool();
		Vector<TaggedObjectSet> tags = pool.getAllTaggedObjectSets();
		Collections.sort(tags, new BaseObjectByFullNameSorter());
		for(TaggedObjectSet set : tags)
		{
			Action action = actionCreator.createAction(factorRefs, set);
			menu.add(new MenuItemWithoutLocation(action));
		}
		
		return menu;
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}

	public UiMenu getGroupBoxMenu(Point menuInvokedAt)
	{
		UiMenu groupBoxMenu = new UiMenu(EAM.text("Menu|Group Box"));
		groupBoxMenu.add(createMenuItem(ActionInsertGroupBox.class, menuInvokedAt));
		groupBoxMenu.add(createMenuItem(ActionGroupBoxAddFactor.class, menuInvokedAt));
		groupBoxMenu.add(createMenuItem(ActionGroupBoxRemoveFactor.class, menuInvokedAt));
		groupBoxMenu.add(createMenuItem(ActionDeleteGroupBox.class, menuInvokedAt));
		
		return groupBoxMenu;
	}	
	
	public UiMenu getInsertMenu(Point menuInvokedAt)
	{
		UiMenu insertMenu = new UiMenu(EAM.text("Menu|Insert"));

		insertMenu.add(createMenuItem(ActionInsertDraftStrategy.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertStrategy.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertContributingFactor.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertDirectThreat.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertTarget.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertIntermediateResult.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertThreatReductionResult.class, menuInvokedAt));
		insertMenu.add(createMenuItem(ActionInsertTextBox.class, menuInvokedAt));
		
		insertMenu.addSeparator();
		insertMenu.add(actions.get(ActionInsertFactorLink.class));
		
		return insertMenu;
	}

	private JMenuItem createMenuItem(Class c, Point menuInvokedAt)
	{
		LocationAction action = (LocationAction)actions.get(c);
		action.setInvocationPoint(menuInvokedAt);
		return new ContextMenuItemAtLocation(action);
	}

	public void showContextMenu(MouseEvent e)
	{
		Point screenPoint = e.getPoint();
		Point2D scaledPoint2D = diagramComponent.fromScreen(screenPoint);
		
		Point scaledPoint = Utilities.createPointFromPoint2D(scaledPoint2D);
		
		JPopupMenu menu = getPopupMenu(scaledPoint);
		mainWindow.updateActionsAndStatusBar();
		menu.show(diagramComponent, e.getX(), e.getY());
	}
	
	public static EAMAction getDiagamModeSwitchItem(DiagramView diagramView, Actions actions)
	{
		if (diagramView.isResultsChainTab())
			return actions.get(ActionShowConceptualModel.class);
		
		return actions.get(ActionShowResultsChain.class);
	}
	
	private MenuItemWithoutLocation getModeSwitchMenuItem()
	{
		if (((DiagramView)mainWindow.getCurrentView()).isStategyBrainstormMode())
			return new MenuItemWithoutLocation(actions.get(ActionShowFullModelMode.class));
		
		return new MenuItemWithoutLocation(actions.get(ActionShowSelectedChainMode.class));
	}
	
	static class ContextMenuItemAtLocation extends JMenuItem implements LocationHolder
	{
		public ContextMenuItemAtLocation(Action action)
		{
			super(action);
		}
		
		public boolean hasLocation()
		{
			return true;
		}
	}

	MainWindow mainWindow;
	DiagramComponent diagramComponent;
	Actions actions;
}
