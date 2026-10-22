
package net.matos.elementalrealms.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SleepingParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected SleepingParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            SpriteSet sprites
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.sprites = sprites;
        this.setSpriteFromAge(sprites);

        // Mostly upward movement, with little sideways spread.
        // Gentle sideways movement and stronger upward movement.
        this.xd = xSpeed * 0.8D;
        this.yd = Math.max(ySpeed, 0.015D);
        this.zd = zSpeed * 0.8D;

        this.gravity = -0.015F;
        this.friction = 0.96F;

        this.lifetime = 60;
        this.quadSize = 0.3F;

        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.alpha = 1.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.removed) {
            this.setSpriteFromAge(this.sprites);

            // Fade out during the final 40% of the particle's lifetime.
            float fadeStart = 0.75F;
            float progress = (float) this.age / this.lifetime;

            if (progress > fadeStart) {
                this.alpha = 1.0F -
                        (progress - fadeStart) / (1.0F - fadeStart);
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public SleepingParticle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new SleepingParticle(
                    level,
                    x,
                    y,
                    z,
                    xSpeed,
                    ySpeed,
                    zSpeed,
                    this.sprites
            );
        }
    }
}
