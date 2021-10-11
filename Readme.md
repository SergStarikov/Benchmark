Benchmark                                                      (size)   Mode  Cnt          Score          Error   Units
b.cpu_cache_by_array.CpuCacheTest.incrementEachElement           1024  thrpt    5        343,175 ±       20,453  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementEachElement           2048  thrpt    5        155,670 ±       67,139  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementEachElement       12582912  thrpt    5          0,032 ±        0,013  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementEachElement       15123456  thrpt    5          0,028 ±        0,001  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementSixteenthElement      1024  thrpt    5       5759,883 ±      451,831  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementSixteenthElement      2048  thrpt    5       2870,367 ±      133,286  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementSixteenthElement  12582912  thrpt    5          0,201 ±        0,008  ops/ms
b.cpu_cache_by_array.CpuCacheTest.incrementSixteenthElement  15123456  thrpt    5          0,165 ±        0,008  ops/ms


b.collection_vs_stream.CollectionVsStreamTest.plainJava          1024   avgt    5       3555,414 ±      184,187   ns/op
b.collection_vs_stream.CollectionVsStreamTest.plainJava          2048   avgt    5       8886,622 ±      500,371   ns/op
b.collection_vs_stream.CollectionVsStreamTest.plainJava      12582912   avgt    5  337563391,162 ± 46167497,724   ns/op
b.collection_vs_stream.CollectionVsStreamTest.plainJava      15123456   avgt    5  387090701,595 ± 45310265,836   ns/op
b.collection_vs_stream.CollectionVsStreamTest.streamJava         1024   avgt    5       4968,425 ±      326,613   ns/op
b.collection_vs_stream.CollectionVsStreamTest.streamJava         2048   avgt    5      10129,974 ±      327,042   ns/op
b.collection_vs_stream.CollectionVsStreamTest.streamJava     12582912   avgt    5  341039530,092 ± 26093931,963   ns/op
b.collection_vs_stream.CollectionVsStreamTest.streamJava     15123456   avgt    5  399590126,733 ± 66656884,241   ns/op