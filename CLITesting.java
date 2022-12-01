import java.io.IOException;
import java.util.Scanner;

public class CLITesting {
    public static void main(String[] args) throws IOException {
        Viterbi v = POSTagging.trainModelFromFiles("PS5/brown-train-sentences.txt", "PS5/brown-train-tags.txt");

        String sentence = "-";
        System.out.println("Input a sentence, or empty line to exit");
        Scanner s = new Scanner(System.in);
        // while not empty line, guess line and output tags
        while (!sentence.isEmpty()) {
            sentence = s.nextLine();
            String[] guess = POSTagging.guessTagsFromTrainedModel(v, sentence);
            for (String part : guess) {
                System.out.print(part + " ");
            }
            System.out.println();
        }

    }
}
