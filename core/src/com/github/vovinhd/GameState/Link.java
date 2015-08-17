package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by vovin on 08/08/2015.
 */
public class Link extends Actor{

    //TODO document this nonsense
    private final Chain chain;
    public int id = 0;
    public Texture textureLeftSegment = new Texture(Gdx.files.internal("line_l.png"));
    public Texture textureRightSegment = new Texture(Gdx.files.internal("line_r.png"));
    public Texture textureCenterSegment = new Texture(Gdx.files.internal("line_c.png"));
    public Texture textureCenterSegment_ = new Texture(Gdx.files.internal("line_c0.png"));
    public TextureRegion textureRegionRect = new TextureRegion(textureCenterSegment);
    public TextureRegion textureRegionRect_ = new TextureRegion(textureCenterSegment_);
    public Sprite spriteLeftSegment = new Sprite(textureLeftSegment);
    public Sprite spriteRightSegment = new Sprite(textureRightSegment);
    public Sprite spriteCenterSegment = new Sprite(textureCenterSegment);
    public boolean hasBeenSwapped;

    public float[] vertices = new float[1];

    public Ball parent;
    public Ball child;
    public float angle;
    public float speed;
    public int length;
    float springConstant;

    public Link(Chain chain, Ball parent, Ball child) {
        this.chain = chain;
        this.parent = parent;
        this.child = child;
        parent.setDown(this);
        child.setUp(this);
        spriteCenterSegment.setOriginCenter();
    }

    void swapParentChild() {
        Ball temp = parent;
        parent = child;
        child = temp;
        angle -= Math.PI;
        TextureRegion tmp = textureRegionRect;
        textureRegionRect = textureRegionRect_;
        textureRegionRect_ = tmp;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //   drawTextureSegment(spriteLeftSegment, batch, parentAlpha);
        //   drawTextureSegment(spriteRightSegment, batch, parentAlpha);
        //   drawTextureSegment(spriteCenterSegment, batch, parentAlpha);
        drawLine((int) parent.position.x, (int) parent.position.y, (int) child.position.x, (int) child.position.y, 1, batch);
    }

    void drawLine(int x1, int y1, int x2, int y2, int thickness, Batch batch) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float rad = (float) Math.atan2(dy, dx);
        batch.draw(textureRegionRect, (float) x1, (float) y1, 0, thickness / 2, dist, thickness, 1, 1, rad * MathUtils.radiansToDegrees);
    }


    public void _act(float delta) {
        if (child == null) return;
        angle += speed * delta;
        Vector2 newPosChild = new Vector2();
        newPosChild.x = parent.position.x + length * (float) Math.cos(angle);
        newPosChild.y = parent.position.y + length * (float) Math.sin(angle);

        child.setPosition(newPosChild);
        child.act(delta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        if (length != link.length) return false;
        if (!parent.equals(link.parent)) return false;
        return child.equals(link.child);

    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + child.hashCode();
        result = 31 * result + length;
        return result;
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", parent=" + parent +
                ", child=" + child +
                ", length=" + length +
                ", angle=" + angle +
                ", speed=" + speed +
                '}';
    }
}
