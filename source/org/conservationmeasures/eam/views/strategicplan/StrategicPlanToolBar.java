package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionViewThreatMatrix;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;
import org.conservationmeasures.eam.utils.ToolBarButton;

public class StrategicPlanToolBar extends EAMToolBar
{
	public StrategicPlanToolBar(Actions actions)
	{
		super(actions, ActionViewThreatMatrix.class, createButtons(actions));
	}
	
	static JComponent[] createButtons(Actions actions)
	{
		JComponent[] buttons = new JComponent[] {
				new Separator(),
				new ToolBarButton(actions, ActionUndo.class),
				new ToolBarButton(actions, ActionRedo.class),
				new Separator(),
				new ToolBarButton(actions, ActionInsertActivity.class),
		};
		return buttons;
	}
}