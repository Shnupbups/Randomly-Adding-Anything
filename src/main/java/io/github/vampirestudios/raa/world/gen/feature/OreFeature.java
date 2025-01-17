package io.github.vampirestudios.raa.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IWorld;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;

public class OreFeature extends Feature<OreFeatureConfig> {

    public OreFeature(Function<Dynamic<?>, ? extends OreFeatureConfig> function_1) {
        super(function_1);
    }

    public boolean generate(IWorld iWorld_1, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator_1, Random random_1, BlockPos blockPos_1, OreFeatureConfig oreFeatureConfig_1) {
        float float_1 = random_1.nextFloat() * 3.1415927F;
        float float_2 = (float)oreFeatureConfig_1.size / 8.0F;
        int int_1 = MathHelper.ceil(((float)oreFeatureConfig_1.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double double_1 = (float)blockPos_1.getX() + MathHelper.sin(float_1) * float_2;
        double double_2 = (float)blockPos_1.getX() - MathHelper.sin(float_1) * float_2;
        double double_3 = (float)blockPos_1.getZ() + MathHelper.cos(float_1) * float_2;
        double double_4 = (float)blockPos_1.getZ() - MathHelper.cos(float_1) * float_2;
        double double_5 = blockPos_1.getY() + random_1.nextInt(3) - 2;
        double double_6 = blockPos_1.getY() + random_1.nextInt(3) - 2;
        int int_3 = blockPos_1.getX() - MathHelper.ceil(float_2) - int_1;
        int int_4 = blockPos_1.getY() - 2 - int_1;
        int int_5 = blockPos_1.getZ() - MathHelper.ceil(float_2) - int_1;
        int int_6 = 2 * (MathHelper.ceil(float_2) + int_1);
        int int_7 = 2 * (2 + int_1);

        for(int int_8 = int_3; int_8 <= int_3 + int_6; ++int_8) {
            for(int int_9 = int_5; int_9 <= int_5 + int_6; ++int_9) {
                if (int_4 <= iWorld_1.getTopY(Type.OCEAN_FLOOR_WG, int_8, int_9)) {
                    return this.generateVeinPart(iWorld_1, random_1, oreFeatureConfig_1, double_1, double_2, double_3, double_4, double_5, double_6, int_3, int_4, int_5, int_6, int_7);
                }
            }
        }

        return false;
    }

    protected boolean generateVeinPart(IWorld iWorld_1, Random random_1, OreFeatureConfig oreFeatureConfig_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6, int int_1, int int_2, int int_3, int int_4, int int_5) {
        int int_6 = 0;
        BitSet bitSet_1 = new BitSet(int_4 * int_5 * int_4);
        Mutable blockPos$Mutable_1 = new Mutable();
        double[] doubles_1 = new double[oreFeatureConfig_1.size * 4];

        int int_8;
        double double_12;
        double double_13;
        double double_14;
        double double_15;
        for(int_8 = 0; int_8 < oreFeatureConfig_1.size; ++int_8) {
            float float_1 = (float)int_8 / (float)oreFeatureConfig_1.size;
            double_12 = MathHelper.lerp(float_1, double_1, double_2);
            double_13 = MathHelper.lerp(float_1, double_5, double_6);
            double_14 = MathHelper.lerp(float_1, double_3, double_4);
            double_15 = random_1.nextDouble() * (double)oreFeatureConfig_1.size / 16.0D;
            double double_11 = ((double)(MathHelper.sin(3.1415927F * float_1) + 1.0F) * double_15 + 1.0D) / 2.0D;
            doubles_1[int_8 * 4] = double_12;
            doubles_1[int_8 * 4 + 1] = double_13;
            doubles_1[int_8 * 4 + 2] = double_14;
            doubles_1[int_8 * 4 + 3] = double_11;
        }

        for(int_8 = 0; int_8 < oreFeatureConfig_1.size - 1; ++int_8) {
            if (doubles_1[int_8 * 4 + 3] > 0.0D) {
                for(int int_9 = int_8 + 1; int_9 < oreFeatureConfig_1.size; ++int_9) {
                    if (doubles_1[int_9 * 4 + 3] > 0.0D) {
                        double_12 = doubles_1[int_8 * 4] - doubles_1[int_9 * 4];
                        double_13 = doubles_1[int_8 * 4 + 1] - doubles_1[int_9 * 4 + 1];
                        double_14 = doubles_1[int_8 * 4 + 2] - doubles_1[int_9 * 4 + 2];
                        double_15 = doubles_1[int_8 * 4 + 3] - doubles_1[int_9 * 4 + 3];
                        if (double_15 * double_15 > double_12 * double_12 + double_13 * double_13 + double_14 * double_14) {
                            if (double_15 > 0.0D) {
                                doubles_1[int_9 * 4 + 3] = -1.0D;
                            } else {
                                doubles_1[int_8 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        for(int_8 = 0; int_8 < oreFeatureConfig_1.size; ++int_8) {
            double double_16 = doubles_1[int_8 * 4 + 3];
            if (double_16 >= 0.0D) {
                double double_17 = doubles_1[int_8 * 4];
                double double_18 = doubles_1[int_8 * 4 + 1];
                double double_19 = doubles_1[int_8 * 4 + 2];
                int int_11 = Math.max(MathHelper.floor(double_17 - double_16), int_1);
                int int_12 = Math.max(MathHelper.floor(double_18 - double_16), int_2);
                int int_13 = Math.max(MathHelper.floor(double_19 - double_16), int_3);
                int int_14 = Math.max(MathHelper.floor(double_17 + double_16), int_11);
                int int_15 = Math.max(MathHelper.floor(double_18 + double_16), int_12);
                int int_16 = Math.max(MathHelper.floor(double_19 + double_16), int_13);

                for(int int_17 = int_11; int_17 <= int_14; ++int_17) {
                    double double_20 = ((double)int_17 + 0.5D - double_17) / double_16;
                    if (double_20 * double_20 < 1.0D) {
                        for(int int_18 = int_12; int_18 <= int_15; ++int_18) {
                            double double_21 = ((double)int_18 + 0.5D - double_18) / double_16;
                            if (double_20 * double_20 + double_21 * double_21 < 1.0D) {
                                for(int int_19 = int_13; int_19 <= int_16; ++int_19) {
                                    double double_22 = ((double)int_19 + 0.5D - double_19) / double_16;
                                    if (double_20 * double_20 + double_21 * double_21 + double_22 * double_22 < 1.0D) {
                                        int int_20 = int_17 - int_1 + (int_18 - int_2) * int_4 + (int_19 - int_3) * int_4 * int_5;
                                        if (!bitSet_1.get(int_20)) {
                                            bitSet_1.set(int_20);
                                            blockPos$Mutable_1.set(int_17, int_18, int_19);
                                            if (oreFeatureConfig_1.target.getCondition().test(iWorld_1.getBlockState(blockPos$Mutable_1))) {
                                                iWorld_1.setBlockState(blockPos$Mutable_1, oreFeatureConfig_1.state, 2);
                                                ++int_6;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return int_6 > 0;
    }
}
