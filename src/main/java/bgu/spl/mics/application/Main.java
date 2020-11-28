package bgu.spl.mics.application;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {

//		class A{};
//		class B extends A{};
//		class C extends B{};
//
//		LinkedList<A> aLinkedList = new LinkedList<>();
//		LinkedList<? extends A> =


		StarWarsParser starWarsParser = getStarWarsParser(args[0]);
		System.out.println(starWarsParser.R2D2);
		System.out.println(starWarsParser.Lando);
		System.out.println(starWarsParser.Ewoks);
		System.out.println(starWarsParser.attacks[0].getDuration());
		System.out.println(starWarsParser.attacks[0].getSerials());
		System.out.println(starWarsParser.attacks[1].getDuration());
		System.out.println(starWarsParser.attacks[1].getSerials());
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
