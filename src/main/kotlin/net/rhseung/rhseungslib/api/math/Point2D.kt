package net.rhseung.rhseungslib.api.math

import net.rhseung.rhseungslib.api.math.Math.pow
import net.rhseung.rhseungslib.api.math.Math.sumOfDouble
import net.rhseung.rhseungslib.api.math.Math.sumOfFloat
import net.rhseung.rhseungslib.api.math.Math.sumOfInt
import kotlin.math.pow

data class Point2D<T : Number>(var x: T, var y: T) {
	
	fun toList(): List<T> {
		return listOf(x, y)
	}
	
	companion object {
		fun abs(p: Point2D<Int>): Int {
			val list = p.toList()
			return sumOfInt(0 until list.count()) { i -> list[i].pow(2) }
		}
		
		fun abs(p: Point2D<Float>): Float {
			val list = p.toList()
			return sumOfFloat(0 until list.count()) { i -> list[i].pow(2) }
		}
		
		fun abs(p: Point2D<Double>): Double {
			val list = p.toList()
			return sumOfDouble(0 until list.count()) { i -> list[i].pow(2) }
		}
	}
}