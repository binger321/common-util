package com.binger.common.nodeBuilder;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/5/9
 * Time: 下午5:00
 * To change this template use File | Settings | File Templates.
 * Description: 支持树状结构显示
 */
@Data
public class Node {
    /**
     * 是否是页节点
     */
    private boolean isLeaf;

    /**
     * 父节点id
     */
    private Integer pId;

    /**
     * id
     */
    private Integer id;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 子节点
     */
    private List<Node> childNodes;

    public Node(Integer pId, Integer id, String code, String name) {
        this.pId = pId;
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;

        Node node = (Node) o;

        if (pId != null ? !pId.equals(node.pId) : node.pId != null) return false;
        return getId() != null ? getId().equals(node.getId()) : node.getId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pId != null ? pId.hashCode() : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
