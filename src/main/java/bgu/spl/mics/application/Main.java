package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
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
	public static void main(String[] args) {
		StarWarsParser starWarsParser = getStarWarsParser("input2.json");
		initPassiveObjects(starWarsParser);
		List<Thread> threadsList = prepareThreads(starWarsParser);
		startAllTheThreads(threadsList);
		completeThreads(threadsList);
		gsonOutput("output.json");
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
		threadsList.add(new Thread(new LeiaMicroservice(starWarsParser.getAttacks(), starWarsParser.getR2D2(), starWarsParser.getLando())));
		threadsList.add(new Thread(new C3POMicroservice()));
		threadsList.add(new Thread(new HanSoloMicroservice()));
		threadsList.add(new Thread(new R2D2Microservice()));
		threadsList.add(new Thread(new LandoMicroservice()));
		return threadsList;
	}

	/**
	 * Main responsibility to first init passive objects
	 */
	private static void initPassiveObjects(StarWarsParser starWarsParser) {
		Ewoks.getInstance().initialize(starWarsParser.getEwoks()); // Main responsibility to first init Ewoks
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
