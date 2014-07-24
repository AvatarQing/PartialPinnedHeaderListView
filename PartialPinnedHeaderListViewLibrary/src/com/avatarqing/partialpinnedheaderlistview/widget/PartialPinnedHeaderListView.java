package com.avatarqing.partialpinnedheaderlistview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class PartialPinnedHeaderListView extends ListView implements
		OnScrollListener {
	public interface PinnedHeaderCallback {
		public View getPinnedHeader();

		public int getStaticHeaderId();
	}

	private static final String TAG = PartialPinnedHeaderListView.class
			.getSimpleName();

	private View mHeaderView = null;
	private View mHeaderStaticContent = null;
	private View mPinnedHeader = null;

	private float mScrollSpeedOfHeaderStaticContent = 0.6f;

	private OnScrollListener mOnScrollListener = null;
	private PinnedHeaderCallback mPinnedHeaderCallback = null;

	public PartialPinnedHeaderListView(Context context) {
		this(context, null);
	}

	public PartialPinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mPinnedHeader != null) {
			measureChild(mPinnedHeader, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mPinnedHeader != null) {
			drawChild(canvas, mPinnedHeader, getDrawingTime());
		}
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		if (l == this) {
			super.setOnScrollListener(l);
		} else {
			mOnScrollListener = l;
		}
	}

	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		super.addHeaderView(v, data, isSelectable);
		mHeaderView = v;
		if (mPinnedHeaderCallback != null) {
			mHeaderStaticContent = v.findViewById(mPinnedHeaderCallback
					.getStaticHeaderId());
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		refreshHeaderView();
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public void setPinnedHeaderCallback(PinnedHeaderCallback callback) {
		mPinnedHeaderCallback = callback;
		if (callback == null) {
			mPinnedHeader = null;
		} else {
			mPinnedHeader = mPinnedHeaderCallback.getPinnedHeader();
			mHeaderStaticContent = findViewById(mPinnedHeaderCallback
					.getStaticHeaderId());
			requestLayout();
			postInvalidate();
		}
	}

	private void init() {
		setOnScrollListener(this);
	}

	private void refreshHeaderView() {
		if (mPinnedHeader == null || mHeaderView == null
				|| mHeaderStaticContent == null) {
			return;
		}

		// 静止区域
		int scrollYOfStaticHeader = Math.abs(mHeaderView.getTop());
		// 静止区域原本应该滚到屏幕上部之外
		if (scrollYOfStaticHeader > 0
				&& scrollYOfStaticHeader < mHeaderStaticContent
						.getMeasuredHeight()) {
			int scrollY = (int) (-scrollYOfStaticHeader * mScrollSpeedOfHeaderStaticContent);
			mHeaderStaticContent.scrollTo(0, scrollY);
		}

		// 固定头
		int distanceToTopOfPinnedHeader = mHeaderView.getBottom()
				- mPinnedHeader.getMeasuredHeight();
		if (distanceToTopOfPinnedHeader <= 0) {
			mPinnedHeader.layout(0, 0, mPinnedHeader.getMeasuredWidth(),
					mPinnedHeader.getMeasuredHeight());
		} else {
			mPinnedHeader.layout(0, 0, 0, 0);
		}
	}
}
