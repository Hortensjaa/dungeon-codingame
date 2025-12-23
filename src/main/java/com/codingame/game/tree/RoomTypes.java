package com.codingame.game.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RoomTypes {

    public static Base getRandomRoom() {
        double r = Math.random();
        if (r < 0.5) {
            return new Empty();
        } else if (r < 0.75) {
            return new Enemies((float) (Math.random()));
        } else {
            return new Treasure((float) (Math.random()));
        }
    }

    @AllArgsConstructor
    @Getter
    public static abstract class Base {
        float difficulty, reward;  // normalised [0,1]
        String name;
    }

    public static class Empty extends Base {
        Empty() {
            super(0, 0, "Empty node");
        }
    }

    public static class Start extends Base {
        public Start() {
            super(0, 0, "Start node");
        }
    }

    public static class Exit extends Base {
        public Exit() {
            super(0, 0, "Exit node");
        }
    }

    public static class Enemies extends Base {
        Enemies(float difficulty) {
            super(difficulty, 0, "Enemies node");
        }
    }

    public static class Treasure extends Base {
        Treasure(float reward) {
            super(0, reward, "Treasure node");
        }
    }
}
