/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.awt.GraphicsEnvironment;
import java.util.Locale;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *  
 */
public class FontDefinitionDialog implements SelectionListener, Listener, IAngleChangeListener
{

    public static final int MIN_FONT_SIZE = 8;

    public static final int MAX_FONT_SIZE = 72;

    private transient FontDefinition fdCurrent = null;

    private transient ColorDefinition cdCurrent = null;

    private transient FontDefinition fdBackup = null;

    private transient ColorDefinition cdBackup = null;

    private transient Shell shell = null;

    private transient Composite cmpContent = null;

    private transient Combo cmbFontNames = null;

    // Should this be a float spin control ?
    private transient IntegerSpinControl iscFontSizes = null;

    private transient FillChooserComposite fccColor = null;

    private transient Button btnATopLeft = null;

    private transient Button btnATopCenter = null;

    private transient Button btnATopRight = null;

    private transient Button btnACenterLeft = null;

    private transient Button btnACenter = null;

    private transient Button btnACenterRight = null;

    private transient Button btnABottomLeft = null;

    private transient Button btnABottomCenter = null;

    private transient Button btnABottomRight = null;

    private transient Button btnBold = null;

    private transient Button btnItalic = null;

    private transient Button btnUnderline = null;

    private transient Button cbStrikethru = null;

    private transient Button cbWrap = null;

    private transient Group grpRotation = null;

    private transient AngleSelectorComposite ascRotation = null;

    private transient IntegerSpinControl iscRotation = null;

    private transient Group grpPreview = null;

    private transient FontCanvas fcPreview = null;

    private transient Composite cmpButtons = null;

    private transient Button btnAccept = null;

    private transient Button btnCancel = null;

    private transient boolean bCancelled = true;

    private transient Button btnLast = null;

