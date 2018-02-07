/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.notify;

import org.miradi.main.EAM;

import java.io.File;

public class NotifyDialogTemplateFactory
{
	public static NotifyDialogTemplate pastedSharedFactors()
	{
		String title = EAM.text("Pasted Shared Factors");
		String notificationTextToUse = "<html><div class='WizardText'>" + EAM.text(
				"Pasted factors take two forms - shared factors and complementary factors " +
				"<br/><br/> " +
				"Pasted Shared Factors " +
				"<ul> " +
				"<li>When pasting to the same type of diagram: all factors</li> " +
				"<li>When pasting from Conceptual Model to Result Chain or vice-versa: targets and strategies only</li> " +
				"</ul> " +
				"<br/> " +
				"The pasted factors are (in the above instances) <b>shared</b>, meaning that any changes made in this diagram will also be reflected in other diagrams that these factors appear. " +
				"In other words, each factor will only exist once in this project, but will be visible in multiple diagram pages. " +
				"<br/><br/> " +
				"Normally, this is what you want. If you really wanted to create duplicate copies of the factors, separate from the originals, you must undo the paste, " +
				"return to the diagram where these factors originated, and paste them there. Then use the &lt;Cut&gt; command to move them to the clipboard, return to this diagram, and paste the new copies here. " +
				"<br/><br/> " +
				"Pasted Complementary Factors " +
				"<ul> " +
				"<li>When pasting from Conceptual Model to Result Chain or vice-versa: Threats and Threat Reduction Results, Contributing Factors and Intermediate Results, Biophysical Factors and Biophysical Results</li> " +
				"</ul> " +
				"<br/> " +
				"The pasted complementary factors are <b>not</b> shared, meaning that any changes made to the pasted factor in this diagram will not affect the original factor, and vice-versa. " +
				"This also means that taxonomy or classification selections made in the original factor will not carry over to its pasted complementary factor.");
		return new NotifyDialogTemplate("PastedSharedFactors", title, notificationTextToUse);
	}
	
	public static NotifyDialogTemplate notifyUserOfInvalidPreferredHomeDirectory(File preferredHomeDir)
	{
		String title = EAM.text("Home Folder Not Found");
		String notificationTextToUse = EAM.getHtmlDialogContent("NoHomeDirectoryFoundMessage.html","@DIRECTORY_NAME@", preferredHomeDir.getAbsolutePath());

		return new NotifyDialogTemplate("HomeFolderNotFound", title, notificationTextToUse);
	}
}
