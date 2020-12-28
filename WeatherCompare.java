import java.net.*;
import java.io.*;
import java.time.*;
import java.util.*;
public class WeatherCompare {
	private static String readData(URL url, String cityName) throws IOException {
		String high = "";
		String low = "";
		try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))){
			String input;
			boolean hasReadHigh = false;
			boolean hasReadLow = false;
			while ((input = in.readLine()) != null) {
				if (input.indexOf("High:") != -1 && !hasReadHigh) {
					high = input.substring(input.indexOf("&deg") - 3, input.indexOf("&deg"));
					high = (high.substring(0, 1).equals(">")) ? high.substring(1,3) : high;
					high = cityName + " High is: " + high;
					hasReadHigh = true;
				}
				if (input.indexOf("Low:") != -1 && !hasReadLow) {
					low = cityName + " Low is: " + input.substring(input.indexOf("&deg") - 2, input.indexOf("&deg"));
					hasReadLow = true;
				}
			}
		}
		return high + "\n" + low;
	}
	private static String calculateAvg() throws IOException{
		ArrayList<Integer> highValues = new ArrayList<>();
		ArrayList<Integer> lowValues = new ArrayList<>();
		int SRH = -100;
		int SRL = -100;
		int LAH = -100;
		int LAL = -100;
		int counter = 0;
		File file = new File("C:\\Users\\Jason Wu\\Desktop\\WeatherData.txt");
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String input;
			while ((input = in.readLine()) != null) {
				counter++;
				if (input.indexOf("San Ramon High is:") != -1) {
					SRH = Integer.parseInt(input.substring(19));
				}
				else if (input.indexOf("LA High is:") != -1) {
					LAH = Integer.parseInt(input.substring(12));
				}
				else if (input.indexOf("San Ramon Low is:") != -1) {
					SRL = Integer.parseInt(input.substring(18));
				}
				else if (input.indexOf("LA Low is:") != -1) {
					LAL = Integer.parseInt(input.substring(11));
				}
				if (SRH != -100 && LAH != -100 && SRL != -100 && LAL != -100) {
					highValues.add(SRH - LAH);
					lowValues.add(SRL - LAL);
					SRH = -100;
					SRL = -100;
					LAH = -100;
					LAL = -100;
				}
			}
		}
		file.delete();
		if (highValues.size() == 0) {
			return "";
		}
		double highSum = 0;
		double lowSum = 0;
		for (Integer val : highValues) {
			highSum += val;
		}
		for (Integer val : lowValues) {
			lowSum += val;
		}
		return "Average of high diff is " + highSum / highValues.size() + " and low diff is " + lowSum / lowValues.size();
	}
	public static void main(String[] args) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Jason Wu\\Desktop\\WeatherData.txt", true));
		URL urlSanRamon = new URL("https://patch.com/california/sanramon/weather");
		URL urlLA = new URL("https://patch.com/california/los-angeles/weather");
		LocalDateTime date = LocalDateTime.now();
		writer.write("\nToday's date is: " + date.toString() + "\n");
		writer.write(readData(urlSanRamon, "San Ramon") + "\n");
		writer.write(readData(urlLA, "LA")+ "\n");
		writer.write(calculateAvg() + "\n");
		writer.close();
	}
}
