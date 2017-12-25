
<div id="topics">
	<div class = "post">
		<h1 class = "postTitle">
			<a id="cb_post_title_url" class="postTitle2" href="http://www.cnblogs.com/caobotao/p/5020857.html">Android必学之AsyncTask</a>
		</h1>
		<div class="clear"></div>
		<div class="postBody">
			<div id="cnblogs_post_body" class="blogpost-body"><p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">AsyncTask,即异步任务,是Android给我们提供的一个处理异步任务的类.通过此类,可以实现UI线程和后台线程进行通讯,后台线程执行异步任务,并把结果返回给UI线程.</span></p>
<p><span style="font-size: 14pt; font-family: 'Microsoft YaHei';">.为什么需要使用异步任务?</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">我们知道,Android中只有UI线程,也就是主线程才能进行对UI的更新操作,而其他线程是不能直接操作UI的.这样的好处是保证了UI的稳定性和准确性,避免多个线程同时对UI进行操作而造成UI的混乱.但Android是一个多线程的操作系统,我们总不能把所有的任务都放在主线程中进行实现,比如网络操作,文件读取等耗时操作,如果全部放到主线程去执行,就可能会造成后面任务的阻塞.Android会去检测这种阻塞,当阻塞时间太长的时候,就会抛出Application Not Responsed(ANR)错误.所以我们需要将这些耗时操作放在非主线程中去执行.这样既避免了Android的单线程模型,又避免了ANR.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 14pt;">.AsyncTask为何而生?</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">提到异步任务,我们能想到用线程,线程池去实现.确实,Android给我们提供了主线程与其他线程通讯的机制.但同时,Android也给我们提供了一个封装好的组件--AsyncTask.利用AsyncTask,我们可以很方便的实现异步任务处理.AsyncTask可以在子线程中更新UI,也封装简化了异步操作.使用线程,线程池处理异步任务涉及到了线程的同步,管理等问题.而且当线程结束的时候还需要使用Handler去通知主线程来更新UI.而AsyncTask封装了这一切,使得我们可以很方便的在子线程中更新UI.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 14pt;">.构建AsyncTask子类的泛型参数</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">AsyncTask&lt;Params,Progress,Result&gt;是一个抽象类,通常用于被继承.继承AsyncTask需要指定如下三个泛型参数:</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">Params</span>:启动任务时输入的参数类型.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">Progress</span>:后台任务执行中返回进度值的类型.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">Result</span>:后台任务执行完成后返回结果的类型.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 14pt;">.构建AsyncTask子类的回调方法</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">AsyncTask主要有如下几个方法:</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">doInBackground</span>:必须重写,异步执行后台线程要完成的任务,耗时操作将在此方法中完成.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">onPreExecute</span>:执行后台耗时操作前被调用,通常用于进行初始化操作.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">onPostExecute</span>:当doInBackground方法完成后,系统将自动调用此方法,并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;"><span style="color: #ff0000;">onProgressUpdate</span>:当在doInBackground方法中调用publishProgress方法更新任务执行进度后,将调用此方法.通过此方法我们可以知晓任务的完成进度.</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">下面通过代码演示一个典型的异步处理的实例--加载网络图片.网络操作作为一个不稳定的耗时操作,从4.0开始就被严禁放入主线程中.所以在显示一张网络图片时,我们需要在异步处理中下载图片,并在UI线程中设置图片.</span></p>
<p>MainActivity.java</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">package</span><span style="color: #000000;"> com.example.caobotao.learnasynctask;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.app.Activity;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.content.Intent;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.Bundle;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.view.View;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.view.View.OnClickListener;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.widget.Button;

</span><span style="color: #0000ff;">public</span> <span style="color: #0000ff;">class</span> MainActivity <span style="color: #0000ff;">extends</span><span style="color: #000000;"> Activity {
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> Button btn_image;</span><span style="color: #000000;">
    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_image </span>=<span style="color: #000000;"> (Button) findViewById(R.id.btn_image);
        btn_image.setOnClickListener(</span><span style="color: #0000ff;">new</span><span style="color: #000000;"> OnClickListener() {
            @Override
            </span><span style="color: #0000ff;">public</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onClick(View v) {
                startActivity(</span><span style="color: #0000ff;">new</span> Intent(MainActivity.<span style="color: #0000ff;">this</span>,ImageActivity.<span style="color: #0000ff;">class</span><span style="color: #000000;">));
            }
        });
    }
}</span></span></pre>
</div>
<p>&nbsp;</p>
<p>ImageActivity.java</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">package</span><span style="color: #000000;"> com.example.caobotao.learnasynctask;

