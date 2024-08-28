package com.acupofjava;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.acupofjava.PlayResult.*;

public class AppTest {
    public void test() {
        PlayResult returnz = play();
        switch (returnz) {
            case Defeat() -> {
            }
            case Victory() -> {
            }
            case Wrong(int hpLeft) -> {
            }
            case Right(String nextWord) -> {
            }
        }
    }

    private PlayResult play() {
        return new Defeat();
    }
}
