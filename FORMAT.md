# Polar v1.1

The polar format resembles the anvil format in many ways, though it is binary, not NBT.

### Header

| Name           | Type            | Notes                                                                   |
|----------------|-----------------|-------------------------------------------------------------------------|
| Magic Number   | int             | `Polr`                                                                  |
| Version        | short           |                                                                         |
| Compression    | byte            | 0 = None, 1 = Zstd                                                      |
| Length of data | varint          | Uncompressed length of data (or just length of data if `Compression=0`) |
| World          | [world](#world) |                                                                         |

### World

| Name             | Type                   | Notes                                    |
|------------------|------------------------|------------------------------------------|
| Min Section      | byte                   | For example, -4 in a vanilla world       |
| Max Section      | byte                   | For example, 19 in a vanilla world       |
| User data        | array[byte]            | Arbitrary user data segment              |
| Number of Chunks | varint                 | Number of entries in the following array |
| Chunks           | array[[chunk](#chunk)] | Chunk data                               |

### Chunk

| Name                     | Type                                 | Notes                                                                                |
|--------------------------|--------------------------------------|--------------------------------------------------------------------------------------|
| Chunk X                  | varint                               |                                                                                      |
| Chunk Z                  | varint                               |                                                                                      |
| Sections                 | array[[section](#section)]           | `maxSection - minSection + 1` entries                                                |
| Entities                 | array[[entity](#entity)]             | `maxSection - minSection + 1` entries                                                | 
| Number of Block Entities | varint                               | Number of entries in the following array                                             |
| Block Entities           | array[[block entity](#block-entity)] |                                                                                      |
| Heightmap Mask           | int                                  | A mask indicating which heightmaps are present. See `AnvilChunk` for flag constants. |
| Heightmaps               | array[bytes]                         | One heightmap for each bit present in Heightmap Mask                                 |
| Length of user data      | varint                               | Number of entries in the following array                                             |
| User data                | array[byte]                          | Arbitrary user data segment                                                          |

### Section

| Name                      | Type          | Notes                                                             |
|---------------------------|---------------|-------------------------------------------------------------------|
| Is Empty                  | bool          | If set, nothing follows                                           |
| Block Palette Size        | varint        |                                                                   |
| Block Palette             | array[string] | Entries are in the form `minecraft:block[key1=value1,key2=value2] |
| Block Palette Data Length | varint        | Only present if `Block Palette Size > 1`                          |
| Block Palette Data        | array[long]   | See the anvil format for more information about this type         |
| Biome Palette Size        | varint        |                                                                   |
| Biome Palette             | array[string] |                                                                   |
| Biome Palette Data Length | varint        | Only present if `Biome Palette Size > 1`                          |
| Biome Palette Data        | array[long]   | See the anvil format for more information about this type         |
| Block Light Data Content  | byte          | 0 = no lighting, 1 = all zero, 2 = all max, 3 = present after     |
| Block Light               | bytes         | A 2048 byte long nibble array, only present if above = 3          |
| Sky Light Data Content    | byte          | 0 = no lighting, 1 = all zero, 2 = all max, 3 = present after     |
| Sky Light                 | bytes         | A 2048 byte long nibble array, only present if above = 3          |

### Entity

| Name                 | Type                     | Notes                                         |
|----------------------|--------------------------|-----------------------------------------------|
| Is Empty             | bool                     |                                               |
| Position             | array[double]            | Index: `0` = X; `1` = Y; `2` = Z              |
| Rotation             | array[float]             | Index: `0` = Yaw; `1` = Pitch                 |
| Entity UUID          | uuid                     |                                               |
| Entity Type          | string                   | Entity Key (for example: `minecraft:creeper`) |
| Number of Passengers | varint                   | Number of entries in the following array      |
| Passengers           | array[[entity](#entity)] |                                               |
| Extra Data           | nbt                      |                                               |

### Block Entity

| Name            | Type   | Notes                                |
|-----------------|--------|--------------------------------------|
| Chunk Pos       | int    |                                      |
| Has ID          | bool   | If unset, Block Entity ID is omitted |
| Block Entity ID | string |                                      |
| Has NBT Data    | bool   | If unset, NBT Data is omitted        |
| NBT Data        | nbt    |                                      |
