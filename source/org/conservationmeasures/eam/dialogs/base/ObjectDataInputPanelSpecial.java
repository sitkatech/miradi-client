/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelFieldLabel;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectDataInputPanelSpecial extends AbstractObjectDataInputPanel
{
	
	public ObjectDataInputPanelSpecial(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse, new ORef[] {new ORef(objectType, idToUse)});
	}
	
	
	public ObjectDataInputPanelSpecial(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new ORef[] {orefToUse});
	}
	
	
	public ObjectDataInputPanelSpecial(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
		setLayout(new BorderLayout());
	}
	
	public UiLabel createLabel(ObjectDataInputField field)
	{
		UiLabel label = new PanelFieldLabel(field.getObjectType(), field.getTag(), JLabel.LEFT);
		label.setBorder(new EmptyBorder(0,0,0,8));
		label.setVerticalAlignment(SwingConstants.TOP);
		return label;
	}
	
	public void addFieldComponent(Component component)
	{
		add(component);
	}
	
	public Box createColumnBox(ObjectDataInputField field)
	{
		Box box = Box.createVerticalBox();
		box.add(createLabel(field));
		box.add(field.getComponent());
		return box;
	}

	public JPanel createColumnJPanel(ObjectDataInputField field)
	{
		return createColumnJPanel(field, field.getComponent().getPreferredSize());
	}
	
	public JPanel createColumnJPanel(ObjectDataInputField field, Dimension dim)
	{
		JPanel box = createGridLayoutPanel(2,1);
		box.setBorder(new EmptyBorder(0,8,0,0));
		field.getComponent().setMaximumSize(new Dimension(dim.width, field.getComponent().getPreferredSize().height));
		box.add(createLabel(field));
		box.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		return box;
	}
	
	public JPanel createColumnJPanelWithIcon(ObjectDataInputField field, Icon icon)
	{
		JPanel box = createGridLayoutPanel(2,1);
		box.add(createLabel(field));
		
		JPanel boxIcon = createGridLayoutPanel(1,2);
		JLabel label = new JLabel(icon);
		//TODO +2 is not a good way of fixing the cropped icon problem
		Dimension dimension = new Dimension(icon.getIconWidth() + 2, icon.getIconHeight());
		label.setPreferredSize(dimension);
		label.setMinimumSize(dimension);
		boxIcon.add(label);
		boxIcon.add(field.getComponent());
		
		box.add(boxIcon, BorderLayout.BEFORE_LINE_BEGINS);
		return box;
	}
	

	public JPanel createGridLayoutPanel(int row, int columns)
	{
		JPanel panel = new JPanel();
		GridLayoutPlus layout = new GridLayoutPlus(0, columns);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		panel.setLayout(layout);
		return panel;
	}
	
	public void addBoldedTextBorder(JPanel panel, String title)
	{
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), EAM.text(title), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, TreeTableWithIcons.Renderer.deriveFont(Font.BOLD)));
	}
	
	abstract public String getPanelDescription();
	
	protected static final int STD_SPACE_20 = 20;
	protected static final int NARROW_DETAILS = 30;
}
