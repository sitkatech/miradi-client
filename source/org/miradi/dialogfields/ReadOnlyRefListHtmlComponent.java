/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.martus.util.xml.XmlUtilities;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.wizard.MiradiHtmlViewer;

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

	public String getText()
	{
		return currentValue;
	}
	
	public void setText(String newValue)
	{
		currentValue = newValue;
		removeAll();
		try
		{
			ORefList refList = new ORefList(newValue);
			String htmlTable = "<HTML><TABLE bgcolor=" + AppPreferences.convertToHexString(EAM.READONLY_BACKGROUND_COLOR)+ ">";
			for (int i = 0; i < refList.size(); ++i)
			{
				BaseObject object = getProject().findObject(refList.get(i));
				if (object == null)
				{
					EAM.logError("Ignored a missing object while in ReadOnlyRefListHtmlComponent.setText(). Ref = " + refList.get(i));
				}
				else
				{
					htmlTable += "<TR><TD>" + XmlUtilities.getXmlEncoded(object.getFullName())  + "</TD></TR>";
				}
			}		
			
			htmlTable += "</TABLE></HTML>";
			MiradiHtmlViewer miradiHtmlViewer = new MiradiHtmlViewer(getMainWindow(), null);
			miradiHtmlViewer.setTextWithoutScrollingToMakeFieldVisible(htmlTable);
			add(miradiHtmlViewer);
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
	private String currentValue;
}
