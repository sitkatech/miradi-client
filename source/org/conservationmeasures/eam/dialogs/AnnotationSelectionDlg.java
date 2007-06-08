/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.Utilities;

public class AnnotationSelectionDlg extends EAMDialog implements ListSelectionListener
{
	public AnnotationSelectionDlg(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow);
		list = poolTable;
		list.getTable().addListSelectionListener(this);
		Box box = Box.createVerticalBox();
		box.add(new PanelTitleLabel("Please select which item should be cloned into this factor, then press the Clone but"));
		box.add(list, BorderLayout.AFTER_LAST_LINE);
		
		setTitle(title);
		JComponent buttonBar = createButtonBar();
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new FastScrollPane(box), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		setModal(true);
		setPreferredSize(new Dimension(900,400));
		Utilities.centerDlg(this);
	}
	
	public BaseObject getSelectedAnnotaton()
	{
		return objectSelected;
	}
	

	public void dispose()
	{
		super.dispose();
		list.dispose();
	}

	private Box createButtonBar()
	{
		PanelButton cancelButton = new PanelButton(new CancelAction());
		cloneButton = new PanelButton(new CloneAction());
		cloneButton.setEnabled(false);
		getRootPane().setDefaultButton(cancelButton);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), cloneButton,  Box.createHorizontalStrut(10), cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		cloneButton.setEnabled(true);
	}
	
	class CloneAction extends AbstractAction
	{
		public CloneAction()
		{
			super(EAM.text("Clone"));
		}

		public void actionPerformed(ActionEvent arg0)
		{
			if (list.getSelectedObject()==null)
			{
				EAM.okDialog(EAM.text("Notice"), new String[] {EAM.text("Please make a selection")});
				return;
			}
			objectSelected = list.getSelectedObject();
			dispose();
		}
	}
	
	
	class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super(EAM.text("Cancel"));
		}

		public void actionPerformed(ActionEvent arg0)
		{
			dispose();
		}
	}
	
	PanelButton cloneButton;
	ObjectTablePanel list;
	BaseObject objectSelected;
}

