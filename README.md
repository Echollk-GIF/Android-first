[TOC]

# 存在的一些疑惑

## 插件使用

SP/MMKV

findViewById/viewBinding

权限获取PermissionsDispatcher App会一起请求权限还是单独请求？

封装度？

项目已经封装的一些api，包括数据请求、webView等

IDE版本问题，举例No candidates found for method call buildConfigField.



# 坑

https://blog.csdn.net/u013855006/article/details/130402439

在`gradle.properties`文件中增加如下代码：

```
android.defaults.buildfeatures.buildconfig=true
```





Error:SDK location not found. Define location with sdk.dir in the local.properties file or with an ANDROID_HOME environment variable.

https://www.jianshu.com/p/703e10517284



## 网络请求

onFailure: Unable to resolve host "my-cloud-music-api-sp3-dev.ixuea.com": No address associated with hostname

在Android.Manifest中的application中设置android:usesCleartextTraffic="true"

# Tips

宽高单位一般用dp，字体大小单位一般用sp



把一个xml布局变为一个View

```java
View view = getLayoutInflater().inflate(R.layout.XXX,null);
```



在fragment中获取xml使用

```
inflater.inflate(R.layout.fragment_dialog_term_service,container,false);
```





延迟调用，类似setTimeOut，先随便找一个控件然后

```java
      copyrightView.postDelayed(new Runnable() {
        @Override
        public void run() {
          Intent intent = new Intent(SplashActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      },1000);
```



# 做了什么

## 深色模式

谷歌官网深色模式文档

https://developer.android.google.cn/guide/topics/ui/look-and-feel/darktheme

需继承Theme.MaterialComponents.DayNight

定义不同的颜色值在values/themes.xml，values-night/themes.xml，对不同名称的颜色设置不同值

手动引用如下：

```
android:background="?android:attr/colorBackground"
```



非矢量图

在drawable-xxhdpi，drawable-night-xxhdpi分别放白天和夜间模式图片，名称一样。

## 通用控制器整体规划和实现

```
BaseActivity：把onPostCreate逻辑拆分为三个方法，方便管理。
	BaseCommonActivity：不同项目可以复用的逻辑，例如：启动界面等
		BaseLogicActivity：本项目的通用逻辑，例如：背景颜色，全局迷你播放控制等。
			BaseTitleActivity：标题相关。
```

# API

## Toast

默认Toast

```java
Toast.makeText(getHostActivity(),"测试",Toast.LENGTH_LONG).show();
```

封装Toast

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:paddingHorizontal="20dp">

  <LinearLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@drawable/shape_toast_background"
    android:gravity="center_horizontal"
    android:orientation="horizontal"
    android:paddingHorizontal="10dp"
    android:paddingVertical="16dp">

    <TextView
      android:id="@+id/content"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:padding="5dp"
      android:text="提示信息"
      android:textColor="@color/white"
      android:textSize="@dimen/s16" />
  </LinearLayout>
</LinearLayout>

```

```java
package com.ixuea.superui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.ixuea.courses.mymusic.R;

/**
 * 超级Toast
 * <p>
 * 显示效果类似QQ，在顶部显示，宽度和屏幕宽度差不多
 * 显示到顶部的好处是，不会挡住键盘，因为很多时候显示提示的时候，都是用户输入时产生的
 * 如果不想显示到顶部，可以设置gravity，或者用户默认的Toast
 */
public class SuperToast {
  private static Context context;
  private static LayoutInflater inflater;

  public static void init(Context context) {
    SuperToast.context = context.getApplicationContext();
    inflater = LayoutInflater.from(SuperToast.context);
  }

  /**
   * 显示
   *
   * @param content
   */
  public static void show(@StringRes int content) {
    show(context.getString(content), R.drawable.shape_toast_background, Toast.LENGTH_LONG);
  }

  /**
   * 显示
   *
   * @param content
   */
  public static void show(String content) {
    show(content, R.drawable.shape_toast_background, Toast.LENGTH_LONG);
  }

  /**
   * 错误提示
   *
   * @param content
   */
  public static void error(@StringRes int content) {
    show(context.getString(content), R.drawable.shape_toast_error_background, Toast.LENGTH_LONG);
  }

  /**
   * 错误提示
   *
   * @param content
   */
  public static void error(String content) {
    show(content, R.drawable.shape_toast_error_background, Toast.LENGTH_LONG);
  }

  /**
   * 成功提示
   *
   * @param content
   */
  public static void success(@StringRes int content) {
    show(context.getString(content), R.drawable.shape_toast_success_background, Toast.LENGTH_LONG);
  }

  public static void show(String content, @DrawableRes int background, int duration) {
    show(content, background, duration, Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 130);
  }

  public static void show(String content, @DrawableRes int background, int duration, int gravity, int xOffset, int yOffset) {
    Toast toast = new Toast(context);

    //设置时长
    toast.setDuration(duration);

    //设置位置
    toast.setGravity(gravity, xOffset, yOffset);

    // 加载布局
    View layout = inflater.inflate(R.layout.super_toast, null);
    View containerView = layout.findViewById(R.id.container);

    //设置背景
    containerView.setBackgroundResource(background);

    TextView contentView = layout.findViewById(R.id.content);
    contentView.setText(content);

    //设置自定义布局
    toast.setView(layout);

    //显示
    toast.show();
  }
}

```

```java
package com.ixuea.courses.mymusic;

import android.app.Application;

import com.ixuea.superui.toast.SuperToast;

/**
 * 全局Application，只会触发一次
 */
public class AppContext extends Application {

  private static AppContext instance;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;

    //初始化toast工具类
    SuperToast.init(getApplicationContext());
  }

  public static AppContext getInstance(){
    return instance;
  }
}


