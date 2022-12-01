import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HiddenMarkovModel {
    Map<String, Map<String, Double>> tagToTransitionFrequency;
    Map<String, Map<String, Double>> tagToObservationFrequency;
    Map<String, Double> tagFrequency;

    public HiddenMarkovModel() {
        tagToObservationFrequency = new HashMap<>();
        tagToTransitionFrequency = new HashMap<>();
        tagFrequency = new HashMap<>();
    }

    /**
     * this method is intended to read from the tagFile and observationFile to perform Viterbi decoding.
     * We also added a '#' character to the start of every sentence to help the parser understand where the sentences begin.
     * The maps will store the observations along with the next state, current states and scores amongst other things.
     *
     * @param tagFile BufferedReader file containing all the tags given to us to train the HMM
     * @param observationFile BufferedReader file containing all the observations given to us to train the HMM
     * @throws IOException
     */

    public void trainFromFile(BufferedReader tagFile, BufferedReader observationFile) throws IOException {

            Map<String, Integer> tagCount = new HashMap<>();
            Map<String, Map<String, Integer>> tagToTransitionCount = new HashMap<>();
            Map<String, Map<String, Integer>> tagToObservationCount = new HashMap<>();

            Map<String, Integer> tagTransitionCount = new HashMap<>();
            int totalTags = 0;

            String tagLine, observationLine;
            //reading from tagFile and observationFile through the BufferedReader
            while ((tagLine = tagFile.readLine()) != null) {
                observationLine = "# " + observationFile.readLine().toLowerCase();
                tagLine = "# " + tagLine;

                //breaking up the tagFile and observationFile into a string array
                String[] exampleTags = tagLine.split(" ");
                String[] exampleObservations = observationLine.split(" ");

                totalTags += exampleTags.length;

                for (int i = 0; i < exampleTags.length; i++) {
                    // create a new key value pair in the tagtoobservation count map if tagCount doesn't contain the tag
                    if (!tagCount.containsKey(exampleTags[i])) {
                        tagCount.put(exampleTags[i], 1);
                        tagToObservationCount.put(exampleTags[i], new HashMap<>());
                        tagToObservationCount.get(exampleTags[i]).put(exampleObservations[i], 1);
                        tagToTransitionCount.put(exampleTags[i], new HashMap<>());
                        tagTransitionCount.put(exampleTags[i], 0);
                    } else {
                        tagCount.put(exampleTags[i], tagCount.get(exampleTags[i]) + 1);
                        if (tagToObservationCount.get(exampleTags[i]).containsKey(exampleObservations[i])) {
                            tagToObservationCount.get(exampleTags[i]).put(exampleObservations[i], tagToObservationCount.get(exampleTags[i]).get(exampleObservations[i]) + 1);
                        } else {
                            tagToObservationCount.get(exampleTags[i]).put(exampleObservations[i], 1);
                        }
                    }

                    if (i < exampleTags.length - 1) {
                        if (tagToTransitionCount.get(exampleTags[i]).containsKey(exampleTags[i + 1])) {
                            tagToTransitionCount.get(exampleTags[i]).put(exampleTags[i + 1], tagToTransitionCount.get(exampleTags[i]).get(exampleTags[i + 1]) + 1);
                        } else {
                            tagToTransitionCount.get(exampleTags[i]).put(exampleTags[i + 1], 1);
                        }

                        tagTransitionCount.put(exampleTags[i], tagTransitionCount.get(exampleTags[i]) + 1);
                    }
                }
            }

            //  for every tag, calculate the tag to observation and tag to transition frequencies from the total count
            // log is applied to avoid decimal precision problems
            for (String tag : tagCount.keySet()) {
                tagFrequency.put(tag, Math.log((double) tagCount.get(tag) / totalTags));
                tagToObservationFrequency.put(tag, new HashMap<>());
                tagToTransitionFrequency.put(tag, new HashMap<>());

                for (String obs : tagToObservationCount.get(tag).keySet()) {
                    tagToObservationFrequency.get(tag).put(
                            obs,
                            Math.log((double) (tagToObservationCount.get(tag).get(obs)) / tagCount.get(tag))
                    );
                }

                for (String transition : tagToTransitionCount.get(tag).keySet()) {
                    tagToTransitionFrequency.get(tag).put(
                            transition,
                            Math.log((double) (tagToTransitionCount.get(tag).get(transition)) / tagTransitionCount.get(tag))
                    );
                }
            }
            tagFile.close();
            observationFile.close();
        }
    }


