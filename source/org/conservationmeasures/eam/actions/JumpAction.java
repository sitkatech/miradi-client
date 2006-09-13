package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.JumpDoer;

public class JumpAction extends MainWindowAction
{
	public JumpAction(MainWindow windowToUse, String label)
	{
		super(windowToUse, label);
		destination = label;
	}

	Doer getDoer()
	{
		JumpDoer doer = (JumpDoer)super.getDoer();
		if(doer == null)
			return null;
		doer.setDestination(destination);
		return doer;
	}	
	
	String destination;
}
