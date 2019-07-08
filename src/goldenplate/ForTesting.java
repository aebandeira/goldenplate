package goldenplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ForTesting {
	public static void main(String[] args) {
		String dateTime = "12/11/2019 17:30";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime fromDateTime = LocalDateTime.parse(dateTime, formatter);

        for (LocalDateTime date = fromDateTime; date.isBefore(fromDateTime.plusMinutes(5)); date = date.plusMinutes(1))
        	System.out.println("Parsed Date : " + date.format(formatter));
		
	}
}
