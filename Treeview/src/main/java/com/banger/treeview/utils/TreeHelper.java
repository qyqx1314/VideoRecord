package com.banger.treeview.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.banger.treeview.bean.ResBean;

public class TreeHelper
{
	private static final String TAG = "TreeHelper";

	/**
	 * 将用户的数据转化为树形数据
	 * 
	 * @param datas 用户的数据
	 * @return 转化为树形结构数据
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<Node> convertDatas2Nodes(List<T> datas)
			throws IllegalArgumentException, IllegalAccessException
	{
		List<Node> nodes = new ArrayList<>();
		Node node = null;
		for (T t : datas)
		{
			int id = -1;
			int pid = -1;
			String label = null;

			node = new Node();
			Class clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				if (field.getAnnotation(com.banger.treeview.utils.annotation.TreeNodeId.class) != null)
				{
					//设置是否允许访问，默认是私有的
					field.setAccessible(true);
					id = field.getInt(t);
				}
				if (field.getAnnotation(com.banger.treeview.utils.annotation.TreeNodePid.class) != null)
				{
					field.setAccessible(true);
					pid = field.getInt(t);
				}
				if (field.getAnnotation(com.banger.treeview.utils.annotation.TreeNodeLabel.class) != null)
				{
					field.setAccessible(true);
					label = (String) field.get(t);
				}
			}
			node = new Node(id, pid, label);
			nodes.add(node);
		}// for end
		
		Log.e(TAG, nodes+"");

		/**
		 * 设置Node间的节点关系
		 */
		for (int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);

			for (int j = i + 1; j < nodes.size(); j++)
			{
				Node m = nodes.get(j);

				if (m.getpId() == n.getId())
				{
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId() == n.getpId())
				{
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}

		for (Node n : nodes)
		{
			setNodeIcon(n);
		}
		return nodes;
	}

	/**
	 * 获取根节点数据集
	 * @param datas 数据
	 * @param defaultExpandLevel 根节点层级
	 * @param <T> 泛型
	 * @return 树形结构数据集
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
     */
	public static <T> List<Node> getSortedNodes(List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		List<Node> result = new ArrayList<Node>();
		List<Node> nodes = convertDatas2Nodes(datas);
		// 获得树的根结点
		List<Node> rootNodes = getRootNodes(nodes);

		for (Node node : rootNodes)
		{
			addNode(result, node, defaultExpandLevel, 1);
		}

		Log.e("TAG", result.size() + "");
		return result;
	}

	/**
	 * 把一个节点的所有孩子节点都放入result
	 * 
	 * @param result 根节点所有数据集
	 * @param node 子节点
	 * @param defaultExpandLevel 父节点层级
	 * @param currentLevel 当前节点层级
	 */
	private static void addNode(List<Node> result, Node node,
			int defaultExpandLevel, int currentLevel)
	{
		result.add(node);
		if (defaultExpandLevel >= currentLevel)
		{
			node.setExpand(true);
		}
		if (node.isLeaf())
			return;

		for (int i = 0; i < node.getChildren().size(); i++)
		{
			addNode(result, node.getChildren().get(i), defaultExpandLevel,
					currentLevel + 1);
		}

	}

	/**
	 * 过滤出可见的节点
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNodes(List<Node> nodes)
	{
		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes)
		{
			if (node.isRoot() || node.isParentExpand())
			{
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 从所有节点中过滤出根节点
	 * 
	 * @param nodes 树形结构数据集
	 * @return 树形结构有根节点的数据集
	 */
	private static List<Node> getRootNodes(List<Node> nodes)
	{
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes)
		{
			if (node.isRoot())
			{
				root.add(node);
			}
		}
		return root;
	}

	/**
	 * 为Node设置图标
	 * 
	 * @param n
	 */
	private static void setNodeIcon(Node n)
	{
		if (n.getChildren().size() > 0 && n.isExpand())
		{
			n.setIcon(ResBean.tree_ex);
		} else if (n.getChildren().size() > 0 && !n.isExpand())
		{
			n.setIcon(ResBean.tree_ec);
		} else
		{
			n.setIcon(-1);
		}
	}

}
