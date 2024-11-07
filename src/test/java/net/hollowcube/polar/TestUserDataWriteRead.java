package net.hollowcube.polar;

import net.hollowcube.polar.chunk.PolarChunk;
import net.hollowcube.polar.chunk.PolarEntity;
import net.hollowcube.polar.chunk.PolarSection;
import net.hollowcube.polar.chunk.PolarWorld;
import net.hollowcube.polar.io.PolarLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class TestUserDataWriteRead {

    static {
        MinecraftServer.init();
    }

    @Test
    void testWriteRead() {
        var world = new PolarWorld();

        var emptySections = new PolarSection[24];
        Arrays.fill(emptySections, new PolarSection());

        var heightmaps = new int[PolarChunk.MAX_HEIGHTMAPS][];
        world.updateChunkAt(0, 0, new PolarChunk(0, 0, emptySections, new PolarEntity[0], List.of(), heightmaps, new byte[0]));

        var wa = new UpdateTimeWorldAccess();
        var loader = new PolarLoader(world).setWorldAccess(wa);
        var instance = new InstanceContainer(UUID.randomUUID(), DimensionType.OVERWORLD, loader);
        var chunk = loader.loadChunk(instance, 0, 0);

        loader.saveChunk(chunk);

        var newPolarChunk = world.chunkAt(0, 0);

        //var savedTime = new NetworkBuffer(ByteBuffer.wrap(newPolarChunk.userData())).read(NetworkBuffer.LONG);
        //assertEquals(wa.saveTime, savedTime);

        loader.loadChunk(instance, 0, 0);
        //assertEquals(wa.loadTime, savedTime);
    }

}