    /**
     * @param parent
     * @param style
     */
    public FontDefinitionDialog(Shell shellParent, FontDefinition fdCurrent, ColorDefinition cdCurrent)
    {
        this.fdCurrent = (FontDefinition) EcoreUtil.copy(fdCurrent);
        this.cdCurrent = (ColorDefinition) EcoreUtil.copy(cdCurrent);
        this.fdBackup = (FontDefinition) EcoreUtil.copy(fdCurrent);
        this.cdBackup = (ColorDefinition) EcoreUtil.copy(cdCurrent);
        shell = new Shell(shellParent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        shell.setLayout(new FillLayout());
        placeComponents();
        populateLists();
        shell.setText("Font Descriptor:");
        shell.setSize(450, 480);
        UIHelper.centerOnScreen(shell);
        shell.layout();
        shell.open();
        while (!shell.isDisposed())
        {
            if (!shell.getDisplay().readAndDispatch())
            {
                shell.getDisplay().sleep();
            }
        }
    }

    private void placeComponents()
    {
        GridLayout glContent = new GridLayout();
        glContent.verticalSpacing = 5;
        glContent.horizontalSpacing = 5;
        glContent.marginHeight = 7;
        glContent.marginWidth = 7;
        glContent.numColumns = 9;

        cmpContent = new Composite(shell, SWT.NONE);
        cmpContent.setLayout(glContent);

        Label lblFont = new Label(cmpContent, SWT.NONE);
        GridData gdLFont = new GridData();
        gdLFont.heightHint = 22;
        lblFont.setLayoutData(gdLFont);
        lblFont.setText("Font:");

        cmbFontNames = new Combo(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBFontNames = new GridData(GridData.FILL_HORIZONTAL);
        gdCMBFontNames.horizontalSpan = 8;
        cmbFontNames.setLayoutData(gdCMBFontNames);
        cmbFontNames.addSelectionListener(this);

        Label lblSize = new Label(cmpContent, SWT.NONE);
        GridData gdLSize = new GridData();
        gdLSize.heightHint = 22;
        lblSize.setLayoutData(gdLSize);
        lblSize.setText("Size:");

        iscFontSizes = new IntegerSpinControl(cmpContent, SWT.NONE, (int) fdCurrent.getSize());
        GridData gdISCFontSizes = new GridData(GridData.FILL_HORIZONTAL);
        gdISCFontSizes.horizontalSpan = 3;
        iscFontSizes.setLayoutData(gdISCFontSizes);
        iscFontSizes.setMinimum(MIN_FONT_SIZE);
        iscFontSizes.setMaximum(MAX_FONT_SIZE);
        iscFontSizes.addListener(this);

        Label lblForeground = new Label(cmpContent, SWT.NONE);
        GridData gdLForeground = new GridData();
        gdLForeground.horizontalSpan = 2;
        lblForeground.setLayoutData(gdLForeground);
        lblForeground.setText("Color:");

        fccColor = new FillChooserComposite(cmpContent, SWT.NONE, cdCurrent, false, false);
        GridData gdFCCColor = new GridData(GridData.FILL_HORIZONTAL);
        gdFCCColor.horizontalSpan = 3;
        fccColor.setLayoutData(gdFCCColor);
        fccColor.addListener(this);

        Label lblStyle = new Label(cmpContent, SWT.NONE);
        GridData gdLStyle = new GridData();
        gdLStyle.heightHint = 22;
        //        gdLStyle.horizontalSpan = 2;
        lblStyle.setLayoutData(gdLStyle);
        lblStyle.setText("Style:");

        btnBold = new Button(cmpContent, SWT.TOGGLE);
        GridData gdBBold = new GridData(GridData.FILL_HORIZONTAL);
        btnBold.setLayoutData(gdBBold);
        // TODO: This should be an image
        btnBold.setText("B");
        btnBold.addSelectionListener(this);
        btnBold.setSelection(fdCurrent.isBold());

        btnItalic = new Button(cmpContent, SWT.TOGGLE);
        GridData gdBItalic = new GridData(GridData.FILL_HORIZONTAL);
        btnItalic.setLayoutData(gdBItalic);
        // TODO: This should be an image
        btnItalic.setText("I");
        btnItalic.addSelectionListener(this);
        btnItalic.setSelection(fdCurrent.isItalic());

        btnUnderline = new Button(cmpContent, SWT.TOGGLE);
        GridData gdBUnderline = new GridData(GridData.FILL_HORIZONTAL);
        btnUnderline.setLayoutData(gdBUnderline);
        // TODO: This should be an image
        btnUnderline.setText("U");
        btnUnderline.addSelectionListener(this);
        btnUnderline.setSelection(fdCurrent.isUnderline());

        Label lblFormat = new Label(cmpContent, SWT.NONE);
        GridData gdLFormat = new GridData(GridData.VERTICAL_ALIGN_FILL);
        gdLFormat.horizontalSpan = 2;
        gdLFormat.verticalSpan = 2;
        lblFormat.setLayoutData(gdLFormat);
        lblFormat.setText("Format:");

        cbStrikethru = new Button(cmpContent, SWT.CHECK);
        GridData gdCBStrikethru = new GridData(GridData.FILL_HORIZONTAL);
        gdCBStrikethru.horizontalSpan = 2;
        gdCBStrikethru.heightHint = 20;
        cbStrikethru.setLayoutData(gdCBStrikethru);
        cbStrikethru.addSelectionListener(this);
        cbStrikethru.setText("Strikethrough");
        cbStrikethru.setSelection(fdCurrent.isStrikethrough());

        Label lblDummy1 = new Label(cmpContent, SWT.NONE);
        GridData gdLDummy1 = new GridData();
        gdLDummy1.horizontalSpan = 6;
        lblDummy1.setLayoutData(gdLDummy1);

        cbWrap = new Button(cmpContent, SWT.CHECK);
        GridData gdCBWrap = new GridData(GridData.FILL_HORIZONTAL);
        gdCBWrap.horizontalSpan = 2;
        gdCBWrap.heightHint = 20;
        cbWrap.setLayoutData(gdCBWrap);
        cbWrap.addSelectionListener(this);
        cbWrap.setText("Wrap");
        cbWrap.setSelection(fdCurrent.isWordWrap());

        createRotationPanel();

        createAlignmentPanel();

        FillLayout flPreview = new FillLayout();
        flPreview.marginHeight = 2;
        flPreview.marginWidth = 3;
        grpPreview = new Group(cmpContent, SWT.NONE);
        grpPreview.setText("Preview");
        GridData gdGRPPreview = new GridData(GridData.FILL_BOTH);
        gdGRPPreview.horizontalSpan = 9;
        grpPreview.setLayoutData(gdGRPPreview);
        grpPreview.setLayout(flPreview);

        fcPreview = new FontCanvas(grpPreview, SWT.NONE, fdCurrent, cdCurrent, true, true, true);

        createButtonPanel();
    }

    private void createAlignmentPanel()
    {
        GridLayout glAlignment = new GridLayout();
        glAlignment.verticalSpacing = 30;
        glAlignment.horizontalSpacing = 20;
        glAlignment.marginHeight = 15;
        glAlignment.marginWidth = 5;
        glAlignment.numColumns = 3;

        Group cmpAlignment = new Group(cmpContent, SWT.NONE);
        GridData gdCMPAlignment = new GridData(GridData.FILL_BOTH);
        gdCMPAlignment.horizontalSpan = 3;
        gdCMPAlignment.verticalSpan = 3;
        cmpAlignment.setText("Alignment");
        cmpAlignment.setLayoutData(gdCMPAlignment);
        cmpAlignment.setLayout(glAlignment);

        btnATopLeft = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBATopLeft = new GridData(GridData.FILL_BOTH);
        btnATopLeft.setToolTipText("Alignment - Top Left");
        btnATopLeft.setImage(UIHelper.getImage("images/alignmenttopleft.gif"));
        btnATopLeft.setLayoutData(gdBATopLeft);
        btnATopLeft.addSelectionListener(this);
        btnATopLeft.getImage().setBackground(btnATopLeft.getBackground());

        btnATopCenter = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBATopCenter = new GridData(GridData.FILL_BOTH);
        btnATopCenter.setToolTipText("Alignment - Top Center");
        btnATopCenter.setImage(UIHelper.getImage("images/alignmenttopcenter.gif"));
        btnATopCenter.setLayoutData(gdBATopCenter);
        btnATopCenter.addSelectionListener(this);
        btnATopCenter.getImage().setBackground(btnATopCenter.getBackground());

        btnATopRight = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBATopRight = new GridData(GridData.FILL_BOTH);
        btnATopRight.setToolTipText("Alignment - Top Right");
        btnATopRight.setImage(UIHelper.getImage("images/alignmenttopright.gif"));
        btnATopRight.setLayoutData(gdBATopRight);
        btnATopRight.addSelectionListener(this);
        btnATopRight.getImage().setBackground(btnATopRight.getBackground());

        btnACenterLeft = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBACenterLeft = new GridData(GridData.FILL_BOTH);
        btnACenterLeft.setToolTipText("Alignment - Center Left");
        btnACenterLeft.setImage(UIHelper.getImage("images/alignmentcenterleft.gif"));
        btnACenterLeft.setLayoutData(gdBACenterLeft);
        btnACenterLeft.addSelectionListener(this);
        btnACenterLeft.getImage().setBackground(btnACenterLeft.getBackground());

        btnACenter = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBACenter = new GridData(GridData.FILL_BOTH);
        btnACenter.setToolTipText("Alignment - Center");
        btnACenter.setImage(UIHelper.getImage("images/alignmentcenter.gif"));
        btnACenter.setLayoutData(gdBACenter);
        btnACenter.addSelectionListener(this);
        btnACenter.getImage().setBackground(btnACenter.getBackground());

        btnACenterRight = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBACenterRight = new GridData(GridData.FILL_BOTH);
        btnACenterRight.setToolTipText("Alignment - Center Right");
        btnACenterRight.setImage(UIHelper.getImage("images/alignmentcenterright.gif"));
        btnACenterRight.setLayoutData(gdBACenterRight);
        btnACenterRight.addSelectionListener(this);
        btnACenterRight.getImage().setBackground(btnACenterRight.getBackground());

        btnABottomLeft = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBABottomLeft = new GridData(GridData.FILL_BOTH);
        btnABottomLeft.setToolTipText("Alignment - Bottom Left");
        btnABottomLeft.setImage(UIHelper.getImage("images/alignmentbottomleft.gif"));
        btnABottomLeft.setLayoutData(gdBABottomLeft);
        btnABottomLeft.addSelectionListener(this);
        btnABottomLeft.getImage().setBackground(btnABottomLeft.getBackground());

        btnABottomCenter = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBABottomCenter = new GridData(GridData.FILL_BOTH);
        btnABottomCenter.setToolTipText("Alignment - Bottom Center");
        btnABottomCenter.setImage(UIHelper.getImage("images/alignmentbottomcenter.gif"));
        btnABottomCenter.setLayoutData(gdBABottomCenter);
        btnABottomCenter.addSelectionListener(this);
        btnABottomCenter.getImage().setBackground(btnABottomCenter.getBackground());

        btnABottomRight = new Button(cmpAlignment, SWT.RADIO);
        GridData gdBABottomRight = new GridData(GridData.FILL_BOTH);
        btnABottomRight.setToolTipText("Alignment - Bottom Right");
        btnABottomRight.setImage(UIHelper.getImage("images/alignmentbottomright.gif"));
        btnABottomRight.setLayoutData(gdBABottomRight);
        btnABottomRight.addSelectionListener(this);
        btnABottomRight.getImage().setBackground(btnABottomRight.getBackground());
    }

    private void createRotationPanel()
    {
        GridLayout glRotation = new GridLayout();
        glRotation.verticalSpacing = 2;
        glRotation.marginHeight = 2;
        glRotation.marginWidth = 2;
        glRotation.numColumns = 3;

        grpRotation = new Group(cmpContent, SWT.NONE);
        GridData gdGRPRotation = new GridData(GridData.FILL_BOTH);
        gdGRPRotation.horizontalSpan = 6;
        gdGRPRotation.verticalSpan = 3;
        gdGRPRotation.heightHint = 180;
        grpRotation.setLayoutData(gdGRPRotation);
        grpRotation.setLayout(glRotation);
        grpRotation.setText("Rotation");

        ascRotation = new AngleSelectorComposite(grpRotation, SWT.BORDER, (int) fdCurrent.getRotation(), Display
            .getCurrent().getSystemColor(SWT.COLOR_WHITE));
        GridData gdASCRotation = new GridData(GridData.FILL_BOTH);
        gdASCRotation.horizontalSpan = 1;
        gdASCRotation.verticalSpan = 3;
        ascRotation.setLayoutData(gdASCRotation);
        ascRotation.setAngleChangeListener(this);

        iscRotation = new IntegerSpinControl(grpRotation, SWT.NONE, (int) fdCurrent.getRotation());
        GridData gdISCRotation = new GridData(GridData.FILL_HORIZONTAL);
        gdISCRotation.horizontalSpan = 2;
        iscRotation.setLayoutData(gdISCRotation);
        iscRotation.setMinimum(-90);
        iscRotation.setMaximum(90);
        iscRotation.setIncrement(1);
        iscRotation.addListener(this);
    }

    private void createButtonPanel()
    {
        GridData gdButtons = new GridData(GridData.FILL_HORIZONTAL);
        gdButtons.heightHint = 30;
        gdButtons.horizontalSpan = 9;
        cmpButtons = new Composite(cmpContent, SWT.NONE);
        cmpButtons.setLayoutData(gdButtons);
        GridLayout glButtons = new GridLayout();
        glButtons.numColumns = 7;
        cmpButtons.setLayout(glButtons);

        GridData gdSelect = new GridData(GridData.GRAB_HORIZONTAL);
        gdSelect.horizontalSpan = 5;
        Label lblBlank = new Label(cmpButtons, SWT.NO_FOCUS);
        lblBlank.setLayoutData(gdSelect);

        GridData gdAccept = new GridData(GridData.GRAB_HORIZONTAL);
        gdAccept.horizontalSpan = 1;
        btnAccept = new Button(cmpButtons, SWT.PUSH);
        btnAccept.setText("   OK   ");
        btnAccept.setToolTipText("Accept");

        GridData gdCancel = new GridData(GridData.GRAB_HORIZONTAL);
        gdCancel.horizontalSpan = 1;
        btnCancel = new Button(cmpButtons, SWT.PUSH);
        btnCancel.setText("Cancel");

        btnAccept.addSelectionListener(this);
        btnCancel.addSelectionListener(this);
    }

    private void populateLists()
    {
        // Populate font names list
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] saFontNames = ge.getAvailableFontFamilyNames(Locale.getDefault());
        for (int iC = 0; iC < saFontNames.length; iC++)
        {
            cmbFontNames.add(saFontNames[iC]);
            if (saFontNames[iC].equalsIgnoreCase(fdCurrent.getName()))
            {
                cmbFontNames.select(iC);
            }
        }
        if (cmbFontNames.getSelectionIndex() == -1)
        {
            cmbFontNames.select(0);
        }

        // Select alignment button
        if (fdCurrent.getAlignment() != null)
        {
            HorizontalAlignment ha = fdCurrent.getAlignment().getHorizontalAlignment();
            VerticalAlignment va = fdCurrent.getAlignment().getVerticalAlignment();
            if (HorizontalAlignment.LEFT_LITERAL.equals(ha))
            {
                if (VerticalAlignment.TOP_LITERAL.equals(va))
                {
                    btnATopLeft.setSelection(true);
                }
                else if (VerticalAlignment.BOTTOM_LITERAL.equals(va))
                {
                    btnABottomLeft.setSelection(true);
                }
                else
                {
                    btnACenterLeft.setSelection(true);
                }
            }
            else if (HorizontalAlignment.RIGHT_LITERAL.equals(ha))
            {
                if (VerticalAlignment.TOP_LITERAL.equals(va))
                {
                    btnATopRight.setSelection(true);
                }
                else if (VerticalAlignment.BOTTOM_LITERAL.equals(va))
                {
                    btnABottomRight.setSelection(true);
                }
                else
                {
                    btnACenterRight.setSelection(true);
                }
            }
            else
            {
                if (VerticalAlignment.TOP_LITERAL.equals(va))
                {
                    btnATopCenter.setSelection(true);
                }
                else if (VerticalAlignment.BOTTOM_LITERAL.equals(va))
                {
                    btnABottomCenter.setSelection(true);
                }
                else
                {
                    btnACenter.setSelection(true);
                }
            }
        }
    }

    private void updatePreview()
    {
        fcPreview.setFontDefinition(fdCurrent);
        fcPreview.redraw();
    }

    public FontDefinition getFontDefinition()
    {
        return this.fdCurrent;
    }

    public ColorDefinition getFontColor()
    {
        return this.cdCurrent;
    }

    /**
     * @return true if user clicked on cancel or closed the font dialog without clicking on 'Ok'.
     */
    public boolean wasCancelled()
    {
        return bCancelled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnBold))
        {
            fdCurrent.setBold(btnBold.getSelection());
            updatePreview();
        }
        else if (oSource.equals(btnItalic))
        {
            fdCurrent.setItalic(btnItalic.getSelection());
            updatePreview();
        }
        else if (oSource.equals(btnUnderline))
        {
            fdCurrent.setUnderline(btnUnderline.getSelection());
            updatePreview();
        }
        else if (oSource.equals(cbStrikethru))
        {
            fdCurrent.setStrikethrough(cbStrikethru.getSelection());
            updatePreview();
        }
        else if (oSource.equals(cbWrap))
        {
            fdCurrent.setWordWrap(cbWrap.getSelection());
            updatePreview();
        }
        else if (oSource.equals(cmbFontNames))
        {
            fdCurrent.setName(cmbFontNames.getText());
            updatePreview();
        }
        else if (oSource.equals(btnAccept))
        {
            shell.dispose();
        }
        else if (oSource.equals(btnCancel))
        {
            this.fdCurrent = this.fdBackup;
            this.cdCurrent = this.cdBackup;
            shell.dispose();
        }
        else if (oSource.equals(this.btnATopLeft))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnACenterLeft))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnABottomLeft))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.BOTTOM_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnATopCenter))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnACenter))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnABottomCenter))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.BOTTOM_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnATopRight))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnACenterRight))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
            updatePreview();
        }
        else if (oSource.equals(this.btnABottomRight))
        {
            fdCurrent.getAlignment().setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
            fdCurrent.getAlignment().setVerticalAlignment(VerticalAlignment.BOTTOM_LITERAL);
            updatePreview();
        }
    }

    public void handleEvent(Event e)
    {
        if (e.widget.equals(iscFontSizes))
        {
            fdCurrent.setSize(iscFontSizes.getValue());
            updatePreview();
        }
        else if (e.widget.equals(iscRotation))
        {
            fdCurrent.setRotation(iscRotation.getValue());
            ascRotation.setAngle(iscRotation.getValue());
            ascRotation.redraw();
            // TODO: Enable this if support for rotated text is added to
            // fontcanvas
            // updatePreview();
        }
        else if (e.widget.equals(fccColor))
        {
            cdCurrent = (ColorDefinition) fccColor.getFill();
            fcPreview.setColor(cdCurrent);
            fcPreview.redraw();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.ui.swt.composites.IAngleChangeListener#angleChanged(int)
     */
    public void angleChanged(int iNewAngle)
    {
        iscRotation.setValue(iNewAngle);
        fdCurrent.setRotation(iNewAngle);
    }
}