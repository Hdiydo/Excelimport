package org.zxiat.entity.target;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
public class Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String type;
    private List<String> tags;
    private List<Object> images; // 图片
    private List<Object> positions; // 位置
    private HashMap<String,String> labels;
    private HashMap<String,String> descs;
    private HashMap<String,List<String>> aliases;
    private HashMap<String,List<Claim>> claims;
    private Date mod;
    private Integer version;
    private List<Gene> genes;
    private boolean pushed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

    public List<Object> getPositions() {
        return positions;
    }

    public void setPositions(List<Object> positions) {
        this.positions = positions;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public HashMap<String, String> getLabels() {
        return labels;
    }

    public void setLabels(HashMap<String, String> labels) {
        this.labels = labels;
    }

    public HashMap<String, String> getDescs() {
        return descs;
    }

    public void setDescs(HashMap<String, String> descs) {
        this.descs = descs;
    }

    public HashMap<String, List<String>> getAliases() {
        return aliases;
    }

    public void setAliases(HashMap<String, List<String>> aliases) {
        this.aliases = aliases;
    }

    public HashMap<String, List<Claim>> getClaims() {
        return claims;
    }

    public void setClaims(HashMap<String, List<Claim>> claims) {
        this.claims = claims;
    }

    public Date getMod() {
        return mod;
    }

    public void setMod(Date mod) {
        this.mod = mod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }
}