//注册
//  <application
//    android:name=".AppContext"/>
```

```java
SuperToast.show("加载中");
```



# 笔记项目提示

## 通用控制器整体规划和实现

此处并不是必须的，只是方便逻辑清晰

BaseActivity：把onPostCreate逻辑拆分为三个方法，方便管理。

```java
/**
 * 所有Activity父类
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 找控件
     */
    protected void initViews(){

    }

    /**
     * 设置数据
     */
    protected void initDatum() {

    }

    /**
     * 设置监听器
     */
    protected void initListeners() {

    }

    /**
     * 在onCreate方法后面调用
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }

}
```

## 遇到的一些报错

### Cannot resolve constructor 'Intent(OnClickListener, Class<DialogPageActivity>)'

```java
 //错误代码示例
protected void initListeners() {
    super.initListeners();
    dialog_page_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(this,DialogPageActivity.class);
      }
    });
  }
```

这个报错是由于`Intent`构造函数的参数不正确导致的。具体原因可能是在创建Intent对象时所传递的参数中，第一个参数`OnClickListener`是一个匿名内部类，而`Intent`构造函数的第一个参数要求是一个上下文对象。因此，可以将第一个参数修改为一个普通的`Context`对象，如下所示：

```java
Intent intent = new Intent(context, DialogPageActivity.class); 
```

同时，确保页面已经在`AndroidManifest.xml`文件中进行了注册

所以可以使用例如如下代码

```java
public class MyActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, DialogPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
```

这样，当执行到创建 `Intent` 对象的代码时，就能够正常创建，并跳转到指定的页面。

# 控件

## TextView

基础属性：

layout_width：组件的宽度

layout_height：组件的高度

id: 组件id

text：设置显示的文本内容

textColor：设置字体颜色

textStyle：设置字体风格，三个可选值：normal（无效果）、bold（加粗）、italic（斜体）

textSize：字体大小，单位一般是用sp

background：控件的背景颜色，可以理解为填充整个控件的颜色，可以是图片

gravity：设置控件中内容的对齐方向

lines：几行的高度

lineSpacingMultiplier: 设置行高，举例

```
android:lineSpacingMultiplier="@dimen/line_space"

<!--行高-->
<item name="line_space" format="float" type="dimen">1.2</item>
```

方法：

setText：设置文本内容



tools:text="loading loading loading"这种tools开头的属性不会直接显示在屏幕上，一般适合开发中查看样式。如果设置为正常的android:text=“开发中代码"，则可能会先显示这几个字再变化为新设置的字符



### 动态替换

String里如果某一段内容需要动态替换，可以先用例如%1$d（%1代表第几个，$d代表类型例如$d代表int类型、$s代表字符串）占位再替换

在strings.xml中定义

```xml
<string name="copyright">Copyright © %1$d Shen . All Rights Reserved</string>
```

在java代码中先获取到string内容再动态传入设置

```java
      int year = SuperDateUtil.currentYear();
      TextView copyrightView = findViewById(R.id.copyright);
      copyrightView.setText(getResources().getString(R.string.copyright,year));
```

### getResources()

getResources()是一个非常重要的方法，它用于获取与Context相关联的Resource类，其包含了应用程序中所有非代码资源文件，如布局文件、字符串、颜色定义等。例如，可以使用getResources()方法来获取应用程序的字符串资源，如下所示：

```
String appName = getResources().getString(R.string.app_name);
```

这将返回应用程序的名称字符串。其中R.string.app_name是在res/values/strings.xml文件中定义的字符串资源ID。



## Button

Button继承于TextView，但也有一些区别：

● Button拥有默认的按钮背景，而TextView默认无背景

● Button的内部文本默认居中对齐，而TextView的内部文本默认靠左对齐

● Button会默认将英文字母转为大写，而TextViww保持原始的英文大小写

与TextView相比，Button增加了两个新属性：

● textAllCaps属性，它指定了是否将英文字母转化为大写，为true表示自动转为大写，为false表示不做大写转换

● onClick属性，它用来接管用户的点击动作，指定了点击按钮时要触发哪个方法（过时，可以用但不推荐）



backgroundTint: 设置背景颜色（在安卓开发中，background和backgroundTint都是用于设置按钮背景色的属性。它们之间的区别在于，background是一个可绘制的背景，可以是一个完全可绘制资源的引用（例如图片、可调整大小位图9-patch、XML状态列表描述等），或者是纯色如黑色。而backgroundTint只能用颜色，不能使用图片等可绘制资源。如果您想要使用图片等可绘制资源作为背景，那么您应该使用background属性。如果您只想使用纯色作为背景，那么您可以使用backgroundTint属性）



app:icon 属性用于设置按钮的图标。

app:iconSize 属性用于设置图标的大小。

app:iconTint 属性用于设置图标的颜色。

app:iconGravity 属性用于设置图标的位置，可以是按钮的开始、中间或结束位置

app:iconPadding 属性用于设置padding间距大小

app:strokeWidth 用于设置边框宽度

app:strokeColor 设置边框颜色

app:cornerRadius 设置圆角



enabled: 设置是否允许点击



监听器

点击监听器：通过setOnClickListener方法设置。按钮被按住少于500毫秒时，会触发点击事件。

长按监听器：通过setOnLongClickListener方法设置。按钮被按住超过500毫秒时，会触发长按事件。

```java
btn.setOnClickListener(new View.onClickListener(){
  @Override
  public void onClick(View v){
    //do something
  }
})
```



Tips:

设置颜色后可能无效，这个是版本问题。解决方法是在themes.xml中设置parent后加.Bridge

如果一个按钮没有边框且没有背景颜色（背景颜色设置为透明），还要设置一个属性为style="?android:attr/borderlessButtonStyle"



## EditText

EditText继承TextView，初此之外还有其他属性

inputType：指定输入的文本类型。若同时使用多种文本类型，则可使用竖线“|”把多种文本类型拼接起来。

maxLength：指定文本允许输入的最大长度。

hint：指定提示文本的内容。

textColorHint：指定提示文本的颜色。

drawableXXX：在输入框的指定方位添加图片

drawablePadding：设置图片与输入内容的间距

paddingXxx：设置内容与边框的间距

background：背景色



方法：

获取输入内容

```java
EdidText et = findViewById(R.id.et);
String text = et.getText().toString();
```



## ImageView

属性：

src：设置图片资源

scaleType：设置图片缩放类型

maxHeight：最大高度（需要和adjustViewBounds配合使用）

maxWidth：最大宽度（需要和adjustViewBounds配合使用）

adjustViewBounds：调整View的界限

图片一般放在res/drawable目录下，设置图像显示一般有两种方法：

在XML文件中，通过属性android:src设置图片资源，属性值格式形如 @drawable/不含扩展名的图片名称。

在Java代码中，调用setImageResource方法设置图片资源，方法参数格式形如 R.drawable.不含扩展名的图片名称。



ImageView本身默认图片居中显示，若要改变图片的显示方式，可通过scaleType属性设定，该属性的取值说明如下：

XML中的缩放类型  ScaleType类中 的缩放类型   说明
fitXY  FIT_XY  拉伸图片使其正好填满视图(图片可能被拉伸变形)
fitStart  FIT_START  保持宽高比例，拉伸图片使其位于视图上方或左侧
fitCenter  FIT_CENTER   保持宽高比例，拉伸图片使其位于视图中间
fitEnd  FIT_END  保持宽高比例，拉伸图片使其位于视图下方或右侧
center  CENTER  保持图片原尺寸，并使其位于视图中间
centerCrop  CENTER_CROP  拉伸图片使其充满视图，并位于视图中间
centerInside  CENTER_INSIDE  保持宽高比例，缩小图片使之位于视图中间(只缩小不放大)



## ProgressBar

属性：

max：进度条的最大值

progress：进度条已完成最大值

indeterminate：如果设置成true，则进度条不精确显示进度

style=""?android:attr/progressBarStyleHorizontal"：水平进度条



方法：

判断是显示还是隐藏

```java
if(progressBar.getVisibility() === View.GONE){//表示当前是隐藏的
  progressBar.setVisibility(View.VUSUVKE);//显示出来
}else{
  progressBar.setVisibility(View.GONE);//隐藏
}
```

获取当前进度值 progressBar.getProgress()；

设置当前进度值 progressBar.setProgress();



## Notification和NotificationManager

### 创建一个NotificationManager

NotificationManager类是一个通知管理器类，这个对象是由系统维护的服务，是以单例模式的方式获得，所以一般并不直接实例化这个对象。在Activity中，可以使用Activity.getSystemService(String)方法获取NotificationManager对象，Activity.getSystemService(String)方法可以通过Android系统级服务的句柄，返回对应的对象。在这里需要返回NotificationManager，所以直接传递Context.NOTIFICATION_SERVICE即可。

### 使用Builder构造器来创建Notification对象

使用NotificationCompat类的Builder构造器来创建Notification对象，可以保证程序在所有版本上都能正常工作。Android8.0新增了通知渠道这个概念，如果没有设置，则通知无法在Android8.0的机器上显示

### NotificationChannel

通知渠道，Android8.0新增了通知渠道这个概念，将允许您为要显示的每种通知类型创建用户可自定义的渠道

通知重要程度设置，NotificationManager类中

IMPRTANCE_NONE 关闭通知

IMPRTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示

IMPRTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示

IMPRTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示

IMPRTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示



Notification属性（前三个是必须设置的）：

setContentTitle(String string)设置标题

setContentText(String string)设置文本内容

setSmalllcon(int icon)设置小图标

setLargelcon(Bitmap icon)设置通知的大图标

setColor(int argb)设置小图标的颜色

setContentIntent(PendingIntent intent)设置点击通知后的跳转意图

setAutoCancel(boblean boolean)设置点击通知后自动清除通知

setWhen(long when)设置通知被创建的时间



举例：

```java
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //判断版本8.0
      NotificationChannel channel = new NotificationChannel("scl", "测试通知", NotificationManager.IMPORTANCE_HIGH);
      manager.createNotificationChannel(channel);
    }

    Notification notification = new NotificationCompat.Builder(this, "scl")
      .setContentTitle("官方通知")
      .setContentText("世界那么大，我想去看看")
      .setSmallIcon(R.drawable.XXX)
      .build();
```

```java
//展示、隐藏
  public void sendNotification(View view){
    manager.notify(1,notication); //这个是int型id
  }

  public void cancelNotification(View view){
    manager.cancel(1,notication); //两个id必须一致
  }
```



## ToolBar

属性（注意如果引入的是androidx里面的toolbar则使用app）：

layout_width：宽度

layout_height：高度

background：背景颜色

app:navigationIcon：导航栏图标（一般是返回箭头）

app:title：主标题

app:titleTextColor：主标题文字颜色

app:titleMarginStart：标题距离左面间距

app:subtitle：子标题

app:subtitleTextColor：子标题文字颜色

app:logo：Logo图标



一般系统会自带一个DarkActionBar，不需要的话可以在themes中将其替换为NoActionBar

导航栏图标点击回调方法：

```java
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //do something
      }
    });
```



## AlertDialog

由于AlertDialog没有公开的构造方法，因此必须借助构建器AlertDialog.Builder才能完成参数设置，AlertDialog.Builder的常用方法说明如下。

setIcon：设置对话框的标题图标。

setTitle：设置对话框的标题文本。

setMessage：设置对话框的内容文本。

setView：设置自定义布局。

setPositiveButton：设置肯定按钮的信息，包括按钮文本和点击监听器。

setNegativeButton：设置否定按钮的信息，包括按钮文本和点击监听器。

setNeutralButton：设置中性按钮的信息，包括按钮文本和点击监听器，该方法比较少用。

1. 创建AlertDialog.Builder对象。
2. 调用setIcon()设置图标，setTitle()或setCustomTitle()设置标题。
3. 设置对话框的内容：setMessage()还有其他方法来指定显示的内容。
4. 调用setPositive/Negative/NeutralButton()设置：确定，取消，中立按钮。
5. 调用create()方法创建这个对象，再调用show()方法将对话框显示出来。

```java
public class MyDialogFragment extends DialogFragment {
    private String text;
    private View.OnClickListener positiveListener;
    private View.OnClickListener negativeListener;

    public void setText(String text) {
        this.text = text;
    }

    public void setPositiveListener(View.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(View.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_dialog_fragment, container, false);

        TextView textView = view.findViewById(R.id.text);
        textView.setText(text);

        Button positiveButton = view.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(positiveListener);

        Button negativeButton = view.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(negativeListener);

        return view;
    }

    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}

```

在外部调用时可以这样使用

```java
MyDialogFragment dialog = new MyDialogFragment();
dialog.setText("文本内容");
dialog.setPositiveListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // 点击确认按钮时执行的操作
    }
});
dialog.setNegativeListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // 点击取消按钮时执行的操作
    }
});
dialog.show(getSupportFragmentManager(), "MyDialogFragment");

```



## PopWindow

常用方法：

setContentView(View contentView):设置PopupWindow显示的View

showAsDropDown(View anchor):相对某:个控件的位置(正左下方)，无偏移

showAsDropDown(View anchor, int xoff, int yoff);相对某个控件的位置，有偏移

setFocusable(boolean focusable)设置是否获取焦点

setBackgroundDrawable(Drawable background)设置背景

dismiss()关闭弹窗

setAnimationStyle(int animationStyle)设置加载动画

setTouchable(boolean touchable)设置触摸使能

setOutsideTouchable(boolean touchable)设置PopupWindow外面的触摸使能

```java
    View popupView = getLayoutInflater().inflate(R.layout.activity_main, null);

    //这里可以链式调用也可以按参数传入
    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT,true);
    popupWindow.showAsDropDown(view);
```



## ListView

ListView同样通过setAdapter方法设置列表项的数据适配器，调用setOnItemClickListener方法设置列表项的点击监听器OnItemClickListener，有时也调用setOnItemLongClickListener方法设置列表项的长按监听器OnItemLongClickListener。

往列表视图填充数据也很容易，先利用基本适配器实现列表适配器，再调用setAdapter方法设置适配器对象。

```java
public class PlanetBaseAdapter extends BaseAdapter {
   private Context mContext; // 声明一个上下文对象
   private List<Planet> mPlanetList; // 声明一个行星信息列表
   // 行星适配器的构造方法，传入上下文与行星列表
   public PlanetBaseAdapter(Context context, List<Planet> planet_list) {
       mContext = context;
       mPlanetList = planet_list;
 }
   // 获取列表项的个数
   public int getCount() {
       return mPlanetList.size();
 }
   // 获取列表项的数据
   public Object getItem(int arg0) {
       return mPlanetList.get(arg0);
 }
   // 获取列表项的编号
   public long getItemId(int arg0) {
       return arg0;
 }
   // 获取指定位置的列表项视图
   public View getView(final int position, View convertView, ViewGroup parent) 
{
       ViewHolder viewholder;
       if (convertView == null) { // 转换视图为空
           viewholder = new ViewHolder(); // 创建一个新的视图持有者 
           // 根据布局文件item_list.xml生成转换视图对象
           convertView =
LayoutInflater.from(mContext).inflate(R.layout.item_list, null);
           viewholder.iv_icon = convertView.findViewById(R.id.iv_icon);
           viewholder.tv_name = convertView.findViewById(R.id.tv_name);
           viewholder.tv_desc = convertView.findViewById(R.id.tv_desc);
           convertView.setTag(viewholder); // 将视图持有者保存到转换视图当中
      } else { // 转换视图非空
           // 从转换视图中获取之前保存的视图持有者
           viewholder = (ViewHolder) convertView.getTag();
    }
       Planet planet = mPlanetList.get(position);
       viewholder.iv_icon.setImageResource(planet.image); // 显示行星的图片
       viewholder.tv_name.setText(planet.name); // 显示行星的名称
       viewholder.tv_desc.setText(planet.desc); // 显示行星的述
       viewholder.iv_icon.requestFocus();
       return convertView;
 }
   // 定义一个视图持有者，以便重用列表项的视图资源
   public final class ViewHolder {
      //这里是所有子控件
       public ImageView iv_icon; // 声明行星图片的图像视图对象
       public TextView tv_name; // 声明行星名称的文本视图对象
       public TextView tv_desc; // 声明行星述的文本视图对象
 } 
}
```

```java
// 初始化行星列表的下拉框
private void initPlanetSpinner() {
   // 获取默认的行星列表，即水星、金星、地球、火星、木星、土星
   planetList = Planet.getDefaultList();
   // 构建一个行星列表的适配器
   PlanetBaseAdapter adapter = new PlanetBaseAdapter(this, planetList);
   // 从布局文件中获取名叫sp_planet的下拉框
   Spinner sp_planet = findViewById(R.id.sp_planet);
   sp_planet.setPrompt("请选择行星"); // 设置下拉框的标题
   sp_planet.setAdapter(adapter); // 设置下拉框的列表适配器
   sp_planet.setSelection(0); // 设置下拉框默认显示第一项
   // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
   sp_planet.setOnItemSelectedListener(new MySelectedListener()); 
}
```



## RecyclerView

RecyclerView是一个灵活的视图，用于提供大型数据集的有限窗口。它是一种视图本身，因此您可以像添加任何其他UI元素一样将RecyclerView添加到布局中。列表中的每个单独元素由一个视图持有者对象定义。当视图持有者被创建时，它没有任何与之关联的数据。

### 基础使用

在使用RecyclerView之前，需要在build.gradle文件中添加以下依赖项：

```
implementation 'androidx.recyclerview:recyclerview:1.2.0'
```

接下来，需要在布局文件中添加RecyclerView

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

再添加一个`item_list.xml`的布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="29dp"
        android:layout_marginTop="33dp"
        android:text="标题"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="4dp"
        android:layout_marginTop="18dp"
        android:text="内容"
        app:layout_constraintStart_toStartOf="@+id/textView"
        app:layout_constraintTop_toBottomOf="@+id/textView" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

定一个实体类如下：

```java
public class News {
    public String title; // 标题
    public String content; //内容
}
```

定义内部类 `ViewHolder`类、 `MyAdapter`类以及设置`RecyclerView`相关逻辑

```java
public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<News> mNewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview);
        // 构造一些数据
        for (int i = 0; i < 50; i++) {
            News news = new News();
            news.title = "标题" + i;
            news.content = "内容" + i;
            mNewsList.add(news);
        }
        mMyAdapter = new MyAdapter(mNewsList);
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            News news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            holder.mTitleContent.setText(news.content);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        TextView mTitleContent;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            mTitleContent = itemView.findViewById(R.id.textView2);
        }
    }
}

```

### 多种Item类型

如果RecyclerView有多种不同的item类型，那么可以在Adapter中重写`getItemViewType`方法来返回不同的view类型。然后，在`onCreateViewHolder`方法中，根据view类型来创建不同的ViewHolder并使用不同的布局文件。

```java
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    // ...

    @Override
    public int getItemViewType(int position) {
        // 根据位置返回不同的view类型
        if (position % 2 == 0) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据view类型创建不同的ViewHolder
        if (viewType == TYPE_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == TYPE_TWO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_two, parent, false);
            return new ViewHolderTwo(view);
        }
        // ...
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 根据view类型绑定数据
        if (holder instanceof ViewHolderOne) {
            // 绑定ViewHolderOne的数据
        } else if (holder instanceof ViewHolderTwo) {
            // 绑定ViewHolderTwo的数据
        }
        // ...
    }

    // 定义ViewHolderOne和ViewHolderTwo类
    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            // 初始化视图
        }
    }

    static class ViewHolderTwo extends RecyclerView.ViewHolder {
        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            // 初始化视图
        }
    }
}

```

### 布局管理器

`RecyclerView`提供了三种布局管理器即：

- LinearLayoutManager 线性布局管理器
- StaggeredGridLayoutManager 瀑布流布局管理器
- GridLayoutManager 网格布局管理器

#### 线性布局管理器

这三种布局管理器都是通过`setLayoutManager`方法来设置
LinearLayoutManager 还可以设置横向滚动，只需将前面`MainActivity`中的`layoutManager`加一句代码即可：

```java
 LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
 layoutManager.setOrientation(RecyclerView.HORIZONTAL);
 mRecyclerView.setLayoutManager(layoutManager);
```

#### 网格布局管理器

如果让一行显示多个，可以设置 `GridLayoutManager`网格布局管理器来实现

```java
  GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,3);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);  也能设置横向滚动
  mRecyclerView.setLayoutManager(layoutManager);

```

#### ItemDecoration

通过给 设置ItemDecoration 来装饰Item的效果，比如我们要设置间隔线

```java
DividerItemDecoration mDivider = new    
                        DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
mRecyclerView.addItemDecoration(mDivider);
```



## ViewPager

### 轮播图

ViewPager一般和Fragment配合使用的情况比较多，例如要实现一个轮播图效果过程

（1）在xml中引入ViewPager

```xml
    <!--左右滚动控件-->
    <androidx.viewpager.widget.ViewPager
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />
```

（2）创建引导页面fragment及xml

```java
public class GuideFragment extends BaseFragment {

  private ImageView icon;

  public static GuideFragment newInstance(Integer data) {

    Bundle args = new Bundle();
    args.putInt("ID",data);

    GuideFragment fragment = new GuideFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected void initViews() {
    super.initViews();
    icon = getView().findViewById(R.id.icon);
  }

  @Override
  protected void initDatum() {
    super.initDatum();
    int data = getArguments().getInt("ID");
    icon.setImageResource(data);
  }

  @Override
  protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_guide, container, false);
  }
}

```

```java
/**
 * Fragment通用父类
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 找控件
     */
    protected void initViews() {

    }

    /**
     * 设置数据
     */
    protected void initDatum() {

    }

    /**
     * 绑定监听器
     */
    protected void initListeners() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取view
        View view = getLayoutView(inflater, container, savedInstanceState);

        //返回view
        return view;
    }

    /**
     * 获取view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * View创建了
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }
}

```

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!--图片控件-->
    <ImageView
        android:id="@+id/icon"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:src="@drawable/splash_logo" />
</FrameLayout>
```

（3）创建适配器

```java
public class GuideAdapter extends FragmentStatePagerAdapter {

  private List<Integer> datum = new ArrayList<>();


  /**
   * 构造方法
   *
   * @param fm
   */
  public GuideAdapter(@NonNull FragmentManager fm) {
    super(fm);
  }

  /**
   * 返回当前位置的Fragment
   *
   * @param position
   * @return
   */
  @NonNull
  @Override
  public Fragment getItem(int position) {
    Integer data = datum.get(position);
    return GuideFragment.newInstance(data);
  }

  /**
   * 有多少个
   *
   * @return
   */
  @Override
  public int getCount() {
    return datum.size();
  }

  public void setDatum(List<Integer> datum) {
    if (datum != null && datum.size() > 0) {
      this.datum.clear();
      this.datum.addAll(datum);

      //通知数据改变了
      notifyDataSetChanged();
    }
  }
}

