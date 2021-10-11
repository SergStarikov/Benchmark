package banchmark_test.collection_vs_stream;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CollectionVsStreamTest {
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({"1024", "2048", "12582912", "15123456", "1147483646"})
        public int size;
        public List<Integer> testList;

        @Setup(Level.Iteration)
        public void setUp() {
            testList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                testList.add(i);
            }
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public List<Integer> plainJava(ExecutionPlan plan) {
        List<Integer> resultList = new ArrayList<>();

        for (Integer number : plan.testList) {
            if (number % 2 == 0) {
                resultList.add(number * 2);
            }
        }

        return resultList;
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 3)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public List<Integer> streamJava(ExecutionPlan plan) {
        return plan.testList.stream()
                .filter(number -> number % 2 == 0)
                .map(number -> number * 2)
                .collect(Collectors.toList());
    }
}
