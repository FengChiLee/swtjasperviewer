/*
 * SWTJasperViewer - Free SWT/JFace report viewer for JasperReports.
 * Copyright (C) 2004  Peter Severin (peter_p_s@users.sourceforge.net)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jasperassistant.designer.viewer.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jasperassistant.designer.viewer.IPosExportXlsHandler;
import com.jasperassistant.designer.viewer.IReportViewer;

/**
 * Single sheet excel export action
 * 
 * @author Peter Severin (peter_p_s@users.sourceforge.net)
 */
public class ExportAsCsvAction extends AbstractExportAction {

	private static final ImageDescriptor ICON = ImageDescriptor.createFromFile(
			ExportAsCsvAction.class, "images/save.gif"); //$NON-NLS-1$

	private static final ImageDescriptor DISABLED_ICON = ImageDescriptor
			.createFromFile(ExportAsCsvAction.class, "images/saved.gif"); //$NON-NLS-1$

	private IPosExportXlsHandler posExportSingleXlsHandler;
	
	/**
	 * @see AbstractExportAction#AbstractExportAction(IReportViewer)
	 */
	public ExportAsCsvAction(IReportViewer viewer) {
		super(viewer);

		setText(Messages.getString("ExportAsCsvAction.label")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportAsCsvAction.tooltip")); //$NON-NLS-1$
		setImageDescriptor(ICON);
		setDisabledImageDescriptor(DISABLED_ICON);

		setFileExtensions(new String[] { "*.csv" }); //$NON-NLS-1$
		setFilterNames(new String[] { Messages.getString("ExportAsCsvAction.filterName") }); //$NON-NLS-1$
		setDefaultFileExtension("csv"); //$NON-NLS-1$
	}

    public void setIPosExportXlsHandler(IPosExportXlsHandler posExportSingleXlsHandler) {

        this.posExportSingleXlsHandler = posExportSingleXlsHandler;
    }
	
	/**
	 * @see com.jasperassistant.designer.viewer.actions.AbstractExportAction#exportWithProgress(java.io.File, net.sf.jasperreports.engine.export.JRExportProgressMonitor)
	 */
	protected void exportWithProgress(File file, JRExportProgressMonitor monitor)
			throws JRException {
	    
        JasperPrint reportFile = null;

        if (posExportSingleXlsHandler != null) {
            reportFile = posExportSingleXlsHandler.getJasperPrint();
        }

        if (reportFile == null) {
            reportFile = getReportViewer().getDocument();
        }
	    
		JRCsvExporter exporter = new JRCsvExporter();
		
		SimpleCsvReportConfiguration configuration = new SimpleCsvReportConfiguration();
		configuration.setProgressMonitor(monitor);
		
		exporter.setConfiguration(configuration);
		
//		SimpleCsvExporterConfiguration scec = new SimpleCsvExporterConfiguration();
//		
//		exporter.setConfiguration(scec);
				
		exporter.setExporterInput(new SimpleExporterInput(reportFile));
		
//      Use the byte-order marker (BOM) to identify the CSV file as a Unicode file.
//      So I write an byte array into OutputStream, then Excel should uses UTF-8 read and display CSV files.	
        //byte-order marker (BOM)
        byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        //insert BOM byte array into outputStream
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(b);
            exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
            
        } catch (IOException e) {
            e.printStackTrace();
            
            exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
        }
		
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT,
//				getReportViewer().getDocument());
//      csvExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
//		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
//		exporter.setParameter(JRExporterParameter.PROGRESS_MONITOR, monitor);
//      csvExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, reportName.concat(".csv"));
//      csvExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");        
		
        try {
            exporter.exportReport();
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch(Exception e) {
                }
            }
        }
	}
	
}
