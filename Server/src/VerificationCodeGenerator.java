import java.util.Random;

public class VerificationCodeGenerator{
	public static String generateVerificationCode(){
		Random random = new Random();
		int a = random.nextInt(9);
		int b = random.nextInt(9);
		int c = random.nextInt(9);
		int d = random.nextInt(9);
		return a + "" + b + "" + c + "" + d;
	}
}
