/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;

import java.text.ParseException;

import javax.swing.event.ListSelectionEvent;

import org.conservationmeasures.eam.actions.ActionCreateSlide;
import org.conservationmeasures.eam.actions.ActionDeleteSlide;
import org.conservationmeasures.eam.actions.ActionMoveSlideDown;
import org.conservationmeasures.eam.actions.ActionMoveSlideUp;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.ObjectListTablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.diagram.DiagramLegendPanel;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class SlideListTablePanel extends ObjectListTablePanel
{
	public SlideListTablePanel(Project project, Actions actions, ORef oref)
	{
		super(project, ObjectType.SLIDE, new SlideListTableModel(project, oref), actions, buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateSlide.class,
		ActionDeleteSlide.class,
		ActionMoveSlideUp.class,
		ActionMoveSlideDown.class
	};
	
	public void valueChanged(ListSelectionEvent event)
	{
		if (!getDiagramView().isSidePanelVisible())
			return;
		
		super.valueChanged(event);
		if (getSelectedObject()==null)
			return;
		ORef oref = ((Slide)getSelectedObject()).getDiagramRef();
		if (!oref.equals(ORef.INVALID))
		{
			getDiagramView().setDiagramTab(oref);
			updateLegendPanel();
		}
	}

	public void updateLegendPanel()
	{
		Slide slide = (Slide)getSelectedObject();
		CodeList list = getDiagarmLegendSettings(slide);
		DiagramLegendPanel panel = getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		
		panel.turnOFFCheckBoxs();
		for (int i=0; i<list.size(); ++i)
		{
			panel.updateCheckBoxes(getProject().getLayerManager(), list.get(i));
		}
		//FIXME: UI not udpated to reflect new settings??
		getDiagramView().updateVisibilityOfFactors();
	}
	
	
	private CodeList getDiagarmLegendSettings(Slide slide)
	{
		try
		{
			return  new CodeList(slide.getData(Slide.TAG_DIAGRAM_LEGEND_SETTINGS));
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}

	private DiagramView getDiagramView()
	{
		return ((DiagramView)EAM.mainWindow.getCurrentView());
	}
	
}
