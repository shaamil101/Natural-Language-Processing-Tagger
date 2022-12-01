import org.bytedeco.opencv.presets.opencv_core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Viterbi {
    final double U = -100.0;
    ArrayList<Map<String, Map<String, Double>>> statesToScore;
    private final HiddenMarkovModel hmm;

    public Viterbi(HiddenMarkovModel hmm) {
        this.hmm = hmm;
        statesToScore = new ArrayList<>();
    }


    /**
     * Performs Viterbi to guess the tags from an array of observations
     * @param observations array of observations
     * @return array of tags
     */
    public String[] guessTags(String[] observations) {
        // Array holding the best guess for each stage of Viterbi
        statesToScore = new ArrayList<>();
        statesToScore.add(new HashMap<>());
        statesToScore.get(0).put("0", new HashMap<>());
        statesToScore.get(0).get("0").put("#", 0.0);

        // Iterate over observations
        for (int i = 1; i < observations.length + 1; i++) {
            String obs = observations[i - 1];

            Map<String, Double> bestStateScore = new HashMap<>();
            Map<String, String> bestStatePrevious = new HashMap<>();

            for (String previousPossibleCurrState : statesToScore.get(i - 1).keySet()) {
                for (String previousPossibleNextState : statesToScore.get(i - 1).get(previousPossibleCurrState).keySet()) {
                   double currScore = statesToScore.get(i - 1) .get(previousPossibleCurrState).get(previousPossibleNextState); // gets current score from previous stage

                   for (String possibleNextState : hmm.tagToTransitionFrequency.get(previousPossibleNextState).keySet()) {
                        double transScore = hmm.tagToTransitionFrequency.get(previousPossibleNextState).get(possibleNextState); // gets transition score
                        double obsScore;
                        if (hmm.tagToObservationFrequency.get(possibleNextState).containsKey(obs))
                            obsScore = hmm.tagToObservationFrequency.get(possibleNextState).get(obs); // gets observation score
                        else
                        {
                            obsScore = U;
                        }

                        double score = currScore + transScore + obsScore; // calculate total score

                        if (!bestStateScore.containsKey(possibleNextState)) {
                            bestStateScore.put(possibleNextState, score);
                            bestStatePrevious.put(possibleNextState, previousPossibleNextState);
                        } else {
                            // only store the best one
                            if (bestStateScore.get(possibleNextState) < score) {
                                bestStateScore.put(possibleNextState, score);
                                bestStatePrevious.put(possibleNextState, previousPossibleNextState);
                            }
                        }
                   }
                }
            }

            Map<String, Map<String, Double>> step = new HashMap<>();

            // creates map from best scores and add to statesToScore list
            for (String nextState : bestStateScore.keySet()) {
                if (!step.containsKey(bestStatePrevious.get(nextState))) {
                    step.put(bestStatePrevious.get(nextState), new HashMap<>());
                }

                step.get(bestStatePrevious.get(nextState)).put(nextState, bestStateScore.get(nextState));
            }

            statesToScore.add(step);
        }

        double bestScore = Integer.MIN_VALUE;
        String bestPrevState = "";
        String bestCurrState = "";

        // find best score to backtrace
        for (String prevState : statesToScore.get(statesToScore.size() - 1).keySet()) {
            for (String currState : statesToScore.get(statesToScore.size() - 1).get(prevState).keySet()) {
                if (statesToScore.get(statesToScore.size() - 1).get(prevState).get(currState) > bestScore) {
                    bestScore = statesToScore.get(statesToScore.size() - 1).get(prevState).get(currState);
                    bestPrevState = prevState;
                    bestCurrState = currState;
                }
            }
        }

        // backtrace to find tags
        String[] guessedTags = new String[observations.length];

        guessedTags[observations.length - 1] = bestCurrState;

        if (observations.length == 1) return guessedTags;

        guessedTags[observations.length - 2] = bestPrevState;

        int i = statesToScore.size() - 2;

        // fetch the path back
        while (bestPrevState != "#" && i >= 2) {
            for (String prevState : statesToScore.get(i).keySet()) {
                for (String currState : statesToScore.get(i).get(prevState).keySet()) {
                    if (currState == bestPrevState) {
                        guessedTags[i - 2] = prevState;
                        bestPrevState = prevState;
                        break;
                    }
                }
            }
            i--;
        }

        return guessedTags;

    }
}