</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.app.Activity;
</span><span style="color: #0000ff;">import</span> android.graphics.*<span style="color: #000000;">;
</span><span style="color: #0000ff;">import</span> android.os.*<span style="color: #000000;">;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.view.View;
</span><span style="color: #0000ff;">import</span> android.widget.*<span style="color: #000000;">;
</span><span style="color: #0000ff;">import</span> java.io.*<span style="color: #000000;">;
</span><span style="color: #0000ff;">import</span> java.net.*<span style="color: #000000;">;

</span><span style="color: #008000;">/**</span><span style="color: #008000;">
 * Created by caobotao on 15/12/2.
 </span><span style="color: #008000;">*/</span>
<span style="color: #0000ff;">public</span> <span style="color: #0000ff;">class</span> ImageActivity <span style="color: #0000ff;">extends</span><span style="color: #000000;"> Activity {
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> ImageView imageView ;
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> ProgressBar progressBar ;
    </span><span style="color: #0000ff;">private</span> <span style="color: #0000ff;">static</span> String URL = "http://pic3.zhongsou.com/image/38063b6d7defc892894.jpg"<span style="color: #000000;">;
    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        imageView </span>=<span style="color: #000000;"> (ImageView) findViewById(R.id.image);
        progressBar </span>=<span style="color: #000000;"> (ProgressBar) findViewById(R.id.progressBar);
        </span><span style="color: #008000;">//</span><span style="color: #008000;">通过调用execute方法开始处理异步任务.相当于线程中的start方法.</span>
        <span style="color: #0000ff;">new</span><span style="color: #000000;"> MyAsyncTask().execute(URL);
    }

    </span><span style="color: #0000ff;">class</span> MyAsyncTask <span style="color: #0000ff;">extends</span> AsyncTask&lt;String,Void,Bitmap&gt;<span style="color: #000000;"> {

        </span><span style="color: #008000;">//</span><span style="color: #008000;">onPreExecute用于异步处理前的操作</span>
<span style="color: #000000;">        @Override
        </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onPreExecute() {
            </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onPreExecute();
            </span><span style="color: #008000;">//</span><span style="color: #008000;">此处将progressBar设置为可见.</span>
<span style="color: #000000;">            progressBar.setVisibility(View.VISIBLE);
        }

        </span><span style="color: #008000;">//</span><span style="color: #008000;">在doInBackground方法中进行异步任务的处理.</span>
<span style="color: #000000;">        @Override
        </span><span style="color: #0000ff;">protected</span><span style="color: #000000;"> Bitmap doInBackground(String... params) {
            </span><span style="color: #008000;">//</span><span style="color: #008000;">获取传进来的参数</span>
            String url = params[0<span style="color: #000000;">];
            Bitmap bitmap </span>= <span style="color: #0000ff;">null</span><span style="color: #000000;">;
            URLConnection connection ;
            InputStream is ;
            </span><span style="color: #0000ff;">try</span><span style="color: #000000;"> {
                connection </span>= <span style="color: #0000ff;">new</span><span style="color: #000000;"> URL(url).openConnection();
                is </span>=<span style="color: #000000;"> connection.getInputStream();
                </span><span style="color: #008000;">//</span><span style="color: #008000;">为了更清楚的看到加载图片的等待操作,将线程休眠3秒钟.</span>
                Thread.sleep(3000<span style="color: #000000;">);
                BufferedInputStream bis </span>= <span style="color: #0000ff;">new</span><span style="color: #000000;"> BufferedInputStream(is);
                </span><span style="color: #008000;">//</span><span style="color: #008000;">通过decodeStream方法解析输入流</span>
                bitmap =<span style="color: #000000;"> BitmapFactory.decodeStream(bis);
                is.close();
                bis.close();
            } </span><span style="color: #0000ff;">catch</span><span style="color: #000000;"> (IOException e) {
                e.printStackTrace();
            } </span><span style="color: #0000ff;">catch</span><span style="color: #000000;"> (InterruptedException e) {
                e.printStackTrace();
            }
            </span><span style="color: #0000ff;">return</span><span style="color: #000000;"> bitmap;
        }

        </span><span style="color: #008000;">//</span><span style="color: #008000;">onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.</span>
<span style="color: #000000;">        @Override
        </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onPostExecute(Bitmap bitmap) {
            </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onPostExecute(bitmap);
            </span><span style="color: #008000;">//</span><span style="color: #008000;">隐藏progressBar</span>
<span style="color: #000000;">            progressBar.setVisibility(View.GONE);
            </span><span style="color: #008000;">//</span><span style="color: #008000;">更新imageView</span>
<span style="color: #000000;">            imageView.setImageBitmap(bitmap);
        }
    }
}</span></span></pre>
</div>
<p><span style="line-height: 1.5;">activity_main.xml</span></p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">&lt;?</span><span style="color: #ff00ff;">xml version="1.0" encoding="utf-8"</span><span style="color: #0000ff;">?&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">LinearLayout </span><span style="color: #ff0000;">xmlns:android</span><span style="color: #0000ff;">="http://schemas.android.com/apk/res/android"</span><span style="color: #ff0000;">
              android:orientation</span><span style="color: #0000ff;">="vertical"</span><span style="color: #ff0000;">
              android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
              android:gravity</span><span style="color: #0000ff;">="center"</span><span style="color: #ff0000;">
              android:layout_height</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #0000ff;">&gt;</span>
    <span style="color: #0000ff;">&lt;</span><span style="color: #800000;">Button
        </span><span style="color: #ff0000;">android:id</span><span style="color: #0000ff;">="@+id/btn_image"</span><span style="color: #ff0000;">
        android:text</span><span style="color: #0000ff;">="加载图片"</span><span style="color: #ff0000;">
        android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
        android:layout_height</span><span style="color: #0000ff;">="wrap_content"</span><span style="color: #0000ff;">/&gt;</span>
   
