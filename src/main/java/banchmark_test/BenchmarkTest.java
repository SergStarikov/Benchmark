package banchmark_test;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;

public class BenchmarkTest {
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        private static final int ARRAY_CONTENT = 777;
        @Param({"1024", "2048", "4096", "8192", "16384", "67000", "67108864"})
        public int size;
        public int[] array;
        public int counter;
        public int mask;

        @Setup(Level.Invocation)
        public void setUp() {
            final int elements = size / 4;
            final int indexes = elements / 16;
            mask = indexes - 1;
            array = new int[elements];
            Arrays.fill(array, ARRAY_CONTENT);
            counter = 0;
            /*for (int i = 0; i < indexes; i++) {
                //seqIndex[i] = 16 * i;
            }*/
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5)
    public void benchLoop(ExecutionPlan plan) {
        for (int i = 0; i < plan.size; i++) {
            plan.array[i] *= 2;
        }

        /*for (int i = 0; i < plan.size; i+=16) {
            plan.array[i] *= 2;
        }*/
    }
}


