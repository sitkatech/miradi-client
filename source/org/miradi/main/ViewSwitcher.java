/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewDiagram;
import org.miradi.actions.views.ActionViewImages;
import org.miradi.actions.views.ActionViewMap;
import org.miradi.actions.views.ActionViewPlanning;
import org.miradi.actions.views.ActionViewReport;
import org.miradi.actions.views.ActionViewSchedule;
import org.miradi.actions.views.ActionViewSummary;
import org.miradi.actions.views.ActionViewTargetViability;
import org.miradi.actions.views.ActionViewThreatMatrix;
import org.miradi.dialogs.fieldComponents.PanelComboBox;

public class ViewSwitcher extends PanelComboBox
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
			actions.get(ActionViewSummary.class),
			actions.get(ActionViewDiagram.class), 
			actions.get(ActionViewTargetViability.class),
			actions.get(ActionViewThreatMatrix.class),
			actions.get(ActionViewPlanning.class),
			actions.get(ActionViewReport.class),
//			actions.get(ActionViewStrategicPlan.class),
//			actions.get(ActionViewMonitoring.class),
//			actions.get(ActionViewWorkPlan.class), 
//			actions.get(ActionViewBudget.class), 
			actions.get(ActionViewMap.class),
			actions.get(ActionViewSchedule.class),
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