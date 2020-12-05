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
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {

//		List<Integer> list = new LinkedList<>();
//		list.add(5); list.add(2); list.add(9);
//		Collections.sort(list);
//		for (Integer inty : list){
//			System.out.println(inty);
//		}
//
			CountDownLatch attackLatch = new CountDownLatch(2);
			CountDownLatch deactivateLatch = new CountDownLatch(1);
			CountDownLatch destroyLatch = new CountDownLatch(1);
			StarWarsParser starWarsParser = getStarWarsParser("C:\\Users\\Danro\\Desktop\\SPL-2\\src\\main\\java\\input2.json");
			Ewoks.getFirstInstance(starWarsParser.getEwoks());
			MicroService Leia = new LeiaMicroservice(starWarsParser.getAttacks(), starWarsParser.getR2D2(), starWarsParser.getLando(), attackLatch, deactivateLatch, destroyLatch);
			MicroService C3PO = new C3POMicroservice(attackLatch);
			MicroService HanSolo = new HanSoloMicroservice(attackLatch);
			MicroService R2D2 = new R2D2Microservice(deactivateLatch);
			MicroService Lando = new LandoMicroservice(destroyLatch);
			List<Thread> threadsList = new ArrayList<>();
			Thread liea_ = new Thread(Leia);
			liea_.setName("Liea");
			Thread C3PO_ = new Thread(C3PO);
			C3PO_.setName("C3PO");
			Thread HanSolo_ = new Thread(HanSolo);
			Thread R2D2_ = new Thread(R2D2);
			Thread Lando_ = new Thread(Lando);
			HanSolo_.setName("HanSolo");
			R2D2_.setName("R2D2");
			Lando_.setName("Lando");

			threadsList.add(liea_);
			threadsList.add(C3PO_);
			threadsList.add(HanSolo_);
			threadsList.add(R2D2_);
			threadsList.add(Lando_);
			System.out.println("---- STARTING THE PROGRAM ----");
			Diary.getInstance();
			for (Thread thread : threadsList) {
				thread.start();
				System.out.println("Starting: " + thread.getName() + " Thread Started");
			}
			for (Thread thread : threadsList)
				try {
					thread.join();
				} catch (InterruptedException ignore) {
					System.out.println("---- Main Thread Problem: Got Interrupted Exeption 'ignore' ----");
				}
			// System.out.println("******* MIS NITAY *******");

			Diary.getInstance().output();

			gsonOutput("C:\\Users\\Danro\\Desktop\\SPL-2\\src\\main\\java\\output.json");
		System.out.println("**** FINISHED TEST WITH NITAY BLESSING");
		Ewoks.reset();
		}

//
//		System.out.println(starWarsParser.R2D2);
//		System.out.println(starWarsParser.Lando);
//		System.out.println(starWarsParser.Ewoks);
//		System.out.println(starWarsParser.attacks[0].getDuration());
//		System.out.println(starWarsParser.attacks[0].getSerials());
//		System.out.println(starWarsParser.attacks[1].getDuration());
//		System.out.println(starWarsParser.attacks[1].getSerials());

	private static void gsonOutput(final String path) {
		Output output = new Output();
		Gson gson = new Gson();
		//Writer writer = Files.newBufferedWriter(Paths.get(path));
		try	(Writer writer = Files.newBufferedWriter(Paths.get(path));){
			 gson.toJson(output,writer);
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
