package net.rhseung.rhseungslib

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mod : ModInitializer {
	const val MOD_ID = "rhseungslib"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	
	fun modID(path: String) = Identifier(MOD_ID, path)
	fun minecraftID(path: String) = Identifier("minecraft", path)
	
	override fun onInitialize() {}
}