package program;

import messaging.*;
import models.IGameComponent;
import models.Model;
import models.ModelLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ScoreKeeper implements IMessageReceiver, IGameComponent {

    private static final int NEXT_LIFE_SCORE = 4000;
    private int score = 0;
    private Matrix4f localTransform;
    private Vector3f color;
    private Vector3f[] position;
    private Vector3f scale;

    private Digit[] digits;

    private Model digitZero;
    private Model digitOne;
    private Model digitTwo;
    private Model digitThree;
    private Model digitFour;
    private Model digitFive;
    private Model digitSix;
    private Model digitSeven;
    private Model digitEight;
    private Model digitNine;

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
        localTransform = new Matrix4f();
        color = new Vector3f(.6f, .6f, .6f);
        digitZero = ModelLoader.loadModel("zero");
        digitOne = ModelLoader.loadModel("one");
        digitTwo = ModelLoader.loadModel("two");
        digitThree = ModelLoader.loadModel("three");
        digitFour = ModelLoader.loadModel("four");
        digitFive = ModelLoader.loadModel("five");
        digitSix = ModelLoader.loadModel("six");
        digitSeven = ModelLoader.loadModel("seven");
        digitEight = ModelLoader.loadModel("eight");
        digitNine = ModelLoader.loadModel("nine");

        position = new Vector3f[]{
                new Vector3f(100, 30, 1),
                new Vector3f(85, 30, 1),
                new Vector3f(70, 30, 1),
                new Vector3f(55, 30, 1),
                new Vector3f(40, 30, 1)
        };

        digits = new Digit[]{
                new Digit(digitZero, position[0]),
                new Digit(digitZero, position[1]),
                new Digit(digitZero, position[2]),
                new Digit(digitZero, position[3]),
                new Digit(digitZero, position[4])
        };

        manager.addGameComponent(digits[0]);
        manager.addGameComponent(digits[1]);
        manager.addGameComponent(digits[2]);
        manager.addGameComponent(digits[3]);
        manager.addGameComponent(digits[4]);

        scale = new Vector3f(8, 10, 1);
    }

    @Override
    public void Receive(Message message) {

        if (message instanceof AsteroidDestroyed) {

            AsteroidDestroyed a = (AsteroidDestroyed) message;

            switch (a.getSize()) {
                case small:
                    score += 50;
                    break;
                case medium:
                    score += 30;
                    break;
                case large:
                    score += 10;
            }
        } else if (message instanceof MessageAlienDestroyed) {
            score += 100;
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
