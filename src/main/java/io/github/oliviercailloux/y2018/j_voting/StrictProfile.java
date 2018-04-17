package io.github.oliviercailloux.y2018.j_voting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import org.slf4j.*;

/**
 * 
 * This class maps the voters to their strict preference (StrictPreference).
 *
 */
public class StrictProfile {
	static Logger Log = LoggerFactory.getLogger(ReadProfile.class.getName());
	private Map<Voter,StrictPreference> association;
	
	public StrictProfile(){
		association = new HashMap<>();
	}
	
	/**
	 * Adds a StrictPreference to a voter. If the voter is already in the map, it replaces the StrictPreference. 
	 * If the voter isn't in the map, it adds a new voter and associates the StrictPreference.
	 * @param voter
	 * @param preference
	 */
	public void addProfile(Voter voter,StrictPreference preference) {
		Log.info("addProfile\n");
		Objects.requireNonNull(voter);
		Objects.requireNonNull(preference);
		Log.debug("parameters : voterId = {}, preferences = {}",voter.getId(),preference);
		association.put(voter,preference);
	}
	
	/**
	 * Writes a StrictProfile into a SOC file.
	 * @param profile a StrictProfile
	 * @throws IOException
	 */
	public static void writeToSOC(StrictProfile profile) throws IOException {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("profil.soc"))){ //TODO: determine how to automatically change file name
	        PrintWriter pWriter = new PrintWriter(bw);
	        pWriter.print(profile);
		}
	}
	
	/**
	 * 
	 * @param voter
	 * @return the strict preference of the voter. If the voter is not in the map, it throws an exception.
	 */
	public StrictPreference getPreference(Voter voter) {
		Log.info("getPreference\n");
		Objects.requireNonNull(voter);
		Log.debug("parameter : voterId = {}\n",voter.getId());
		if(!association.containsKey(voter)) {
			throw new NoSuchElementException("Voter " + voter + "is not in the map !");
		}
		Log.debug("returns voter's preference = {}\n",association.get(voter));
		return association.get(voter);
	}
	
	/**
	 * 
	 * @return all the voters in the map.
	 */
	public Iterable<Voter> getAllVoters(){
		return association.keySet();
	}
	
	/**
	 * 
	 * @return the number of voters in the profile
	 */
	public int getNbVoters() {
		return association.size();
	}
	
	/**
	 * 
	 * @return the sum of the vote count 
	 */
	public int getSumVoteCount() {
		return association.size();
	}
	
	/**
	 * @return all the unique StrictPreference in the profile.
	 */
	public Iterable<StrictPreference> getUniquePreferences(){
		Log.info("getUniquePreferences\n");
		Iterable<StrictPreference> unique = new LinkedHashSet<StrictPreference>();
		Iterator<StrictPreference> i1 = association.values().iterator();
		boolean alreadyInList = false;
		while(i1.hasNext()) {
			StrictPreference pref = i1.next();
			Log.debug("next preference : {}\n",pref);
			Iterator<StrictPreference> i2 = unique.iterator();
			while(i2.hasNext()) {
				if(pref.equals(i2.next())) {
					Log.debug("{} already in the list\n",pref);
					alreadyInList = true;
				}
			}
			if(!alreadyInList) {
				((LinkedHashSet<StrictPreference>) unique).add(pref);
			}
		}
		return unique;
	}
	
	/**
	 * 
	 * @return the number of different StrictPreference in the map.
	 */
	public int getNbUniquePreferences() {
		Log.info("getnbUniquePreference\n");
		Iterator<StrictPreference> iterator = getUniquePreferences().iterator();
		int size = 0;
		while(iterator.hasNext()) {
			size++;
			iterator.next();
		}
		return size;
	}
	
	/**
	 * 
	 * @return the number of different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public int getNbAlternativesComplete() {
		Log.info("getNbAlternativesComplete\n");
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator=association.values().iterator();
		return iterator.next().size();
	}

	/**
	 * 
	 * @return all the different alternatives in a complete profile. A profile is complete when all votes are about the same alternatives exactly.
	 */
	public Iterable<Alternative> getAlternativesComplete(){
		Log.info("getAlternativesComplete\n");
		if(!isComplete()) {
			throw new IllegalArgumentException("The profile is not complete.");
		}
		Iterator<StrictPreference> iterator=association.values().iterator();
		return iterator.next().getPreferences();
	}
	/**
	 * 
	 * @return whether the profile is complete : all votes in the profile are about the same alternatives
	 */
	public boolean isComplete() {
		Log.info("isComplete\n");
		Iterator<StrictPreference> iterator = association.values().iterator();
		StrictPreference pref = iterator.next();
		Log.debug("first preferences :{}\n",pref);
		while(iterator.hasNext()) {
			StrictPreference pref2=iterator.next();
			Log.debug("next preferences : {}\n",pref2);
			if(!pref.hasSameAlternatives(pref2)) {
				Log.debug("Not the same alternatives\n");
				return false;
			}
		}
		Log.debug("Profile is complete.\n");
		return true;
	}

	/**
	 * 
	 * @param preferences
	 * @return how many voters voted for the StrictPreference.
	 */
	public int getNbVoterByPreference(StrictPreference preferences) {
		Log.info("getNbVoterByPreference\n");
		Objects.requireNonNull(preferences);
		Log.debug("parameter preferences : {}\n",preferences);
		int nbVotes = 0;
		Iterator<Voter> votersIterator = getAllVoters().iterator();
		while(votersIterator.hasNext()) {
			Voter v=votersIterator.next();
			Log.debug("Voter with id = {} voted for : {}\n",v.getId(),association.get(v));
			if(association.get(v).equals(preferences)) {
				nbVotes++;
			}
		}
		Log.debug("number of votes for {} is {}\n",preferences,nbVotes);
		return nbVotes;
	}
	
	
	/**
	 * 
	 * @return a complete Strict Profile into a string in SOC format
	 */
	public String toSOC() {
		Log.info("toSOC\n");
		String soc="";
		soc += getNbAlternativesComplete() + "\n";
		Iterator<Alternative> i1=getAlternativesComplete().iterator();
		while(i1.hasNext()) {
			soc += i1.next().getId() + "\n";
		}
		soc += getNbVoters() + ", "+getSumVoteCount() + ", " + getNbUniquePreferences()+"\n";
		Iterator<StrictPreference> i2 = getUniquePreferences().iterator();
		while(i2.hasNext()) {
			StrictPreference pref = i2.next();
			soc += getNbVoterByPreference(pref) + pref.toString() + "\n";
		}
		return soc;
	}
}
