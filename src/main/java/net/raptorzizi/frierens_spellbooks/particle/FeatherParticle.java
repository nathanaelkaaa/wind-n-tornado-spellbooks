package net.raptorzizi.frierens_spellbooks.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FeatherParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final float rotationSpeed;

    public FeatherParticle(ClientLevel level, double x, double y, double z,
                           SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);

        this.scale(0.7f + this.random.nextFloat() * 0.6f);

        this.oRoll = this.roll = this.random.nextFloat() * (float) (Math.PI * 2);
        this.rotationSpeed = (this.random.nextFloat() - 0.5f) * 0.1f;

        this.gravity = -0.015f;

        this.lifetime = (int) (8.0f / (Math.random() * 0.8 + 0.2)) + 12;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age > this.lifetime / 2) {
            this.alpha = 1.0f - ((float) this.age - (float) (this.lifetime / 2)) / (float) this.lifetime;
        }

        this.oRoll = this.roll;
        this.roll += this.rotationSpeed;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        if (this.age == 1) {
            this.xd += (this.random.nextDouble() * 2.0 - 1.0) * 0.2;
            this.yd = 0.3 + this.random.nextFloat() / 100.0f;
            this.zd += (this.random.nextDouble() * 2.0 - 1.0) * 0.2;
        } else if (this.age <= 10) {
            this.yd -= 0.003;
        }

        this.xd += (this.random.nextFloat() - 0.5f) * 0.003f;
        this.zd += (this.random.nextFloat() - 0.5f) * 0.003f;

        this.move(this.xd, this.yd, this.zd);

        if (this.onGround) {
            this.xd = 0;
            this.zd = 0;
        }

        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new FeatherParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}