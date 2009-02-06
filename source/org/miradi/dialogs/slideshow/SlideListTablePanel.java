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
package org.miradi.dialogs.slideshow;

import java.text.ParseException;

import javax.swing.event.ListSelectionEvent;

import org.miradi.actions.ActionCreateSlide;
import org.miradi.actions.ActionDeleteSlide;
import org.miradi.actions.ActionMoveSlideDown;
import org.miradi.actions.ActionMoveSlideUp;
import org.miradi.actions.ActionSlideShowViewer;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Slide;
import org.miradi.objects.SlideShow;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;
import org.miradi.views.diagram.DiagramLegendPanel;
import org.miradi.views.diagram.DiagramView;


public class SlideListTablePanel extends ObjectListTablePanel
{
	public SlideListTablePanel(MainWindow mainWindowToUse, ORef oref)
	{
		super(mainWindowToUse, new SlideListTableModel(mainWindowToUse.getProject(), oref), buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateSlide.class,
		ActionDeleteSlide.class,
		ActionMoveSlideUp.class,
		ActionMoveSlideDown.class,
		ActionSlideShowViewer.class
	};
	
	public void valueChanged(ListSelectionEvent event)
	{
		super.valueChanged(event);
		if (getSelectedObject()==null)
			return;
		ORef oref = ((Slide)getSelectedObject()).getDiagramRef();
		if (!oref.equals(ORef.INVALID))
		{
			processSelection(oref);
		}
	}

	private void processSelection(ORef oref)
	{
		inSelectionLogic = true;
		try
		{
			// FIXME: Need to force the diagram view to make this 
			// specific diagram visible, not just pick the right tab
			getDiagramView().setDiagramTab(oref);
			updateLegendPanel();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			inSelectionLogic = false;
		}

	}
	
	protected void selectFirstRow()
	{
	}

	
	public void updateLegendPanel()
	{
		Slide slide = (Slide)getSelectedObject();
		CodeList list = getDiagarmLegendSettingsForSlide(slide);
		DiagramLegendPanel panel = getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
	}
	
	
	private CodeList getDiagarmLegendSettingsForSlide(Slide slide)
	{
		try
		{
			return  new CodeList(slide.getData(Slide.TAG_DIAGRAM_LEGEND_SETTINGS));
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to read slide settings:" + e.getMessage());
			return new CodeList();
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		BaseObject object = getSelectedObject();
		if (((object!=null) && isSetDataForSlideShow(event)))
		{
			ObjectTableModel model = getTable().getObjectTableModel();
			model.resetRows();
			model.fireTableDataChanged();
			
			int newRow = model.findRowObject(object.getId());
			if (newRow>=0)
				getTable().setRowSelectionInterval(newRow, newRow);
			return;
		}

		if (doesSlideNeedToBeUpdated(event))
		{
			updateCurrentSelectedSlide(event);
			return;
		}
		
		super.commandExecuted(event);
	}
	
	private void updateCurrentSelectedSlide(CommandExecutedEvent event)

	{
		Slide slide = (Slide)getSelectedObject();
		if (slide==null)
			return;
		
		CommandSetObjectData cmd = ((CommandSetObjectData)event.getCommand());
		
		try
		{
			String tag = cmd.getFieldTag();
			if (tag.equals(ViewData.TAG_DIAGRAM_HIDDEN_TYPES))
			{
				// NOTE: Since we are inside commandExecuted, we can't execute another command here,
				// which is ok, because this is purely a side effect of the actual command, so we 
				// wouldn't want to independently undo/redo it
				getProject().setObjectData(slide.getRef(), Slide.TAG_DIAGRAM_LEGEND_SETTINGS, cmd.getDataValue());
			}
			
			if (isDiagramType(tag))
			{
				// NOTE: Since we are inside commandExecuted, we can't execute another command here,
				// which is ok, because this is purely a side effect of the actual command, so we 
				// wouldn't want to independently undo/redo it
				getProject().setObjectData(slide.getRef(), Slide.TAG_DIAGRAM_OBJECT_REF, cmd.getDataValue());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to update slide:" + e.getMessage());
		}
	}

	private boolean isDiagramType(String tag)
	{
		return tag.equals(ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF) || tag.equals(ViewData.TAG_CURRENT_RESULTS_CHAIN_REF);
	}

	private boolean doesSlideNeedToBeUpdated(CommandExecutedEvent event)
	{
		if (inSelectionLogic)
			return false;
		
		if (!event.isSetDataCommand())
			return false;
		
		CommandSetObjectData cmd = ((CommandSetObjectData)event.getCommand());
		if (cmd.getObjectType()!=ViewData.getObjectType())
			return false;
		
		String tag = cmd.getFieldTag();
		return (tag.equals(ViewData.TAG_DIAGRAM_HIDDEN_TYPES) || isDiagramType(tag));
	}
	
	private boolean isSetDataForSlideShow(CommandExecutedEvent event)
	{
		if (!event.isSetDataCommand())
			return false;
		CommandSetObjectData cmd = ((CommandSetObjectData)event.getCommand());
		return (cmd.getObjectType()==SlideShow.getObjectType());
	}
	
	
	private DiagramView getDiagramView()
	{
		return ((DiagramView)getMainWindow().getCurrentView());
	}
	
	boolean inSelectionLogic;
}
