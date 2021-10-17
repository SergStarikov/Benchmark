## CPU cache test   

All measurements were taken with an Intel Core i7-1165G7 processor (https://www.techpowerup.com/cpu-specs/core-i7-1165g7.c2343).

The smaller the amount of data you use for a particular data structure, the higher the chance that it will fit into the CPU
cache, which can lead to significantly better performance.

For the test, an array with a variable number of int type elements was taken. 
```java
@Param({"512", "1024", "65535", "1258291", "90123456"})
int size;

array = new int[size];
```
Both loops multiply the elements of the array by two. However, while the first loop changes every element, the second loop
modifies only every 16th element. 

|Cache|                  |
|-----|------------------|
|   L1|    96K (per core)|
|   L2| 1.25MB (per core)|
|   L3|     12MB (shared)|

|Benchmark                              |  (size)|   Mode|  Cnt|     Score|      Error|   Units|
|---------------------------------------|--------|-------|-----|----------|-----------|--------|
|CpuCacheTest.incrementEachElement      |     512|  thrpt|    5|   826,731| ±   60,944|  ops/ms|
|CpuCacheTest.incrementEachElement      |    1024|  thrpt|    5|   409,314| ±   10,639|  ops/ms|
|CpuCacheTest.incrementEachElement      |   65535|  thrpt|    5|     6,304| ±    0,048|  ops/ms|
|CpuCacheTest.incrementEachElement      | 1258291|  thrpt|    5|     0,326| ±    0,010|  ops/ms|
|CpuCacheTest.incrementEachElement      |90123456|  thrpt|    5|     0,004| ±    0,001|  ops/ms|
|CpuCacheTest.incrementSixteenthElement |     512|  thrpt|    5| 10971,864| ±  270,293|  ops/ms|
|CpuCacheTest.incrementSixteenthElement |    1024|  thrpt|    5|  3562,348| ± 1093,332|  ops/ms|
|CpuCacheTest.incrementSixteenthElement |   65535|  thrpt|    5|    74,575| ±    2,760|  ops/ms|
|CpuCacheTest.incrementSixteenthElement | 1258291|  thrpt|    5|     3,682| ±    0,249|  ops/ms|
|CpuCacheTest.incrementSixteenthElement |90123456|  thrpt|    5|     0,027| ±    0,001|  ops/ms|

#### Ratio operations per millisecond for processing each sixteenth item to each item

|  (size)| incrementSixteenthElement score / incrementEachElement score|
|--------| ------------------------------------------------------------|
|     512|                                                     13,27138|
|    1024|                                                     8,703216| 
|   65535|                                                     11,82979| 
| 1258291|                                                     11,29448| 
|90123456|                                                         6,75| 

As long as the array fits into the L1 and L2 caches, access time is very low. But as soon as the array becomes too large
and has to be read from the L3 cache, access time increases noticeably. And the same happens again as soon as the array
does not fit into the L3 cache and has to be read from main memory.

## Collection Vs Stream Test   

A test that shows what is the optimal processing flow to choose for a collection. For simplicity and clarity, ArrayList
as the most common spread implementation of the List interface was chosen for test. The number of items in the collection
(collection capacity) is set 
[here](https://github.com/SergStarikov/Benchmark/blob/2ffcbe82953120c864f84c70a11f947e1128e879/src/main/java/banchmark_test/collection_vs_stream/CollectionVsStreamTest.java#L13).
```java
@Param({"512", "1024", "65535", "1258291", "90123456"})
public int size;

testList = new ArrayList<>(size);
```
An ArrayList was filled by Integer objects. Then there was a check: if an item is even, then it is added to the result list.
For plain Java, a method has been used foreach loop. For streamJava: built stream object from the collection, filtered it
and collected to result list. For streamParallelJava - the aforementioned stream was paralleled.

|Benchmark                                 |Size     |  Mode  |Cnt|          Score |         Error | Units|
|------------------------------------------|---------|--------|---|----------------|---------------|------|
|CollectionVsStreamTest.plainJava          |     512 | avgt   | 5 |      1391,104  |  ±     351,619  | ns/op|
|CollectionVsStreamTest.plainJava          |    1024 | avgt   | 5 |      2825,122  |  ±     364,257  | ns/op|
|CollectionVsStreamTest.plainJava          |   65535 | avgt   | 5 |    224093,832  |  ±   16209,096  | ns/op|
|CollectionVsStreamTest.plainJava          | 1258291 | avgt   | 5 |  20160753,782  |  ± 1271418,805  | ns/op|
|CollectionVsStreamTest.streamJava         |     512 | avgt   | 5 |      2406,095  |  ±      71,500  | ns/op|
|CollectionVsStreamTest.streamJava         |    1024 | avgt   | 5 |      4266,421  |  ±     202,649  | ns/op|
|CollectionVsStreamTest.streamJava         |   65535 | avgt   | 5 |    198611,370  |  ±    5869,790  | ns/op|
|CollectionVsStreamTest.streamJava         | 1258291 | avgt   | 5 |  17257672,766  |  ± 1103101,317  | ns/op|
|CollectionVsStreamTest.streamParallelJava |     512 | avgt   | 5 |     23195,098  |  ±    1009,837  | ns/op|
|CollectionVsStreamTest.streamParallelJava |    1024 | avgt   | 5 |     25991,880  |  ±     458,190  | ns/op|
|CollectionVsStreamTest.streamParallelJava |   65535 | avgt   | 5 |    128904,106  |  ±    2183,604  | ns/op|
|CollectionVsStreamTest.streamParallelJava | 1258291 | avgt   | 5 |  11126631,022  |  ±  517747,658  | ns/op|

As can be seen from the results, with small collection sizes, the loop showed slightly better results than the stream and
much better than the parallelized stream. This discrepancy is explained by the fact that additional objects are needed for
the stream, and in the case of a parallelized stream, you still need to manage threads, split and join data.

With large collection sizes, the results are opposite: the parallel stream took the least time to complete the operation.
The greatest is the loop.

## False sharing (Cache line test)

The experiment consists of two test runs in which you can run two of the methods concurrently on different threads.
The first test run will execute the methods modifyFarA() and modifyFarB(), which modify two array elements that are 16
elements apart. The second test run will execute the methods modifyNearA() and modifyNearB(), which modify two adjacent
array elements.

|Benchmark                      |(size)|  Mode|  Cnt|  Score|   Error|  Units|
|-------------------------------|------|------|-----|-------|--------|-------|
|FalseSharing.far               |    17|  avgt|    5|  3,925| ± 0,531|  ns/op|
|FalseSharing.far:modifyFarA    |    17|  avgt|    5|  2,079| ± 0,105|  ns/op|
|FalseSharing.far:modifyFarB    |    17|  avgt|    5|  5,772| ± 1,078|  ns/op|
|FalseSharing.near              |    17|  avgt|    5|  3,804| ± 0,260|  ns/op|
|FalseSharing.near:modifyNearA  |    17|  avgt|    5|  3,844| ± 0,235|  ns/op|
|FalseSharing.near:modifyNearB  |    17|  avgt|    5|  3,764| ± 0,363|  ns/op|

If elements of an integer array are 16 elements or more apart, they will be located in different cache lines. If they
are closer to each other, chances become great that they will end up in the same cache line.
When two threads run in parallel long enough, they end up on different CPU cores. CPU cores do not share an L1 cache—each
core has its own. As each core copies the cache line into its respective L1 cache and executes the update to the variable,
the core notifies the other cores of the update and tells them to refresh their L1 cache in case that L1 cache is out of sync.