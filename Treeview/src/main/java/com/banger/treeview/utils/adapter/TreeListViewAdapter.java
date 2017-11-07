package com.banger.treeview.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.banger.treeview.utils.Node;
import com.banger.treeview.utils.TreeHelper;


public abstract class TreeListViewAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<Node> mAllNodes;
	//可见节点数据集
	protected List<Node> mVisibleNodes;
	protected LayoutInflater mInflater;

	protected ListView mTree;

	/**
	 * 设置Node的点击回调
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnTreeNodeClickListener
	{
		void onClick(Node node, int position);
	}

	private OnTreeNodeClickListener mListener;

	public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener)
	{
		this.mListener = mListener;
	}

	public TreeListViewAdapter(ListView tree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
		this.mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

		this.mTree = tree;

		this.mTree.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				expandOrCollapse(position);

				if (mListener != null)
				{
					mListener.onClick(mVisibleNodes.get(position), position);
				}

			}

		});

	}

	/**
	 * 点击搜索或者展开
	 * 
	 * @param position
	 */
	private void expandOrCollapse(int position)
	{
		//获取已显示的节点
		Node n = mVisibleNodes.get(position);
		if (n != null)
		{
			//如果不是父节点，直接返回
			if (n.isLeaf())
				return;
			//设置节点展开或关闭
			n.setExpand(!n.isExpand());
			//过滤显示的节点
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
			//视图刷新
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount()
	{
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mVisibleNodes.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{	//获取可见的节点
		Node node = mVisibleNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		// 设置内边距
		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
		return convertView;
	}

	public abstract View getConvertView(Node node, int position,
			View convertView, ViewGroup parent);

}