```

（4）在Activity中设置适配器

```java
  protected void initDatum() {
    super.initDatum();
    //创建适配器
    adapter = new GuideAdapter(getSupportFragmentManager());
    //设置适配器到控件
    list.setAdapter(adapter);

    //准备数据
    List<Integer> datum = new ArrayList<>();
    datum.add(R.drawable.guide2);
    datum.add(R.drawable.guide2);
    datum.add(R.drawable.guide2);
    datum.add(R.drawable.guide2);
    datum.add(R.drawable.guide2);

    //设置数据到适配器
    adapter.setDatum(datum);
  }
```

### Tab栏切换

由于TabLayout和TabLayout内部组件TabItem在material包中，需要添加material包的依赖才能使用。打开项目app下的build.gradle，在闭包dependencies中添加下面一行代码

```
implementation 'com.google.android.material:material:1.1.0'
```

布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tab_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/colorWhite">

        <com.google.android.material.tabs.TabItem
            android:id="@+id/item_active"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="新闻" />

        <com.google.android.material.tabs.TabItem
            android:id="@+id/item_news"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="资源" />

        <com.google.android.material.tabs.TabItem
            android:id="@+id/item_resouece"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="活跃达人" />
    </com.google.android.material.tabs.TabLayout>


    <androidx.viewpager.widget.ViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></androidx.viewpager.widget.ViewPager>

</androidx.coordinatorlayout.widget.CoordinatorLayout>

```

