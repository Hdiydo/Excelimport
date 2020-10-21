package org.zxiat.entity.target;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @description: 控制点树
 * @author: ZhouWei
 * @create: 2020-08
 **/
@Data
@ToString
public class ControlPointTree {
    private String name;
    private String images;
    private String remarks;
    private List<ControlPointTree> children; // 子节点
}
