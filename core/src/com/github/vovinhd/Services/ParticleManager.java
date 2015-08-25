package com.github.vovinhd.Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by vovin on 24/08/2015.
 */
public class ParticleManager {

    private ParticleEffect wallContact;
    private ParticleEffect brickContact;
    private ParticleEffect brickDestroy;

    private Array<ParticleEffect> current = new Array<>();

    public ParticleManager() {
        wallContact = new ParticleEffect();
        wallContact.load(Gdx.files.internal("wallbump.particleeffect"), Gdx.files.internal(""));
        brickContact = new ParticleEffect();
        brickContact.load(Gdx.files.internal("wallbump.particleeffect"), Gdx.files.internal(""));
        brickDestroy = new ParticleEffect();
        brickDestroy.load(Gdx.files.internal("wallbump.particleeffect"), Gdx.files.internal(""));
    }

    public void addEffect(ParticleType type, Vector2 position) {
        ParticleEffect pe;
        switch (type) {
            case WALL_CONTACT:
                pe = new ParticleEffect(wallContact);
                break;
            case BRICK_CONTACT:
                pe = new ParticleEffect(wallContact);
                break;
            case BRICK_DESTROY:
                pe = new ParticleEffect(wallContact);
                break;
            default:
                Gdx.app.log(getClass().getCanonicalName(), "Could not add particleeffect, type " + type.toString() + " is not covered by addEffect()");
                return;
        }
        pe.setPosition(position.x, position.y);
        current.add(pe);
        return;
    }

    public void render(float deltaTime, SpriteBatch batch) {
        for (ParticleEffect pe : current) {
            pe.update(deltaTime);
            pe.draw(batch);
        }
    }

    public enum ParticleType {
        WALL_CONTACT,
        BRICK_CONTACT,
        BRICK_DESTROY
    }
}