我们使用约束布局。首先添加TabLayout，宽度选择适应父布局，高度选择自适应。然后想要滑动切换视图，必须在TabLayout中加入TabItem组件，有多少个页面进行切换，就加入多少个TabItem。最后加入ViewPager，让他充满整个布局。

Activity代码

```java
//Activity中我们大致可能分成两步走：
//1. 为ViewPager 创建Adapter
//2. ViewPager 与TabLayout的绑定
public class CommunityFragment extends Fragment {

    DemoCollectionPagerAdapter demoCollectionPagerAdapter;
    ActiveFragment activeFragment = new ActiveFragment();
    NewsFragment newsFragment = new NewsFragment();
    ResourceFragment resourceFragment = new ResourceFragment();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_community, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //ViewPager 与TabLayout的绑定
        demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getChildFragmentManager());
      //获取界面布局文件中ViewPager 和TabLayout 组件
        ViewPager viewPager = view.findViewById(R.id.pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        if (viewPager == null) {
            return;
        } else {
          //为ViewPager设置Adapte
            viewPager.setAdapter(demoCollectionPagerAdapter);
          //在ViewPager中使用addOnPageChangeListener方法绑定TabLayout。在TabLayout中使用addOnTabSelectedListener方法绑定ViewPager，这样即可完成ViewPager与TabLayout的结合
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        }
    }
  
  //创建Adapter
    public class DemoCollectionPagerAdapter extends FragmentPagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return newsFragment;
            }
            if (i == 1) {
                return resourceFragment;
            }
            return activeFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

```

## HorizontalScrollView

```xml
<?xml version="1.0" encoding="utf-8"?>
<HorizontalScrollView xmlns:android="http://schemas.android.com/apk/res/android"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:paddingVertical="@dimen/padding_outer"
  android:scrollbars="none">

  <LinearLayout
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:paddingHorizontal="@dimen/padding_meddle">

  </LinearLayout>
</HorizontalScrollView>

```



## cardView

CardView 是一个用于实现卡片式布局效果的重要控件，它继承自 FrameLayout 类，具有圆角背景和阴影，看上去有立体效果。

使用 CardView 时，需要注意以下几点：

1. 首先，需要在项目中添加 CardView 的依赖项。在应用或模块的 build.gradle 文件中添加所需工件的依赖项：`implementation "androidx.cardview:cardview:1.0.0"`。
2. 在布局文件中创建 CardView 控件，使用方法跟 ViewGroup 一致，可以在 CardView 中添加一系列的子控件。一般情况下，把 CardView 当做一个父容器，里面可以包含其他子 View，在 ListView 或者 RecyclerView 中充当 item 布局使用。

下面是一个简单的例子，它演示了如何使用 CardView 来创建一个卡片式布局：

```xml
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="5dp"
    app:contentPadding="8dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <TextView
            android:id="@+id/tv_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Jaynm"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/tv_time"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="2020-01-18"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="@+id/tv_name" />

        <TextView
            android:id="@+id/tv_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="一个移动端了开发者，对未来的思考"
            app:layout_constraintStart_toStartOf="@+id/tv_name"
            app:layout_constraintTop_toBottomOf="@+id/tv_name" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</androidx.cardview.widget.CardView>
```

CardView 还有许多其他属性可以设置，例如 `card_view:cardCornerRadius` 可以设置圆角大小；`card_view:cardElevation` 可以设置 z 轴的阴影；`card_view:cardBackgroundColor` 可以设置背景颜色等等。

## 底部向上的弹窗

```java
public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 指定您的自定义布局
        View view = inflater.inflate(R.layout.my_bottom_sheet_layout, container, false);

        // 获取并设置 ImageView 的图片来源
        ImageView imageView = view.findViewById(R.id.delete_all);
        imageView.setImageResource(R.drawable.your_image);

        // 获取并设置 TextView 的文本内容
        TextView textView = view.findViewById(R.id.loop_model);
        textView.setText("您的文本内容");

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        // 调用 show 方法来显示弹窗
        show(fragmentManager, "MyBottomSheetDialogFragment");
    }
}

```

```java
MyBottomSheetDialogFragment myBottomSheetDialogFragment = new MyBottomSheetDialogFragment();
myBottomSheetDialogFragment.show(getSupportFragmentManager());
```



# Shape

在安卓开发中，可以使用Shape定义各种各样的形状，也可以定义一些图片资源。相对于传统图片来说，使用Shape可以减少资源占用，减少安装包大小，还能够很好地适配不同尺寸的手机

Shape有以下几种属性：

1. **shape**: 定义形状，默认为矩形，可以设置为矩形（rectangle）、椭圆形（oval）、线性形状（line）、环形（ring）。
2. **corners**: 定义圆角的半径。
3. **gradient**: 定义渐变效果。
4. **padding**: 定义内边距。
5. **size**: 定义大小。
6. **solid**: 定义填充颜色。
7. **stroke**: 定义边框。



每个属性都有一些子属性，下面是各个属性的详细解释：

1. shape：shape的形状，默认为矩形，可以设置为矩形（rectangle）、椭圆形 (oval)、线性形状 (line)、环形 (ring) android:shape= ["rectangle" | "oval" | "line" | "ring"] 下面的属性只有在android:shape="ring时可用： android:innerRadius 尺寸，内环的半径。
2. corners：corners是用来设置圆角的半径的，可以单独设置每个角的半径。corners有以下子属性：
   - android:radius：设置四个角的半径。
   - android:topLeftRadius：左上角的半径。
   - android:topRightRadius：右上角的半径。
   - android:bottomLeftRadius：左下角的半径。
   - android:bottomRightRadius：右下角的半径。
3. gradient：gradient是用来设置渐变效果的，有以下子属性：
   - android:startColor：渐变开始颜色。
   - android:endColor：渐变结束颜色。
   - android:centerColor：渐变中间颜色。
   - android:type：渐变类型，有linear和radial两种类型。
   - android:angle：渐变方向，0表示从左到右，90表示从上到下。
4. padding：padding是用来设置内边距的，有以下子属性：
   - android:left：左内边距。
   - android:right：右内边距。
   - android:top：上内边距。
   - android:bottom：下内边距。
5. size：size是用来设置大小的，有以下子属性：
   - android:width：宽度。
   - android:height：高度。
6. solid：solid是用来设置填充颜色的，有以下子属性：
   - android:color：填充颜色。
7. stroke：stroke是用来设置边框的，有以下子属性：
   - android:width：边框宽度。
   - android:color：边框颜色。



以下是一些例子：

1.矩形

```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FF0000" />
    <corners android:radius="10dp" />
    <stroke
        android:width="2dp"
        android:color="#000000" />
</shape>

```

2.圆形

```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#FF0000" />
    <size
        android:width="50dp"
        android:height="50dp" />
</shape>
```

3.渐变

```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <gradient
        android:startColor="#FF0000"
        android:endColor="#00FF00"
        android:type="linear"
        android:angle="0" />
</shape>

```

# 下拉刷新

添加控件

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:overScrollMode="never"/>
    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

    ...
</FrameLayout>
```

配置

```java
//刷新箭头颜色
binding.refresh.setColorSchemeResources(R.color.primary);

//刷新圆圈颜色
binding.refresh.setProgressBackgroundColorSchemeResource(R.color.white);
```

刷新监听器

```java
binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        loadData();
    }
});
```

结束刷新

```java
private void endRefresh() {
    binding.refresh.setRefreshing(false);
}
```



# 布局

## Tips

剩余部分均分可用占位+权重方式如

```xml
    <View
      android:layout_width="0dp"
      android:layout_height="0dp"
      android:layout_weight="1" />
