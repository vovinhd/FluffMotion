package com.github.vovinhd.GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.vovinhd.GameState.Ball;
import com.github.vovinhd.GameState.Obstacle;

/**
 * Created by vovin on 20/08/2015.
 */
public class ChainContactListener implements ContactListener {

    private GameMode gameMode;

    public ChainContactListener(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        contact.getWorldManifold();
        Gdx.app.log("Collision", fixtureA.toString() + " <- A | B -> " + fixtureB.toString());
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Ball) {
            resolveContact(fixtureA, fixtureB, contact.getWorldManifold());
        } else if (fixtureB.getUserData() != null && fixtureB.getUserData() instanceof Ball) {
            resolveContact(fixtureB, fixtureA, contact.getWorldManifold());
        }

    }

    private void resolveContact(Fixture ballFixture, Fixture collisionFixture, WorldManifold manifold) {
        if (collisionFixture.getUserData() == null) {
            bounceOff((Ball) ballFixture.getUserData(), manifold.getNormal());
        } else if (collisionFixture.getUserData() instanceof MapProperties) {
            MapProperties properties = (MapProperties) collisionFixture.getUserData();
            if (properties.get("trigger") != null) {
                String trigger = (String) properties.get("trigger");
                gameMode.trigger(trigger);
            }
        } else if (collisionFixture.getUserData() instanceof Obstacle) {
            Obstacle obstacle = (Obstacle) collisionFixture.getUserData();
            obstacle.collideWithPlayer((Ball) ballFixture.getUserData());
            bounceOff((Ball) ballFixture.getUserData(), manifold.getNormal());
        }

    }

    public void bounceOff(Ball ball, Vector2 collisionNormal) {
        ball.bounceOff(collisionNormal);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.isSensor() || fixtureB.isSensor()) {
            contact.setEnabled(false);
        }

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
