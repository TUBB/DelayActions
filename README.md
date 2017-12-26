DelayActions
=========
执行某个操作时需要满足一些前提条件，而这些前提条件需要`用户参与`才能满足。
比如进入`个人资料界面`的前提是要先`登录`，而登录这个操作需要用户的参与才能完成。
平常的做法可能是利用事件通知机制（[EventBus][2]、[BroadcastReceiver][3]），把`登录成功`这个事件发送出去，告知前面的代码可以跳到`个人资料界面`了。
但如果前提条件有多个呢，是不是得发送多个事件呀，是不是得监听多个事件呀，是不是很麻烦呀（`有没有被恶心过😆`）。
那有没有比较优雅的方式来完成呢，嗯嗯，编写这个小框架的目的就在此~

特性
======== 
 
 * 抽象出`目标行为`和`前提条件行为`，从`复杂`到`简单`就这么简单
 * `前提条件`检测异步化，再也不用担心`UI Thread`卡卡卡了
 * 多个`前提条件行为`串行执行


使用
=====

### 添加依赖
```groovy
dependencies {
    api 'com.tubb.delayactions:delayactions:0.0.2'
}
```

### 定义`目标行为`
`目标行为`被抽象为[CoreAction][4]接口，实现该接口就可以
```java
class OrderDetailCoreAction implements CoreAction {

    private Activity mActivity;

    OrderDetailCoreAction(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        mActivity.startActivity(new Intent(mActivity, OrderDetailActivity.class));
    }
}
```

### 定义`前提条件行为`
`前提条件行为`被抽象为[PremiseAction][5]接口，实现该接口就可以
```java
public class LoginPremiseAction implements PremiseAction {
    private Activity mContext;
    public LoginPremiseAction(Activity context) {
        mContext = context;
    }

    @Override
    public boolean isPremiseCheckAsync() {
        return true;
    }

    @NonNull
    @Override
    public Observable<Boolean> finished() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(UserInfoCache.isLogin(mContext));
            }
        });
    }

    @Override
    public void execute() {
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }
}
```
[PremiseAction][5]接口一共定义了三个接口方法，`PremiseAction.finished()`方法用来检测前提条件是否满足了，返回值为[RxJava][6]中的`Observable<Boolean>`。
`PremiseAction.isPremiseCheckAsync()`方法用来判定`PremiseAction.finished()`方法的调用是否为异步。
`PremiseAction.execute()`方法定义`前提条件行为`具体的操作（需要用户参与），比如登录。

### 执行`目标行为`
`目标行为`的执行需要添加一些`前提条件行为`，多个`前提条件行为`将被以队列的方式串行执行
```java
CoreAction coreAction = new OrderDetailCoreAction(this);
ActionUnit unit = DelayActions.instance().createActionUnit(coreAction)
        .addPremiseAction(new LoginPremiseAction(this))
        .addPremiseAction(new DiscountPremiseAction(this));
DelayActions.instance().post(unit);
```

### 通知`前提条件行为`完成
当用户完成了`前提条件行为`时，需要通知框架，框架会去重新检测是否所有的`前提条件行为`都完成了，如果都完成，`目标行为`将被执行
```java
UserInfoCache.setLogin(this, true);
DelayActions.instance().notifyLoop(); // 通知框架重新检测
finish();
```

详细使用请参照[Demo][7]工程，强烈建议clone下来看一看，听说可以节省一局农药的时间

巨人的肩膀
=========

Inspired by [jinyb09017/delayActionDemo][1]

License
-------

    Copyright 2017 TUBB

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: https://github.com/jinyb09017/delayActionDemo
[2]: https://github.com/greenrobot/EventBus
[3]: http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.1.1_r1/android/content/BroadcastReceiver.java#BroadcastReceiver
[4]: https://github.com/TUBB/DelayActions/blob/master/library/src/main/java/com/tubb/delayactions/CoreAction.java
[5]: https://github.com/TUBB/DelayActions/blob/master/library/src/main/java/com/tubb/delayactions/PremiseAction.java
[6]: https://github.com/ReactiveX/RxJava
[7]: https://github.com/TUBB/DelayActions/tree/master/app