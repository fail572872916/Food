package com.food.lmln.food.fragment;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.food.lmln.food.R;
import com.food.lmln.food.adapter.FoodStyle1Adapter;
import com.food.lmln.food.bean.FoodInfo;
import com.food.lmln.food.bean.OrderInfo;
import com.food.lmln.food.callback.ClothAddCallback;
import com.food.lmln.food.db.Constant;
import com.food.lmln.food.db.DbManger;
import com.food.lmln.food.db.SqlHelper;
import com.food.lmln.food.utils.CommonUtils;
import com.food.lmln.food.utils.MoveImageView;
import com.food.lmln.food.utils.MyBitmapUtil;
import com.food.lmln.food.view.ScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import static com.food.lmln.food.utils.HttpUtils.Url;

public class BlankFragment extends Fragment {
    private List<String> data;
    private List<FoodInfo> subListGridView;
    private FoodStyle1Adapter mAdapter;
    private String text;
    private String big_imageUrl;
    private String small_imageUrl;
    ScrollGridView gd_frgment1;
    private  int  pageCount;//总个数
    private int pageIndex=1; //当前页数;
    private int  pageSize=8;//每页显示的个数
    private int  pageNum;//每页显示的个数
    private List<FoodInfo> personList;
    private SQLiteDatabase db;
    SqlHelper helper;
    private ImageView holdCart;
    private RelativeLayout holdRootView;

    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    // 路径测量
    private PathMeasure mPathMeasure;
    // 购物车商品数目
    private int goodsCount = 0;
    LinearLayout   rl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view= inflater.inflate(R.layout.fragment_blank1, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_fragment1);
           rl  = (LinearLayout) view.findViewById(R.id.f1);
        holdCart = (ImageView) getActivity().findViewById(R.id.main_holdCart);
        holdRootView = (RelativeLayout) getActivity().findViewById(R.id.container);
        helper=DbManger.getInstance(getActivity());
        db = helper.getWritableDatabase();
        init();
        viewPager.setAdapter(new MyAdapter());
        return view;
    }

    /**
     * 获得数据
     * 判断操作
     */
    private void init() {
        pageCount= DbManger.getCountPerson(db, Constant.TABLE_NAME_FOODINFO);
        pageNum = (int) Math.ceil(pageCount/(double)pageSize);
        data = new ArrayList<>();
        for (int i = 1; i <4; i++) {
            data.add( i+"");
        }

    }
    /**
     *
     * 适配器  MyAdapter
     */
    class MyAdapter extends PagerAdapter {
        private ViewPager pager;
        LayoutInflater inflater = null;
        //用于存储回收掉的View
        private List<WeakReference<LinearLayout>> viewList=new ArrayList<WeakReference<LinearLayout>>();  ;
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

//            container.removeView((View) object);
//            LinearLayout view = (LinearLayout) object;
//            container.removeView(view);
//            viewList.add(new WeakReference<LinearLayout>(view));
            container.removeView(viewList.get(position).get());

        }

        @Override
        public int getCount() {
            return data.size();

        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            if (pager == null) {
//                pager = (ViewPager) container;
//            }
//            View view = null;
//            // 从废弃的里去取 取到则使用 取不到则创建
//            if (viewList.size() > 0) {
//                if (viewList.get(0) != null) {
//                    view = initView(viewList.get(0).get(), position);
//                    viewList.remove(0);
//                }
//            }
//            view = initView(null, position);
//            pager.addView(view);
//
//            return view;

            LayoutInflater inflater=getActivity().getLayoutInflater();
//            第二种方式： inflater=LayoutInflater.from(this);
//            将布局加载到程序中
           ViewHolder viewHolder = new ViewHolder();
            View layout =inflater.inflate(R.layout.fragment_plus_one, null);
            viewHolder.im_big = (ImageView) layout.findViewById(R.id.im_big);
            viewHolder.im_small = (ImageView) layout.findViewById(R.id.im_small);
            viewHolder.tv_small_text = (TextView) layout.findViewById(R.id.tv_small_text);

               int  positionNum   =Integer.parseInt(data.get(position));
                gd_frgment1 = (ScrollGridView) layout.findViewById(R.id.gd_frgment1);


            initData(positionNum,viewHolder);
            container.addView(layout);
            return layout;

        }

    }


