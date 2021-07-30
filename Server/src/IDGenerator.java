import java.util.UUID;

public class IDGenerator {
	
	public static String generateID(){
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] a){
		System.out.println(generateID());
		System.out.println(generateID());
		System.out.println(generateID());
		System.out.println(generateID());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(generateID());
		System.out.println(generateID());
		System.out.println(generateID());
		System.out.println(generateID());
		System.out.println(generateID().length());
	}
}
