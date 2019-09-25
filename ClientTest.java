import javax.swing.JFrame;
import java.util.*;

public class ClientTest {

	public static void main(String[] args) {
		Client nukem;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Name. ");
		String name = sc.nextLine();
		nukem = new Client("127.0.0.1", name);
		nukem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nukem.startRun();

	}

}
