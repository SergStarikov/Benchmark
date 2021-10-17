package banchmark_test.false_sharing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

public class FalseSharing {
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({"17"})
        int size;
        int[] array;

        @Setup(Level.Trial)
        public void setUp() {
            array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = i;
            }
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Group("near")
    public void modifyNearA(ExecutionPlan plan) {
        plan.array[0]++;
    }
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Group("near")
    public void modifyNearB(ExecutionPlan plan) {
        plan.array[1]++;
    }
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Group("far")
    public void modifyFarA(ExecutionPlan plan) {
        plan.array[0]++;
    }
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Group("far")
    public void modifyFarB(ExecutionPlan plan) {
        plan.array[16]++;
    }
}
