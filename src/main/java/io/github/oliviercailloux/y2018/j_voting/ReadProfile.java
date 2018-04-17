package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;
import org.slf4j.*;


public class ReadProfile {
	
	static Logger Log = LoggerFactory.getLogger(ReadProfile.class.getName());
	/**
	 * @param path a string : the path of the file to read 
	 * @return fileRead, a list of String where each element is a line of the SOC or SOI file read
	 *
	 */
	public static List<String> fromSOCorSOI(String path) throws IOException {
		Log.info("fromSOCorSOI : \n") ;
		Log.debug("parameter : path = {}\n",path);
		InputStream f= ReadProfile.class.getClassLoader().getResourceAsStream(path);
		try(BufferedReader in = new BufferedReader(new InputStreamReader(f))){
			String line;
			List<String> fileRead = new ArrayList<String>();
			while ((line= in.readLine()) != null) {
				Log.debug("next line : {}\n",line);
				fileRead.add(line);
			}
		    return fileRead;
	    }
	}
	
	/**
	 * @param fileRead a list of strings : data which was read in a SOC/SOI file
	 * This function prints strings from the list passed as an argument
	 * 
	 */
	public static void displayProfileFromReadFile(List<String> fileRead){
		Log.info("displayProfileFromReadFile : \n") ;
		Log.debug("parameter : fileRead = {}\n",fileRead);
		Iterator<String> it = fileRead.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
	
	public static StrictPreference GetAlternatives(int nbAlternatives,List<String> file){
		Log.info("GetAlternatives :") ;
		Log.debug("parameters : nbAlternatives = {}, file = {}\n",nbAlternatives,file); 
		Iterator<String> it = file.iterator();
		String s1; 
		LinkedHashSet<Alternative> alternatives= new LinkedHashSet<Alternative>();
		for(int k=1;k<=nbAlternatives;k++){//we add each alternative to a list
			s1=it.next();
			Log.debug("next Alternative : {}\n",s1);
			if (s1.contains(",")){//line with alternative doesn't contain ,
				throw new Error("Error: nbAlternative is not correct");
			}
			alternatives.add(new Alternative(Integer.parseInt(s1)));
		}
		StrictPreference listAlternatives = new StrictPreference(alternatives);
		Log.debug("returns listAlternatives : {}\n",listAlternatives);
		return listAlternatives;
	}
	
	
	
	public static List<Integer> GetNbVoters(String s){
		Log.info("GetNbVoters :") ;
		Log.debug("parameter : s={}\n",s);
		List<Integer> list=new ArrayList<Integer>();
		String[] line=s.split(",");
		list.add(Integer.parseInt(line[0].trim()));
		list.add(Integer.parseInt(line[1].trim()));
		list.add(Integer.parseInt(line[2].trim()));
		Log.debug("returns list : {}\n",list);
		return list;
	}
	
	public static StrictPreference GetPreferences(StrictPreference listeAlternatives, String s1){
		String [] s2=s1.split(",");
		LinkedHashSet<Alternative> pref= new LinkedHashSet<Alternative>();
		size=Integer.parseInt(s2[0].trim());
		for(int j=1;j<s2.length;j++){//we collect all the alternatives
			Iterator<Alternative> i1=listeAlternatives.getPreferences().iterator();
			Alternative a=i1.next();
			boolean alternative_ok=false;
			while(i1.hasNext()&&!alternative_ok){
				if(a.getId()==(Integer.parseInt(s2[j].trim()))){
					alternative_ok=true;
				}
				else{
					a=i1.next();
				}
			}
			pref.add(a);
		}
		return new StrictPreference(pref);
	}
	
	
	
	private static int size =0;
	private static int id =1;// id is the id for the voters we will create
	
	public static StrictProfile CreateProfiles(StrictPreference profil, int nbVoters){
		StrictProfile SProfile = new StrictProfile();
		for(int m=0;m<size;m++){//we create as many profiles as voters 
			Voter v =new Voter(id);
			id++;
			if (id-1>nbVoters){//we can't create more voters than the number of voters
				throw new Error("Error: i>nbVoters");
			}

			System.out.println("Voter"+v.getId()+"pref"+profil);
			SProfile.addProfile(v,profil);
		}
		return SProfile;
	}

	
	public static StrictProfile GetProfiles(List<String> file, StrictPreference listeAlternatives, int nbVoters){
		Log.info("GetProfiles :") ;
		Iterator<String> it = file.iterator();
		StrictProfile SProfile = new StrictProfile();
		String s1; //where we store the current line
		while(it.hasNext()){
			s1=it.next();
			if (!s1.contains(",")){// if the line doesn't contain , it's the line of an alternative
				throw new Error("Error: nbAlternative is not correct");
			}
			else{
				StrictPreference profil=GetPreferences(listeAlternatives,s1);
				SProfile =CreateProfiles(profil, nbVoters);
			}

		}
		return SProfile;

	}
	

	/**
	 * Creates a StrictProfile with the information of the fileRead List<String> extracted from a file.
	 * @param fileRead
	 * @return 
	 */

	public static StrictProfile createProfileFromReadFile(List<String> fileRead){
		Log.info("createProfileFromReadFile : \n") ;
		Iterator<String> it = fileRead.iterator();
		StrictProfile SProfile= new StrictProfile();
		String line_nb_voters;
		int nbAlternatives=Integer.parseInt(it.next());	//first number of the file is the number of alternative
		List<String> Alternatives=new ArrayList<String>();
		List<String> Profiles=new ArrayList<String>();
		for(int i=1;i<=nbAlternatives;i++){//get the lines with the alternatives
			Alternatives.add(it.next());
		}
		line_nb_voters=it.next();//get the line with the nb of voters
		while(it.hasNext()){//get the rest of the file
			Profiles.add(it.next());
		}
		StrictPreference listeAlternatives =GetAlternatives(nbAlternatives,Alternatives);
		List<Integer> list_int= GetNbVoters(line_nb_voters);
		SProfile=GetProfiles(Profiles,listeAlternatives, list_int.get(0));
		return SProfile;
	}
	
	
	
	
	/**
	 * This function calls fromSOCorSOI function if there is a SOC or a SOI file in the FILES directory
	 * 
	 **/
	public static void main(String[] args) throws IOException {
		List<String> socToRead = fromSOCorSOI("io/github/oliviercailloux/y2018/j_voting/profil.soc"); 
		@SuppressWarnings("unused")
		StrictProfile SProfile = createProfileFromReadFile(socToRead);
		
	
		/*// read SOC file
		List<String> socToRead = fromSOCorSOI("io/github/oliviercailloux/y2018/j_voting/profil.soc"); 
		System.out.println("SOC file :");
		displayProfileFromReadFile(socToRead);
		
		// read SOI file
		List<String> soiToRead = fromSOCorSOI("io/github/oliviercailloux/y2018/j_voting/profil.soi"); 
		System.out.println("SOI file :");
		displayProfileFromReadFile(soiToRead);*/

	}
}
