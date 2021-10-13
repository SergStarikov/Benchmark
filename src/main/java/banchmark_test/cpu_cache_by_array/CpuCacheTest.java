package banchmark_test.cpu_cache_by_array;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CpuCacheTest {
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        private static final int ARRAY_CONTENT = 777;
        @Param({"512", "1024", "65535", "1258291", "90123456"})
        int size;
        int[] array;

        @Setup(Level.Iteration)
        public void setUp() {
            array = new int[size];
            Arrays.fill(array, ARRAY_CONTENT);
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 1) // running in a separate process
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int[] incrementEachElement(ExecutionPlan plan, Blackhole blackhole) {
        for (int i = 0; i < plan.size; i++) {
            plan.array[i] *= 2;
            // to avoid JIT optimisation
            blackhole.consume(plan.array[i]);
        }
        return plan.array;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 3)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int[] incrementSixteenthElement(ExecutionPlan plan, Blackhole blackhole) {
        for (int i = 0; i < plan.size; i+=16) {
            plan.array[i] *= 2;
            blackhole.consume(plan.array[i]);
        }
        return plan.array;
    }
}
