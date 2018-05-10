package com.binger.common.nodeBuilder;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/5/9
 * Time: 下午7:31
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class TreeBuilder {
    public static int level = 0;
    public static List<Node> builderTree(List<Node> nodeList) {
        List<Node> rootList = findRootList(nodeList);
        List<Node> nodes = findChild(rootList, nodeList);
        return nodes;
    }

    private static List<Node> findChild(List<Node> parentNodeList, List<Node> nodeList) {
        parentNodeList.forEach(node -> {
            List<Node> childNodes = nodeList
                    .parallelStream()
                    .filter(node1 -> node1.getPId().equals(node.getId()))
                    .map(node1 -> {
                        node1.setLevel(node.getLevel() + 1);
                        return node1;
                    })
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(childNodes)) {
                node.setLeaf(true);
            } else {
                node.setLeaf(false);
                childNodes = findChild(childNodes, nodeList);
                node.setChildNodes(childNodes);
            }
        });
        return parentNodeList;
    }

    private static List<Node> findRootList(List<Node> nodeList) {
        List<Node> rootNodes = nodeList
                .stream()
                .filter(node -> node.getPId() == 0)
                .map(node -> {
                    node.setLeaf(false);
                    node.setLevel(0);
                    return node;})
                .collect(Collectors.toList());
        return rootNodes;
    }
}
