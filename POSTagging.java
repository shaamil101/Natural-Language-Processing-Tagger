import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class POSTagging {
    /**
     * Trains hidden Markov model and returns Viterbi model from it
     * @param sentencesFilePath filepath for sentences
     * @param tagsFilePath filepath for tags
     * @return trained Viterbi model
     * @throws IOException
     */
    public static Viterbi trainModelFromFiles(String sentencesFilePath, String tagsFilePath) throws IOException {
        BufferedReader sentIn = new BufferedReader(new FileReader(sentencesFilePath));
        BufferedReader tagsIn = new BufferedReader(new FileReader(tagsFilePath));

        HiddenMarkovModel hmm = new HiddenMarkovModel();
        hmm.trainFromFile(tagsIn, sentIn);

        Viterbi v = new Viterbi(hmm);

        sentIn.close();
        tagsIn.close();

        return v;
    }

    /**
     * Returns string array of tags from a sentence
     * @param v trained Viterbi
     * @param sentence sentence to try to guess
     * @return array of tags
     */
    public static String[] guessTagsFromTrainedModel(Viterbi v, String sentence) {
        String[] guess = v.guessTags(sentence.split(" "));
        return guess;
    }

    /**
     * calculate number of tags guessed by the model
     * @param v trained Viterbi
     * @param sentence sentence to try
     * @param tags ground truth
     * @return number of correct tags
     */
    public static int testSentence(Viterbi v, String sentence, String tags) {
        String[] sentParts = sentence.split(" ");
        String[] guess = v.guessTags(sentParts);
        String[] groundTruth = tags.split(" ");

        int right = 0;

        for (int i = 0; i < guess.length; i++) {
            if (guess[i].equals(groundTruth[i])) right++;
        }


        return right;
    }

    /**
     * Prints correct and total amount of tags from a pair of files
     * @param v trained Viterbi
     * @param sentenceFilePath filepath for sentences to test
     * @param tagFilePath filepath for tags to confirm
     * @throws IOException
     */
    public static void testFiles(Viterbi v, String sentenceFilePath, String tagFilePath) throws IOException {

        BufferedReader sentIn = new BufferedReader(new FileReader(sentenceFilePath));
        BufferedReader tagsIn = new BufferedReader(new FileReader(tagFilePath));

        int total = 0, right = 0;
        String sent, tags;
        while ((sent = sentIn.readLine()) != null) {
            tags = tagsIn.readLine();
            total += tags.split(" ").length;

            right += testSentence(v, sent, tags);
        }

        System.out.printf("Got %d/%d right\n", right, total);

        sentIn.close();
        tagsIn.close();
    }
    public static void main(String[] args) throws IOException {
        try {
            // Hard-coded test
            Viterbi v = trainModelFromFiles("PS5/simple-train-sentences.txt", "PS5/simple-train-tags.txt");
            String[] guess = guessTagsFromTrainedModel(v, "my dog barks at night");
            for (String part : guess)
                System.out.print(part + " ");
            System.out.println("\n");

            guess = guessTagsFromTrainedModel(v, "the dog is fast");
            for (String part : guess)
                System.out.print(part + " ");
            System.out.println("\n");

            // Test full set
            Viterbi v2 = trainModelFromFiles("PS5/brown-train-sentences.txt", "PS5/brown-train-tags.txt");

            testFiles(v2, "PS5/brown-test-sentences.txt", "PS5/brown-test-tags.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
