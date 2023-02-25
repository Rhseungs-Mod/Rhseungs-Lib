package net.rhseung.rhseungslib

import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MainInitializer : ModInitializer {
	const val MOD_ID = "rhseungslib"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
	
	override fun onInitialize() {
	}
}