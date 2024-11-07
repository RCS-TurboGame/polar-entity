package net.hollowcube.polar.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.CoordConversion;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static net.minestom.server.instance.Chunk.CHUNK_SECTION_SIZE;

/**
 * A Java type representing the latest version of the world format.
 */
@SuppressWarnings("UnstableApiUsage")
public class PolarWorld {
    public static final int MAGIC_NUMBER = 0x506F6C72; // `Polr`
    public static final short LATEST_VERSION = 8;

    public static final short VERSION_UNIFIED_LIGHT = 1;
    public static final short VERSION_USERDATA_OPT_BLOCK_ENT_NBT = 2;
    public static final short VERSION_MINESTOM_NBT_READ_BREAK = 3;
    public static final short VERSION_WORLD_USERDATA = 4;
    public static final short VERSION_SHORT_GRASS = 5; // >:(
    public static final short VERSION_DATA_CONVERTER = 6;
    public static final short VERSION_IMPROVED_LIGHT = 7;

    public static CompressionType DEFAULT_COMPRESSION = CompressionType.ZSTD;

    // Polar metadata
    private final short version;
    private final int dataVersion;
    private CompressionType compression;

    // World metadata
    private byte minSection;
    private byte maxSection;
    private byte @NotNull [] userData;

    // Chunk data
    private final Long2ObjectMap<PolarChunk> chunks = new Long2ObjectOpenHashMap<>();

    public PolarWorld() {
        this(LATEST_VERSION, MinecraftServer.DATA_VERSION, DEFAULT_COMPRESSION, (byte) -4, (byte) 19, new byte[0], List.of());
    }

    public PolarWorld(@NotNull DimensionType dimensionType) {
        this(
                LATEST_VERSION,
                MinecraftServer.DATA_VERSION,
                DEFAULT_COMPRESSION,
                (byte) (dimensionType.minY() / CHUNK_SECTION_SIZE),
                (byte) (dimensionType.maxY() / CHUNK_SECTION_SIZE - 1),
                new byte[0],
                List.of()
        );
    }

    public PolarWorld(
            short version,
            int dataVersion,
            @NotNull CompressionType compression,
            byte minSection, byte maxSection,
            byte @NotNull [] userData,
            @NotNull List<PolarChunk> chunks
    ) {
        this.version = version;
        this.dataVersion = dataVersion;
        this.compression = compression;

        this.minSection = minSection;
        this.maxSection = maxSection;
        this.userData = userData;

        for (var chunk : chunks) {
            var index = CoordConversion.chunkIndex(chunk.x(), chunk.z());
            this.chunks.put(index, chunk);
        }
    }

    public short version() {
        return version;
    }

    public int dataVersion() {
        return dataVersion;
    }

    public @NotNull CompressionType compression() {
        return compression;
    }

    public void setCompression(@NotNull CompressionType compression) {
        this.compression = compression;
    }

    public byte minSection() {
        return minSection;
    }

    public byte maxSection() {
        return maxSection;
    }

    public void setSectionCount(byte minSection, byte maxSection) {
        for (long l : chunks.keySet()) {
            chunks.put(l, WorldHeightUtil.updateChunkHeight(chunks.get(l), minSection, maxSection));
        }

        this.minSection = minSection;
        this.maxSection = maxSection;
    }

    public byte @NotNull [] userData() {
        return userData;
    }

    public void userData(byte @NotNull [] userData) {
        this.userData = userData;
    }

    public @Nullable PolarChunk chunkAt(int x, int z) {
        return chunks.getOrDefault(CoordConversion.chunkIndex(x, z), null);
    }

    public void updateChunkAt(int x, int z, @NotNull PolarChunk chunk) {
        chunks.put(CoordConversion.chunkIndex(x, z), chunk);
    }

    public @NotNull Collection<PolarChunk> chunks() {
        return chunks.values();
    }

    public enum CompressionType {
        NONE,
        ZSTD;

        private static final CompressionType[] VALUES = values();

        public static @Nullable CompressionType fromId(int id) {
            if (id < 0 || id >= VALUES.length) return null;
            return VALUES[id];
        }
    }
}
