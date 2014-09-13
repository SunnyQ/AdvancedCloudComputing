import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;

/**
 * Created by Wei on 9/13/14.
 */


public class JsonParser {
	public static void main(String[] args) throws IOException {
		File file = new File("/Users/parasitew/Documents/DataSet/data.json");
		com.fasterxml.jackson.core.JsonParser jp;

		JsonFactory jf = new JsonFactory();

		jp = jf.createParser(file);
		jp.nextToken(); // Will return JsonToken.START_OBJECT.

		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jp.getCurrentName();
			System.out.println("fieldname:   \t" + fieldname);
			jp.nextToken();

			if ("content".equals(fieldname)) {
				System.out.println(jp.getCurrentToken());
			} else if ("content-type".equals(fieldname)){
				System.out.println("\t" + jp.getCurrentName());
				jp.nextToken(); // Skip END_OBJECT of http_headers
			} else if ("links".equals(fieldname)) {
				if (jp.nextToken() == JsonToken.END_ARRAY) {
					System.out.println("links is empty");
					System.out.println("this is a dangling node");
				} else {
					int cnt = 1;
					do {
						if ("href".equals(jp.getCurrentName())) {
							jp.nextToken();
							String link = jp.getText();
							System.out.println("link " + cnt + ": " + link);
							cnt++;
						}
					} while (jp.nextToken() != JsonToken.END_ARRAY);
				}
			}

		}


	}

}
