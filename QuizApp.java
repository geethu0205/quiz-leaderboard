import java.io.*;
import java.net.*;
import java.util.*;

public class QuizApp {

    static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    static final String REG_NO = "RA76"; 

    public static void main(String[] args) throws Exception {

        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            String urlStr = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + i;
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String line;
            StringBuilder content = new StringBuilder();

            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();

            String response = content.toString();

        
            String[] blocks = response.split("\\{");

            for (String block : blocks) {

                if (block.contains("roundId") &&
                        block.contains("participant") &&
                        block.contains("score")) {

                    String roundId = extract(block, "roundId");
                    String participant = extract(block, "participant");
                    String scoreStr = extractScore(block);

                    if (roundId.isEmpty() || participant.isEmpty() || scoreStr.isEmpty()) {
                        continue;
                    }

                    int score = Integer.parseInt(scoreStr);

                    String key = roundId + "_" + participant;

                    if (!seen.contains(key)) {
                        seen.add(key);
                        scores.put(participant,
                                scores.getOrDefault(participant, 0) + score);
                    }
                }
            }

            System.out.println("Poll " + i + " done...");
            Thread.sleep(5000);
        }

        
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        int total = 0;

        System.out.println("\n===== FINAL LEADERBOARD =====");
        for (Map.Entry<String, Integer> e : list) {
            System.out.println(e.getKey() + " -> " + e.getValue());
            total += e.getValue();
        }

        System.out.println("Total Score: " + total);
    }

    
    static String extract(String text, String key) {
        try {
            int start = text.indexOf(key) + key.length() + 3;
            int end = text.indexOf("\"", start);
            return text.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    
    static String extractScore(String text) {
        try {
            int start = text.indexOf("score") + 8;
            StringBuilder number = new StringBuilder();

            while (start < text.length()) {
                char c = text.charAt(start);
                if (Character.isDigit(c)) {
                    number.append(c);
                } else {
                    break;
                }
                start++;
            }

            return number.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
