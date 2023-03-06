package net.rhseung.rhseungslib

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Mod : ModInitializer {
	private const val MODID = "rhseungslib"
	val LOGGER: Logger = LoggerFactory.getLogger(MODID)
	
	fun modID(path: String) = Identifier(MODID, path)
	fun minecraftID(path: String) = Identifier("minecraft", path)
	
	override fun onInitialize() {}
}