package program;

import messaging.*;
import models.Asteroid;
import models.IGameComponent;
import models.Model;
import models.ModelLoader;
import org.joml.Vector3f;

public class ScoreKeeper implements IMessageReceiver, IGameComponent {

    private static final int NEXT_LIFE_SCORE = 4000;
    private static final Model digitZero = ModelLoader.loadModel("zero");
    private static final Model digitOne = ModelLoader.loadModel("one");
    private static final Model digitTwo = ModelLoader.loadModel("two");
    private static final Model digitThree = ModelLoader.loadModel("three");
    private static final Model digitFour = ModelLoader.loadModel("four");
    private static final Model digitFive = ModelLoader.loadModel("five");
    private static final Model digitSix = ModelLoader.loadModel("six");
    private static final Model digitSeven = ModelLoader.loadModel("seven");
    private static final Model digitEight = ModelLoader.loadModel("eight");
    private static final Model digitNine = ModelLoader.loadModel("nine");
    private int score = 0;
    private Vector3f[] digitPositions;
    private Digit[] digits;
    private float time;

    private int nextLifeScore;

    private float extraLifeEffectTime;
    private float nextExtraLifeEffectTime;

    private boolean playExtraLifeEffect;
    private int extraLifeEffectCounter;

    private DisplayManager manager;

    public ScoreKeeper(DisplayManager manager) {

        this.manager = manager;

        MessageManager.getInstance().RegisterForMessage(this);

        digitPositions = new Vector3f[]{
                new Vector3f(120, 30, 1),
                new Vector3f(100, 30, 1),
                new Vector3f(80, 30, 1),
                new Vector3f(60, 30, 1),
                new Vector3f(40, 30, 1)
        };

        digits = new Digit[]{
                new Digit(digitZero, digitPositions[0]),
                new Digit(digitZero, digitPositions[1]),
                new Digit(digitZero, digitPositions[2]),
                new Digit(digitZero, digitPositions[3]),
                new Digit(digitZero, digitPositions[4])
        };

        manager.addGameComponent(digits[0]);
        manager.addGameComponent(digits[1]);
        manager.addGameComponent(digits[2]);
        manager.addGameComponent(digits[3]);
        manager.addGameComponent(digits[4]);
    }

    @Override
    public void Receive(Message message) {

        if (message instanceof AsteroidDestroyed) {
            Asteroid a = ((AsteroidDestroyed) message).getAsteroid();
            switch (a.getSize()) {
                case small:
                    score += 100;
                    break;
                case medium:
                    score += 50;
                    break;
                case large:
                    score += 20;
            }
        } else if (message instanceof MessageSmallSaucerDestroyed) {
            score += 1000;
        } else if (message instanceof MessageLargeSaucerDestroyed) {
            score += 200;
        }
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.SCOREKEEPER;
    }

    @Override
    public void update(DisplayManager manager, float msec) {

        time += msec;

        if (time > .5) {
            SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.BEATONE);
            time = 0f;
        }

        String characterScore = String.valueOf(score);

        for (int i = 0; i < characterScore.length(); i++) {

            Character c = characterScore.charAt(i);

            int location = characterScore.length() - i - 1;

            switch (c) {
                case '0':
                    digits[location].setModel(digitZero);
                    break;
                case '1':
                    digits[location].setModel(digitOne);
                    break;
                case '2':
                    digits[location].setModel(digitTwo);
                    break;
                case '3':
                    digits[location].setModel(digitThree);
                    break;
                case '4':
                    digits[location].setModel(digitFour);
                    break;
                case '5':
                    digits[location].setModel(digitFive);
                    break;
                case '6':
                    digits[location].setModel(digitSix);
                    break;
                case '7':
                    digits[location].setModel(digitSeven);
                    break;
                case '8':
                    digits[location].setModel(digitEight);
                    break;
                case '9':
                    digits[location].setModel(digitNine);
                    break;
            }
        }

        if (score > nextLifeScore) {
            nextLifeScore += NEXT_LIFE_SCORE;
            playExtraLifeEffect = true;
            nextExtraLifeEffectTime = 0;
            MessageManager.getInstance().PostMessage(new MessageNewLife());
        }

        if (playExtraLifeEffect) {
            extraLifeEffectTime += msec;

            if (extraLifeEffectTime > nextExtraLifeEffectTime) {
                if (extraLifeEffectCounter++ < 40) {
                    SoundManager.getInstance().PlaySound(SoundManager.SoundEffect.EXTRASHIP);
                    nextExtraLifeEffectTime += .03f;
                } else {
                    playExtraLifeEffect = false;
                    extraLifeEffectCounter = 0;
                    extraLifeEffectTime = 0;
                }
            }
        }
    }
}
