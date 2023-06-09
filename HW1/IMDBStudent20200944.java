import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.GenericOptionsParser;


public class IMDBStudent20200944{

	public static class IMDBMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
		private final LongWritable one = new LongWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
		{
			String[] splt = value.toString().split("::");
			String genre = splt[2];
			StringTokenizer itr = new StringTokenizer(genre, "|");
			while (itr.hasMoreTokens()) 
			{
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IMDBReducer extends Reducer<Text,LongWritable,Text,LongWritable> 
	{
		private LongWritable cnt = new LongWritable();

		public void reduce(Text key, Iterable<LongWritable> value, Context context) throws IOException, InterruptedException 
		{
			int sum = 0;
			for (LongWritable val : value) 
			{
				sum += val.get();
			}

			cnt.set(sum);
			context.write(key, cnt);
		}
	}

	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) 
		{
			System.err.println("Usage: IMDB <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "IMDBStudent20200944");
		job.setJarByClass(IMDBStudent20200944.class);
		job.setMapperClass(IMDBMapper.class);
		job.setReducerClass(IMDBReducer.class);
        	job.setCombinerClass(IMDBReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		FileSystem.get(job.getConfiguration()).delete( new Path(otherArgs[1]), true);
		job.waitForCompletion(true);
	}
}
