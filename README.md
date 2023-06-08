# DataEngineering
MapReduce Programming Using Hadoop & Spark
<br/><br/>
<h3>HADOOP SETUP</h3>

1. 하둡 압축 풀기
    
    ```java
    cd ~
    tar -xvd bigdata/hadoop-3.3.1.tar.gz
    ln -s hadoop-3.3.1 hadoop
    ```
    
    vi ~/hadoop/etc/hadoop/core-site.xml
    
    ```java
    <configuration>
            <property>
                    <name>fs.defaultFS</name>
                    <value>hdfs://localhost:9000</value>
            </property>
            <property>
                    <name>hadoop.tmp.dir</name>
                    <value>/home/bigdata/hadoop/tmp</value>
            </property>
    </configuration>
    ```
    
    vi ~/hadoop/etc/hadoop/hdfs-site.xml
    
    ```
    <configuration>
            <property>
                    <name>dfs.replication</name>
                    <value>1</value>
            </property>
    </configuration>
    ```
    
    vi ~/hadoop/etc/hadoop/yarn-site.xml
    
    ```
    <configuration>
    
            <!-- Site specific YARN configuration properties -->
            <property>
                    <name>yarn.nodemanager.aux-services</name>
                    <value>mapreduce_shuffle</value>
            </property>
    </configuration>
    ```
    
    vi ~/hadoop/etc/hadoop/hadoop-env.sh
    
    ```
    #export JAVA_HOME
    
    export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
    ```
    
    Format
    
    ```
    $ cd ~/hadoop/bin
    $ ./hadoop namenode -format
    ```
    
2. hdfs 시작하기
    
    ```java
    $ cd ~/hadoop/sbin
    $ ./start-dfs.sh
    $ jps //namenode, secondarynamenode, datanode 등이 보여야 함
    ```
    
    hdfs에서 나의 home directory 만둘기
    
    ```
    $ cd ~/hadoop/bin
    $ ./hdfs dfs -ls /
    $ ./hdfs dfs -mkdir /user
    $ ./hdfs dfs -ls /
    Found 1 items
    drwxr-xr-x   - bigdata supergroup          0 2023-04-03 17:34 /user
    $ ./hdfs dfs -ls
    ls: `.': No such file or directory
    $ ./hdfs dfs -mkdir /user/bigdata
    ```
    
    ```
    $ cd ~/hadoop/bin
    $ ./hdfs dfs -mkdir input
    $ ./hdfs dfs -ls
    Found 1 items
    drwxr-xr-x   - bigdata supergroup          0 2023-04-03 17:38 input
    $ ./hdfs dfs -copyFromLocal ~/hadoop/README.txt input
    $ ./hdfs dfs -ls input
    Found 1 items
    -rw-r--r--   1 bigdata supergroup        175 2023-04-03 17:40 input/README.txt
    $ ./hdfs dfs -copyToLocal input/README.txt /tmp
    $ ls -alF /tmp/README.txt
    -rw-r--r-- 1 bigdata bigdata 175  4월  3 17:44 /tmp/README.txt
    ```
    
    ```
    $ ./hdfs dfs -cat input/README.txt
    For the latest information about Hadoop, please visit our website at:
    
    http://hadoop.apache.org/
    
    and our wiki, at:
    
    https://cwiki.apache.org/confluence/display/HADOOP/
    
    $ cat /tmp/README.txt
    For the latest information about Hadoop, please visit our website at:
    
    http://hadoop.apache.org/
    
    and our wiki, at:
    
    https://cwiki.apache.org/confluence/display/HADOOP/
    
    $ cat ~/hadoop/README.txt
    For the latest information about Hadoop, please visit our website at:
    
    http://hadoop.apache.org/
    
    and our wiki, at:
    
    https://cwiki.apache.org/confluence/display/HADOOP/
    ```
    

1. lab.tar 파일 사용하기
    
    ```java
    cd ~/다운로드
    mv lab.tar ..
    cd ..
    tar -xvf lab.tar
    
    ```
    
    lab code 빌드
    
    ```java
    sudo apt install ant
    cd ~/lab
    ant
    ```
    
    PATH에 hadoop/bin, hadoop/sbin 추가하기
    
    ```java
    $ vi ~/.bashrc
    
    export PATH=$PATH:~/hadoop/bin:~/hadoop/sbin
    ```
    
    coding
    
    ```java
    cd ~/lab
    vi src/WordCount.java
    ```
    
    build
    
    ```java
    cd ~/lab
    ant
    ```
    
    wordcount 실행
    
    ```java
    cd ~/lab
    hadoop jar build/hadoop-project.jar WordCount input/README.txt output
    ```
    
    결과 확인
    
    ```java
    hdfs dfs -ls output
    hdfs dfs -cat output/part-r-00000
    ```
    

1. Hadoop Shutdown
    
    ```java
    cd ~/hadoop/sbin
    ./stop-all.sh
    ```
    
<br/><br/>
<h3>SPARK SETUP</h3>

1. lab.spark.tar 파일 사용
    
         압축 풀기
    
    ```java
    cd
    tar -xvf bigdata/spark-3.2.1-bin-hadoop3.2.tgz
    ```
    
    링크 걸기
    
    ```java
    ln -s spark-3.2.1-bin-hadoop3.2 spark
    ```
    
    lab code 압축 풀기
    
    ```java
    tar -xvf lab.spark.tar
    ```
    
    .bashrc에 환경 추가하기
    
    ```java
    vi ~/.bashrc
    
    export SPARK_HOME=~/spark
    export PATH=$PATH:$SPARK_HOME/bin
    ```
    
    programming & compile
    
    ```java
    cd ~/lab.spark
    vi src/WordCount.java
    ant
    ```
    
    실행
    
    -local 파일 시스템의 데이터를 처리하는 명령
    
    ```java
    spark-submit --class WordCount build/spark-project.jar ~/hadoop/README.txt output
    ```
    
    -HDFS 상의 데이터를 처리하는 명령
    
    ```java
    spark-submit --class WordCount build/spark-project.jar hdfs://localhost:9000/user/bigdata/input/NOTICE.txt output
    ```
