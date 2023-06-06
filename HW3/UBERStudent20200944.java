import scala.Tuple2;
import java.io.Serializable;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

public final class UBERStudent20200944 {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: UBER <in-file> <out-file>");
            System.exit(1);
        }

        SparkSession spark = SparkSession
                .builder()
                .appName("UBERStudent20200944")
                .getOrCreate();

        JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();

        // mapper
        JavaPairRDD<String, String> words = lines.mapToPair(new PairFunction<String, String, String>() {
            public Tuple2<String, String> call(String s) {
				String[] days = {"MON", "TUE", "WED", "THR", "FRI", "SAT", "SUN"};
				
                StringTokenizer itr = new StringTokenizer(s, ",");
                String num = itr.nextToken(); 
                String tmpDate = itr.nextToken();
                String vehicles = itr.nextToken(); 
                String trips = itr.nextToken(); 

                itr = new StringTokenizer(tmpDate, "/");
                int month = Integer.parseInt(itr.nextToken());
                int day = Integer.parseInt(itr.nextToken());
                int year = Integer.parseInt(itr.nextToken());

                LocalDate date = LocalDate.of(year, month, day);
                DayOfWeek tmpDayOfWeek = date.getDayOfWeek();
                int dayOfWeekNumber = tmpDayOfWeek.getValue();
                String dayOfWeek = days[dayOfWeekNumber - 1];

                String key = num + "," + dayOfWeek;
                String value = trips + "," + vehicles;

                return new Tuple2(key, value);
            }
        });

        JavaPairRDD<String, String> counts = words.reduceByKey(new Function2<String, String, String>() {
            public String call(String val1, String val2) {
                String[] val1_splited = val1.split(",");
                String[] val2_splited = val2.split(",");

                int trips = Integer.parseInt(val1_splited[0]) + Integer.parseInt(val2_splited[0]);
                int vehicles = Integer.parseInt(val1_splited[1]) + Integer.parseInt(val2_splited[1]);

                return trips + "," + vehicles;
            }
        });

        counts.saveAsTextFile(args[1]);
        spark.stop();
    }
}