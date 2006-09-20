/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewBudget;
import org.conservationmeasures.eam.actions.views.ActionViewCalendar;
import org.conservationmeasures.eam.actions.views.ActionViewDiagram;
import org.conservationmeasures.eam.actions.views.ActionViewImages;
import org.conservationmeasures.eam.actions.views.ActionViewInterview;
import org.conservationmeasures.eam.actions.views.ActionViewMap;
import org.conservationmeasures.eam.actions.views.ActionViewMonitoring;
import org.conservationmeasures.eam.actions.views.ActionViewStrategicPlan;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.views.ActionViewWorkPlan;

public class ViewSwitcher extends JComboBox
{
	static public ViewSwitcher create(Actions actions, Class defaultActionClass)
	{
		Object[] views = getViewSwitchActions(actions);
		ViewSwitcher switcher = new ViewSwitcher(views);
		Action defaultAction = actions.get(defaultActionClass);
		switcher.addActionListener(new ActionHandler(switcher, defaultAction));
		return switcher;
	}

	public static Action[] getViewSwitchActions(Actions actions)
	{
		Action[] views = new Action[] {
			// TODO: Delete the commented-out views here, and at the same time
			// delete the views themselves, and all references to them
			//actions.get(ActionViewTable.class),
				
				
			actions.get(ActionViewInterview.class), 
			actions.get(ActionViewDiagram.class), 
			actions.get(ActionViewThreatMatrix.class),
			actions.get(ActionViewStrategicPlan.class),
			actions.get(ActionViewMonitoring.class),
			actions.get(ActionViewWorkPlan.class), 
			actions.get(ActionViewCalendar.class),
			actions.get(ActionViewBudget.class), 
			actions.get(ActionViewMap.class),
			actions.get(ActionViewImages.class),

		};
		return views;
	}
	
	private ViewSwitcher(Object[] choices)
	{
		super(choices);
		setToolTipText(EAM.text("TT|Switch to a different view"));
		setMaximumRowCount(choices.length);
	}
	
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	static class ActionHandler implements ActionListener
	{
		public ActionHandler(ViewSwitcher switcherToControl, Action defaultAction)
		{
			switcher = switcherToControl;
			defaultValue = defaultAction;
			selectDefaultAction();
		}
		
		public void actionPerformed(ActionEvent event)
		{
			JComboBox comboBox = (JComboBox)event.getSource();
			Action action = (Action)comboBox.getSelectedItem();
			action.actionPerformed(null);
			selectDefaultAction();
		}

		private void selectDefaultAction()
		{
			switcher.setSelectedItem(defaultValue);
		}
		
		ViewSwitcher switcher;
		Action defaultValue;
	}
}