```



## LinearLayout

属性：

orientation 布局中组件的排列方式

gravity 控制组件所包含的子元素的对齐方式，可多个组合

layout_gravity 控制该组件在父容器里的对齐方式

background 为该组件设置一个背景图片，或者是直接用颜色覆盖

divider 分割线

showDivides 设置分割线所在的位置，none（无），beginning（开始），end（结束），middle（每两个组件间）

dividerPadding设置分割线的padding

layout_weight（权重） 该属性是用来等比例的划分区域



## RelativeLayout

相对布局中的视图位置由两个因素所影响：

● 与该视图平级的其他视图

● 上级视图（也就是它归属的RelativeLayout）

如果不设定下级视图的参照物，那么下级视图默认显示在RelativeLayout内部的左上角



常见属性：

根据父容器定位：

layout_alignParentLeft 左对齐

layout_alignParentRight 右对齐

layout_alignParentTop 顶部对齐

layout_alignParentBottom 底部对齐

layout_centerHorizontal 水平居中

layout_centerVertical 垂直居中

layout_centerInParent 中间位置

根据兄弟组件定位：

layout_toLeftOf 放置于参考组件的左边

layout_toRightOf 放置于参考组件的右边

layout_above 放置于参考组件的上方

layout_below 放置于参考组件的下方

layout_alignTop 对齐参考组件的上边界

layout_alignBottom 对齐参考组件的下边界

layout_alignLeft 对齐参考组件的左边界

layout_alignRIght 对齐参考组件的右边界



## GridLayout

网格布局支持多行多列的表格排列

网格布局默认从左往右、从上到下排列

属性：

orientation，设置水平显示还是垂直显示

columnCount属性，它指定了网格的列数，即每行能放多少个视图

rowCount属性，它指定了网格的行数，即每列能放多少个视图



子控件属性：

layout_column 显示在第几列

layout_columnSpan 横向跨几列

layout_columnWeight 横向剩余空间分配方式

layout_gravity 在网格中的显示位置

layout_row 显示在第几行

layout_rowSpan 横向跨几行

layout_rowWeight 纵向剩余空间分配方式



## DrawerLayout

DrawerLayout 是一个布局控件，它允许放置两个直接子控件。第一个子控件是主屏幕中显示的内容，第二个是滑动菜单中显示的内容。

使用 DrawerLayout 时，有几点需要注意：

1. 主内容区要放在侧边菜单前面。
2. 主内容区最好以 DrawerLayout 为界面的根布局，否则可能会出现触摸事件被屏蔽的问题。
3. 侧滑菜单部分的布局必须设置 layout_gravity 属性，表示侧滑菜单是在左边还是右边。

下面是一个简单的例子，它演示了如何使用 DrawerLayout 来创建一个从左边滑出的抽屉视图（侧滑栏）：

```xml
<androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:openDrawer="start">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:text="我是主页" />

    </RelativeLayout>

    <RelativeLayout
        android:layout_width="250dp"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="#ffffff">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:text="我是侧滑栏" />

    </RelativeLayout>

</androidx.drawerlayout.widget.DrawerLayout>
```

需要注意的是，DrawerLayout 要设置 `tools:openDrawer="start"`；而且侧滑栏 layout 要设置 `android:layout_gravity="start"`。如果改成 `tools:openDrawer="end"`，侧滑栏 layout 要设置 `android:layout_gravity="end"`。这样，侧滑栏就可以从右边滑出了。



显示侧滑

```java
//打开侧滑按钮点击
binding.content.leftButton.setOnClickListener(v -> {
    binding.drawer.openDrawer(GravityCompat.START);
});
```

隐藏

```java
private void closeDrawer() {
    binding.drawer.closeDrawer(GravityCompat.START);
}
```

返回处理

```java
/**
 * 关闭界面时调用
 */
@Override
public void onBackPressed() {
    if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
        //如果侧滑打开了，返回关闭侧滑，不关闭应用
        closeDrawer();
        return;
    }
    super.onBackPressed();
}
```

# Activity

## 跳转和结束

Activity的启动这里指的是跳转，从一个页面跳转到一个新的页面，就相当于启动了一个新的页面。从当前页面跳到新页面，跳转代码如下：

```java
startActivity(new Intent(源页面.this,目标页面.class))
```

从当前页面回到上一个页面，相当于关闭当前页面，返回代码如下

```java
finish();//结束当前的活动页面
```

## 生命周期

onCreate：创建活动，此时将页面布局加载到内存中，进入了初始状态

onStart：开始活动，将页面展示在屏幕，进入了就绪状态

onResume：恢复活动，活动页面进入活跃状态，此时页面能够和用户进行交互。

onPause：暂停活动，页面进入暂停状态，无法和用户进行交互。

onStop：停止活动，页面将不在屏幕显示。

onDestory：销毁活动，回收Activity占用的资源，将页面从内存中清除。

onRestart：重启活动，重新加载内存中的页面数据。

onNewIntent：重用已存在的活动实例。如果一个Activity已经启动了，并且存在于当前栈，而当前栈的启动模式为SingleTask，SingleInstance，SingleTop（此时在任务栈顶端），那么再次启动该Activity的话，并不会重新进行onCreate，而是会执行onNewIntent方法。

## 启动模式

Android允许在创建Activity时设置启动模式，通过启动模式控制Activity的出入栈行为。

静态设置设置方式：打开AndroidManifest.xml文件，给activity添加属性android:launchMode。如以下表示该activity使用standard标准模式，默认也是标准模式。

动态设置设置方式：通过 Intent 动态设置 Activity启动模式：intent.setFlags(Intent.XXX);

Activity之间传递信息

## Activity之间传递信息

Intent是各个组件之间信息沟通的桥梁，能够让Android各组件之间进行沟通。

Intent可以完成3部分工作：

● 表明本次通信从哪里来，往哪里走，要怎么走。

● 发送方可以携带消息给接收方，接收方可以从收到的Intent解析数据。

● 发送方如果想要知道接收方的处理结果，接收方也可以通过Intent返回结果。



Intent主要元素如下：

元素名称    设置方法    说明与用途
Component    setComponent    组件，它指定意图的来源与目标
Action     setAction    动作，它指定意图的动作行为
Data    setData    即Uri,它指定动作要操纵的数据路径
Category    addCategory    类别，它指定意图的操作类别
Type    setType    数据类型，它指定消息的数据类型
Extras    putExtras    扩展信息，它指定装载的包裹信息
Flags    setFlags    标志位，它指定活动的启动标志



分为显式Intent和隐式Intent

### 显式Intent

在Intent的构造函数中指定：

```java
Intent intent = new Intent(this, NextActivity.class);
startActivity(intent)
```

调用setClass指定：

```java
Intent intent = new Intent();
intent.setClass(this, NextActivity.class);
startActivity(intent)
```

调用setComponent指定

```java
Intent intent = new Intent();
ComponentName component = new ComponentName(this, NextActivity.class);
intent.setComponent(component);
startActivity(intent)
```

### **隐式Intent**

没有明确指定所要跳转的页面，而是通过一些动作字符串来让系统自动匹配。通常是App不想向外暴露Activity的名称，只给出一些定义好的字符串。这些字符串可以自己定义，也有系统定义的。

常见的系统动作如下：

Intent类的系统动作常量名    系统动作的常量值    说明
ACTION_ MAIN    android.intent.action.MAIN    App启动时的入口
ACTION VIEW    android.intent. action.VIEW    向用户显示数据
ACTION_ SEND    android.intent.action.SEND    分享内容
ACTION_ CALL    android.intent.action.CALL    直接拨号
ACITON_ DIAL    android.intent.action.DIAL    准备拨号
ACTION_ SENDTO    android.intent.action.SENDTO    发送短信
ACTION ANSWER    android.intent.action.ANSWER    接听电话



下面以调用系统拨号页面举例：

```java
//发送
String phone = "12345";
Intent intent = new Intent();
//这里表示设置意图动作为准备拨号
intent.setAction(Intent.ACTION_DIAL);//后面通过getIntent().getAction()获取
Uri uri = Uri.parse("phone:"+phone);
intent.setData(uri);
startActivity(intent);

//获取
String action = getIntent().getAction();
if(Constant.ACTION_PHONE.equals(action)){
  //do something
}
```

## **向下一个Activity发送消息**

Intent使用Bundle对象存放待传递的数据信息

Intent重载了很多putExtra方法用于传递各种类型的信息，包括整数类型，字符串等。但是显然通过调用putExtra方法会很不好管理，因为数据都是零碎传递。所以Android引入了Bundle，其内部是一个Map，使用起来也和Map一样。

```java
Intent intent = new Intent(this, NextActivity.class);
//通过bundle包装数据
Bundle bundle = new Bundle();
bundle.putString("stringKey", "stringValue");
intent.putExtras(bundle);
startActivity(intent);
```

然后下一个Activity就可以通过intent获取到所想要的数据了：

```java
Bundle bundle = getIntent().getExtras();
String stringValue = bundle.getString("stringKey");
```

## **向上一个Activity返回消息**

处理下一个页面的应答数据，详细步骤说明如下：

● 上一个页面打包好请求数据，调用startActivityForResult方法（过时，用registerForActivityResult）执行跳转动作

● 下一个页面接收并解析请求数据，进行相应处理

● 下一个页面在返回上一个页面时，打包应答数据并调用setResult方法返回数据包裹

● 上一个页面重写方法onActivityResult，解析获得下一个页面的返回数据

```java
private ActivityResultLauncher<Intent> register;

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

    findViewById(R.id.bt).setOnClickListener(this);

    //回调函数，返回到这个页面时所执行的程序
    register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
       //回调函数
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Intent intent = result.getData();
                        if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                            //获取到返回的数据
                            Bundle bundle = intent.getExtras();
                            //...
                        }
                    }
                }
            });
}

@Override
public void onClick(View v) {
    Intent intent = new Intent(this, MainActivity3.class);
    //跳转下一页面
    register.launch(intent);

}
```

下一个页面接收到数据，处理之后返回结果给上一个页面

```java
Bundle bundle = getIntent().getExtras();
//...页面进行处理
//返回数据给上一个页面
Bundle bundle = new Bundle();
bundle.putString("stringKey", "stringValue");
intent.putExtras(bundle);
setResult(Activity.RESULT_OK, intent);
finish();
```



# Fragment



在Fragment直接使用findViewById不会有提示，要调用getView().findViewById()。这个问题其实可以进行一下封装来解决，不过影响不大。



在Fragment获取this可以使用getActivity()

在Fragment中调用getActivity().finish()其实是把Fragment所在的activity关闭



获取Fragment的XML可用

```java
  protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.super_round_dialog_loading, container, false);
  }
