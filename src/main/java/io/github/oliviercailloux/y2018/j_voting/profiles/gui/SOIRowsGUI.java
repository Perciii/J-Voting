package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfileI;

public class SOIRowsGUI extends ProfileDefaultGUI  {

	private static final Logger LOGGER = LoggerFactory.getLogger(SOCRowsGUI.class.getName());
	
	@Override
	public List<String> createColumns() {
	LOGGER.debug("createColumns :");
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();//if profile get from file is SOC, create a StrictProfile from it
		
		int i = 0; 
		
		//COLUMNS
		
		List<String> titles = new ArrayList<>();
		titles.add("Voters");
		for(int a=1;a<=strictProfile.getMaxSizeOfPreference();a++ ){
			titles.add("Alternative " + a);
			i++;
		}
		for (i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}

		return titles;
	}

	@Override
	public void checkRadioButton() {
		LOGGER.debug("checkRadioButtons");
		columnsButton.setSelection(false);			
		rowsButton.setSelection(true);		
		wrapButton.setSelection(false);
	}
	
	@Override
	public void populateRows() {
	LOGGER.debug("populateRows :");
		//ROWS
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		
		Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
		
		int nbAlternatives = strictProfile.getNbAlternatives();

		List<String> line  = new ArrayList<>();
		for(Voter v : allVoters){

			line.add("Voter " + v.getId());
			Preference pref = strictProfile.getPreference(v);
			Iterable<Alternative> allPref = Preference.toAlternativeSet(pref.getPreferencesNonStrict());
			for(Alternative a : allPref){
				System.out.println(a);
				line.add(a.toString());
			}

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(line.toArray(new String[nbAlternatives+1]));	
			line.clear(); // empty the list

		}
		
	}

	public static void main (String [] args) throws IOException {
		SOIRowsGUI soiRows = new SOIRowsGUI();
		profileBuilder = soiRows.tableDisplay(args);
	}
	
}
