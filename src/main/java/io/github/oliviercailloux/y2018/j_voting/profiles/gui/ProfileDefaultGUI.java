package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ReadProfile;

/**
 * Generalization of profile displaying GUIs
 */
public class ProfileDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDefaultGUI.class.getName());
	protected static final Display display = Display.getDefault();
	protected static final Shell mainShell = new Shell(display, SWT.CLOSE | SWT.RESIZE);
	protected static Button columnsButton = new Button(mainShell, SWT.RADIO);
	protected static Button rowsButton = new Button(mainShell, SWT.RADIO);
	protected static Button wrapButton = new Button(mainShell, SWT.RADIO);
	// protected Button editButton = new Button(mainShell, SWT.PUSH);
	// protected Button editSaveButton = new Button(mainShell, SWT.PUSH);
	protected static TableViewer tableViewer = new TableViewer(mainShell, SWT.MULTI | SWT.BORDER);
	protected static Table table = tableViewer.getTable();
	protected static Integer voterToModify = null;
	protected static StrictPreference newpref;
	protected static ProfileBuilder profileBuilder;

	/**
	 * Displays the window : the table containing the profile as well as the
	 * buttons.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void displayProfileWindow(String[] args) throws IOException {
		LOGGER.debug("displayProfileWindow");
		Preconditions.checkNotNull(args[0]);

		String arg = args[0];// arg is the file path
		ReadProfile rp = new ReadProfile();
		try (FileInputStream is = new FileInputStream(arg)) {
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			/*
			 * editButton.setText("Edit"); editButton.addSelectionListener(new
			 * SelectionAdapter() {
			 * 
			 * @Override public void widgetSelected(SelectionEvent e) {
			 * editStrictPreference(arg); // open editButton modal } });
			 */

			displayRadioButtons(args);

			tableDisplay();

			mainShell.setText("Edit Profile");
			mainShell.pack();
			mainShell.open();

			while (!display.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
	}

	/**
	 * Displays the table containing the profile
	 */
	public void tableDisplay() {
		LOGGER.debug("tableDisplay");

		// table layout handling
		mainShell.setLayout(new GridLayout());
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		createColumns();
		populateRows();

		checkRadioButton();

		TableColumn[] tableColumns = table.getColumns();
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.pack();
		}
	}

	/**
	 * Displays the radio buttons to choose the layout of the profile. This method
	 * works for both SOC and SOI.
	 * 
	 * @param args
	 *            contains the name of the file with the profile
	 */
	public void displayRadioButtons(String[] args) {
		LOGGER.debug("displayRadioButtons :");
		columnsButton.setText("Columns");
		rowsButton.setText("Rows");
		wrapButton.setText("Wrapped");

		String fileExtension = args[0].substring(args[0].length() - 3);
		if (fileExtension.equals("soc")) {
			columnsButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					emptyTable();
					new SOCColumnsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
			rowsButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					emptyTable();
					new SOCRowsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
			wrapButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					emptyTable();
					new SOCWrappedColumnsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
		} else if (fileExtension.equals("soi")) {
			columnsButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					emptyTable();
					new SOIColumnsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
			rowsButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					emptyTable();
					new SOIRowsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
			wrapButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					emptyTable();
					new SOIWrappedColumnsGUI().tableDisplay();
					table.setRedraw(true);
				}
			});
		} else {
			throw new IllegalArgumentException("The file is neither soc nor soi.");
		}
	}

	/**
	 * Checks the right radio button.
	 */
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(true);
		rowsButton.setSelection(false);
		wrapButton.setSelection(false);
	}

	public void createColumns() {
		LOGGER.debug("createColumns :");
		// if profile get from file is SOC, create a StrictProfile from it
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		Iterable<Voter> allVoters = strictProfile.getAllVoters(); // get voters from profile
		int i = 0;
		// COLUMNS
		List<String> titles = new ArrayList<>();
		for (Voter v : allVoters) {
			titles.add("Voter " + v.getId());
			i++;
		}
		for (i = 0; i < titles.size(); i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles.get(i));
		}
	}

	/**
	 * Fills the table of the profile with the alternatives : by default, each
	 * column contains the preference of a voter
	 */
	public void populateRows() {
		LOGGER.debug("populateRowsSOI :");
		// ROWS
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		List<String> alternatives = new ArrayList<>();

		int nbAlternatives = strictProfile.getMaxSizeOfPreference();

		for (int i = 0; i < nbAlternatives; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			// create a row with ith alternatives
			item.setText(strictProfile.getIthAlternativesAsStrings(i).toArray(new String[nbAlternatives]));
			alternatives.clear(); // empty the list
		}
	}

	/**
	 * Displays the editButton window, where you can choose to modify/add a
	 * StrictPreference of a voter
	 * 
	 * @param arg
	 *            not <code>null</code>
	 */
	public void editStrictPreference(String arg) {
		LOGGER.debug("editPreference :");
		Preconditions.checkNotNull(arg);
		final Shell modalShell = new Shell(display, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		modalShell.setText("Edit");
		modalShell.setLayout(new GridLayout(2, true));

		Label label = new Label(modalShell, SWT.NULL);
		label.setText("Which voter do you want to change ?");

		final Text text = new Text(modalShell, SWT.SINGLE | SWT.BORDER);

		Label labelPref = new Label(modalShell, SWT.NULL);
		labelPref.setText("Choose the preference :");

		final Text textPref = new Text(modalShell, SWT.SINGLE | SWT.BORDER);
		/*
		 * editSaveButton.setText("Save"); editSaveButton.setLayoutData(new
		 * GridData(GridData.HORIZONTAL_ALIGN_END));
		 */

		Button cancelButton = new Button(modalShell, SWT.PUSH);
		cancelButton.setText("Cancel");

		modalShell.pack();
		modalShell.open();

		text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					voterToModify = Integer.parseInt(text.getText());
					// saveButton.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					// saveButton.setEnabled(false);
				}
			}
		});

		textPref.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					newpref = new ReadProfile().createStrictPreferenceFrom(textPref.getText());
					// saveButton.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : {} ", iae);
					// saveButton.setEnabled(false);
				}
			}
		});
		/*
		 * editSaveButton.addSelectionListener(new SelectionAdapter() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * LOGGER.debug("voterToModify : {} ", voterToModify);
		 * LOGGER.debug("pref : {} ", newpref); modif(); save(arg);
		 * modalShell.dispose(); table.removeAll(); populateRows();
		 * table.setRedraw(true); } });
		 */

		cancelButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				modalShell.dispose();
			}
		});

		modalShell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE)
					modalShell.dispose();
				event.doit = false;
			}
		});
	}

	/**
	 * Edits the profile with the given information
	 */
	public void modif() {
		LOGGER.debug("modif :");
		Voter voter = new Voter(voterToModify);
		LOGGER.debug("New preference for voter v {} : {}", voter, newpref);
		// change preference for this Voter in global ProfileBuilder
		profileBuilder.addVote(voter, newpref);
	}

	/**
	 * Saves the changes to the file containing the profile.
	 * 
	 * @param outputFile
	 */
	public void save(String outputFile) {
		LOGGER.debug("saveButton :");
		Preconditions.checkNotNull(outputFile);
		File file = new File(outputFile);
		try (OutputStream outputStream = new FileOutputStream(file)) {
			String fileExtension = file.toString().substring(file.toString().length() - 3);
			if (fileExtension.equals("soc")) {
				StrictProfile sp = profileBuilder.createStrictProfile();
				sp.writeToSOC(outputStream);
			} else { // fileExtension == "soi"
				StrictProfileI spi = profileBuilder.createStrictProfileI();
				spi.writeToSOI(outputStream);
			}
		} catch (IOException ioe) {
			MessageBox dialog = new MessageBox(mainShell, SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("IOException");
			dialog.setMessage("Error when opening Stream : " + ioe);
			dialog.open();
		}
	}

	/**
	 * Empties the table : removes all data and columns. Removes the saveButton
	 * button if it isn't disposed yet.
	 */
	public void emptyTable() {
		LOGGER.debug("emptyTable");
		table.setRedraw(false);
		table.removeAll();
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
	}
}
