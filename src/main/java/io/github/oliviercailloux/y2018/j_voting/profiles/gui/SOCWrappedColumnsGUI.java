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

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;

public class SOCWrappedColumnsGUI extends ColumnsDefaultGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCWrappedColumnsGUI.class.getName());

	@Override
	public void createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
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
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		// ROWS
		List<String> alternatives = new ArrayList<>();

		int nbAlternatives = strictProfile.getNbAlternatives();// nb of rows

		for (int i = 0; i < nbAlternatives; i++) {
			// get ith alternative of each voter
			List<Alternative> ithAlternatives = strictProfile.getIthAlternativesOfUniquePreferences(i);
			for (Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(alternatives.toArray(new String[nbAlternatives])); // create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}

	public static void main(String[] args) throws IOException {
		SOCWrappedColumnsGUI socWrapped = new SOCWrappedColumnsGUI();
		socWrapped.displayProfileWindow(args);
	}

}
