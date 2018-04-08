package models;

import program.DisplayManager;

public interface IGameComponent {
    ObjectType getObjectType();

    void update(DisplayManager manager, float msec);

    public enum ObjectType {
        SPACESHIP,
        BULLET,
        ASTEROID,
        MESSAGEMANAGER,
        ENEMY_SHIP,
        DIGIT,
        SCOREKEEPER,
        ENEMY_BULLET,
        ENEMYSHIPMANAGER,
        PARTICLESYSTEM,
        PARTICLE,
        ASTEROIDGENERATOR
    }
}
