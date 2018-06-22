package io.github.oliviercailloux.y2018.j_voting.profiles.management;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;

import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;

/**
 * 
 * The ReadProfile class provides methods creating and displaying Profiles from
 * different resources (e.g. InputStream, URL).
 *
 */
public class ReadProfile {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadProfile.class.getName());

	/**
	 * Creates a StrictProfile with the information extracted from the InputStream
	 * given as parameter.
	 * 
	 * @param is
	 *            <code>not null</code> the InputStream from which the data has to
	 *            be extracted. InputStream is closed in this method.
	 * @return sProfile a StrictProfile
	 * @throws IOException
	 */
	public ProfileI createProfileFromStream(InputStream is) throws IOException {
		LOGGER.debug("CreateProfileFromReadFile : ");
		Preconditions.checkNotNull(is);

		try (InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)) {
			List<String> fileRead = CharStreams.readLines(isr);
			Iterator<String> it = fileRead.iterator();
			String lineNbVoters;
			int nbAlternatives = Integer.parseInt(it.next()); // first number of the file is the number of alternatives
			LOGGER.debug("number of alternatives : {}", nbAlternatives);
			List<String> alternatives = new ArrayList<>();
			List<String> profiles = new ArrayList<>();
			for (int i = 1; i <= nbAlternatives; i++) {// get the lines with the alternatives
				String nextAlternative = it.next().trim();
				int indexOfFirstComma = nextAlternative.indexOf(",");

				if (indexOfFirstComma != -1) {
					String nextAlternativeNumber = nextAlternative.substring(0, indexOfFirstComma);
					alternatives.add(nextAlternativeNumber);
				} else {
					alternatives.add(nextAlternative);
				}
			}
			LOGGER.debug("alternatives : {}", alternatives);
			lineNbVoters = it.next().trim();// get the line with the nb of voters
			LOGGER.debug("line with stats about the votes ); {}", lineNbVoters);
			while (it.hasNext()) {// get the rest of the file
				profiles.add(it.next().trim());
			}
			LOGGER.debug("lines with the number of votes for each StrictPreference : {}", profiles);
			StrictPreference listeAlternatives = getAlternatives(alternatives);
			List<Integer> listInt = getStatsVoters(lineNbVoters);
			return buildProfile(profiles, listeAlternatives, listInt.get(0));
		}
	}

	/**
	 * 
	 * @param table
	 *            not <code>null</code> a Table displayed as Voters in columns
	 * @return a restricted ProfileI
	 */
	public ProfileI createProfileFromColumnsTable(Table table) {
		LOGGER.debug("createProfileFromColumnsTable : ");
		Preconditions.checkNotNull(table);

		ProfileBuilder profileBuilder = new ProfileBuilder();
		int nbColumns = table.getColumnCount();
		for (int column = 0; column < nbColumns; column++) {// browse columns
			String newPrefString = table.getItem(0).getText(column);
			for (TableItem item : Iterables.skip(Arrays.asList(table.getItems()), 1)) {
				String altText = item.getText(column);
				if (altText != null && altText != "") {
					newPrefString += "," + altText;
				}
			}
			Voter voter = new Voter(column + 1);
			StrictPreference newPref = new ReadProfile().createStrictPreferenceFrom(newPrefString);
			profileBuilder.addVote(voter, newPref);
		}

		return profileBuilder.createProfileI().restrictProfile();
	}

	/**
	 * 
	 * @param table
	 *            <code>null</code> a Table displayed as Voters in rows
	 * @return a restricted ProfileI
	 */
	public ProfileI createProfileFromRowsTable(Table table) {
		LOGGER.debug("createProfileFromRowsTable : ");
		Preconditions.checkNotNull(table);

		ProfileBuilder profileBuilder = new ProfileBuilder();
		int nbItems = table.getItemCount();
		int nbColumns = table.getColumnCount();
		for (int item = 0; item < nbItems; item++) {// browse columns
			String newPrefString = table.getItem(item).getText(0);
			for (int column = 1; column < nbColumns; column++) {
				String altText = table.getItem(item).getText(column);
				if (altText != null && altText != "") {
					newPrefString += "," + altText;
				}
			}
			Voter voter = new Voter(item + 1);
			StrictPreference newPref = new ReadProfile().createStrictPreferenceFrom(newPrefString);
			profileBuilder.addVote(voter, newPref);
		}

		return profileBuilder.createProfileI().restrictProfile();
	}

	/**
	 * Creates a StrictProfile with the information extracted from the URL given as
	 * parameter
	 * 
	 * @param url
	 *            <code>not null</code> the URL from which the data has to be
	 *            extracted
	 * @return a StrictProfile
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public ProfileI createProfileFromURL(URL url) throws IOException {
		LOGGER.debug("CreateProfileFromURL : ");
		Preconditions.checkNotNull(url);
		LOGGER.debug("parameter : URL = {}", url.toString());

		Client client = ClientBuilder.newClient();
		Response response = client.target(url.toString()).request().get();
		try (InputStream is = (InputStream) response.getEntity()) {
			return createProfileFromStream(is).restrictProfile();
		}
	}

	/**
	 * This method prints strings from the read file which InputStream is passed as
	 * an argument. It uses an InputStreamReader using UTF-8 to read the Stream
	 * 
	 * @param is
	 *            an InputStream <code>not null</code> to read from the desired
	 *            file<br>
	 *            InputStream is closed in this method
	 * @throws IOException
	 */
	public void displayProfileFromStream(InputStream is) throws IOException {
		LOGGER.debug("DisplayProfileFromFile : ");
		Preconditions.checkNotNull(is);
		LOGGER.debug("parameter : InputStream = {}", is);
		try (InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)) {
			List<String> fileRead = CharStreams.readLines(isr);
			for (String line : fileRead) {
				System.out.println(line);
			}
		}
	}

	/**
	 * @param listOfStrings
	 *            <code>not null</code> a list of strings each containing an
	 *            alternative
	 * @return the Alternatives, in the list of strings, given as a
	 *         StrictPreference.
	 */
	private StrictPreference getAlternatives(List<String> listOfStrings) {
		LOGGER.debug("GetAlternatives :");
		Preconditions.checkNotNull(listOfStrings);
		LOGGER.debug("parameter : file = {}", listOfStrings);
		List<Alternative> alternatives = new ArrayList<>();
		for (String alternative : listOfStrings) {
			LOGGER.debug("next Alternative : {}", alternative);
			alternatives.add(new Alternative(Integer.parseInt(alternative)));
		}
		StrictPreference listAlternatives = new StrictPreference(alternatives);
		LOGGER.debug("returns listAlternatives : {}", listAlternatives);
		return listAlternatives;
	}

	/**
	 * @param s
	 *            <code>not null</code> the line with the voters statistics (number,
	 *            sum of count, number of unique alternatives)
	 * @return a List with the three computed statistics
	 */
	private List<Integer> getStatsVoters(String s) {
		LOGGER.debug("GetNbVoters :");
		Preconditions.checkNotNull(s);
		LOGGER.debug("parameter : s = {}", s);
		List<Integer> list = new ArrayList<>();
		String[] line = s.split(",");
		list.add(Integer.parseInt(line[0].trim()));
		list.add(Integer.parseInt(line[1].trim()));
		list.add(Integer.parseInt(line[2].trim()));
		LOGGER.debug("returns list : {}\n", list);
		return list;
	}

	/**
	 * @param listeAlternatives
	 *            <code>not null</code> the alternatives of the profile
	 * @param s1
	 *            <code>not null</code> a line of the profile containing the number
	 *            of voters for a preference followed by the preference (list of
	 *            alternatives)
	 * @return the StrictPreference given in the line s1
	 */
	public StrictPreference getPreferences(StrictPreference listeAlternatives, String s1) {
		LOGGER.debug("GetPreferences");
		Preconditions.checkNotNull(listeAlternatives);
		Preconditions.checkNotNull(s1);
		LOGGER.debug("parameters : listeAlternatives {}, s1 {}", listeAlternatives, s1);

		String[] alternatives = s1.split(",");
		List<Alternative> pref = new ArrayList<>();
		for (String alternative : Iterables.skip(Arrays.asList(alternatives), 1)) {
			Alternative alter = new Alternative(Integer.parseInt(alternative.trim()));
			LOGGER.debug("next alternative {}", alter.getId());
			if (listeAlternatives.contains(alter)) {
				LOGGER.debug("correct alternative");
				pref.add(alter);
			} else {
				LOGGER.debug("alternative not in the profile");
				throw new IllegalArgumentException(
						"The line s1 contains an alternative that is not in the profile's alternatives");
			}
		}
		return new StrictPreference(pref);
	}

	/**
	 * 
	 * @param stringPreference
	 *            not <code>null</null>
	 * @return the strictPreference in the string. The string only contains the
	 *         alternatives.
	 */
	public StrictPreference createStrictPreferenceFrom(String stringPreference) {
		LOGGER.debug("GetPreferences");
		Preconditions.checkNotNull(stringPreference);
		LOGGER.debug("parameters : s1 {}", stringPreference);
		String[] s2 = stringPreference.split(",");
		List<Alternative> pref = new ArrayList<>();
		for (String strAlt : s2) {
			Alternative alter = new Alternative(Integer.parseInt(strAlt.trim()));
			LOGGER.debug("next alternative {}", alter.getId());
			pref.add(alter);
		}
		if (pref.size() == 0) {
			throw new IllegalArgumentException("The preference is empty.");
		}
		return new StrictPreference(pref);
	}

	/**
	 * @param file
	 *            <code>not null</code> the lines with the number of votes for each
	 *            preference
	 * @param listAlternatives
	 *            <code>not null</code> the alternatives of the profile
	 * @param nbVoters
	 *            <code>not null</code> the number of voters
	 * @return the created StrictProfile
	 */
	public ProfileI buildProfile(List<String> file, StrictPreference listAlternatives, int nbVoters) {
		LOGGER.debug("BuildProfiles :");
		Preconditions.checkNotNull(file);
		Preconditions.checkNotNull(listAlternatives);
		Preconditions.checkNotNull(nbVoters);

		StrictProfileBuilder profile = new StrictProfileBuilder();
		for (String line : file) {
			LOGGER.debug("next line : {}", line);
			if (!line.contains(",")) {// if the line doesn't contain "," it's the line of an alternative
				throw new IllegalArgumentException("the first string of file is an alternative line.");
			}
			String[] lineAsArray = line.split(",");
			StrictPreference pref = getPreferences(listAlternatives, line);
			LOGGER.debug("to add : {} votes for the StrictPreference {}", lineAsArray[0].trim(), pref);
			profile.addVotes(pref, Integer.parseInt(lineAsArray[0].trim()));
		}
		return profile.createProfileI();
	}

	/**
	 * This method displays the contents of the profiles in the resources, if they
	 * exist
	 **/
	public void main(String[] args) throws IOException {
		// read SOC file
		try (InputStream socStream = getClass().getResourceAsStream("profil.soc")) {
			LOGGER.debug("SOC Profile stream : {}", socStream);
			displayProfileFromStream(socStream);
		}

		// read SOI file
		try (InputStream soiStream = getClass().getResourceAsStream("profil.soi")) {
			LOGGER.debug("SOI Profile stream : {}", soiStream);
			displayProfileFromStream(soiStream);
		}
	}
}