//    private View initView(LinearLayout view, int position) {
//        Log.d("BlankFragment", "position:fsafd" + position);
//        ViewHolder viewHolder = null;
//        if (view == null) {
//            view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_plus_one, null);
//            viewHolder = new ViewHolder();
//            viewHolder.im_big = (ImageView) view.findViewById(R.id.im_big);
//            viewHolder. im_small = (ImageView) view.findViewById(R.id.im_small);
//            viewHolder. tv_small_text = (TextView) view.findViewById(R.id.tv_small_text);
//            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        /**
//         * 初始化数据
//         */
//        int  positionNum   =Integer.parseInt(data.get(position));
//        Log.d("BlankFragment", "positionNum:" + (positionNum-1));
//
//        gd_frgment1 = (ScrollGridView) view.findViewById(R.id.gd_frgment1);
//
//        initData(positionNum, viewHolder);
//
//        return view;
//    }
    /**
     *
     * @param position 传入第i页进行翻转  加载每一页 的数据
     */
    private void initData(int position ,ViewHolder viewHolder) {
        pageIndex=position;
        personList = DbManger.getListByPageIndex(db, Constant.TABLE_NAME_FOODINFO,pageIndex,pageSize);
//        获取子列表
        subListGridView = personList.subList(2, personList.size());
        List<FoodInfo> bigList = personList.subList(0, 2);
        MyBitmapUtil utils;   utils = new MyBitmapUtil();
        for (int i = 0; i < bigList.size(); i++) {
            big_imageUrl=  bigList.get(0).getIamge();
            small_imageUrl= bigList.get(1).getIamge();
            text=bigList.get(1).getName();
        }
        big_imageUrl=Url+big_imageUrl;
        small_imageUrl=Url+small_imageUrl;
        utils.display(big_imageUrl,viewHolder.im_big);
        utils.display(small_imageUrl,viewHolder.im_small);
        viewHolder.tv_small_text.setText(text+"");
        mAdapter =new FoodStyle1Adapter(subListGridView,getActivity());

        Log.d("BlankFragment", "subListGridView:" +position);
//        });
//        initAnimation();
        gd_frgment1.setAdapter(mAdapter);

        gd_frgment1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("BlankFragment", subListGridView.get(position).getName());
                EventBus.getDefault().post(new OrderInfo(0, subListGridView.get(position).getName(),
                subListGridView.get(position).getPrice(), 0));
            }
        });


//        gd_frgment1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//         @Override
//         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                 if (CommonUtils.isFastDoubleClick()) {
//                     Toast.makeText(getActivity(), "请慢点", Toast.LENGTH_SHORT).show();
//                     return;
//                 }else{
//                     EventBus.getDefault().post(new OrderInfo(0, subListGridView.get(position).getName(),
//                             subListGridView.get(position).getPrice(), 0));
//
//
//                     //弹出Toast或者Dialog
//                 }
//         }
//     });

    }

//    public void sendOrder(int position){
//
//        EventBus.getDefault().post(new OrderInfo(0, subListGridView.get(position).getName(),
//                subListGridView.get(position).getPrice(), 0));
//    }


    /**
     * 加载动画
     */
    private void initAnimation() {


        AnimationSet set = new AnimationSet(false);
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(500);
        set.addAnimation(animation);

        animation = new TranslateAnimation(1, 13, 10, 50);
        animation.setDuration(300);
        set.addAnimation(animation);
//                animation = new RotateAnimation(30,10);
//        animation.setDuration(300);
//        set.addAnimation(animation);

        animation = new ScaleAnimation(5,0,2,0);
        animation.setDuration(300);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 1);

        gd_frgment1.setLayoutAnimation(controller);
    }


    private class ViewHolder {
        public ImageView im_big;
        public ImageView im_small;
        public TextView tv_small_text;

    }




    //添加购物车动画
    private void addCloth(ImageView clothIcon) {
        int[] childCoordinate = new int[2];
        int[] parentCoordinate = new int[2];
        int[] shopCoordinate = new int[2];

        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        clothIcon.getLocationInWindow(childCoordinate);
        holdRootView.getLocationInWindow(parentCoordinate);
        holdCart.getLocationInWindow(shopCoordinate);
        //2.自定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(getActivity());
        clothIcon.setDrawingCacheEnabled(true);
        img.setImageBitmap(Bitmap.createBitmap(clothIcon.getDrawingCache()));
        clothIcon.setDrawingCacheEnabled(false);
        //3.设置img在父布局中的坐标位置
        img.setX(childCoordinate[0] - parentCoordinate[0]);
        img.setY(childCoordinate[1] - parentCoordinate[1]);
        //4.父布局添加该Img
        holdRootView.addView(img);

        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        //开始的数据点坐标就是 addV的坐标
        startP.x = childCoordinate[0] - parentCoordinate[0];
        startP.y = childCoordinate[1] - parentCoordinate[1];
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = shopCoordinate[0] - parentCoordinate[0];
        endP.y = shopCoordinate[1] - parentCoordinate[1];
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = endP.x;
        controlP.y = startP.y;

        //启动属性动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF",
                new MoveImageView.PointFTypeEvaluator(controlP), startP, endP);

        animator.setDuration(500);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后 父布局移除 img
                Object target = ((ObjectAnimator) animation).getTarget();
                holdRootView.removeView((View) target);
                //购物车 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.cart_scale);
                holdCart.startAnimation(scaleAnim);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }



    }
