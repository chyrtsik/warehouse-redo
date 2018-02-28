package com.artigile.warehouse.gui.core.wizard;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valery Barysok, 2013-07-31
 */
public interface WizardStep {

    String getName();

    Wizard getWizard();

    void setWizard(Wizard wizard);

    void updatePanel(JPanel panel);

    void clearFieldValue();

    Dimension getPreferredSize();
}
