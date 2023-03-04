package net.rhseung.rhseungslib.api.math

import kotlin.math.pow
import kotlin.math.roundToInt

object Math {
	fun <T> calculate(
		vararg numbers: T,
		operation: (a: T, b: T) -> T,
	): T {
		return numbers.reduce { acc, t -> operation(acc, t) }
	}
	
	fun sumOfInt(
		range: IntRange,
		operation: (k: Int) -> Int,
	): Int {
		var ret = 0
		for (i in range) ret += operation(i)
		return ret
	}
	
	fun sumOfFloat(
		range: IntRange,
		operation: (k: Int) -> Float,
	): Float {
		var ret = 0F
		for (i in range) ret += operation(i)
		return ret
	}
	
	fun sumOfDouble(
		range: IntRange,
		operation: (k: Int) -> Double,
	): Double {
		var ret = 0.0
		for (i in range) ret += operation(i)
		return ret
	}
	
	fun sumOfInt(
		range: IntRange,
		operation: (k: Int, n: Int) -> Int,
	): Int {
		var ret = 0
		for (i in range) ret += operation(i, range.last)
		return ret
	}
	
	fun sumOfFloat(
		range: IntRange,
		operation: (k: Int, n: Int) -> Float,
	): Float {
		var ret = 0F
		for (i in range) ret += operation(i, range.last)
		return ret
	}
	
	fun sumOfDouble(
		range: IntRange,
		operation: (k: Int, n: Int) -> Double,
	): Double {
		var ret = 0.0
		for (i in range) ret += operation(i, range.last)
		return ret
	}
	
	fun produceOfInt(
		range: IntRange,
		operation: (k: Int) -> Int,
	): Int {
		var ret = 1
		for (i in range) ret *= operation(i)
		return ret
	}
	
	fun produceOfFloat(
		range: IntRange,
		operation: (k: Int) -> Float,
	): Float {
		var ret = 1F
		for (i in range) ret *= operation(i)
		return ret
	}
	
	fun produceOfDouble(
		range: IntRange,
		operation: (k: Int) -> Double,
	): Double {
		var ret = 1.0
		for (i in range) ret *= operation(i)
		return ret
	}
	
	fun produceOfInt(
		range: IntRange,
		operation: (k: Int, n: Int) -> Int,
	): Int {
		var ret = 1
		for (i in range) ret *= operation(i, range.last)
		return ret
	}
	
	fun produceOfFloat(
		range: IntRange,
		operation: (k: Int, n: Int) -> Float,
	): Float {
		var ret = 1F
		for (i in range) ret *= operation(i, range.last)
		return ret
	}
	
	fun produceOfDouble(
		range: IntRange,
		operation: (k: Int, n: Int) -> Double,
	): Double {
		var ret = 1.0
		for (i in range) ret *= operation(i, range.last)
		return ret
	}
	
	fun Int.pow(n: Int): Int {
		if (n < 0) return 0
		
		var acc = 1
		var power = n
		while (power-- != 0) {
			acc *= this
		}
		
		return acc
	}
	
	fun Int.root(n: Int): Double {
		return n.toDouble().pow(1.0 / this)
	}
	
	fun Int.root(n: Float): Double {
		return n.pow(1F / this).toDouble()
	}
	
	fun Int.root(n: Double): Double {
		return n.pow(1.0 / this)
	}
	
	/**
	 * k = 2  -> rms
	 * k = 1  -> arithmeticMean
	 * k = 0  -> geometricMean
	 * k = -1 -> harmonicMean
	 */
	fun mean(
		vararg numbers: Int,
		k: Int = 1,
	): Double {
		check(numbers.isNotEmpty())
		
		if (k == 0) return geometricMean(*numbers)
		
		val n = numbers.count()
		return k.root((1.0 / n) * sumOfDouble(0 until n) { i -> numbers[i].pow(k).toDouble() })
	}
	
	fun mean(
		vararg numbers: Float,
		k: Int = 1,
	): Double {
		check(numbers.isNotEmpty())
		
		if (k == 0) return geometricMean(*numbers)
		
		val n = numbers.count()
		return k.root((1.0 / n) * sumOfDouble(0 until n) { i -> numbers[i].pow(k).toDouble() })
	}
	
	fun mean(
		vararg numbers: Double,
		k: Int = 1,
	): Double {
		check(numbers.isNotEmpty())
		
		if (k == 0) return geometricMean(*numbers)
		
		val n = numbers.count()
		return k.root((1.0 / n) * sumOfDouble(0 until n) { i -> numbers[i].pow(k) })
	}
	
	fun quadraticMean(vararg numbers: Int): Double {
		return mean(*numbers, k = 2)
	}
	
	fun quadraticMean(vararg numbers: Float): Double {
		return mean(*numbers, k = 2)
	}
	
	fun quadraticMean(vararg numbers: Double): Double {
		return mean(*numbers, k = 2)
	}
	
	fun rms(vararg numbers: Int): Double {
		return mean(*numbers, k = 2)
	}
	
	fun rms(vararg numbers: Float): Double {
		return mean(*numbers, k = 2)
	}
	
	fun rms(vararg numbers: Double): Double {
		return mean(*numbers, k = 2)
	}
	
	fun arithmeticMean(vararg numbers: Int): Double {
		return mean(*numbers, k = 1)
	}
	
	fun arithmeticMean(vararg numbers: Float): Double {
		return mean(*numbers, k = 1)
	}
	
	fun arithmeticMean(vararg numbers: Double): Double {
		return mean(*numbers, k = 1)
	}
	
	fun geometricMean(vararg numbers: Int): Double {
		return numbers.count().root(calculate(*numbers.toTypedArray()) { a, b -> a * b })
	}
	
	fun geometricMean(vararg numbers: Float): Double {
		return numbers.count().root(calculate(*numbers.toTypedArray()) { a, b -> a * b })
	}
	
	fun geometricMean(vararg numbers: Double): Double {
		return numbers.count().root(calculate(*numbers.toTypedArray()) { a, b -> a * b })
	}
	
	fun harmonicMean(vararg numbers: Int): Double {
		return mean(*numbers, k = -1)
	}
	
	fun harmonicMean(vararg numbers: Float): Double {
		return mean(*numbers, k = -1)
	}
	
	fun harmonicMean(vararg numbers: Double): Double {
		return mean(*numbers, k = -1)
	}
	
	fun Float.isPotentialInt(): Boolean {
		return this.toInt().toFloat() == this
	}
	
	fun Double.isPotentialInt(): Boolean {
		return this.toInt().toDouble() == this
	}
	
	fun floor(n: Float): Int = n.toInt()
	fun floor(n: Double): Int = n.toInt()
	
	fun ceil(n: Float) = if (n.isPotentialInt()) n.toInt() else floor(n) + 1
	fun ceil(n: Double) = if (n.isPotentialInt()) n.toInt() else floor(n) + 1
	
	fun Float.roundTo(n: Int = 0): Float {
		check(n >= 0)
		
		val p = 10F.pow(n)
		return (this * p).roundToInt() / p
	}
	
	fun Double.roundTo(n: Int = 0): Double {
		check(n >= 0)
		
		val p = 10.0.pow(n)
		return (this * p).roundToInt() / p
	}
}