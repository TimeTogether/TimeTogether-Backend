package timetogether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class TimetogetherApplication {

	public static void main(String[] args) {
		InetAddress local;
		try {
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			System.out.println("local ip : "+ip);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		SpringApplication.run(TimetogetherApplication.class, args);

	}

}