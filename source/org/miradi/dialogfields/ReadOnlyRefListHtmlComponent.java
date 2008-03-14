/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.martus.util.xml.XmlUtilities;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

import com.jhlabs.awt.BasicGridLayout;

public class ReadOnlyRefListHtmlComponent extends MiradiPanel
{
	public ReadOnlyRefListHtmlComponent(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		
		setLayout(new BasicGridLayout(0, 1));		
		setBackground(EAM.READONLY_BACKGROUND_COLOR);
		setForeground(EAM.READONLY_FOREGROUND_COLOR);
	}

	public void setText(String newValue)
	{
		removeAll();
		try
		{
			
			ORefList refList = new ORefList(newValue);
			String htmlTable = "<HTML><TABLE bgcolor=" + AppPreferences.convertToHex(EAM.READONLY_BACKGROUND_COLOR)+ ">";
			for (int i = 0; i < refList.size(); ++i)
			{
				BaseObject object = getProject().findObject(refList.get(i)); 
				htmlTable += "<TR><TD>" + XmlUtilities.getXmlEncoded(object.getFullName())  + "</TD></TR>";
			}		
			
			htmlTable += "</TABLE></HTML>";
			add(new HtmlFormViewer(getMainWindow(), htmlTable, null));
		}
		catch(Exception e)
		{
			EAM.errorDialog(EAM.text("Internal Error"));
			EAM.logException(e);
		}
		
		if (getTopLevelAncestor() != null)
			getTopLevelAncestor().validate();
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
}
