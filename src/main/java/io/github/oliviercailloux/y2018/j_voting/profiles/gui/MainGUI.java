package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import static org.eclipse.swt.events.SelectionListener.*;

import java.io.IOException;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.swt.layout.*;


public class MainGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class.getName());
	
	final static Display display = Display.getDefault();
	final static Shell mainShell = new Shell (display, SWT.CLOSE);
	final static Label label = new Label(mainShell, 0);
	final static Button columnsGUI = new Button (mainShell, SWT.PUSH);
	final static Button rowsGUI = new Button (mainShell, SWT.PUSH);
	final static Button wrappedColumnsGUI = new Button (mainShell, SWT.PUSH);
	final static Button selectFileToRead = new Button (mainShell, SWT.PUSH);
	
	static String fileToRead = "";
	static String[] profileToRead = new String[1];
	
	public static void displayGUI() {
		
		//file chooser
		FileDialog fileChooser = new FileDialog(mainShell, SWT.OPEN);
		fileChooser.setFilterExtensions(new String [] {"*.soc", "*.soi"});
		System.out.println("result : " + fileToRead);
		
		Font boldFont = new Font( label.getDisplay(), new FontData( "Arial", 20, SWT.BOLD ));
		label.setFont(boldFont);
		
		label.setText("SOC Profile Editing");
		selectFileToRead.setText("Select your profile");
		columnsGUI.setText("Columns : Voters");
		rowsGUI.setText("Rows : Voters");
		wrappedColumnsGUI.setText("Columns : Voters wrapped");
		
		label.setLocation(new Point(100, 10));
		label.setSize(new Point(300, 30));
		
		selectFileToRead.setLocation(new Point(100, 130));
		selectFileToRead.setSize(new Point(150, 30));
		
		columnsGUI.setLocation(new Point(20, 70));
		columnsGUI.setSize(new Point(150, 30));

		rowsGUI.setLocation(new Point(190, 70));
		rowsGUI.setSize(new Point(150, 30));

		wrappedColumnsGUI.setLocation(new Point(360, 70));
		wrappedColumnsGUI.setSize(new Point(150, 30));
		
		/*
		columnsGUI.setBounds(20, 60, 100, 50);
		rowsGUI.setBounds(130, 60, 100, 100);
		wrappedColumnsGUI.setBounds(240, 60, 100, 50);*/
		if(fileToRead == "") {
			mainShell.setText("Profile editing - No profile loaded");
		} else {
			mainShell.setText("Profile editing - " + fileToRead);
		}
		mainShell.setBounds(400, 400, 600, 300);
		mainShell.open ();
		
		selectFileToRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileToRead = fileChooser.open();
				if(fileToRead == "") {
					mainShell.setText("Profile editing - No profile loaded");
				} else {
					mainShell.setText("Profile editing - " + fileToRead);
					profileToRead[0] = fileToRead;
				}
			}
		});
		
		columnsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Columns GUI : {}", ioe);
				}
			}
		});
		
		rowsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCRowsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Rows GUI : {}", ioe);
				}
			}
		});
		
		wrappedColumnsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCWrappedColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Rows GUI : {}", ioe);
				}
			}
		});
		
		while (!mainShell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	public static void main(String[] args) {
		displayGUI();
	}

}
