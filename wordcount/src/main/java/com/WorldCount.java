package com;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class WorldCount {

    // 继承Mapper类,Mapper类的四个泛型参数分别为：输入key类型，输入value类型，输出key类型，输出value类型
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1); // output value
        private Text word = new Text(); // output key

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);

            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }

        }
    }

    // Reduce类，继承了Reducer类
    public static class Reduce extends
            Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            System.out.println(key + ":  " + new IntWritable(sum));
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
//        Configuration conf = new Configuration();//读取Hadoop配置信
//        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
//        System.out.println("1:"+otherArgs[0]+"2:"+otherArgs[1]);
        System.out.println("1:" + args[0] + "2:" + args[1]);
        if (args.length != 2) {
            System.err
                    .println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        Job job = new Job(); // 创建一个作业对象
        job.setJarByClass(WorldCount.class); // 设置运行/处理该作业的类
        job.setJobName("WordCount");

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}