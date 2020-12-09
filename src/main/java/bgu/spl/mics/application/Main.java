package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args)  {
		int Number_Of_Ewoks = 10;
		initPassiveObjects(Number_Of_Ewoks);

		List<Integer> serials_1 = new ArrayList<>(); // {1,4,2,7}
		List<Integer> serials_2 = new ArrayList<>(); // {3,8,10,2,1,6}
		List<Integer> serials_3 = new ArrayList<>(); // {4,1}
		List<Integer> serials_4 = new ArrayList<>(); // {7,8,9}
		List<Integer> serials_5 = new ArrayList<>(); // {5}
		List<Integer> serials_6 = new ArrayList<>(); // {1,4,2,7}
		List<Integer> serials_7 = new ArrayList<>(); // {3,8,10,2,1,6}
		List<Integer> serials_8 = new ArrayList<>(); // {4,1}
		List<Integer> serials_9 = new ArrayList<>(); // {7,8,9}
		List<Integer> serials_10 = new ArrayList<>(); // {5}
		List<Integer> serials_11 = new ArrayList<>(); // {1,4,2,7}
		List<Integer> serials_12 = new ArrayList<>(); // {3,8,10,2,1,6}
		List<Integer> serials_13 = new ArrayList<>(); // {4,1}
		List<Integer> serials_14 = new ArrayList<>(); // {7,8,9}
		List<Integer> serials_15 = new ArrayList<>(); // {5}

		serials_1.add(1); serials_1.add(4); serials_1.add(2); serials_1.add(7);
		serials_2.add(3); serials_2.add(8); serials_2.add(10); serials_2.add(2); serials_2.add(1); serials_2.add(6);
		serials_3.add(4); serials_3.add(1);
		serials_4.add(7); serials_4.add(8); serials_4.add(9);
		serials_5.add(5);
		serials_6.add(1); serials_6.add(4); serials_6.add(2); serials_6.add(7);
		serials_7.add(3); serials_7.add(8); serials_7.add(10); serials_7.add(2); serials_7.add(1); serials_7.add(6);
		serials_8.add(4); serials_8.add(1);
		serials_9.add(7); serials_9.add(8); serials_9.add(9);
		serials_10.add(5);
		serials_11.add(1); serials_11.add(4); serials_11.add(2); serials_11.add(7);
		serials_12.add(3); serials_12.add(8); serials_12.add(10); serials_12.add(2); serials_12.add(1); serials_12.add(6);
		serials_13.add(4); serials_13.add(1);
		serials_14.add(7); serials_14.add(8); serials_14.add(9);
		serials_15.add(5);

		int attack_duration_1 = 50;
		int attack_duration_2 = 150;
		int attack_duration_3 = 100;

		Attack attack1 = new Attack(serials_1, attack_duration_1); Attack attack9 = new Attack(serials_6, attack_duration_2);
		Attack attack2 = new Attack(serials_11, attack_duration_3); Attack attack10 = new Attack(serials_2, attack_duration_1);
		Attack attack3 = new Attack(serials_7, attack_duration_2); Attack attack11 = new Attack(serials_12, attack_duration_3);
		Attack attack4 = new Attack(serials_3, attack_duration_1); Attack attack12 = new Attack(serials_8, attack_duration_2);
		Attack attack5 = new Attack(serials_13, attack_duration_3); Attack attack13 = new Attack(serials_4, attack_duration_1);
		Attack attack6 = new Attack(serials_9, attack_duration_2); Attack attack14 = new Attack(serials_14, attack_duration_3);
		Attack attack7 = new Attack(serials_5, attack_duration_1); Attack attack15 = new Attack(serials_10, attack_duration_2);
		Attack attack8 = new Attack(serials_15, attack_duration_3);

		Attack[] Leia_1_attacks = {attack1,attack8,attack3,attack4,attack9,attack6,attack13,attack11};
		Attack[] Leia_2_attacks = {attack12, attack5, attack15,attack7,attack14};
		Attack[] Leia_3_attacks = {attack10,attack2};

		long R2D2_Deactivate_Duration = 300;
		long Lando_Destroy_Duration = 500;

		List<Thread> threadsList = new ArrayList<>();
		threadsList.add(new Thread(new LeiaMicroservice(Leia_1_attacks)));
		threadsList.add(new Thread(new LeiaMicroservice(Leia_2_attacks)));
		threadsList.add(new Thread(new LeiaMicroservice(Leia_3_attacks)));
		threadsList.add(new Thread(new C3POMicroservice()));
		threadsList.add(new Thread(new C3POMicroservice()));
		threadsList.add(new Thread(new C3POMicroservice()));
		threadsList.add(new Thread(new HanSoloMicroservice()));
		threadsList.add(new Thread(new HanSoloMicroservice()));
		threadsList.add(new Thread(new HanSoloMicroservice()));
		threadsList.add(new Thread(new R2D2Microservice(R2D2_Deactivate_Duration)));
		threadsList.add(new Thread(new LandoMicroservice(Lando_Destroy_Duration)));
		threadsList.add(new Thread(new TerminateMicroservice()));
		startAllTheThreads(threadsList);
		completeThreads(threadsList);
		gsonOutput("PappyPappy.json");

//		if (args.length >= 2) {
//			StarWarsParser starWarsParser = getStarWarsParser(args[0]);
//			initPassiveObjects(starWarsParser);
//			List<Thread> threadsList = prepareThreads(starWarsParser);
//			startAllTheThreads(threadsList);
//			completeThreads(threadsList);
//			gsonOutput(args[1]);
//		}
//		else
//			System.out.println("Please provide paths for input and output files.");
	}

	/**
	 * main waiting for all threads in threadList
	 */
	private static void completeThreads(List<Thread> threadsList) {
		for (Thread thread : threadsList) {
			try {
				thread.join();
			} catch (InterruptedException ignore) {
			}
		}
	}

	/**
	 * starting all the threads in the threadList
	 */
	private static void startAllTheThreads(List<Thread> threadsList) {
		for (Thread thread : threadsList) {
			thread.start();
		}
	}

	/**
	 * initializing all the threads in our application
	 * @param starWarsParser = the data object of the input json file
	 * @return list of the threads
	 */
	private static List<Thread> prepareThreads(StarWarsParser starWarsParser) {
		List<Thread> threadsList = new ArrayList<>();
		threadsList.add(new Thread(new LeiaMicroservice(starWarsParser.getAttacks())));
		threadsList.add(new Thread(new C3POMicroservice()));
		threadsList.add(new Thread(new HanSoloMicroservice()));
		threadsList.add(new Thread(new R2D2Microservice(starWarsParser.getR2D2())));
		threadsList.add(new Thread(new LandoMicroservice(starWarsParser.getLando())));
		return threadsList;
	}

	/**
	 * Main responsibility to first init passive objects
	 */
	private static void initPassiveObjects(int Number_Of_Ewoks) {
		Ewoks.getInstance().initialize(Number_Of_Ewoks); // Main responsibility to first init Ewoks
		LatchSingleton.initialize(); // Main responsibility to first init Ewoks
	}

	private static void gsonOutput(final String path) {
		Gson gson = new Gson();
		try	(Writer writer = Files.newBufferedWriter(Paths.get(path));){
			 gson.toJson(Diary.getInstance(),writer);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static StarWarsParser getStarWarsParser(final String path) {
		Gson gson = new Gson();
		try	(Reader reader = new FileReader(path)){
			return gson.fromJson(reader, StarWarsParser.class);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
