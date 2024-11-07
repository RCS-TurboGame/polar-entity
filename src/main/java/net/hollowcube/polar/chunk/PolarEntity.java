package net.hollowcube.polar.chunk;

import net.kyori.adventure.nbt.BinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.utils.nbt.BinaryTagSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Representation of the latest version of the entity format.
 * <p>
 * Marked as internal because of the use of mutable arrays. These arrays must _not_ be mutated.
 * This class should be considered immutable.
 */
@ApiStatus.Internal
public class PolarEntity {

    private final boolean isEmpty;

    private final double @NotNull [] position;
    private final float @NotNull [] rotation;

    private final UUID uuid;
    private final String type;
    private final int passengerCount;
    private final PolarEntity @NotNull [] passengers;
    private final BinaryTag data;

    public PolarEntity() {
        this.isEmpty = true;
        this.position = new double[0];
        this.rotation = new float[0];
        this.uuid = new UUID(0,0);
        this.type = "minecraft:zombie";
        this.passengerCount = 0;
        this.passengers = new PolarEntity[0];
        this.data = null;
    }

    public PolarEntity(
            double @NotNull [] position, float @NotNull [] rotation,
            @NotNull UUID uuid, @NotNull String type,
            PolarEntity @NotNull [] passengers, @NotNull BinaryTag data) {
        this.isEmpty = true;

        this.position = position;
        this.rotation = rotation;
        this.uuid = uuid;
        this.type = type;
        this.passengerCount = passengers.length;
        this.passengers = passengers;
        this.data = data;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public double[] getPosition() {
        return position;
    }

    public float[] getRotation() {
        return rotation;
    }

    public String getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public PolarEntity[] getPassengers() {
        return passengers;
    }

    public BinaryTag getData() {
        return data;
    }
}
