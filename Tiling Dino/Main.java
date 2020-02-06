import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {

	public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("test1.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			    lines.add(line);
            }        

            TilingDino td = new TilingDino();
			List<String> result = td.tileImage(lines);
            for (String line : result)
                System.out.println(line);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error occurred when reading file");
		}
	}

}

