package io.github.vampirestudios.raa.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.vampirestudios.raa.materials.CustomArmorMaterial;
import io.github.vampirestudios.raa.materials.CustomToolMaterial;
import io.github.vampirestudios.raa.materials.Material;
import io.github.vampirestudios.raa.materials.OreInformation;
import io.github.vampirestudios.raa.registries.Materials;
import io.github.vampirestudios.raa.RandomlyAddingAnything;
import io.github.vampirestudios.raa.api.enums.GeneratesIn;
import io.github.vampirestudios.raa.api.enums.OreTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavingSystem {

    private static File CONFIG_PATH = FabricLoader.getInstance().getConfigDirectory();

    private static final Gson DEFAULT_GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();

    private static File configFile;
    private static File configPath;
    private static String configFilename = "materials";
    private static Gson gson = DEFAULT_GSON;
    private static int fileNumber = 0;

    public static boolean init() {
        configPath = new File(new File(CONFIG_PATH, "raa"), "materials");
        if (!configPath.exists()) {
            configPath.mkdirs();
            return true;
        }
        configFile = new File(configPath, configFilename + "_" + fileNumber + ".json");
        return !configFile.exists();
    }

    public static void createFile() {
        configFile = new File(configPath, configFilename + "_" + fileNumber + ".json");
        if (!configFile.exists()) {
            try {
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(configFile));
                MaterialJSON[] materialJSONS = toJSON();
                for (int a = 0; a < materialJSONS.length; a++) {
                    if (a == 0) {
                        fileWriter.write("[" + "\n" + gson.toJson(materialJSONS[a]) + ",");
                        fileWriter.newLine();
                        fileWriter.flush();
                        continue;
                    }
                    if (a == materialJSONS.length - 1) {
                        fileWriter.write(gson.toJson(materialJSONS[a]));
                        fileWriter.newLine();
                        fileWriter.flush();
                        continue;
                    }
                    fileWriter.write(gson.toJson(materialJSONS[a]) + ",");
                    fileWriter.newLine();
                    fileWriter.flush();
                }
                fileWriter.write("]");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readFile() {
        configFile = new File(configPath, configFilename + "_" + fileNumber + ".json");
        try {
            FileReader fileReader = new FileReader(configFile);
            for (Material material : fromJSON(gson.fromJson(fileReader, MaterialJSON[].class))) {
                String id = material.getName().toLowerCase();
                for (Map.Entry<String, String> entry : RandomlyAddingAnything.CONFIG.namingLanguage.getCharMap().entrySet()) {
                    id = id.replace(entry.getKey(), entry.getValue());
                }
                Registry.register(Materials.MATERIALS, new Identifier(RandomlyAddingAnything.MOD_ID, id), material);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Material> fromJSON(MaterialJSON[] fromJson) {
        List<Material> materials = new ArrayList<>();
        for (MaterialJSON materialJSON : fromJson) {
            OreInformationJSON oreInformationJSON = materialJSON.oreInformationJSON;
            OreInformation oreInformation = new OreInformation(oreInformationJSON.oreTypes, oreInformationJSON.generatesIn, new Identifier(oreInformationJSON.overlayTexture),
                    oreInformationJSON.oreCount, oreInformationJSON.minXPAmount, oreInformationJSON.maxXPAmount, oreInformationJSON.oreClusterSize);
            Material material = new Material(oreInformation, materialJSON.name, materialJSON.rgb, new Identifier(materialJSON.storageBlockTexture), new Identifier(materialJSON.resourceItemTexture),
                    new Identifier(materialJSON.nuggetTexture), materialJSON.armor, materialJSON.armorMaterial, materialJSON.tools, materialJSON.weapons, materialJSON.toolMaterial,
                    materialJSON.glowing, materialJSON.oreFlower, materialJSON.food);
            materials.add(material);
        }

        return materials;
    }

    private static MaterialJSON[] toJSON() {
        List<MaterialJSON> materialJSONS = new ArrayList<>();
        Materials.MATERIALS.forEach(material -> {
            OreInformation oreInformation = material.getOreInformation();
            OreInformationJSON oreInformationJSON = new OreInformationJSON(oreInformation.getOreType(),
                    oreInformation.getGenerateIn(), material.getOreInformation().getOverlayTexture().toString(), oreInformation.getOreCount(),
                    oreInformation.getMinXPAmount(), oreInformation.getMaxXPAmount(), oreInformation.getOreClusterSize());
            String nuggetTexture;
            if (material.getNuggetTexture() == null) {
                nuggetTexture = "null";
            } else {
                nuggetTexture = material.getNuggetTexture().toString();
            }
            MaterialJSON materialJSON = new MaterialJSON(oreInformationJSON, material.getName(), material.getRGBColor(),
                    material.getStorageBlockTexture().toString(), material.getResourceItemTexture().toString(), nuggetTexture, material.hasArmor(), material.getArmorMaterial(), material.hasTools(),
                    material.hasWeapons(), material.getToolMaterial(), material.isGlowing(), material.hasOreFlower(), material.hasFood());
            materialJSONS.add(materialJSON);
        });

        return materialJSONS.toArray(new MaterialJSON[0]);
    }

    protected static class MaterialJSON {
        OreInformationJSON oreInformationJSON;
        public String name;
        int rgb;
        String storageBlockTexture;
        String resourceItemTexture;
        String nuggetTexture;
        boolean armor;
        CustomArmorMaterial armorMaterial;
        boolean tools;
        boolean weapons;
        CustomToolMaterial toolMaterial;
        boolean glowing;
        boolean oreFlower;
        boolean food;

        MaterialJSON(OreInformationJSON oreInformationJSON, String name, int rgb, String storageBlockTexture,
                     String resourceItemTexture, String nuggetTexture, boolean armor, CustomArmorMaterial armorMaterial,
                     boolean tools, boolean weapons, CustomToolMaterial toolMaterial, boolean glowing, boolean oreFlower, boolean food) {
            this.oreInformationJSON = oreInformationJSON;
            this.name = name;
            this.rgb = rgb;
            this.storageBlockTexture = storageBlockTexture;
            this.resourceItemTexture = resourceItemTexture;
            this.nuggetTexture = nuggetTexture;
            this.armor = armor;
            this.armorMaterial = armorMaterial;
            this.tools = tools;
            this.weapons = weapons;
            this.toolMaterial = toolMaterial;
            this.glowing = glowing;
            this.oreFlower = oreFlower;
            this.food = food;
        }
    }

    protected static class OreInformationJSON {
        OreTypes oreTypes;
        GeneratesIn generatesIn;
        String overlayTexture;
        int oreCount;
        int minXPAmount;
        int maxXPAmount;
        private int oreClusterSize;

        OreInformationJSON(OreTypes oreTypes, GeneratesIn generatesIn, String overlayTexture, int oreCount, int minXPAmount, int maxXPAmount, int oreClusterSize) {
            this.oreTypes = oreTypes;
            this.generatesIn = generatesIn;
            this.overlayTexture = overlayTexture;
            this.oreCount = oreCount;
            this.minXPAmount = minXPAmount;
            this.maxXPAmount = maxXPAmount;
            this.oreClusterSize = oreClusterSize;
        }
    }
}
