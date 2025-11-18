package com.hbm.handler.ae2;

//Import movable TEs here:
import com.hbm.blocks.generic.BlockBedrockOreTE;

import appeng.api.AEApi;
import appeng.api.movable.IMovableRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public class AE2CompatHandler {
    public static void init() {
        if (Loader.isModLoaded("appliedenergistics2")) {
            registerHandler();
            registerSpatialIOMovable();
        }
    }

    @Optional.Method(modid = "appliedenergistics2")
    private static void registerHandler() {
        AEApi.instance().registries().externalStorage().addExternalStorageInterface(new MSUExternalStorageHandler());
		AEApi.instance().registries().externalStorage().addExternalStorageInterface(new AFLExternalStorageHandler());
    }
    
    @Optional.Method(modid = "appliedenergistics2")
    private static void registerSpatialIOMovable() {
        IMovableRegistry reg = AEApi.instance().registries().movable();
        reg.whiteListTileEntity(BlockBedrockOreTE.TileEntityBedrockOre.class);
        //TODO: More to come...
    }
}