<span style="color: #0000ff;">&lt;/</span><span style="color: #800000;">LinearLayout</span><span style="color: #0000ff;">&gt;</span></span></pre>
</div>
<p>progress.xml</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">&lt;?</span><span style="color: #ff00ff;">xml version="1.0" encoding="utf-8"</span><span style="color: #0000ff;">?&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">LinearLayout </span><span style="color: #ff0000;">xmlns:android</span><span style="color: #0000ff;">="http://schemas.android.com/apk/res/android"</span><span style="color: #ff0000;">
              android:orientation</span><span style="color: #0000ff;">="vertical"</span><span style="color: #ff0000;">
              android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
              android:gravity</span><span style="color: #0000ff;">="center"</span><span style="color: #ff0000;">
              android:layout_height</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #0000ff;">&gt;</span>
    <span style="color: #0000ff;">&lt;</span><span style="color: #800000;">ProgressBar
        </span><span style="color: #ff0000;">style</span><span style="color: #0000ff;">="?android:attr/progressBarStyleHorizontal"</span><span style="color: #ff0000;">
        android:id</span><span style="color: #0000ff;">="@+id/progress"</span><span style="color: #ff0000;">
        android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
        android:layout_height</span><span style="color: #0000ff;">="wrap_content"</span><span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;/</span><span style="color: #800000;">LinearLayout</span><span style="color: #0000ff;">&gt;</span></span></pre>
</div>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">由于涉及到网络操作,需要在AndroidManifest.xml中添加网络操作权限:&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</span></p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">运行结果:</span></p>
<p><img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205131104221-1155515176.png" alt="" />&nbsp; <img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205142222346-987142943.jpg" alt="" />&nbsp;&nbsp;<img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205131201268-735733615.png" alt="" />&nbsp; <img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205142447846-707085168.jpg" alt="" />&nbsp;&nbsp;<img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205131225580-1101164220.png" alt="" /></p>
<p>&nbsp;</p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 15px;">下面再演示一个模拟更新进度条的实例.</span></p>
<p>MainActivity.java</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">package</span><span style="color: #000000;"> com.example.caobotao.learnasynctask;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.app.Activity;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.content.Intent;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.Bundle;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.view.View;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.view.View.OnClickListener;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.widget.Button;

</span><span style="color: #0000ff;">public</span> <span style="color: #0000ff;">class</span> MainActivity <span style="color: #0000ff;">extends</span><span style="color: #000000;"> Activity {
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> Button btn_progress;
    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_progress </span>=<span style="color: #000000;"> (Button) findViewById(R.id.btn_progress);
        btn_progress.setOnClickListener(</span><span style="color: #0000ff;">new</span><span style="color: #000000;"> OnClickListener() {
            @Override
            </span><span style="color: #0000ff;">public</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onClick(View v) {
                startActivity(</span><span style="color: #0000ff;">new</span> Intent(MainActivity.<span style="color: #0000ff;">this</span>,ProgressActivity.<span style="color: #0000ff;">class</span><span style="color: #000000;">));
            }
        });
    }
}</span></span></pre>
</div>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>ProgressActivity.java</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">package</span><span style="color: #000000;"> com.example.caobotao.learnasynctask;

