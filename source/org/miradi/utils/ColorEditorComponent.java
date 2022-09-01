/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.utils;

import org.miradi.main.EAM;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ColorEditorComponent extends JColorChooser
{
    public ColorEditorComponent(Color initialColorToUse, ChangeListener changeListenerToUse)
    {
        super(initialColorToUse);
        changeListener = changeListenerToUse;

        initializeColorChooser();
    }

    private void initializeColorChooser()
    {
        try
        {
            getSelectionModel().addChangeListener(changeListener);
            setPreviewPanel(new JPanel());

            AbstractColorChooserPanel[] panels = getChooserPanels();
            for (AbstractColorChooserPanel panel : panels)
            {
               if(!panel.getDisplayName().equals("RGB"))
               {
                   removeChooserPanel(panel);
                   continue;
               }

                removeAlphaSlider(panel);
            }
        }
        catch (Exception e)
        {
            EAM.alertUserOfNonFatalException(e);
        }
    }

    private static void removeAlphaSlider(AbstractColorChooserPanel panel) throws NoSuchFieldException, IllegalAccessException
    {
        Field panelField = panel.getClass().getDeclaredField("panel");
        panelField.setAccessible(true);

        Object colorPanel = panelField.get(panel);
        Field f2 = colorPanel.getClass().getDeclaredField("spinners");
        f2.setAccessible(true);
        Object spinners = f2.get(colorPanel);

        Object alphaSliderPanel = Array.get(spinners, 3);
        Field alphaSliderField = alphaSliderPanel.getClass().getDeclaredField("slider");
        alphaSliderField.setAccessible(true);
        JSlider alphaSlider = (JSlider) alphaSliderField.get(alphaSliderPanel);
        alphaSlider.setEnabled(false);
        alphaSlider.setVisible(false);
        Field alphaSpinnerField = alphaSliderPanel.getClass().getDeclaredField("spinner");
        alphaSpinnerField.setAccessible(true);
        JSpinner alphaSpinner = (JSpinner) alphaSpinnerField.get(alphaSliderPanel);
        alphaSpinner.setEnabled(false);
        alphaSpinner.setVisible(false);

        Field alphaField = alphaSliderPanel.getClass().getDeclaredField("label");
        alphaField.setAccessible(true);
        JLabel label = (JLabel) alphaField.get(alphaSliderPanel);
        label.setVisible(false);
    }

    public void dispose()
	{
        if (changeListener != null)
            getSelectionModel().removeChangeListener(changeListener);
	}

    private ChangeListener changeListener;
}
