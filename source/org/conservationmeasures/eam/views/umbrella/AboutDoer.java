/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.utils.HtmlFormEventHandler;
import org.conservationmeasures.eam.utils.HtmlViewPanel;
import org.conservationmeasures.eam.views.Doer;

public class AboutDoer extends Doer  implements HtmlFormEventHandler
{
	public AboutDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		//FIXME:  error only happens when entering from splash page: the following code resolves the problem but not sure why
		// wiht this code no error is thrown at any time and the splash page behavor is as before
		
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					try
					{
						doIt(null);
					}
					catch(Exception e)
					{
						EAM.logException(e);
					}
				}
			});
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		new HtmlViewPanel("About Miradi", this.getClass(), "About.html", this).showOkDialog();
	}


	public void buttonPressed(String buttonName)
	{
	}

	public void linkClicked(String linkDescription)
	{
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void setComponent(String name, JComponent component)
	{
		if (name.equals("Version"))
		{
			String text = "<html><strong>" + VersionConstants.VERSION_STRING  + "</strong></html>";
			((JLabel)component).setText(text);
		}
	}
	
}
