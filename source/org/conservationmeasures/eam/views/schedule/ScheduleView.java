package org.conservationmeasures.eam.views.schedule;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

public class ScheduleView extends UmbrellaView
{

	public ScheduleView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ScheduleToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		add(new UiScrollPane(new ScheduleComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Schedule";
	}

}

class ScheduleComponent extends JLabel
{
	public ScheduleComponent()
	{
		super(new ImageIcon(ScheduleView.class.getResource("gantt.jpg")));
	}
}