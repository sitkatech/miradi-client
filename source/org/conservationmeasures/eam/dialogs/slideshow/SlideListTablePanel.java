/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import java.text.ParseException;

import javax.swing.event.ListSelectionEvent;

import org.conservationmeasures.eam.actions.ActionCreateSlide;
import org.conservationmeasures.eam.actions.ActionDeleteSlide;
import org.conservationmeasures.eam.actions.ActionMoveSlideDown;
import org.conservationmeasures.eam.actions.ActionMoveSlideUp;
import org.conservationmeasures.eam.actions.ActionSlideShowViewer;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.dialogs.base.ObjectTableModel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.diagram.DiagramLegendPanel;
import org.conservationmeasures.eam.views.diagram.DiagramView;


public class SlideListTablePanel extends ObjectListTablePanel
{
	public SlideListTablePanel(Project project, Actions actions, ORef oref)
	{
		super(project, new SlideListTableModel(project, oref), actions, buttons);
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

	private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
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
