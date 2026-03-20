package cz.blackdragoncz.lostdepths.storage;

/**
 * Tracks wall-clock time spent in NuroTech tick processing.
 * Measures actual nanoseconds consumed by NT block entities per server tick,
 * independent of global TPS — if TPS drops from another mod, NT's own
 * measured time remains accurate.
 */
public class NTPerformanceTracker {
	private static boolean enabled = false;

	// Rolling window of last 100 ticks
	private static final int WINDOW_SIZE = 100;
	private static final long[] tickTimesNs = new long[WINDOW_SIZE];
	private static int tickIndex = 0;
	private static int tickCount = 0;

	// Per-tick accumulator (multiple block entities add to this within one tick)
	private static long currentTickNs = 0;

	public static boolean isEnabled() {
		return enabled;
	}

	public static void setEnabled(boolean state) {
		enabled = state;
		if (!enabled) {
			reset();
		}
	}

	public static void reset() {
		tickIndex = 0;
		tickCount = 0;
		currentTickNs = 0;
		for (int i = 0; i < WINDOW_SIZE; i++) {
			tickTimesNs[i] = 0;
		}
	}

	/**
	 * Call at the start of an NT block entity's tick.
	 * Returns the current nanoTime for measuring duration.
	 */
	public static long beginMeasure() {
		if (!enabled) return 0;
		return System.nanoTime();
	}

	/**
	 * Call at the end of an NT block entity's tick.
	 * Adds elapsed time to the current tick's accumulator.
	 */
	public static void endMeasure(long startNano) {
		if (!enabled || startNano == 0) return;
		currentTickNs += System.nanoTime() - startNano;
	}

	/**
	 * Called once per server tick (end of tick event) to finalize the measurement.
	 */
	public static void onTickEnd() {
		if (!enabled) return;
		tickTimesNs[tickIndex] = currentTickNs;
		tickIndex = (tickIndex + 1) % WINDOW_SIZE;
		if (tickCount < WINDOW_SIZE) tickCount++;
		currentTickNs = 0;
	}

	/**
	 * Average NT processing time per tick over the sample window, in microseconds.
	 */
	public static double getAverageTickUs() {
		if (tickCount == 0) return 0;
		long sum = 0;
		for (int i = 0; i < tickCount; i++) {
			sum += tickTimesNs[i];
		}
		return (sum / (double) tickCount) / 1000.0;
	}

	/**
	 * Peak NT processing time in a single tick over the sample window, in microseconds.
	 */
	public static double getPeakTickUs() {
		long peak = 0;
		for (int i = 0; i < tickCount; i++) {
			if (tickTimesNs[i] > peak) peak = tickTimesNs[i];
		}
		return peak / 1000.0;
	}

	/**
	 * What percentage of a 50ms tick (20 TPS) does NT consume on average.
	 */
	public static double getTickPercentage() {
		return (getAverageTickUs() / 50000.0) * 100.0;
	}

	public static int getSampleCount() {
		return tickCount;
	}
}
