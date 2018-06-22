package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfileI;

public class SOIWrappedColumnsGUI extends ColumnsDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOIWrappedColumnsGUI.class.getName());

	// TODO: change everything so that the GUI allows editing

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// if profile get from file is SOC, create a StrictProfile from it
		Set<Preference> uniquePreferences = strictProfile.getUniquePreferences();

		// COLUMNS
		List<String> titles = new ArrayList<>();
		for (Preference p : uniquePreferences) {
			int nbVoters = strictProfile.getNbVoterForPreference(p);

			String voterOrVoters = (nbVoters > 1) ? " voters" : " voter";
			titles.add(nbVoters + voterOrVoters);
		}
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
	}

	@Override
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(false);
		rowsButton.setSelection(false);
		wrapButton.setSelection(true);
	}

	@Override
	public void populateRows() {
		LOGGER.debug("populateRows :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		// ROWS
		List<String> alternatives = new ArrayList<>();

		int nbAlternatives = strictProfile.getNbAlternatives();// nb of rows

		for (int i = 0; i < nbAlternatives; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			// create a row with ith alternatives
			item.setText(strictProfile.getIthAlternativesOfUniquePrefAsString(i).toArray(new String[nbAlternatives]));
			alternatives.clear(); // empty the list
		}
	}

	public static void main(String[] args) throws IOException {
		SOIWrappedColumnsGUI soiWrapped = new SOIWrappedColumnsGUI();
		soiWrapped.displayProfileWindow(args);
	}

}
