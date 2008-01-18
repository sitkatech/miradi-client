/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.dialogs.base.DisposablePanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

public class ControlPanel extends DisposablePanel
{
	public ControlPanel()
	{
		super(new BasicGridLayout(2, 1));
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);

		add(createTitleBar(EAM.text("Control Bar")));
	}

	public UiLabel createTitleBar(String text)
	{
		UiLabel title = new PanelTitleLabel(text);
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		title.setBackground(getBackground());
		
		return title;
	}
	
}
