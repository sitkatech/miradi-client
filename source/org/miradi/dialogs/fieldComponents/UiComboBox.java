/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.fieldComponents;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

import org.martus.swing.UiLanguageDirection;
import org.martus.swing.Utilities;
import org.martus.util.language.LanguageOptions;


public class UiComboBox<E> extends JComboBox<E>
{

    public UiComboBox()
    {
        initalize();
    }


    public UiComboBox(E[] items)
    {
        super(items);
        initalize();
    }

    private void initalize()
    {
        setComponentOrientation();
        addKeyListener(new UiComboBoxKeyListener());
    }

    public void setUI(ComboBoxUI ui)
    {
		/* UGLY HACK: Under MS Windows, some fonts like Arabic extend beyond their declared bounds,
		 * so we have to pad them. If we do that in combo boxes, another Swing bug
		 * causes the dropdown arrow to become twice as large.
		 * If we are running under MS Windows, we'll tweak the button size.
		 * But under Linux, this would cause the entire combo border to disappear, and
		 * when using a padded language the dropdown button is half as wide as it should be */
        if(Utilities.isMSWindows() && LanguageOptions.needsLanguagePadding())
            ui = new SlimArrowComboBoxUi();
        super.setUI(ui);
    }

    private void setComponentOrientation()
    {
        setComponentOrientation(UiLanguageDirection.getComponentOrientation());
        setRenderer(new UiComboListCellRenderer());
    }

    class UiComboBoxKeyListener extends KeyAdapter
    {
        public void keyReleased(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_SPACE)
            {
                if(isPopupVisible())
                    hidePopup();
                else
                    showPopup();
                e.consume();
                return;
            }
            super.keyReleased(e);
        }
    }

    class UiComboListCellRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            setComponentOrientation(UiLanguageDirection.getComponentOrientation());
            setHorizontalAlignment(UiLanguageDirection.getHorizontalAlignment());
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }

        public Dimension getPreferredSize()
        {
            Dimension dimension = super.getPreferredSize();
            dimension.height += LanguageOptions.getExtraHeightIfNecessary();
            return dimension;
        }

    }

    class SlimArrowComboBoxUi extends BasicComboBoxUI
    {
        protected LayoutManager createLayoutManager()
        {
            return new SlimArrowLayoutManager();
        }

        public JButton getArrowButton()
        {
            return arrowButton;
        }

        class SlimArrowLayoutManager extends ComboBoxLayoutManager
        {
            public void layoutContainer(Container parent)
            {
                super.layoutContainer(parent);
                Rectangle rect = getArrowButton().getBounds();
                JScrollBar scrollbar = new JScrollBar();
                rect.width = scrollbar.getPreferredSize().width;
                getArrowButton().setBounds(rect);
            }
        }
    }

}
