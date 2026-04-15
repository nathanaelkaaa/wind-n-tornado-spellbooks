package net.raptorzizi.wind_spellbooks.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class TornadoGroundSmokeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public TornadoGroundSmokeParticle(ClientLevel level, double x, double y, double z,
                                      SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.scale(5f + this.random.nextFloat() * 3.0f);
        this.lifetime = 15 + (int)(this.random.nextFloat() * 10);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.gravity = 0f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.alpha = Mth.clampedLerp(1f, 0f, (float) age / lifetime);

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
            this.scale(1.015f);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float f) { return LightTexture.FULL_BRIGHT; }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new TornadoGroundSmokeParticle(level, x, y, z, sprites);
        }
    }
}