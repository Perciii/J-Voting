package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;

import org.slf4j.*;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;



public class ReadProfile {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReadProfile.class.getName());
	
	/**
	 * Creates a StrictProfile with the information extracted from a file.
	 * @param is <code>not null</code> the InputStream from which the data has to be extracted. InputStream is closed in this method.
	 * @return sProfile a StrictProfile
	 * @throws IOException 
	 */

	public StrictProfile createProfileFromStream(InputStream is) throws IOException{
		LOGGER.debug("ReadProfile : createProfileFromReadFile : ");
		Preconditions.checkNotNull(is);
		
		try(InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)){
			List<String> fileRead =  CharStreams.readLines(isr);

			Iterator<String> it = fileRead.iterator();
			StrictProfile sProfile = new StrictProfile();
			String lineNbVoters;
			int nbAlternatives = Integer.parseInt(it.next());	//first number of the file is the number of alternatives
			LOGGER.debug("number of alternatives : {}", nbAlternatives);
			List<String> alternatives = new ArrayList<>();
			List<String> profiles = new ArrayList<>();
			for(int i = 1 ; i <= nbAlternatives ; i++){//get the lines with the alternatives
				alternatives.add(it.next());
			}
			LOGGER.debug("alternatives : {}", alternatives);
			lineNbVoters = it.next();//get the line with the nb of voters
			LOGGER.debug("line with stats about the votes ); {}", lineNbVoters);
			while(it.hasNext()){//get the rest of the file
				profiles.add(it.next());
			}
			LOGGER.debug("lines with the number of votes for each StrictPreference : {}", profiles);
			StrictPreference listeAlternatives = getAlternatives(nbAlternatives, alternatives);
			List<Integer> listInt = getStatsVoters(lineNbVoters);
			sProfile = buildProfile(profiles, listeAlternatives, listInt.get(0));
			return sProfile;
		}
	}
	
	/**
	 * @param is an InputStream <code>not null</code> to read from the desired file, InputStream is closed in this method
	 * This function prints strings from the read file which InputStream is passed as an argument
	 * @throws IOException 
	 */
	public void displayProfileFromStream(InputStream is) throws IOException{
		LOGGER.debug("ReadProfile : displayProfileFromFile : ") ;
		Preconditions.checkNotNull(is);
		LOGGER.debug("parameter : InputStream = {}", is);
		
		
		try(InputStreamReader isr = new InputStreamReader(is, Charsets.UTF_8)){
			List<String> fileRead =  CharStreams.readLines(isr);
			Iterator<String> it = fileRead.iterator();
			while(it.hasNext()){
				System.out.println(it.next());
			}
		}
		
	}
	
	/**
	 * @param nbAlternatives the number of alternatives in the profile
	 * @param file a list of strings each containing an alternative
	 * @return the alternatives in the profile given in a list of string.
	 */
	public StrictPreference getAlternatives(int nbAlternatives,List<String> file){
		LOGGER.debug("ReadProfile : GetAlternatives :");
		Preconditions.checkNotNull(nbAlternatives);
		Preconditions.checkNotNull(file);
		LOGGER.debug("parameters : nbAlternatives = {}, file = {}", nbAlternatives, file); 
		Iterator<String> it = file.iterator();
		String s1; 
		List<Alternative> alternatives= new ArrayList<>();
		for(int k = 1 ; k <= nbAlternatives ; k++){//we add each alternative to a list
			s1 = it.next();
			LOGGER.debug("next Alternative : {}", s1);
			if (s1.contains(",")){//line with alternative doesn't contain ,
				throw new Error("Error: nbAlternative is not correct");
			}
			alternatives.add(new Alternative(Integer.parseInt(s1)));
		}
		StrictPreference listAlternatives = new StrictPreference(alternatives);
		LOGGER.debug("returns listAlternatives : {}", listAlternatives);
		return listAlternatives;
	}
	
	
	/**
	 * @param s the line with the voters statistics (number, sum of count, number of unique alternatives)
	 * @return a list with the three computed statistics
	 */
	public List<Integer> getStatsVoters(String s){
		LOGGER.debug("ReadProfile : getNbVoters :");
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
	 * @param listeAlternatives the alternatives of the profile
	 * @param s1 a line of the profile containing the number of voters for a preference followed by the preference (list of alternatives)
	 * @return the StrictPreference given in the line s1
	 */
	public StrictPreference getPreferences(StrictPreference listeAlternatives, String s1){
		LOGGER.debug("ReadProfile : getPreferences");
		Preconditions.checkNotNull(listeAlternatives);
		Preconditions.checkNotNull(s1);
		LOGGER.debug("parameters : listeAlternatives {}, s1 {}", listeAlternatives, s1);
		String[] s2 = s1.split(",");
		List<Alternative> pref = new ArrayList<>();
		for(int i = 1 ; i < s2.length ; i++){//we collect all the alternatives, skipping the first element which is the nb of votes
			Alternative alter = new Alternative(Integer.parseInt(s2[i].trim()));
			LOGGER.debug("next alternative {}", alter.getId());
			if(listeAlternatives.contains(alter)) {
				LOGGER.debug("correct alternative");
				pref.add(alter);
			}
			else {
				LOGGER.debug("alternative not in the profile");
				throw new IllegalArgumentException("The line s1 contains an alternative that is not in the profile's alternatives");
			}
		}
		return new StrictPreference(pref);
	}
	
	/**
	 * @param pref a StrictPreference
	 * @param nbVoters the number of voters that voted for the preference as parameter
	 * @param profile the StrictProfile to which the votes will be added
	 */
	public void addVotes(StrictPreference pref, int nbVoters, StrictProfile profile){
		LOGGER.debug("ReadProfile : addVotes");
		Preconditions.checkNotNull(pref);
		Preconditions.checkNotNull(nbVoters);
		Preconditions.checkNotNull(profile);
		LOGGER.debug("parameters : nbVoters {} for the preference {}", nbVoters, pref); 
		for(int m = 0 ; m < nbVoters ; m++){//we create as many profiles as voters 
			Voter v =new Voter(profile.nextVoterId);
			LOGGER.debug("adds the voter {} and the pref as parameter to the profile", profile.nextVoterId);
			profile.nextVoterId++;
			profile.addProfile(v, pref);
		}
	}

	/**
	 * @param file the lines with the number of votes for each preference
	 * @param listAlternatives the alternatives of the profile
	 * @param nbVoters the number of voters
	 * @return the created StrictProfile
	 */
	public StrictProfile buildProfile(List<String> file, StrictPreference listAlternatives, int nbVoters){
		LOGGER.debug("ReadProfile : buildProfiles :");
		Preconditions.checkNotNull(file);
		Preconditions.checkNotNull(listAlternatives);
		Preconditions.checkNotNull(nbVoters);
		Iterator<String> it = file.iterator();
		StrictProfile profile = new StrictProfile();
		String s1; //where we store the current line
		while(it.hasNext()){
			s1 = it.next();
			LOGGER.debug("next line : {}",s1);
			if (!s1.contains(",")){// if the line doesn't contain , it's the line of an alternative
				throw new IllegalArgumentException("the first string of file is an alternative line.");
			}
			String[] line = s1.split(",");
			StrictPreference pref = getPreferences(listAlternatives,s1);
			LOGGER.debug("to add : {} votes for the StrictPreference {}",line[0].trim(),pref);
			addVotes(pref, Integer.parseInt(line[0].trim()), profile);
		}
		return profile;
	}
	
	
	/**
	 * This function displays the contents of the profiles in the resources, if they exist 
	 **/
	public void main(String[] args) throws IOException {
		//read SOC file
		try(InputStream socStream = getClass().getResourceAsStream("profil.soc")){
			LOGGER.debug("SOC Profile stream : {}", socStream);
			//StrictProfile socProfile = createProfileFromFile(socPath);
			displayProfileFromStream(socStream);
		}
		
		//read SOI file
		try(InputStream soiStream = getClass().getResourceAsStream("profil.soi")){
			LOGGER.debug("SOI Profile stream : {}", soiStream);
			//StrictProfile socProfile = createProfileFromFile(socPath);
			displayProfileFromStream(soiStream);
		}
	}
}