```

静态添加Fragment：在activity的xml中引入fragment

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

  <fragment
    android:name="com.example.myapplication.BlankFragment1"
    android:id="@+id/fragment1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
</LinearLayout>
```

动态添加Fragment

```xml
  <fragment
    android:id="@+id/fragment1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

```java
    replaceFragment(new BlankFragment1());

  //动态切换fragment
  private void replaceFragment(Fragment fragment) {
    //fragment管理类
    FragmentManager fragmentManager = getSupportFragmentManager();
    //fragment的替换动作由完成
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    //创建replace事件
    transaction.replace(R.id.fragment1,fragment);
    //提交replace
    transaction.commit();
  }
```

## Activity与Fragment通信

### 原生方案 Bundle

Activity发送给Fragment

```java
//发送
Bundle bundle = new Bundle();
bundle.putString("message","我我我");
BlankFragment1 bf = new BlankFragment1();
bf.setArguments(bundle);
replaceFragment(bf);
```

```java
//接收
Bundle bundle = this.getArguments();
bundle.getString("message");
```

### 接口

定义接口

```java
public interface IFragmentCallback {
  void sendMessageToActivity(String msg);
  String getMsgFromActivity(String msg);
}
```

在fragment中创建

```java
    private IFragmentCallback fragmentCallback;
    public void setFragmentCallback(IFragmentCallback callback){
      fragmentCallback = callback;
    }
```

在activity中创建

```java
    BlankFragment1 bf = new BlankFragment1();
    bf.setFragmentCallback(new IFragmentCallback() {
      @Override
      public void sendMessageToActivity(String msg) {
         //do something
      }

      @Override
      public String getMsgFromActivity(String msg) {
        //do something
        return "xxxx";
      }
    });
    replaceFragment(bf);
```

fragment发送数据给activity

```java
fragmentCallback.sendMessageToActivity("hello");
```

fragment从activity拿到数据

```java
fragmentCallback.getMsgFromActivity("null");
```

## Fragment生命周期

打开界面

onCreate->onCreateView()->onActicityCreated()->onStart()->onResume()

按下主屏键

onPause()->onStop()

重新打开界面

onStart()->onResume()

按后退键

onPause()->onStop()->onDestoryView()->onDestory()->onDetach()



## DialogFragment

设置点击屏幕外不关闭为setCancelable(false);

关闭弹窗可调用dismiss()。注意：dismiss()是Dialog类的一个方法，主要作用是让dialog从屏幕上消失。调用dismiss()方法后，会删除视图，调用Onstop回调，并把mShowing置为false。如果您想要隐藏对话框而不是销毁它，可以使用hide()方法。hide()方法仅仅隐藏了对话框并没有销毁，如果打算用这方法来灭掉对话框就会出现问题，在Activity销毁的时候就会出现崩溃日志了，因为Activity销毁时是需要把对话框都关闭掉的。

Acticity系统会自动创建，而Fragment需要我们手动去创建

对于Fragment来说，让Fragment显示首先要创建Fragment的newInstance方法，对于Fragment来说推荐调用newInstance方法去创建而不是直接new一个。在newInstance里面可以传参数

```java
/**
 * 所有DialogFragment对话框父类
 */
public abstract class BaseDialogFragment extends DialogFragment {
    /**
     * 找控件
     */
    protected void initViews() {
    }

    /**
     * 设置数据
     */
    protected void initDatum() {
    }

    /**
     * 绑定监听器
     */
    protected void initListeners() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取view
        View view = getLayoutView(inflater, container, savedInstanceState);

        //返回view
        return view;
    }

    /**
     * 获取view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * View创建了
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }
}

```



```java
/**
 * 服务条款和隐私协议对话框
 */
public class TermServiceDialogFragment extends BaseViewModelDialogFragment<FragmentDialogTermServiceBinding> {

    private static final String TAG = "TermServiceDialogFragment";

    private View.OnClickListener onAgreementClickListener;

    public static TermServiceDialogFragment newInstance() {

        Bundle args = new Bundle();

        TermServiceDialogFragment fragment = new TermServiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager
     * @param onAgreementClickListener 同意按钮点击回调
     */
    public static void show(FragmentManager fragmentManager, View.OnClickListener onAgreementClickListener) {
        //创建fragment
        TermServiceDialogFragment fragment = newInstance();

        fragment.onAgreementClickListener = onAgreementClickListener;

        //显示
        //TAG只是用来查找Fragment的
        fragment.show(fragmentManager, "TermServiceDialogFragment");
    }

    @Override
    protected void initViews() {
        super.initViews();
        //点击弹窗外边不能关闭
        setCancelable(false);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        binding.primary.setOnClickListener(view -> {
            dismiss();
            onAgreementClickListener.onClick(view);
        });

        binding.disagree.setOnClickListener(view -> {
            dismiss();
            SuperProcessUtil.killApp();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //修改宽度，默认比AlertDialog.Builder显示对话框宽度窄，看着不好看
        //参考：https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = (int) (ScreenUtil.getScreenWith(getContext()) * 0.9);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
```

```java
/**
 * 屏幕工具类
 */
public class ScreenUtil {
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWith(Context context) {
        //获取window管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //创建显示对象
        DisplayMetrics outDisplayMetrics = new DisplayMetrics();

        //获取默认显示对象
        wm.getDefaultDisplay().getMetrics(outDisplayMetrics);

        //返回屏幕宽度
        return outDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        //获取window管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //创建显示对象
        DisplayMetrics outDisplayMetrics = new DisplayMetrics();

        //获取默认显示对象
        wm.getDefaultDisplay().getMetrics(outDisplayMetrics);

        //返回屏幕宽度
        return outDisplayMetrics.heightPixels;
    }
}

```

```java
/**
 * 进程工具类
 */
public class SuperProcessUtil {
    /**
     * 杀死当前应用
     */
    public static void killApp() {
        Process.killProcess(Process.myPid());
    }
}
```

使用例如

```java
    /**
     * 显示对话框
     */
    private void showTermsServiceAgreementDialog() {
        TermServiceDialogFragment.show(getSupportFragmentManager(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultPreferenceUtil.getInstance(getHostActivity()).setAcceptTermsServiceAgreement();
                checkPermission();
            }
        });
    }
```



# 广播

与广播有关的方法主要有以下3个。

sendBroadcast：发送广播。

registerReceiver：注册广播的接收器，可在onStart或onResume方法中注册接收器。

unregisterReceiver：注销广播的接收器，可在onStop或onPause方法中注销接收器。


## 收发应用广播

### 标准广播

定义一个广播接收器：

// 定义一个标准广播的接收器
public class StandardReceiver extends BroadcastReceiver {

```java
// 定义一个标准广播的接收器
public class StandardReceiver extends BroadcastReceiver {

    public static final String STANDARD_ACTION = "com.example.broadcaststudy.standard";

    // 一旦接收到标准广播，马上触发接收器的onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction().equals(STANDARD_ACTION)){
            Log.d("hhh", "收到一个标准广播");
        }
    }
}
```

在Activity中动态注册接收器：


```java
public class BroadcastStandardActivity extends AppCompatActivity {

    private StandardReceiver standardReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_standard);

        findViewById(R.id.btn_send_standard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送标准广播
                Intent intent = new Intent(standardReceiver.STANDARD_ACTION);
                sendBroadcast(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        standardReceiver = new StandardReceiver();
        // 创建一个意图过滤器，只处理STANDARD_ACTION的广播
        IntentFilter filter = new IntentFilter(StandardReceiver.STANDARD_ACTION);
        // 注册接收器，注册之后才能正常接收广播
        registerReceiver(standardReceiver, filter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // 注销接收器，注销之后就不再接收广播
        unregisterReceiver(standardReceiver);
    }
}

```

### 有序广播

由于广播没指定唯一的接收者，因此可能存在多个接收器，每个接收器都拥有自己的处理逻辑。这些接收器默认是都能够接受到指定广播并且是之间的顺序按照注册的先后顺序，也可以通过指定优先级来指定顺序。

先收到广播的接收器A，既可以让其他接收器继续收听广播，也可以中断广播不让其他接收器收听。

```java
public class BroadOrderActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ORDER_ACTION = "com.example.broadcaststudy.order";
    private OrderAReceiver orderAReceiver;
    private OrderBReceiver orderBReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_order);
        findViewById(R.id.btn_send_order).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 创建一个指定动作的意图
        Intent intent = new Intent(ORDER_ACTION);
        // 发送有序广播
        sendOrderedBroadcast(intent, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 多个接收器处理有序广播的顺序规则为：
        // 1、优先级越大的接收器，越早收到有序广播；
        // 2、优先级相同的时候，越早注册的接收器越早收到有序广播
        orderAReceiver = new OrderAReceiver();
        IntentFilter filterA = new IntentFilter(ORDER_ACTION);
        filterA.setPriority(8);
        registerReceiver(orderAReceiver, filterA);

        orderBReceiver = new OrderBReceiver();
        IntentFilter filterB = new IntentFilter(ORDER_ACTION);
        filterB.setPriority(10);
        registerReceiver(orderBReceiver, filterB);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(orderAReceiver);
        unregisterReceiver(orderBReceiver);
    }
}

```

### 静态广播

广播也可以通过静态代码的方式来进行注册。广播接收器也能在AndroidManifest.xml注册，并且注册时候的节点名为receiver，一旦接收器在AndroidManifest.xml注册，就无须在代码中注册了。

之所以罕见静态注册，是因为静态注册容易导致安全问题，故而Android 8.0之后废弃了大多数静态注册。


## 监听系统广播

除了应用自身的广播，系统也会发出各式各样的广播，通过监听这些系统广播，App能够得知周围环境发生了什么变化，从而按照最新环境调整运行逻辑。

### 监听网络变更广播

```java
// 定义一个网络变更的广播接收器
private class NetworkReceiver extends BroadcastReceiver {
    // 一旦接收到网络变更的广播，马上触发接收器的onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            NetworkInfo networkInfo = intent.getParcelableExtra("networkInfo");
            String networkClass =
                NetworkUtil.getNetworkClass(networkInfo.getSubtype());
            desc = String.format("%s\n%s 收到一个网络变更广播，网络大类为%s，" +
                                 "网络小类为%s，网络制式为%s，网络状态为%s",
                                 desc, DateUtil.getNowTime(),
                                 networkInfo.getTypeName(),
                                 networkInfo.getSubtypeName(), networkClass,
                                 networkInfo.getState().toString());
            tv_network.setText(desc);
        }
    }
}

