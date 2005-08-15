/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.conservationmeasures.eam.actions.ActionViewDiagram;
import org.conservationmeasures.eam.actions.ActionViewInterview;
import org.conservationmeasures.eam.actions.Actions;

public class ViewSwitcher extends JComboBox
{
	static public ViewSwitcher create(Actions actions, Class defaultActionClass)
	{
		Object[] views = new Object[] {
			actions.get(ActionViewInterview.class), 
			actions.get(ActionViewDiagram.class), 
//			"Task View", 
//			"Budget View", 
//			"GIS Map View",
		};
		ViewSwitcher switcher = new ViewSwitcher(views);
		Action defaultAction = actions.get(defaultActionClass);
		switcher.addActionListener(new ActionHandler(switcher, defaultAction));
		return switcher;
	}
	
	private ViewSwitcher(Object[] choices)
	{
		super(choices);
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