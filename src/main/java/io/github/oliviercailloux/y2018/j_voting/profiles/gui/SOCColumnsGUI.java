package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;

public class SOCColumnsGUI extends ColumnsDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCColumnsGUI.class.getName());
	private String alternativeToAdd;
	private final Label label = new Label(mainShell, SWT.NULL);
	private final Text newAlternativeText = new Text(mainShell, SWT.SINGLE | SWT.BORDER);
	private final Button addAlternativeButton = new Button(mainShell, SWT.PUSH);

	@Override
	public void tableDisplay() {
		LOGGER.debug("tableDisplay");

		// table layout handling
		mainShell.setLayout(new GridLayout());
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		label.setText("New alternative : ");
		addAlternativeButton.setText("Add");

		newAlternativeText.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					alternativeToAdd = newAlternativeText.getText();
					addAlternativeButton.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					addAlternativeButton.setEnabled(false);
				}
			}
		});

		addAlternativeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addAlternative(alternativeToAdd);
			}
		});

		createColumns();
		populateRows();
		handleDragAndDrop();
		checkRadioButton();

		TableColumn[] tableColumns = table.getColumns();
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.pack();
		}

		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(MainGUI.fileToRead);
			}
		});
	}

	public void addAlternative(String alternative) {
		LOGGER.debug("addAlternative :");

		if (Strings.isNullOrEmpty(alternative))
			return;

		try {
			Integer.parseInt(alternative);
		} catch (NumberFormatException nfe) {
			LOGGER.debug("NumberFormatException : {}", nfe);
			MessageBox messageBox = new MessageBox(mainShell, SWT.OK);
			messageBox.setText("Warning");
			messageBox.setMessage("Alternative should be an integer !");
			messageBox.open();
			return;
		}

		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		if (strictProfile.getAlternatives().contains(new Alternative(Integer.parseInt(alternative)))) {
			MessageBox messageBox = new MessageBox(mainShell, SWT.OK);
			messageBox.setText("Warning");
			messageBox.setMessage("This alternative already exists !");
			messageBox.open();
		} else {
			TableItem item = new TableItem(table, SWT.NONE);
			List<String> altForEveryone = Collections.nCopies(strictProfile.getNbVoters(), alternative);
			item.setText(altForEveryone.toArray(new String[strictProfile.getNbVoters()]));
			newAlternativeText.setText("");
		}
	}

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		// if profile get from file is SOC, create a StrictProfile from it
		StrictProfile strictProfile = profileBuilder.createStrictProfile();

		Iterable<Voter> allVoters = strictProfile.getAllVoters(); // get voters from profile

		// COLUMNS
		List<String> titles = new ArrayList<>();
		for (Voter v : allVoters) {
			titles.add("Voter " + v.getId());
		}
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
	}

	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		// ROWS
		List<String> alternatives = new ArrayList<>();

		for (int i = 0; i < strictProfile.getNbAlternatives(); i++) {
			// get ith alternative of each voter
			List<Alternative> ithAlternatives = strictProfile.getIthAlternatives(i);
			for (Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(alternatives.toArray(new String[strictProfile.getNbAlternatives()])); // create a row with ith
																								// alternatives
			alternatives.clear(); // empty the list
		}
	}

	public static void main(String[] args) throws IOException {
		SOCColumnsGUI socColumns = new SOCColumnsGUI();
		socColumns.displayProfileWindow(args);
	}

}
