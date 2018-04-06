package models;

import program.DisplayManager;

public interface IGameComponent {
    ObjectType getObjectType();

    void update(DisplayManager manager, float msec);

    public enum ObjectType {
        spaceship,
        bullet,
        asteroid,
        messagemanager,
        asteroidgenerator
    }
}
