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

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.Utilities;

public class AnnotationSelectionDlg extends EAMDialog
{
	public AnnotationSelectionDlg(MainWindow mainWindow, ObjectTablePanel poolTable)
	{
		super(mainWindow);
		list = poolTable;
		add(list);
		
		JComponent buttonBar = createButtonBar();
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new FastScrollPane(list), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		setModal(true);
		setPreferredSize(new Dimension(600,400));
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
		PanelButton cancel = new PanelButton(new CancelAction());
		PanelButton clone = new PanelButton(new CloneAction());
		getRootPane().setDefaultButton(cancel);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), cancel, clone, Box.createHorizontalStrut(10)};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
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
			list.getTable().clearSelection();
			dispose();
		}
	}
	
	ObjectTablePanel list;
	BaseObject objectSelected;
}

