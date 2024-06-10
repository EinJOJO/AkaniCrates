package it.einjojo.akani.crates.util;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ScrambledArrayList<T> {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final List<T> original;
    private final ArrayList<T> scrambledArrayList;
    private final int[] originalIndices;

    public ScrambledArrayList(@NotNull List<T> list) {
        this.scrambledArrayList = new ArrayList<>(list);
        this.original = new ArrayList<>(list);
        this.originalIndices = new int[list.size()];
        for (int i = 0; i < originalIndices.length; i++) {
            originalIndices[i] = i;
        }
        scramble();
    }

    public void scramble() {
        for (int i = 0; i < scrambledArrayList.size(); i++) {
            int randomIndex = RANDOM.nextInt(scrambledArrayList.size());
            T temp = scrambledArrayList.get(i);
            scrambledArrayList.set(i, scrambledArrayList.get(randomIndex));
            scrambledArrayList.set(randomIndex, temp);
            // save what index the icon was at before scrambling
            int tempIndex = originalIndices[i];
            originalIndices[i] = originalIndices[randomIndex];
            originalIndices[randomIndex] = tempIndex;
        }
    }

    public List<T> getOriginalList() {
        return original;
    }

    public List<T> getScrambledList() {
        return scrambledArrayList;
    }

    public int[] getOriginalIndices() {
        return originalIndices;
    }

    public int getOriginalIndexByScrambledIndex(int scrambledIndex) {
        return originalIndices[scrambledIndex];
    }

    public T getOriginalByScrambledIndex(int scrambledIndex) {
        return original.get(getOriginalIndexByScrambledIndex(scrambledIndex));
    }

    public T get(int index) {
        return scrambledArrayList.get(index);
    }

    public int size() {
        return scrambledArrayList.size();
    }


}
