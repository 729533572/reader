package com.hysoso.www.viewlibrary;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义TextView，解决计算listView中item高度的时候，如果其中的TextView字数超过一行，只计算到一行高度的问题
 */
public class MFSTextView extends TextView {
	private Context context;
	   public MFSTextView(Context context) {
	      super(context);
	      this.context = context;
	   }
	   public MFSTextView(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      this.context = context;
	   }
	   public MFSTextView(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      this.context = context;
	   }
	   @Override
	   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	       super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	       Layout layout = getLayout();
	       if (layout != null) {
	           int height = (int)Math.ceil(getMaxLineHeight(this.getText().toString()))
	                   + getCompoundPaddingTop() + getCompoundPaddingBottom();
	           int width = getMeasuredWidth();            
	           setMeasuredDimension(width, height);
	       }
	   }

	   private float getMaxLineHeight(String str) {
	      float height = 0.0f;
	      @SuppressWarnings("deprecation")
		float screenW = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
	      float paddingLeft = ((LinearLayout)this.getParent()).getPaddingLeft();
	      float paddingReft = ((LinearLayout)this.getParent()).getPaddingRight();
	//这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
	 int line = (int) Math.ceil( (this.getPaint().measureText(str)/(screenW-paddingLeft-paddingReft))); 
	 height = (this.getPaint().getFontMetrics().descent-this.getPaint().getFontMetrics().ascent)*line; return height;}
}
