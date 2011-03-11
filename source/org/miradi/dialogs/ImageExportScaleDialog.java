/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;

import org.miradi.dialogs.base.DataInputPanel;
import org.miradi.dialogs.base.DialogWithDisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FillerLabel;

import com.toedter.components.JSpinField;

public class ImageExportScaleDialog extends DialogWithDisposablePanel
{
	public ImageExportScaleDialog(MainWindow mainWindow, int defaultScalePercent)
	{
		super(mainWindow);

		setTitle(EAM.text("Title|Export Image Scale"));
		setModal(true);
		
		DataInputPanel scaleRow = new DataInputPanel(mainWindow.getProject());
		scaleRow.setLayout(new OneRowGridLayout());
		scaleRow.add(new PanelTitleLabel(EAM.text("Image Scale %")));
		final int MAX_VALUE = 300;
		spinner = new JSpinField(1, MAX_VALUE);
		spinner.setValue(MAX_VALUE);
		Dimension preferredSize = spinner.getPreferredSize();
		int width = preferredSize.width;
		Dimension minimumSize = new Dimension(width, preferredSize.height);
		spinner.setMinimumSize(minimumSize);
		spinner.setPreferredSize(minimumSize);
		spinner.setValue(defaultScalePercent);
		
		scaleRow.add(spinner);

		DataInputPanel panel = new DataInputPanel(mainWindow.getProject());
		panel.setLayout(new OneColumnGridLayout());
		panel.add(new PanelTitleLabel(EAM.text("For a larger, higher-resolution image, choose a scale above 100%")));
		panel.add(new PanelTitleLabel(EAM.text("For a smaller, lower-resolution image, choose a scale below 100%")));
		panel.add(new FillerLabel());
		panel.add(scaleRow);
		panel.add(new FillerLabel());
		setMainPanel(panel);

		PanelButton exportButton = new PanelButton(EAM.text("Button|Export..."));
		exportButton.addActionListener(new OkHandler());
		PanelButton cancelButton = new PanelButton(EAM.text("Button|Cancel"));
		setSimpleCloseButton(cancelButton);

		Vector<Component> buttons = new Vector<Component>();
		buttons.add(Box.createHorizontalGlue());
		buttons.add(exportButton);
		buttons.add(cancelButton);
		setButtons(buttons);
	}

	public boolean userChoseOk()
	{
		return userChoseOk;
	}
	
	public int getScalePercent()
	{
		return spinner.getValue();
	}

	class OkHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			userChoseOk = true;
			dispose();
		}
	}
	
	private boolean userChoseOk;
	private JSpinField spinner;
}