```

```java
@Override
protected void onStart() {
    super.onStart();
    networkReceiver = new NetworkReceiver(); // 创建一个网络变更的广播接收器
    // 创建一个意图过滤器，只处理网络状态变化的广播
    IntentFilter filter = new
        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    registerReceiver(networkReceiver, filter); // 注册接收器，注册之后才能正常接收广播
}
@Override
protected void onStop() {
    super.onStop();
    unregisterReceiver(networkReceiver); // 注销接收器，注销之后就不再接收广播
}

```

# 服务组件 Service

可在后台执行长时间运行操作而不提供界面的应用组件。如下载文件、播放音乐

Service有两种方式：

- startService ：简单地启动，之后不能进行交互
- bindService：启动并绑定Service之后，可以进行交互

通过一个音乐播放器实例来介绍这两种方式

## startService

 首先创建MusicHelper用来播放音频

```java
public class MusicHelper {

    private MediaPlayer mediaPlayer;

    private Context context;

    private final int[] musics = new int[]{R.raw.daoxiang, R.raw.qingtian};

    private int musicIndex = 0;

    private boolean prepared = false;

    public MusicHelper(Context context) {
        this.context = context;
        //创建MediaPlayer对象
        createMediaPlayer();
    }

    //创建MediaPlayer对象
    private void createMediaPlayer() {

        this.mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    //播放
    public void play() {
        if (mediaPlayer.isPlaying()) {
            return;
        }
        if (prepared) {
            mediaPlayer.start();
            Log.d("hhh", "播放音频 play");
            return;
        }
        try {
            //这里路径要注意：android.resource:// + 包名 + R.raw.XXXX
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + musics[musicIndex]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Log.d("hhh", "播放音频 play");
                prepared = true;
            }
        });
    }

    //暂停
    public void pause() {
        if (!mediaPlayer.isPlaying()) {
            return ;
        }
        mediaPlayer.pause();
    }

    //下一首
    public void next(){
        musicIndex = musicIndex + 1;
        musicIndex = musicIndex % musics.length;
        destroy();
        createMediaPlayer();
        play();
    }

    //销毁
    public void destroy(){
        if(mediaPlayer == null){
            return ;
        }
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        prepared = false;
    }
}

```

实现Service类：

```java
public class MusicService extends Service {

    private MusicHelper musicHelper;

    //在onCreate中创建MusicHelper
    @Override
    public void onCreate() {
        super.onCreate();
        musicHelper = new MusicHelper(this);
    }

    //在onDestroy中销毁MusicHelper
    @Override
    public void onDestroy() {
        super.onDestroy();
        musicHelper.destroy();
        musicHelper = null;
    }

    //播放音频
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicHelper.play();
        Log.d("hhh", "播放音频 onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

```

在AndroidManifest.xml中注册：

```
<service
         android:name=".service.MusicService"
         android:enabled="true"
         android:exported="true" />

```

播放，暂停音乐：

```java
public class StartServicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_servic);

        //播放
        findViewById(R.id.btPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hhh", "点击了按钮");
                Intent intent = new Intent(StartServicActivity.this, MusicService.class);
                //这里会自动调用Service的onStartCommand方法
                startService(intent);
            }
        });

        //暂停
        findViewById(R.id.btStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartServicActivity.this, MusicService.class);
                //这里会直接调用Service的onDestroy方法，销毁Service
                stopService(intent);
            }
        });
    }
}

```

startService只能简单启动和销毁Service，没办法进行交互，也就没办法进行暂停，下一首等功能。

## bindService

在Service类中添加了Binder类：

```java
public class MusicService2 extends Service {

    private MusicHelper musicHelper;

    //在onCreate中创建MusicHelper
    @Override
    public void onCreate() {
        super.onCreate();
        musicHelper = new MusicHelper(this);
    }

    //在onDestroy中销毁MusicHelper
    @Override
    public void onDestroy() {
        super.onDestroy();
        musicHelper.destroy();
        musicHelper = null;
    }

    //播放音频
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicHelper.play();
        Log.d("hhh", "播放音频 onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        private MusicService2 service;

        public MyBinder(MusicService2 service) {
            this.service = service;
        }

        public void play() {
            service.musicHelper.play();
        }

        public void next() {
            service.musicHelper.next();
        }

        public void pause() {
            service.musicHelper.pause();
        }

        public void destroy() {
            service.musicHelper.destroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("hhh", "onBind");
        return new MyBinder(this);
    }
}

```

连接类MyConn：

- 调用bindService之后，客户端端连上Service
- 触发MyConn类的onServiceConnected方法，获取Binder对象
- 之后可以Binder对象和Service交互（播放、暂停、下一首）

```java
public class MyConn implements ServiceConnection {

    public MusicService2.MyBinder myBinder = null;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myBinder = (MusicService2.MyBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}

```

此时就可以真正实现播放，暂停，下一首等功能了：

```java
public class BindServiceActivity extends AppCompatActivity {

    private MyConn myConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service);

        //初始化服务
        initService();

        //播放
        findViewById(R.id.btPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hhh", "点击了按钮");
                if(myConn.myBinder == null){
                    return ;
                }
                myConn.myBinder.play();
            }
        });

        //暂停
        findViewById(R.id.btStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myConn.myBinder == null){
                    return ;
                }
                myConn.myBinder.pause();
            }
        });

        //下一首
        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myConn.myBinder == null){
                    return ;
                }
                myConn.myBinder.next();
            }
        });
    }

    //初始化服务
    public void initService(){
        //开启服务
        Intent intent = new Intent(this, MusicService2.class);
        startService(intent);

        //绑定服务
        if(myConn == null){
            myConn = new MyConn();
            intent = new Intent(this, MusicService2.class);
            //这里会自动调用MyConn的onServiceConnected方法
            bindService(intent, myConn, 0);
        }
    }
}

```

# 网络通信

Android中常用的组合是OkHttp+Retrofit。

## 添加权限

```
<uses-permission android:name="android.permission.INTERNET" />
```

## okhttp

okhttp是android平台使用最广泛的第三方网络框架，okhttp做了很多网络优化，功能也很强大。

okhttp有同步、异步两种接口

- 同步接口：阻塞方式
- 异步接口：自动创建线程进行网络请求

```java
private void testGet() {
    OkHttpClient client = new OkHttpClient();

    String url = Config.ENDPOINT + "v1/songs";

    Request request = new Request.Builder()
            .url(url)
            .build();

    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "onFailure: "+e.getLocalizedMessage());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            Log.d(TAG, "onResponse: "+response.body().string());
        }
    });
}
```

## retrofit

在Android开发中，Retrofit是当下最热的一个网络请求库。

底层默认使用okhttp封装的，准确来说,网络请求的工作本质上是okhttp完成，而 Retrofit 仅负责网络请求接口的封装。

其作用主要是简化代码、提高可维护性。

另外，最重要的是：okhttp异步请求的回调运行在子线程，而retrofit的异步请求的回调默认运行在主线程。

一、根据接口创建Java接口

```java
//创建一个接口
public interface HttpbinService {

  @POST("post")
  Call<ResponseBody> post(@Field("username") String userName, @Field("password") String pwd);

  @GET("get")
  Call<ResponseBody> get(@Query("username") String userName, @Query("password") String pwd);
}
```

二、创建retrofit对象，并生成接口实现类对象

```java
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Retrofit retrofit = new Retrofit().Builder().baseUrl("hrrps:/xxx").build();
    HttpbinService httpbinService = retrofit.create(HttpbinService.class);
  }

  public void postAsync(){
    retrofit2.Call<ResponseBody> call = retrofit.post("admin","123456");
    call.enqueue(new retrofit2.Callback<ResponseBody>() {
      //响应
      @Override
      public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
        //获得
        ResponseBody result = response.body();
      }

      //请求失败
      @Override
      public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
        t.printStackTrace();
      }
    });
  }
```

## 综合

封装NetworkModule

```java
package com.ixuea.courses.mymusic.component.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.ixuea.courses.mymusic.AppContext;
import com.ixuea.courses.mymusic.config.Config;
import com.ixuea.courses.mymusic.util.JSONUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {
  /**
   * 提供OkHttpClient
   */
  public static OkHttpClient provideOkHttpClient() {
    //初始化okhttp
    OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

    //配置缓存
//    Cache cache = new Cache(AppContext.getInstance().getCacheDir(), Config.NETWORK_CACHE_SIZE);
//    okhttpClientBuilder.cache(cache);

    okhttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS) //连接超时时间
      .writeTimeout(60, TimeUnit.SECONDS) //写，也就是将数据发送到服务端超时时间
      .readTimeout(60, TimeUnit.SECONDS); //读，将服务端的数据下载到本地

    if (Config.DEBUG) {
      //调试模式

      //创建okhttp日志拦截器
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

      //设置日志等级
      loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);

      //添加到网络框架中
      okhttpClientBuilder.addInterceptor(loggingInterceptor);

      //添加chucker实现应用内显示网络请求信息拦截器
      okhttpClientBuilder.addInterceptor(new ChuckerInterceptor.Builder(AppContext.getInstance()).build());
    }

    return okhttpClientBuilder.build();
  }

  /**
   * 提供Retrofit实例
   *
   * @param okHttpClient
   * @return
   */
  public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
      //让retrofit使用okhttp
      .client(okHttpClient)

      //api地址
      .baseUrl(Config.ENDPOINT)

      //适配rxjava
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

      //使用gson解析json
      //包括请求参数和响应
      .addConverterFactory(GsonConverterFactory.create(JSONUtil.createGson()))

      //创建retrofit
      .build();
  }
}

