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

import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;

public class SOIWrappedColumnsGUI extends ColumnsDefaultGUI{
	private static final Logger LOGGER = LoggerFactory.getLogger(SOIWrappedColumnsGUI.class.getName());
	
	//TODO: change everything so that the GUI allows editing
	
	@Override
	public List<String> createColumns() {
		LOGGER.debug("createColumns :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();//if profile get from file is SOC, create a StrictProfile from it
		Set<Preference> uniquePreferences = strictProfile.getUniquePreferences();
		
		//COLUMNS
		List<String> titles = new ArrayList<>();
		for(Preference p : uniquePreferences){
			int nbVoters = strictProfile.getNbVoterForPreference(p);
			
			String voterOrVoters = (nbVoters > 1) ? " voters" : " voter";
			titles.add(nbVoters + voterOrVoters);
		}
		for (int i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		return titles;
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
		//ROWS
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		List<String> alternatives = new ArrayList<>();
		
		int nbAlternatives = strictProfile.getNbAlternatives();//nb of rows

		for(int i = 0 ; i < nbAlternatives ; i++){
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(strictProfile.getIthAlternativesOfUniquePrefAsString(i).toArray(new String[nbAlternatives]));	// create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}
	
	public static void main (String [] args) throws IOException {
		LOGGER.debug("Main");
		SOIWrappedColumnsGUI soiWrapped = new SOIWrappedColumnsGUI();
		profileBuilder = soiWrapped.tableDisplay(args);
	}

}
