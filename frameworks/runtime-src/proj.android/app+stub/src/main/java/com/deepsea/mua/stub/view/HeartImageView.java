package com.deepsea.mua.stub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.deepsea.mua.stub.R;

/**
 * 心形ImageView 可以设置边框
 * @author DerekYan
 */
public class HeartImageView extends ImageView {

	private Context mContext;

	private int border_size = 0;// 边框厚度
	private int in_border_color = 0;// 内圆边框颜色
	private int out_border_color = 0;// 外圆边框颜色
	private int defColor = 0xFFFFFFFF;// 默认颜色

	private int width = 0;// 控件的宽度
	private int height = 0;// 控件的高度

	private String shape_type;// 形状的类型

	public HeartImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public HeartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setAttributes(attrs);
	}

	public HeartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setAttributes(attrs);
	}

	/**
	 * 获得自定义属性
	 *
	 * @param attrs
	 */
	private void setAttributes(AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray mArray = mContext.obtainStyledAttributes(attrs,
				R.styleable.heartimageview);
		// 得到边框厚度，否则返回0
		border_size = mArray.getDimensionPixelSize(
				R.styleable.heartimageview_border_size, 0);
		// 得到内边框颜色，否则返回默认颜色
		in_border_color = mArray.getColor(
				R.styleable.heartimageview_in_border_color, defColor);
		// 得到外边框颜色，否则返回默认颜色
		out_border_color = mArray.getColor(
				R.styleable.heartimageview_out_border_color, defColor);
		// 得到形状的类型
		shape_type = mArray.getString(R.styleable.heartimageview_shape_type);

		mArray.recycle();// 回收mArray
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// super.onDraw(canvas); 必须去掉该行或注释掉，否则会出现两张图片
		// 得到传入的图片
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class) {// 如果该传入图片是.9格式的图片
			return;
		}

		// 将图片转为位图
		Bitmap mBitmap = ((BitmapDrawable) drawable).getBitmap();

		Bitmap cpBitmap = mBitmap.copy(Config.ARGB_8888, true);
		// 得到画布宽高
		width = getWidth();
		height = getHeight();
//
		int radius = 0;//

		radius = (width < height ? width : height) / 2;
//
		Bitmap shapeBitmap = drawShapeBitmap(cpBitmap, radius);
		canvas.drawBitmap(shapeBitmap, width / 2 - radius, height / 2 - radius,
				null);

	}

	/**
	 * 画出指定形状的图片
	 *
	 * @param radius
	 * @return
	 */
	private Bitmap drawShapeBitmap(Bitmap bmp, int radius) {
		// TODO Auto-generated method stub
		Bitmap squareBitmap;// 根据传入的位图截取合适的正方形位图
		Bitmap scaledBitmap;// 根据diameter对截取的正方形位图进行缩放
		Log.i("HeartImageView", "radius:" + radius);
		int diameter = radius * 2;
		// 位图的宽高
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片

		squareBitmap = bmp;
		// 对squareBitmap进行缩放为diameter边长的正方形位图
		if (w != diameter
				|| h != diameter) {

			if (w < diameter || h < diameter) {
				//位图宽高没有ImageView的宽高大 需要放大
				float scale; //缩放倍数
				scale = 1f * diameter / (Math.min(w, h));
				Matrix matrix = new Matrix();
				matrix.postScale(scale,scale);

				squareBitmap =  Bitmap.createBitmap(squareBitmap,0,0,w,h,matrix,false);

				if (w != h) {
					//从矩形图中截取正中间的正方形部分。
					scaledBitmap = centerSquareScaleBitmap(squareBitmap, diameter);
				} else {
					scaledBitmap = squareBitmap;
				}
			} else {
				//从矩形图中截取正中间的正方形部分。
				scaledBitmap = centerSquareScaleBitmap(squareBitmap, diameter);
			}
		} else {
			scaledBitmap = squareBitmap;
		}


		Bitmap outputbmp = Bitmap.createBitmap(scaledBitmap.getWidth(),
				scaledBitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outputbmp);// 创建一个相同大小的画布
		Paint paint = new Paint();// 定义画笔
		paint.setAntiAlias(true);// 设置抗锯齿
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);

		//设置边框
		Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(out_border_color);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(border_size);

		Path path = new Path();

		//右心形
		path.moveTo(diameter / 2f, diameter / 7f);
		path.cubicTo((diameter /6f) * 5  , 0 - ( diameter / 5f), (diameter / 5f) * 7  , (diameter / 5f) * 2  , diameter / 2f, diameter- border_size );
		//左心形
		path.moveTo(diameter / 2f, diameter / 7f);
		path.cubicTo(diameter / 6f , 0 - ( diameter / 5f), 0 - (diameter / 5f) * 2, (diameter / 5f) * 2, diameter / 2f, diameter - border_size );

		canvas.drawPath(path, paint);


		// 设置Xfermode的Mode
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(scaledBitmap, 0, 0, paint);

		canvas.drawPath(path, borderPaint);

		bmp = null;
		squareBitmap = null;
		scaledBitmap = null;

		return outputbmp;

	}

	/**

	 * @param bitmap      原图
	 * @param edgeLength  希望得到的正方形部分的边长
	 * @return  缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
	{
		if(null == bitmap || edgeLength <= 0)
		{
			return  null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if(widthOrg > edgeLength && heightOrg > edgeLength)
		{
			//压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try{
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			}
			catch(Exception e){
				return null;
			}

			//从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try{
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
				scaledBitmap.recycle();
			}
			catch(Exception e){
				return null;
			}
		}

		return result;
	}

}