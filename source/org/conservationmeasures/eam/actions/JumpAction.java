package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.JumpDoer;

public class JumpAction extends MainWindowAction
{
	public JumpAction(MainWindow windowToUse, String label)
	{
		super(windowToUse, label);
	}

	Doer getDoer()
	{
		Doer rawDoer = super.getDoer();
		if(rawDoer == null)
			return null;
		JumpDoer doer = (JumpDoer)rawDoer;
		doer.setDestination(getClass());
		return doer;
	}	
}
