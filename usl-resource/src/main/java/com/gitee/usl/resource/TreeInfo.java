package com.gitee.usl.resource;

/**
 * @author hongda.li
 */
public class TreeInfo {
    private Integer id;
    private Integer parentId;
    private String name;
    private String belongs;
    private Integer order;
    private String className;
    private String viewUrl;
    private Boolean byUsl;

    public Integer getId() {
        return id;
    }

    public TreeInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }

    public TreeInfo setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public TreeInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getBelongs() {
        return belongs;
    }

    public TreeInfo setBelongs(String belongs) {
        this.belongs = belongs;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public TreeInfo setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public TreeInfo setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public TreeInfo setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
        return this;
    }

    public Boolean getByUsl() {
        return byUsl;
    }

    public TreeInfo setByUsl(Boolean byUsl) {
        this.byUsl = byUsl;
        return this;
    }
}