</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.app.Activity;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.AsyncTask;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.AsyncTask.Status;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.Bundle;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.widget.ProgressBar;

</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> java.util.Scanner;

</span><span style="color: #008000;">/**</span><span style="color: #008000;">
 * Created by caobotao on 15/12/2.
 </span><span style="color: #008000;">*/</span>
<span style="color: #0000ff;">public</span> <span style="color: #0000ff;">class</span> ProgressActivity <span style="color: #0000ff;">extends</span><span style="color: #000000;"> Activity{
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> ProgressBar progressBar;
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> MyAsyncTask myAsyncTask;
    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        progressBar </span>=<span style="color: #000000;"> (ProgressBar) findViewById(R.id.progress);
        myAsyncTask </span>= <span style="color: #0000ff;">new</span><span style="color: #000000;"> MyAsyncTask();
        myAsyncTask.execute();
    }<br />}

    </span><span style="color: #0000ff;">class</span> MyAsyncTask <span style="color: #0000ff;">extends</span> AsyncTask&lt;Void,Integer,Void&gt;<span style="color: #000000;">{
        @Override
        </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onProgressUpdate(Integer... values) {
            </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onProgressUpdate(values);
            </span><span style="color: #008000;">//</span><span style="color: #008000;">通过publishProgress方法传过来的值进行进度条的更新.</span>
            progressBar.setProgress(values[0<span style="color: #000000;">]);
        }

        @Override
        </span><span style="color: #0000ff;">protected</span><span style="color: #000000;"> Void doInBackground(Void... params) {
            </span><span style="color: #008000;">//</span><span style="color: #008000;">使用for循环来模拟进度条的进度.</span>
            <span style="color: #0000ff;">for</span> (<span style="color: #0000ff;">int</span> i = 0;i &lt; 100; i ++<span style="color: #000000;">){
                </span><span style="color: #008000;">//</span><span style="color: #008000;">调用publishProgress方法将自动触发onProgressUpdate方法来进行进度条的更新.</span>
<span style="color: #000000;">                publishProgress(i);
                </span><span style="color: #0000ff;">try</span><span style="color: #000000;"> {
                    </span><span style="color: #008000;">//</span><span style="color: #008000;">通过线程休眠模拟耗时操作</span>
                    Thread.sleep(300<span style="color: #000000;">);
                } </span><span style="color: #0000ff;">catch</span><span style="color: #000000;"> (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            </span><span style="color: #0000ff;">return</span> <span style="color: #0000ff;">null</span><span style="color: #000000;">;
        }
    }
}</span></span></pre>
</div>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>activity_main.xml</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">&lt;?</span><span style="color: #ff00ff;">xml version="1.0" encoding="utf-8"</span><span style="color: #0000ff;">?&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">LinearLayout </span><span style="color: #ff0000;">xmlns:android</span><span style="color: #0000ff;">="http://schemas.android.com/apk/res/android"</span><span style="color: #ff0000;">
              android:orientation</span><span style="color: #0000ff;">="vertical"</span><span style="color: #ff0000;">
              android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
              android:gravity</span><span style="color: #0000ff;">="center"</span><span style="color: #ff0000;">
              android:layout_height</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #0000ff;">&gt;</span>
    <span style="color: #0000ff;">&lt;</span><span style="color: #800000;">Button
        </span><span style="color: #ff0000;">android:id</span><span style="color: #0000ff;">="@+id/btn_progress"</span><span style="color: #ff0000;">
        android:text</span><span style="color: #0000ff;">="加载进度条"</span><span style="color: #ff0000;">
        android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
        android:layout_height</span><span style="color: #0000ff;">="wrap_content"</span><span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;/</span><span style="color: #800000;">LinearLayout</span><span style="color: #0000ff;">&gt;</span></span></pre>
</div>
<p>progress.xml</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">&lt;?</span><span style="color: #ff00ff;">xml version="1.0" encoding="utf-8"</span><span style="color: #0000ff;">?&gt;</span>
<span style="color: #0000ff;">&lt;</span><span style="color: #800000;">LinearLayout </span><span style="color: #ff0000;">xmlns:android</span><span style="color: #0000ff;">="http://schemas.android.com/apk/res/android"</span><span style="color: #ff0000;">
              android:orientation</span><span style="color: #0000ff;">="vertical"</span><span style="color: #ff0000;">
              android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
              android:gravity</span><span style="color: #0000ff;">="center"</span><span style="color: #ff0000;">
              android:layout_height</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #0000ff;">&gt;</span>
    <span style="color: #0000ff;">&lt;</span><span style="color: #800000;">ProgressBar
        </span><span style="color: #ff0000;">style</span><span style="color: #0000ff;">="?android:attr/progressBarStyleHorizontal"</span><span style="color: #ff0000;">
        android:id</span><span style="color: #0000ff;">="@+id/progress"</span><span style="color: #ff0000;">
        android:layout_width</span><span style="color: #0000ff;">="match_parent"</span><span style="color: #ff0000;">
        android:layout_height</span><span style="color: #0000ff;">="wrap_content"</span><span style="color: #0000ff;">/&gt;</span>
<span style="color: #0000ff;">&lt;/</span><span style="color: #800000;">LinearLayout</span><span style="color: #0000ff;">&gt;</span></span></pre>
</div>
<p>&nbsp;</p>
<p><span style="font-size: 15px;">同样需要在AndroidManifest.xml中添加网络操作权限:&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</span></p>
<p><span style="font-size: 15px;">运行结果:</span></p>
<p><img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205171102502-334710737.png" alt="" />&nbsp; <img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205171400627-1045809191.jpg" alt="" />&nbsp; &nbsp;<img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205171149549-2006380909.png" alt="" />&nbsp; &nbsp;<img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205172621080-270103745.jpg" alt="" />&nbsp; &nbsp;<img src="http://images2015.cnblogs.com/blog/834105/201512/834105-20151205172911064-690353081.png" alt="" /></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">点击'加载进度条'按钮后程序看起来运行正常.但是,正如上面图示,如果接着点击BACK键,紧接着再次点击'加载进度条'按钮,会发现进度条的进度一直是零,过了一会才开始更新.这是为什么呢?</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">根据上述的讲解,我们知道,AsyncTask是基于线程池进行实现的,当一个线程没有结束时,后面的线程是不能执行的.所以必须等到第一个task的for循环结束后,才能执行第二个task.我们知道,当点击BACK键时会调用Activity的onPause()方法.为了解决这个问题,我们需要在Activity的onPause()方法中将正在执行的task标记为cancel状态,在doInBackground方法中进行异步处理时判断是否是cancel状态来决定是否取消之前的task.</span></p>
<p>更改ProgressActivity.java如下:</p>
<div class="cnblogs_code">
<pre><span style="font-size: 13px;"><span style="color: #0000ff;">package</span><span style="color: #000000;"> com.example.caobotao.learnasynctask;

</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.app.Activity;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.AsyncTask;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.AsyncTask.Status;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.os.Bundle;
</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> android.widget.ProgressBar;

</span><span style="color: #0000ff;">import</span><span style="color: #000000;"> java.util.Scanner;

</span><span style="color: #008000;">/**</span><span style="color: #008000;">
 * Created by caobotao on 15/12/2.
 </span><span style="color: #008000;">*/</span>
<span style="color: #0000ff;">public</span> <span style="color: #0000ff;">class</span> ProgressActivity <span style="color: #0000ff;">extends</span><span style="color: #000000;"> Activity{
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> ProgressBar progressBar;
    </span><span style="color: #0000ff;">private</span><span style="color: #000000;"> MyAsyncTask myAsyncTask;
    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        progressBar </span>=<span style="color: #000000;"> (ProgressBar) findViewById(R.id.progress);
        myAsyncTask </span>= <span style="color: #0000ff;">new</span><span style="color: #000000;"> MyAsyncTask();
        </span><span style="color: #008000;">//</span><span style="color: #008000;">启动异步任务的处理</span>
<span style="color: #000000;">        myAsyncTask.execute();
    }

    </span><span style="color: #008000;">//</span><span style="color: #008000;">AsyncTask是基于线程池进行实现的,当一个线程没有结束时,后面的线程是不能执行的.</span>
<span style="color: #000000;">    @Override
    </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onPause() {
        </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onPause();
        </span><span style="color: #0000ff;">if</span> (myAsyncTask != <span style="color: #0000ff;">null</span> &amp;&amp; myAsyncTask.getStatus() ==<span style="color: #000000;"> Status.RUNNING) {
            </span><span style="color: #008000;">//</span><span style="color: #008000;">cancel方法只是将对应的AsyncTask标记为cancelt状态,并不是真正的取消线程的执行.</span>
            myAsyncTask.cancel(<span style="color: #0000ff;">true</span><span style="color: #000000;">);
        }
    }

    </span><span style="color: #0000ff;">class</span> MyAsyncTask <span style="color: #0000ff;">extends</span> AsyncTask&lt;Void,Integer,Void&gt;<span style="color: #000000;">{
        @Override
        </span><span style="color: #0000ff;">protected</span> <span style="color: #0000ff;">void</span><span style="color: #000000;"> onProgressUpdate(Integer... values) {
            </span><span style="color: #0000ff;">super</span><span style="color: #000000;">.onProgressUpdate(values);
            </span><span style="color: #008000;">//</span><span style="color: #008000;">通过publishProgress方法传过来的值进行进度条的更新.</span>
            progressBar.setProgress(values[0<span style="color: #000000;">]);
        }

        @Override
        </span><span style="color: #0000ff;">protected</span><span style="color: #000000;"> Void doInBackground(Void... params) {
            </span><span style="color: #008000;">//</span><span style="color: #008000;">使用for循环来模拟进度条的进度.</span>
            <span style="color: #0000ff;">for</span> (<span style="color: #0000ff;">int</span> i = 0;i &lt; 100; i ++<span style="color: #000000;">){
                </span><span style="color: #008000;">//</span><span style="color: #008000;">如果task是cancel状态,则终止for循环,以进行下个task的执行.</span>
                <span style="color: #0000ff;">if</span><span style="color: #000000;"> (isCancelled()){
                    </span><span style="color: #0000ff;">break</span><span style="color: #000000;">;
                }
                </span><span style="color: #008000;">//</span><span style="color: #008000;">调用publishProgress方法将自动触发onProgressUpdate方法来进行进度条的更新.</span>
<span style="color: #000000;">                publishProgress(i);
                </span><span style="color: #0000ff;">try</span><span style="color: #000000;"> {
                    </span><span style="color: #008000;">//</span><span style="color: #008000;">通过线程休眠模拟耗时操作</span>
                    Thread.sleep(300<span style="color: #000000;">);
                } </span><span style="color: #0000ff;">catch</span><span style="color: #000000;"> (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            </span><span style="color: #0000ff;">return</span> <span style="color: #0000ff;">null</span><span style="color: #000000;">;
        }
    }
}</span></span></pre>
</div>
<p>&nbsp;</p>
<p><span style="font-family: 'Microsoft YaHei'; font-size: 14pt;">.使用AsyncTask的注意事项</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">① 必须在UI线程中创建AsyncTask的实例.</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">②&nbsp;只能在UI线程中调用AsyncTask的execute方法.</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">③ AsyncTask被重写的四个方法是系统自动调用的,不应手动调用.</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">④ 每个AsyncTask只能被执行(execute方法)一次,多次执行将会引发异常.</span></p>
<p><span style="font-size: 15px; font-family: 'Microsoft YaHei';">⑤&nbsp;AsyncTask的四个方法,只有doInBackground方法是运行在其他线程中,其他三个方法都运行在UI线程中,也就说其他三个方法都可以进行UI的更新操作.</span></p>
<p>&nbsp;</p>
<style><!--
.cbt{font-size:15px;}
    .cbt div{line-height: 25px;}
    .cbta{margin-bottom:10px;}
    .cbtb{margin-top:10px;}
    .cus:link {color: #436EEE}
    .cus:visited {color: #436EEE}
    .cus:hover {color: #EE4000}
    .cus:active {color: #436EEE}
--></style>
<div class="cbt" style="background-color: #c1cdc1;">
<div style="border: solid 1px #E5E5E5; padding: 10px; background: #FFFEFE url('http://images.cnblogs.com/cnblogs_com/grenet/226272/o_o_o_info.png') no-repeat scroll 15px 50%; padding-left: 80px;">
<div class="cbta">作者：<a class="cus" style="text-decoration: none;" href="http://www.cnblogs.com/caobotao" target="_blank">caobotao</a></div>
<div>出处： <a class="cus" style="text-decoration: none;" href="http://www.cnblogs.com/caobotao/p/5020857.html" target="_blank"> http://www.cnblogs.com/caobotao/p/5020857.html </a></div>
<div class="cbtb">本文版权归作者和博客园共有，欢迎转载，但未经作者同意必须保留此段声明，且在文章页面明显位置给出原文链接，否则保留追究法律责任的权利。</div>




		