```

创建HttpObserver

```java
package com.ixuea.courses.mymusic.component.api;

import com.ixuea.courses.mymusic.activity.BaseLogicActivity;
import com.ixuea.courses.mymusic.component.observer.ObserverAdapter;
import com.ixuea.courses.mymusic.fragment.BaseFragment;
import com.ixuea.courses.mymusic.fragment.BaseLogicFragment;
import com.ixuea.courses.mymusic.model.response.BaseResponse;
import com.ixuea.courses.mymusic.util.ExceptionHandlerUtil;

import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Response;


/**
 * 网络请求Observer
 */
public abstract class HttpObserver<T> extends ObserverAdapter<T> {
  private static final String TAG = "HttpObserver";
  private boolean isShowLoading = false; //默认为false
  private final BaseLogicActivity activity;

  public HttpObserver(BaseLogicActivity activity, boolean isShowLoading) {
    super();
    this.activity = activity;
    this.isShowLoading = isShowLoading;
  }

  public HttpObserver(BaseLogicFragment fragment) {
    super();
    this.activity = (BaseLogicActivity) fragment.getActivity();
    this.isShowLoading = true;
  }

  /**
   * 请求成功
   *
   * @param data
   */
  public abstract void onSucceeded(T data);

  /**
   * 请求失败
   *
   * @param data
   * @param e
   * @return true:自己处理；false:框架处理
   */
  public boolean onFailed(T data, Throwable e) {

    return false;
  }

  /**
   * 请求结束，成功失败都会调用（调用前调用），使用在这里隐藏加载提示
   */
  public void onEnd() {
    if (isShowLoading) {
      activity.hideLoading();
    }
  }

  @Override
  public void onSubscribe(Disposable d) {
    super.onSubscribe(d);
    if (isShowLoading) {
      //显示加载对话框
      activity.showLoading("加载中");
    }
  }

  @Override
  public void onNext(T t) {
    super.onNext(t);

    onEnd();

    if (isSucceeded(t)) {
      //请求正常
      onSucceeded(t);
    } else {
      //请求出错了
      handlerRequest(t, null);
    }
  }

  @Override
  public void onError(Throwable e) {
    super.onError(e);
    onEnd();

    //处理错误
    handlerRequest(null, e);
  }

  /**
   * 网络请求是否成功了
   *
   * @param t
   * @return
   */
  private boolean isSucceeded(T t) {
    if (t instanceof Response) {
      //retrofit里面的响应对象

      //获取响应对象
      Response response = (Response) t;

      //获取响应码
      int code = response.code();

      //判断响应码
      if (code >= 200 && code <= 299) {
        //网络请求正常
        return true;
      }

    } else if (t instanceof BaseResponse) {
      //判断具体的业务请求是否成功
      BaseResponse response = (BaseResponse) t;

      return response.isSucceeded();
    }

    return false;
  }

  /**
   * 处理错误网络请求
   *
   * @param data
   * @param error
   */
  private void handlerRequest(T data, Throwable error) {
    if (onFailed(data, error)) {
      //回调了请求失败方法
      //并且该方法返回了true

      //返回true就表示外部手动处理错误
      //那我们框架内部就不用做任何事情了
    } else {
      ExceptionHandlerUtil.handlerRequest(data, error);
    }

  }
}

```

创建DefaultService如

```java
package com.ixuea.courses.mymusic.component.api;

import com.ixuea.courses.mymusic.component.comment.model.Comment;
import com.ixuea.courses.mymusic.component.sheet.model.Sheet;
import com.ixuea.courses.mymusic.component.sheet.model.SheetWrapper;
import com.ixuea.courses.mymusic.model.response.DetailResponse;
import com.ixuea.courses.mymusic.model.response.ListResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 默认远程数据源
 */
public interface DefaultService {
  /**
   * 歌单列表
   *
   * @return
   */
  @GET("v1/sheets")
  Observable<ListResponse<Sheet>> sheets(@Query(value = "category") String category, @Query(value = "size") int size);


  /**
   * 歌单详情
   * @param testHeader
   * @param id
   * @return
   */
  @GET("v1/sheets/{id}")
  Observable<DetailResponse<Sheet>> sheetDetail(@Header("testHeader") String testHeader, @Path("id") String id);

  /**
   * 评论列表
   * @return
   */
  @GET("v1/comments")
  Observable<ListResponse<Comment>> comments();
  
    /**
   * 广告列表
   *
   * @return
   */
  @GET("v1/ads")
  Observable<ListResponse<Ad>> ads(@Query(value = "position") int position);
}

```

首先创建数据模型如

```java
/**
 * 广告模型
 */
public class Ad extends Common {
    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String icon;

    /**
     * 点击广告后跳转的地址
     */
    private String uri;

    /**
     * 类型，0：图片；10：视频；20：应用
     */
    private byte style;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte getStyle() {
        return style;
    }

    public void setStyle(byte style) {
        this.style = style;
    }
}

```

创建DefaultRepository

```java
package com.ixuea.courses.mymusic.repository;

import com.ixuea.courses.mymusic.component.api.DefaultService;
import com.ixuea.courses.mymusic.component.api.NetworkModule;

public class DefaultRepository {
  private static DefaultRepository instance;
  private final DefaultService service;

  public DefaultRepository() {
    //虽然当前类是单例设计模式，但因为直接调用provideRetrofit这样的方法
    //所以虽然代码是和MVVM架构模块那边（商城）复用了，但他们不是一个单例对象
    service = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient()).create(DefaultService.class);
  }

  /**
   * 返回当前对象的唯一实例
   * 单例设计模式
   * 由于移动端很少有高并发
   * 所以这个就是简单判断
   *
   * @return
   */
  public synchronized static DefaultRepository getInstance() {
    if (instance == null) {
      instance = new DefaultRepository();
    }
    return instance;
  }

  /**
   * 广告列表
   *
   * @return
   */
  public Observable<ListResponse<Ad>> ads(int position) {
    return service.ads(position)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 首页banner界面广告
   *
   * @return
   */
  public Observable<ListResponse<Ad>> bannerAd() {
    return ads(Constant.VALUE0);
  }
}

```

使用

```java
//广告API
Observable<ListResponse<Ad>> ads = DefaultRepository.getInstance().bannerAd();

ads
        .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(new HttpObserver<ListResponse<Ad>>() {
            @Override
            public void onSucceeded(ListResponse<Ad> data) {
                //添加轮播图
                datum.add(new BannerData(
                        data.getData().getData()
                ));
            }
        });
```





# SP

sharedPreferences对数据的存储和读取类似Map，提供put和set方法。

```java
/**
 * 偏好设置工具类
 * 是否登录了，是否显示引导界面，用户Id
 */
public class DefaultPreferenceUtil {
    /**
     * 偏好设置文件名称
     */
    private static final String NAME = "xxx";

    private static final String TERMS_SERVICE = "TERMS_SERVICE";

    private static DefaultPreferenceUtil instance;
    private final Context context;
    private final SharedPreferences preference;

    /**
     * 构造方法
     *
     * @param context
     */
    public DefaultPreferenceUtil(Context context) {
        //保存上下文
        this.context = context.getApplicationContext();

        //这样写有内存泄漏
        //因为当前工具类不会马上释放
        //如果当前工具类引用了界面实例
        //当界面关闭后
        //因为界面对应在这里还有引用
        //所以会导致界面对象不会被释放
        //this.context = context;

        //获取系统默认偏好设置，在设置界面保存的值就可以这样获取
        preference = PreferenceManager.getDefaultSharedPreferences(this.context);

        //自定义名称
//        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取偏好设置单例
     *
     * @param context
     * @return
     */
    public synchronized static DefaultPreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DefaultPreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 设置用户协议
     */
    public void setAcceptTermsServiceAgreement(boolean value) {
        putBoolean(TERMS_SERVICE, value);
    }

    /**
     * 获取是否同意了用户协议
     *
     * @return
     */
    public boolean getAcceptTermsServiceAgreement() {
        return getBoolean(TERMS_SERVICE)
    }

    /**
     * 保存boolean
     *
     * @param key
     * @param value
     */
    private void putBoolean(String key, boolean value) {
        preference.edit().putBoolean(key, value).apply();
    }
  
    public static boolean getBoolean(String key) {
        return preference.getBoolean(key, false);
    }
}

```

可以把sharedPreferences再封装一下方便使用例如

```java
/**
 * 项目中特有逻辑
 */
public class BaseLogicActivity extends BaseCommonActivity {

    protected PreferenceUtil sp;

    @Override
    protected void initDatum() {
        super.initDatum();
      
        sp = PreferenceUtil.getInstance(getHostActivity());
    }
}

```

也可以直接使用例如

```java
    if(DefaultPreferenceUtil.getInstance(getHostActivity()).getAcceptTermsServiceAgreement()) {
    //已经同意了用户协议
    checkPermission();
} else {
    showTermsServiceAgreementDialog();
}
```



# 重点内容

# 需要练习的

对话框

Tab切换

RecycleView

输入框+Toast