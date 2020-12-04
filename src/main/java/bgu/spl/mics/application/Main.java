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
		StarWarsParser starWarsParser = getStarWarsParser(args[0]);
		Ewoks.getFirstInstance(starWarsParser.getEwoks());
		MicroService Leia = new LeiaMicroservice(starWarsParser.getAttacks(), starWarsParser.getR2D2(), starWarsParser.getLando(), attackLatch, deactivateLatch, destroyLatch);
		MicroService C3PO = new C3POMicroservice(attackLatch);
		MicroService HanSolo = new HanSoloMicroservice(attackLatch);
		MicroService R2D2 = new R2D2Microservice(deactivateLatch);
		MicroService Lando = new LandoMicroservice(destroyLatch);
		List<Thread> threadsList = new ArrayList<>();
		threadsList.add(new Thread(Leia));
		threadsList.add(new Thread(C3PO));
		threadsList.add(new Thread(HanSolo));
		threadsList.add(new Thread(R2D2));
		threadsList.add(new Thread(Lando));
		for (Thread thread: threadsList) {
			thread.start();
			System.out.println(thread.getName() + " Thread Started");
		}
		for (Thread thread: threadsList)
			try { thread.join(); }
		catch (InterruptedException ignore) {
			System.out.println("Main thread is mother fucker");
		}
		Diary.getInstance().output();


//
//		System.out.println(starWarsParser.R2D2);
//		System.out.println(starWarsParser.Lando);
//		System.out.println(starWarsParser.Ewoks);
//		System.out.println(starWarsParser.attacks[0].getDuration());
//		System.out.println(starWarsParser.attacks[0].getSerials());
//		System.out.println(starWarsParser.attacks[1].getDuration());
//		System.out.println(starWarsParser.attacks[1].getSerials());
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
