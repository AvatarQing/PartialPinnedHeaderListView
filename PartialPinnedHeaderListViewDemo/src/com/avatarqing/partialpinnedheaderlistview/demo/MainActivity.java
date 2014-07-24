package com.avatarqing.partialpinnedheaderlistview.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import com.avatarqing.partialpinnedheaderlistview.widget.PartialPinnedHeaderListView;
import com.avatarqing.partialpinnedheaderlistview.widget.PartialPinnedHeaderListView.PinnedHeaderCallback;

public class MainActivity extends Activity {
	private PartialPinnedHeaderListView mList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		initListView();
	}

	private void findViews() {
		mList = (PartialPinnedHeaderListView) findViewById(android.R.id.list);
	}

	private void initListView() {
		View headerView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.header, mList, false);
		mList.addHeaderView(headerView);
		mList.setPinnedHeaderCallback(new PinnedHeaderCallback() {
			@Override
			public View getPinnedHeader() {
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.pinned_header, mList, false);
				return view;
			}

			@Override
			public int getStaticHeaderId() {
				return R.id.header_static_content;
			}
		});

		// 设置适配器
		List<String> dataList = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			dataList.add("Item No." + i);
		}
		ArrayAdapter<String> adatper = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1, dataList);
		mList.setAdapter(adatper);
	}
}
